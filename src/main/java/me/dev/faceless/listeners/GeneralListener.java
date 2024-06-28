package me.dev.faceless.listeners;

import dev.faceless.swiftlib.lib.events.GlobalEventHandler;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.Items;
import me.dev.faceless.message.MessageManager;
import me.dev.faceless.utils.HealthManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class GeneralListener {
    private static final GlobalEventHandler eventHandler = GlobalEventHandler.get();

    public static void init() {
        eventHandler.addListener(PlayerInteractEvent.class, event -> {
            Player player = event.getPlayer();
            if(event.getHand() != EquipmentSlot.HAND) return;
            if(player.getInventory().getItemInMainHand().getType().isAir()) return;
            ItemStack item = player.getInventory().getItemInMainHand();
            if(!Items.isHeart(item)) return;
            if (HealthManager.increaseMaxHealth(player, 1, p -> HealthManager.getMaxHealth(p) < MainConfig.getMaxHeartLimit())){
                item.setAmount(item.getAmount() - 1);
                MessageManager.getMessage(MessageManager.consumedHeartsPath).send(player);
            }
        });
    }
}
