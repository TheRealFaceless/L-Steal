package me.dev.faceless.items.recipes;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.util.EnumUtils;
import me.dev.faceless.LSteal;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.Items;
import me.dev.faceless.utils.RecipeUtils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Recipes {
    private static final Config MAIN_CONFIG = MainConfig.MAIN_CONFIG;
    public static final String heartRecipe = MainConfig.heartRecipe;

    public static final List<String> defHeartShape = List.of(
            "DDD",
            "DGD",
            "DDD"
    );

    public static final List<String> defHeartIngredients = List.of(
            "D:DIAMOND_BLOCK",
            "G:GOLD_BLOCK"
    );

    private static final NamespacedKey HEART_RECIPE_KEY = new NamespacedKey(LSteal.getPlugin(), "heart_recipe");

    public static void registerHeart(JavaPlugin plugin) {
        if (!MainConfig.isHeartCraftingEnabled()) return;

        RecipeUtils.ConfigRecipeResult recipeResult = RecipeUtils.deserializeRecipe(MAIN_CONFIG.getConfig(), heartRecipe);
        List<String> heartShape = !recipeResult.shape().isEmpty() ? recipeResult.shape() : defHeartShape;
        List<String> heartKeys = !recipeResult.keys().isEmpty() ? recipeResult.keys() : defHeartIngredients;

        Map<Character, Material> ingredientMap = parseKeys(heartKeys);

        ShapedRecipe heartRecipe = new ShapedRecipe(HEART_RECIPE_KEY, Items.heartItem(1));
        heartRecipe.shape(heartShape.toArray(new String[0]));

        for (Map.Entry<Character, Material> entry : ingredientMap.entrySet()) {
            heartRecipe.setIngredient(entry.getKey(), new RecipeChoice.MaterialChoice(entry.getValue()));
        }

        plugin.getServer().addRecipe(heartRecipe);
    }

    private static Map<Character, Material> parseKeys(List<String> keys) {
        Map<Character, Material> ingredientMap = new HashMap<>();
        for (String key : keys) {
            String[] parts = key.split(":");
            if (parts.length == 2) {
                char symbol = parts[0].charAt(0);
                Material material = Material.getMaterial(parts[1].toUpperCase());
                if (material == null) material = EnumUtils.findClosestEnum(parts[1].toUpperCase(), Material.values());
                if (material != null) ingredientMap.put(symbol, material);
            }
        }
        return ingredientMap;
    }
}

