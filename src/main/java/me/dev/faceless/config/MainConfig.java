package me.dev.faceless.config;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;
import dev.faceless.swiftlib.lib.text.ConsoleLogger;
import dev.faceless.swiftlib.lib.util.EnumUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class MainConfig {
    private static final Config MAIN_CONFIG = ConfigManager.getManager().createOrGetConfig("config.yml");

    public static final String maxHeartLimit = "settings.max-heart-limit";
    public static final String minHeartLimit = "settings.min-heart-limit";
    public static final String reviveBeaconEnabled = "settings.revive-beacon-enabled";
    public static final String heartCraftingEnabled = "settings.heart-crafting-enabled";
    public static final String environmentalHeartLose = "settings.environmental-heart-lose";

    public static final String heartMaterial = "heart.heart-material";
    public static final String heartItemName = "heart.heart-item-name";
    public static final String heartItemLore = "heart.heart-item-lore";

    public static final String heartRecipe = "recipes.heart-recipe";
    public static final String reviveBeaconRecipe = "recipes.revive-beacon-recipe";

    public static void init() {
        Map<String, Object> values = new HashMap<>();
        values.put(maxHeartLimit, 40);
        values.put(minHeartLimit, 5);
        values.put(reviveBeaconEnabled, true);
        values.put(heartCraftingEnabled, true);
        values.put(environmentalHeartLose, false);

        values.put(heartMaterial, Material.RED_DYE.name());
        values.put(heartItemName, "&cHeart");
        values.put(heartItemLore, List.of("&eLine 1", "&eLine 2"));

        values.put(heartRecipe, "");
        values.put(reviveBeaconRecipe, "");

        values.forEach((key, value)-> {
            if(!MAIN_CONFIG.contains(key)) MAIN_CONFIG.set(key, value);
        });
    }

    public static int getMaxHeartLimit() {
        return Math.min(getConfigValue(maxHeartLimit, Integer.class), 255);
    }

    public static int getMinHeartLimit() {
        return Math.max(getConfigValue(minHeartLimit, Integer.class), 0);
    }

    public static boolean isReviveBeaconEnabled() {
        return getConfigValue(reviveBeaconEnabled, Boolean.class);
    }

    public static boolean isHeartCraftingEnabled() {
        return getConfigValue(heartCraftingEnabled, Boolean.class);
    }

    public static boolean isEnvironmentalHeartLoseEnabled() {
        return getConfigValue(environmentalHeartLose, Boolean.class);
    }

    public static String getHeartRecipe() {
        return getConfigValue(heartRecipe, String.class);
    }

    public static String getReviveBeaconRecipe() {
        return getConfigValue(reviveBeaconRecipe, String.class);
    }

    public static Material getHeartMaterial() {
        String heartMaterialStr = getConfigValue(heartMaterial, String.class);
        if (heartMaterialStr == null) {
            ConsoleLogger.logError("Failed to get heart material because it is null, using default material.");
            return Material.RED_DYE;
        }
        Material heartMaterial;
        try {
            heartMaterial = Material.valueOf(heartMaterialStr);
        }catch (IllegalArgumentException e) {
            heartMaterial = EnumUtils.findClosestEnum(heartMaterialStr, Material.values());
        }
        return heartMaterial;
    }

    public static String getHeartItemName() {
        return getConfigValue(heartItemName, String.class);
    }

    public static List<String> getHeartItemLore() {
        try {
            @SuppressWarnings("unchecked")
            List<String> lore = getConfigValue(heartItemLore, List.class);
            return lore;
        }catch (ClassCastException | NullPointerException ignored) {}
        return List.of();
    }

    private static <T> T getConfigValue(String path, Class<T> clazz) {
        if (MAIN_CONFIG.get(path, clazz) == null) init();
        return MAIN_CONFIG.get(path, clazz);
    }
}
