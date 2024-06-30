package me.dev.faceless.config;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;
import dev.faceless.swiftlib.lib.text.ConsoleLogger;
import dev.faceless.swiftlib.lib.util.EnumUtils;
import me.dev.faceless.utils.RecipeUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class MainConfig {
    public static final Config MAIN_CONFIG = ConfigManager.getManager().createOrGetConfig("config.yml");

    public static final String maxHeartLimit = "settings.max-heart-limit";
    public static final String minHeartLimit = "settings.min-heart-limit";
    public static final String heartCraftingEnabled = "settings.heart-crafting-enabled";
    public static final String environmentalHeartLose = "settings.environmental-heart-lose";
    public static final String environmentalHeartDrop = "settings.environmental-heart-drop";
    public static final String autoHeartGainOnKill = "settings.auto-heart-gain-on-kill";

    public static final String heartMaterial = "heart.heart-material";
    public static final String heartItemName = "heart.heart-item-name";
    public static final String heartItemData = "heart.heart-item-custom-model-data";
    public static final String heartItemLore = "heart.heart-item-lore";


    public static final String heartRecipe = "recipes.heart-recipe";

    public static List<String> defHeartShape = List.of(
            "DDD",
            "DGD",
            "DDD");

    public static List<String> defHeartIngredients = List.of(
            "D:DIAMOND_BLOCK",
            "G:GOLD_BLOCK"
    );

    public static void init() {
        Map<String, Object> values = new HashMap<>();
        values.put(maxHeartLimit, 40);
        values.put(minHeartLimit, 5);
        values.put(heartCraftingEnabled, true);
        values.put(environmentalHeartLose, false);
        values.put(environmentalHeartDrop, false);
        values.put(autoHeartGainOnKill, false);

        values.put(heartMaterial, Material.RED_DYE.name());
        values.put(heartItemName, "&cHeart");
        values.put(heartItemData, 0);
        values.put(heartItemLore, List.of("&eLine 1", "&eLine 2"));

        values.forEach((key, value)-> {if(!MAIN_CONFIG.contains(key)) MAIN_CONFIG.set(key, value, false);});
        if(!MAIN_CONFIG.contains(heartRecipe)) RecipeUtils.serializeRecipe(defHeartShape, defHeartIngredients, MAIN_CONFIG.getConfig(), heartRecipe);
        MAIN_CONFIG.save();
    }

    public static int getMaxHeartLimit() {
        return Math.min(getConfigValue(maxHeartLimit, Integer.class) * 2, 255);
    }

    public static int getMinHeartLimit() {
        return Math.max(getConfigValue(minHeartLimit, Integer.class) * 2, 0);
    }

    public static boolean isHeartCraftingEnabled() {
        return getConfigValue(heartCraftingEnabled, Boolean.class);
    }

    public static boolean isEnvironmentalHeartLoseEnabled() {
        return getConfigValue(environmentalHeartLose, Boolean.class);
    }

    public static boolean isEnvironmentalHeartDropEnabled() {
        return getConfigValue(environmentalHeartDrop, Boolean.class);
    }

    public static boolean isAutoHeartGainOnKill() {
        return getConfigValue(autoHeartGainOnKill, Boolean.class);
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

    public static Integer getHeartItemData() {
        return getConfigValue(heartItemData, Integer.class);
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
