package me.dev.faceless.items;

import dev.faceless.swiftlib.lib.text.TextContext;
import dev.faceless.swiftlib.lib.util.ItemCreator;
import me.dev.faceless.LSteal;
import me.dev.faceless.config.MainConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static NamespacedKey HEART_KEY = new NamespacedKey(LSteal.getPlugin(), "heart-key");

    public static ItemStack heartItem(int hearts) {
        List<Component> lore = new ArrayList<>();
        MainConfig.getHeartItemLore().forEach(string -> lore.add(TextContext.formatLegacy(string).decoration(TextDecoration.ITALIC, false)));
        ItemStack heart = ItemCreator.get(MainConfig.getHeartMaterial())
                .setName(TextContext.formatLegacy(MainConfig.getHeartItemName()).decoration(TextDecoration.ITALIC, false))
                .setLore(lore)
                .build();

        ItemMeta meta = heart.getItemMeta();
        meta.getPersistentDataContainer().set(HEART_KEY, PersistentDataType.STRING, "");
        heart.setAmount(hearts);
        heart.setItemMeta(meta);
        return heart;
    }

    public static boolean isHeart(ItemStack item) {
        if(!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(HEART_KEY);
    }

}
