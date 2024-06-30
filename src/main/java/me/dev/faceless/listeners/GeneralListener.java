package me.dev.faceless.listeners;

import dev.faceless.swiftlib.lib.events.GlobalEventHandler;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.Items;
import me.dev.faceless.message.MessageManager;
import me.dev.faceless.utils.HealthManager;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GeneralListener {
    private static final GlobalEventHandler eventHandler = GlobalEventHandler.get();
    private static final int healthAmount = 2;

    public static void init() {
        // Click heart listener
        eventHandler.addListener(PlayerInteractEvent.class, event -> {
            Player player = event.getPlayer();
            if(event.getHand() != EquipmentSlot.HAND) return;
            if(!event.getAction().isRightClick()) return;
            if(player.getInventory().getItemInMainHand().getType().isAir()) return;
            ItemStack item = player.getInventory().getItemInMainHand();
            if(!Items.isHeart(item)) return;
            if (HealthManager.increaseMaxHealth(player, healthAmount, p -> HealthManager.getMaxHealth(p) < MainConfig.getMaxHeartLimit())){
                item.setAmount(item.getAmount() - 1);
                MessageManager.getMessage(MessageManager.consumedHeartsPath).send(player);
            }
        });

        // Player death listener
        eventHandler.addListener(PlayerDeathEvent.class, event -> {
            Player player = event.getPlayer();
            LivingEntity killer = getLivingEntity(player);
            if(!(killer instanceof Player pKiller)) {
                if(!MainConfig.isEnvironmentalHeartLoseEnabled()) return;
                if(!HealthManager.reduceMaxHealth(player, healthAmount, p -> HealthManager.getMaxHealth(p) - healthAmount >= MainConfig.getMinHeartLimit())) return;
                if(!MainConfig.isEnvironmentalHeartDropEnabled()) return;
                player.getWorld().dropItem(player.getLocation(), Items.heartItem(1));
            }else {
                if (!HealthManager.reduceMaxHealth(player, healthAmount, p -> HealthManager.getMaxHealth(p) - healthAmount >= MainConfig.getMinHeartLimit())) return;
                if(MainConfig.isAutoHeartGainOnKill()) {
                    if (HealthManager.increaseMaxHealth(pKiller, healthAmount, p -> HealthManager.getMaxHealth(p) < MainConfig.getMaxHeartLimit())) return;
                    player.getWorld().dropItem(player.getLocation(), Items.heartItem(1));
                }else player.getWorld().dropItem(player.getLocation(), Items.heartItem(1));
            }
        });
    }

    private static @Nullable LivingEntity getLivingEntity(Player player) {
        LivingEntity killer = player.getKiller();
        if (killer == null) {
            EntityDamageEvent lastDamageEvent = player.getLastDamageCause();
            if (lastDamageEvent instanceof EntityDamageByEntityEvent damageByEntityEvent) {
                Entity damager = damageByEntityEvent.getDamager();
                if (damager instanceof Projectile projectile) {
                    if (projectile.getShooter() instanceof LivingEntity) killer = (LivingEntity) projectile.getShooter();
                } else if (damager instanceof LivingEntity) killer = (LivingEntity) damager;
            }
        }
        return killer;
    }
}
