package me.dev.faceless.commands;

import dev.faceless.swiftlib.lib.command.Command;
import dev.faceless.swiftlib.lib.command.CommandContext;
import dev.faceless.swiftlib.lib.command.ICommand;
import dev.faceless.swiftlib.lib.command.ITabComplete;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.Items;
import me.dev.faceless.message.MessageManager;
import me.dev.faceless.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class LStealCommand extends Command {

    public LStealCommand() {
        super("lsteal", "", "", List.of("ls"));
    }

    @ICommand(permission = "ls.admin")
    public void reload(CommandContext context) {
        final long startTime = System.currentTimeMillis();
        ConfigManager.getManager().reloadAll();
        MessageManager.reload();
        if(context.sender() instanceof Player player) {
            MessageManager.getMessage(MessageManager.configReloadedPath).replace("{ms}", (System.currentTimeMillis() - startTime) + "ms").send(player);
        }
    }

    @ICommand(permission = "ls.admin", tabCompleter = "playerscompleter")
    public void set(CommandContext context) {
        if(!(context.sender() instanceof Player player)) return;
        String[] args = context.args();
        if(args.length != 3) return;
        String playerName = args[1];
        String amountStr = args[2];
        System.out.println(amountStr);

        Player target = Bukkit.getPlayer(playerName);
        if(target == null) {
            MessageManager.getMessage(MessageManager.playerNotFoundPath).send(player);
            return;
        }
        try {
            final int amount = Integer.parseInt(amountStr) * 2;
            int minHeartLimit = MainConfig.getMinHeartLimit();
            int maxHeartLimit = MainConfig.getMaxHeartLimit();

            if(!HealthManager.setMaxHealth(target, amount, p -> amount <= maxHeartLimit && amount >= minHeartLimit)) {
                MessageManager.getMessage(MessageManager.cannotModifyPath).replace("{player}", target.getName()).send(player);
                return;
            }
            MessageManager.getMessage(MessageManager.setPlayerHealthPath).replace("{player}", target.getName()).replace("{amount}", amountStr).send(player);
        }catch (NumberFormatException e){
            MessageManager.getMessage(MessageManager.invalidArgumentPath).send(player);
        }
    }

    @ICommand(permission = "ls.admin", tabCompleter = "playerscompleter")
    public void add(CommandContext context) {
        if(!(context.sender() instanceof Player player)) return;
        String[] args = context.args();
        if(args.length != 3) return;
        String playerName = args[1];
        String amountStr = args[2];

        Player target = Bukkit.getPlayer(playerName);
        if(target == null) {
            MessageManager.getMessage(MessageManager.playerNotFoundPath).send(player);
            return;
        }
        try {
            final int amount = Integer.parseInt(amountStr) * 2;
            int minHeartLimit = MainConfig.getMinHeartLimit();
            int maxHeartLimit = MainConfig.getMaxHeartLimit();

            if(!HealthManager.increaseMaxHealth(target, amount, p -> {
                int currentHealth = HealthManager.getMaxHealth(p);
                int newHealth = currentHealth + amount;
                return newHealth <= maxHeartLimit && newHealth >= minHeartLimit;
            })) {
                MessageManager.getMessage(MessageManager.cannotModifyPath).replace("{player}", target.getName()).send(player);
                return;
            }
            MessageManager.getMessage(MessageManager.addedToPlayerHealthPath).replace("{player}", target.getName()).replace("{amount}", amountStr).send(player);
        }catch (NumberFormatException e){
            MessageManager.getMessage(MessageManager.invalidArgumentPath).send(player);
        }
    }

    @ICommand(permission = "ls.default")
    public void withdraw(CommandContext context) {
        if(!(context.sender() instanceof Player player)) return;
        String[] args = context.args();
        if(args.length != 2) return;
        String amountStr = args[1];

        try {
            final int amount = Integer.parseInt(amountStr) * 2;
            int minHeartLimit = MainConfig.getMinHeartLimit();
            int currentMaxHealth = HealthManager.getMaxHealth(player);

            if (HealthManager.reduceMaxHealth(player, amount, p -> currentMaxHealth - amount >= minHeartLimit)) {
                player.getInventory().addItem(Items.heartItem(amount / 2));
                MessageManager.getMessage(MessageManager.withdrewHeartsPath)
                        .replace("{amount}", amountStr)
                        .send(player);
            } else {
                MessageManager.getMessage(MessageManager.notEnoughHeartsToWithdrawPath).send(player);
            }
        }catch (NumberFormatException e) {
            MessageManager.getMessage(MessageManager.invalidArgumentPath).send(player);
        }
    }

    @ITabComplete(name = "playerscompleter")
    public List<String> playerscompleter(CommandContext context) {
        String[] args = context.args();
        if(args.length != 2) return List.of();
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(string -> string.startsWith(args[1]))
                .toList();
    }
}
