package me.dev.faceless.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RecipeUtils {

    public static void serializeRecipe(List<String> shape, List<String> keys, FileConfiguration config, String path) {
        config.set(path + ".shape", shape);
        config.set(path + ".key", keys);
    }

    public static ConfigRecipeResult deserializeRecipe(FileConfiguration config, String path) {
        List<String> shapeList = config.getStringList(path + ".shape");
        if (shapeList.isEmpty()) return new ConfigRecipeResult(List.of(), List.of());

        List<String> keysList = config.getStringList(path + ".key");
        if (keysList.isEmpty()) return new ConfigRecipeResult(shapeList, List.of());

        return new ConfigRecipeResult(shapeList, keysList);
    }

    public record ConfigRecipeResult(List<String> shape, List<String> keys) {}

}
