package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            // Display the player's Akoins balance
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(PrefixMessage.serveur() + "§b" + PlayerBuilder.getPlayerMoney(player.getName()) + " §aAkoins");
            } else {
                // Console can't use this part of the command
                sender.sendMessage("This command is only available for players.");
            }
            return true;
        }

        if (!sender.hasPermission("money.manage")) {
            sender.sendMessage(PrefixMessage.erreur() + "You don't have permission to use this command.");
            return false;
        }

        if (!isInteger(args[2])) {
            sender.sendMessage(PrefixMessage.erreur() + "The value is not a number.");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(PrefixMessage.erreur() + "The player is not online.");
            return false;
        }

        int amount = Integer.parseInt(args[2]);
        switch (args[0]) {
            case "add" -> {
                PlayerBuilder.addPlayerMoney(targetPlayer.getName(), (long) amount);
            }
            case "set" -> {
                PlayerBuilder.setPlayerMoney(targetPlayer.getName(), (long) amount);
            }
            default -> {
                sender.sendMessage(PrefixMessage.erreur() + "Invalid usage. Use /money add/set <player> <amount>");
                return false;
            }
        }

        sender.sendMessage(PrefixMessage.serveur() + "The player now has §b" + PlayerBuilder.getPlayerMoney(targetPlayer.getName()) + " §aAkoins");
        return true;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
