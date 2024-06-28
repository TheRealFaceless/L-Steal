package me.dev.faceless.commands;

import dev.faceless.swiftlib.lib.command.Command;
import dev.faceless.swiftlib.lib.command.CommandContext;
import dev.faceless.swiftlib.lib.command.ICommand;
import dev.faceless.swiftlib.lib.storage.yaml.ConfigManager;
import me.dev.faceless.config.MainConfig;
import me.dev.faceless.items.Items;
import me.dev.faceless.message.MessageManager;
import me.dev.faceless.utils.HealthManager;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public class LStealCommand extends Command {

    public LStealCommand() {
        super("lsteal", "", "", List.of("ls"));
    }

    @ICommand(permission = "ls.reload")
    public void reload(CommandContext context) {
        final long startTime = System.currentTimeMillis();
        ConfigManager.getManager().reloadAll();
        MessageManager.reload();
        if(context.sender() instanceof Player player) {
            MessageManager.getMessage(MessageManager.configReloadedPath).replace("{ms}", (System.currentTimeMillis() - startTime) + "ms").send(player);
        }
    }

    @ICommand(permission = "ls.default")
    public void withdraw(CommandContext context) {
        if(!(context.sender() instanceof Player player)) return;
        String[] args = context.args();
        if(args.length != 2) return;
        String amountStr = args[1];
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        }catch (NumberFormatException e) {
            MessageManager.getMessage(MessageManager.invalidArgumentPath).send(player);
            return;
        }
        int minHeartLimit = MainConfig.getMinHeartLimit();
        int currentMaxHealth = HealthManager.getMaxHealth(player);

        int finalAmount = amount;
        if (HealthManager.reduceMaxHealth(player, amount, p -> currentMaxHealth - finalAmount >= minHeartLimit)) {
            player.getInventory().addItem(Items.heartItem(amount));
            MessageManager.getMessage(MessageManager.withdrewHeartsPath)
                    .replace("{amount}", String.valueOf(amount))
                    .send(player);
        } else {
            MessageManager.getMessage(MessageManager.notEnoughHeartsToWithdrawPath).send(player);
        }
    }
}
