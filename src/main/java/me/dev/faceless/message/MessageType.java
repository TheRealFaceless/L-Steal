package me.dev.faceless.message;

import dev.faceless.swiftlib.lib.text.TextContext;
import me.dev.faceless.LSteal;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum MessageType {
    CHAT {
        @Override
        public void send(Player player, Message message) {
            if (message.getFormat() == MessageFormat.LEGACY) player.sendMessage(TextContext.formatLegacy(message.getText()));
            else player.sendMessage(TextContext.format(message.getText()));
        }
    },
    ACTION_BAR {
        @Override
        public void send(Player player, Message message) {
            if (message.getFormat() == MessageFormat.LEGACY) player.sendActionBar(TextContext.formatLegacy(message.getText()));
            else player.sendActionBar(TextContext.format(message.getText()));
        }
    },
    TITLE {
        @Override
        public void send(Player player, Message message) {
            if (message.getFormat() == MessageFormat.LEGACY) player.showTitle(Title.title(TextContext.formatLegacy(message.getText()), Component.empty()));
            else player.showTitle(Title.title(TextContext.format(message.getText()), Component.empty()));
        }
    },
    SUBTITLE {
        @Override
        public void send(Player player, Message message) {
            if (message.getFormat() == MessageFormat.LEGACY) player.showTitle(Title.title(Component.empty(), TextContext.formatLegacy(message.getText())));
            else player.showTitle(Title.title(Component.empty(), TextContext.format(message.getText())));
        }
    },
    BOSSBAR {
        @Override
        public void send(Player player, Message message) {
            BossBar bossBar;
            if (message.getFormat() == MessageFormat.LEGACY) bossBar = BossBar.bossBar(TextContext.formatLegacy(
                    message.getText()),
                    0,
                    message.getBossbarColor() == null ? BossBar.Color.PURPLE : message.getBossbarColor(),
                    message.getBossbarOverlay() == null ? BossBar.Overlay.NOTCHED_6 : message.getBossbarOverlay());
            else bossBar = BossBar.bossBar(TextContext.format(
                    message.getText()),
                    0,
                    message.getBossbarColor() == null ? BossBar.Color.PURPLE : message.getBossbarColor(),
                    message.getBossbarOverlay() == null ? BossBar.Overlay.NOTCHED_6 : message.getBossbarOverlay());
            player.showBossBar(bossBar);
            Bukkit.getScheduler().runTaskLater(LSteal.getPlugin(), ()-> player.hideBossBar(bossBar), 120);
        }
    };

    public abstract void send(Player player, Message message);
}
