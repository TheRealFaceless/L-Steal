package me.dev.faceless;

import dev.faceless.swiftlib.SwiftLib;
import dev.faceless.swiftlib.lib.command.Command;
import lombok.Getter;
import me.dev.faceless.commands.LStealCommand;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.recipes.Recipes;
import me.dev.faceless.listeners.GeneralListener;
import me.dev.faceless.message.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LSteal extends JavaPlugin {
    @Getter private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        SwiftLib.onEnable(this);
        plugin = this;

        GeneralListener.init();

        MainConfig.init();
        MessageManager.initDefaultMessages();

        Recipes.registerHeart(this);

        Command.register(this, new LStealCommand());
    }
}
