package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Vanilla.Player.Class.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrefixMessage.logs() + "Vous ne pouvez pas utiliser cette commande");
            return false;
        }

        if (args.length > 0) {
            if (!PlayerClass.getPlayerClass(player).hasPermission("money.manage")) {
                return false;
            }

            if (!isInteger(args[2])) {
                player.sendMessage(PrefixMessage.erreur() + "La valeur n'est pas un nombre");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(PrefixMessage.erreur() + "Le joueur n'est pas en ligne");
                return false;
            }

            int amount = Integer.parseInt(args[2]);
            switch (args[0]) {
                case "add" -> PlayerClass.getPlayerClass(targetPlayer).addMoney(amount);
                case "set" -> PlayerClass.getPlayerClass(targetPlayer).setMoney(amount);
            }

            player.sendMessage(PrefixMessage.serveur() + "Le joueur a maintenant §b" + PlayerClass.getPlayerClass(targetPlayer).getMoney() + " §aAkoins");
            return true;
        }

        player.sendMessage(PrefixMessage.serveur() + "§b" + PlayerClass.getPlayerClass(player).getMoney() + " §aAkoins");

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
