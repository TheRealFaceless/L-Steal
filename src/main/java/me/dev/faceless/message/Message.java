package me.dev.faceless.message;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.text.ConsoleLogger;
import dev.faceless.swiftlib.lib.util.EnumUtils;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Message {
    private final String path;
    private String text;
    private final MessageType type;
    private final MessageFormat format;

    private BossBar.Color bossbarColor = BossBar.Color.PURPLE;
    private BossBar.Overlay bossbarOverlay = BossBar.Overlay.NOTCHED_6;

    public Message(String path, String text, MessageType type, MessageFormat format) {
        this.path = path;
        this.text = text;
        this.type = type;
        this.format = format;
    }

    public Message(String path, String text, MessageType type, MessageFormat format, BossBar.Color bossbarColor, BossBar.Overlay bossbarOverlay) {
        this.path = path;
        this.text = text;
        this.type = type;
        this.format = format;
        this.bossbarColor = bossbarColor;
        this.bossbarOverlay = bossbarOverlay;
    }

    public void serialize(Config config) {
        Map<String, Object> map = new HashMap<>();
        map.put("text", this.text);
        map.put("type", this.type.name());
        map.put("format", this.format.name());
        if(type == MessageType.BOSSBAR) {
            map.put("boss-bar-color", bossbarColor);
            map.put("boss-bar-overlay", bossbarOverlay);
        }

        config.set(path, map, false);
    }

    public Message replace(String a, String b) {
        text = text.replace(a, b);
        return this;
    }

    public void send(Player... players) {
        Arrays.asList(players).forEach(player -> this.type.send(player, this));
    }

    public static Message deserialize(String path, Map<String, Object> map) {
        String text = (String) map.get("text");
        String typeString = (String) map.get("type");
        String formatString = (String) map.get("format");
        MessageType type;
        MessageFormat format;

        try {
            type = MessageType.valueOf(typeString);
        } catch (IllegalArgumentException e) {
            type = EnumUtils.findClosestEnum(typeString, MessageType.values());
        }

        try {
            format = MessageFormat.valueOf(formatString);
        } catch (IllegalArgumentException e) {
            format = EnumUtils.findClosestEnum(formatString, MessageFormat.values());
        }

        if (type == MessageType.BOSSBAR) {
            String colorString = (String) map.get("type.color");
            String overlayString = (String) map.get("type.overlay");
            BossBar.Color bossbarColor = BossBar.Color.PURPLE;
            BossBar.Overlay bossbarOverlay = BossBar.Overlay.NOTCHED_6;

            if(colorString != null) {
                try {
                    bossbarColor = BossBar.Color.valueOf(colorString);
                } catch (IllegalArgumentException | NullPointerException e) {
                    bossbarColor = EnumUtils.findClosestEnum(colorString, BossBar.Color.values());
                    if (bossbarColor == null)
                        ConsoleLogger.logInfo("Invalid BossBar color for message at path: " + path);
                }
            }

            if (overlayString != null) {
                try {
                    bossbarOverlay = BossBar.Overlay.valueOf(overlayString);
                } catch (IllegalArgumentException | NullPointerException e) {
                    bossbarOverlay = EnumUtils.findClosestEnum(overlayString, BossBar.Overlay.values());
                    if (bossbarOverlay == null)
                        ConsoleLogger.logInfo("Invalid BossBar overlay for message at path: " + path);
                }
            }
            return new Message(path, text, type, format, bossbarColor, bossbarOverlay);
        }
        return new Message(path, text, type, format);
    }

    @Override
    public String toString() {
        return "Message{" +
                "path='" + path + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                '}';
    }
}
