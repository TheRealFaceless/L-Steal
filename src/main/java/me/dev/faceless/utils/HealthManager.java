package me.dev.faceless.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class HealthManager {

    public static boolean increaseMaxHealth(Player player, int amount, Predicate<Player> condition) {
        if (!condition.test(player)) return false;
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.setBaseValue(healthAttribute.getBaseValue() + amount);
            return true;
        }
        return false;
    }

    public static boolean reduceMaxHealth(Player player, int amount, Predicate<Player> condition) {
        if (!condition.test(player)) return false;
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            double newHealth = Math.max(0, healthAttribute.getBaseValue() - amount);
            healthAttribute.setBaseValue(newHealth);
            return true;
        }
        return false;
    }

    public static boolean setMaxHealth(Player player, int amount, Predicate<Player> condition) {
        if (!condition.test(player)) return false;
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.setBaseValue(amount);
            return true;
        }
        return false;
    }

    public static int getMaxHealth(Player player) {
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        return healthAttribute != null ? (int) healthAttribute.getBaseValue() : 0;
    }
}
