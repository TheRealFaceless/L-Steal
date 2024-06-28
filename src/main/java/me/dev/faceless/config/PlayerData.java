package me.dev.faceless.config;

import dev.faceless.swiftlib.lib.storage.yaml.Config;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerData {
    private static final Config PLAYER_DATA_CONFIG = ConfigManager.getManager().createOrGetConfig("data/player-data.yml");

    public static void addPlayerToBannedList(Player player) {
        String uuid = String.valueOf(player.getUniqueId());
        List<String> bannedPlayers = getPlayerDataConfig().getConfig().getStringList("banned-players");

        if (!bannedPlayers.contains(uuid)) {
            bannedPlayers.add(uuid);
            getPlayerDataConfig().set("banned-players", bannedPlayers);
            savePlayerDataConfig();
        }
    }

    public static boolean isPlayerBanned(Player player) {
        String uuid = String.valueOf(player.getUniqueId());
        List<String> bannedPlayers = getPlayerDataConfig().getConfig().getStringList("banned-players");

        return bannedPlayers.contains(uuid);
    }

    public static void removePlayerFromBannedList(Player player) {
        String uuid = String.valueOf(player.getUniqueId());
        List<String> bannedPlayers = getPlayerDataConfig().getConfig().getStringList("banned-players");

        if (bannedPlayers.contains(uuid)) {
            bannedPlayers.remove(uuid);
            getPlayerDataConfig().set("banned-players", bannedPlayers);
            savePlayerDataConfig();
        }
    }

    private static Config getPlayerDataConfig() {
        return PLAYER_DATA_CONFIG;
    }

    private static void savePlayerDataConfig() {
        PLAYER_DATA_CONFIG.save();
    }
}
