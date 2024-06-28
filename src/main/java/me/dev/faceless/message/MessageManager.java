package me.dev.faceless.message;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager {
    private static final Config MESSAGES_CONFIG = ConfigManager.getManager().createOrGetConfig("messages.yml");
    private static final Map<String, Message> messages = new HashMap<>();
    public static final List<String> comments = List.of(
            " Configuration for Messages",
            " =========================",
            "",
            " This configuration file contains all messages used in the plugin.",
            " You can customize the messages by changing the text and types below.",
            "",
            " Message Types:",
            " - CHAT: Sends a chat message to the player.",
            " - ACTION_BAR: Sends an action bar message to the player.",
            " - TITLE: Sends a title message to the player.",
            " - SUBTITLE: Sends a subtitle message to the player.",
            " - BOSSBAR: Displays a boss bar message to the player.",
            "",
            " Message Formats:",
            " - LEGACY: Uses legacy formatting codes (e.g., &a for green).",
            " - MINI_MESSAGE: Uses MiniMessage formatting. (e.g <green> for green)",
            "",
            " Example of a BossBar message setup:",
            "",
            " path:",
            "   text: \"Your message here\"",
            "   type: \"BOSSBAR\"",
            "   boss-bar-color: \"RED\"",
            "   boss-bar-overlay: \"NOTCHED_6\"",
            "   format: \"LEGACY\"",
            "",
            " BossBar Colors:",
            " - PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE",
            "",
            " BossBar Overlays:",
            " - PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20",
            "",
            " There are currently 2 placeholders,",
            " - config-reloaded (\"{ms}\") = time it takes config to reload",
            " - withdrew-hearts (\"{amount}\") = amount of hearts withdrawn"
    );

    public static final String configReloadedPath = "config-reloaded";
    public static final String invalidArgumentPath = "invalid-argument";
    public static final String withdrewHeartsPath = "withdrew-hearts";
    public static final String consumedHeartsPath = "consumed-hearts";
    public static final String notEnoughHeartsToWithdrawPath = "not-enough-hearts-to-withdraw";

    public static void initDefaultMessages() {
        Message configReloaded = new Message(configReloadedPath, "&aConfiguration files reloaded after {ms}.", MessageType.CHAT, MessageFormat.LEGACY);
        Message invalidArgument = new Message(invalidArgumentPath, "&cInvalid Argument!", MessageType.CHAT, MessageFormat.LEGACY);
        Message consumedHearts = new Message(consumedHeartsPath, "&aYou have consumed a heart", MessageType.CHAT, MessageFormat.LEGACY);
        Message withdrewHearts = new Message(withdrewHeartsPath, "&aSuccessfully withdrew {amount} hearts!", MessageType.CHAT, MessageFormat.LEGACY);
        Message notEnoughHeartsToWithdraw = new Message(notEnoughHeartsToWithdrawPath, "&cNot enough hearts to withdraw!", MessageType.CHAT, MessageFormat.LEGACY);

        registerMessage(configReloaded);
        registerMessage(invalidArgument);
        registerMessage(consumedHearts);
        registerMessage(withdrewHearts);
        registerMessage(notEnoughHeartsToWithdraw);

        MESSAGES_CONFIG.getConfig().setComments(configReloadedPath , comments);
        MESSAGES_CONFIG.save();
    }

    public static void registerMessage(Message message) {
        if(!MESSAGES_CONFIG.contains(message.getPath())) {
            messages.put(message.getPath(), message);
            message.serialize(MESSAGES_CONFIG);
        }else loadMessage(message.getPath());
    }

    public static void loadMessage(String path) {
        Map<String, Object> messageMap = null;
        try {
           messageMap = MESSAGES_CONFIG.get(path, Map.class);
        }catch (ClassCastException ignored){}

        if (messageMap == null) return;
        Message message = Message.deserialize(path, messageMap);
        messages.put(message.getPath(), message);
    }

    public static Message getMessage(String path) {
        Message message = messages.get(path);
        if(message != null) return message;
        initDefaultMessages();
        return messages.get(path);
    }

    public static List<Message> getMessages() {
        return messages.values().stream().toList();
    }

    public static void reload() {
        messages.clear();
        initDefaultMessages();
    }
}
