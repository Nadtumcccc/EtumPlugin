package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Vanilla.Player.Class.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandPay implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrefixMessage.logs() + "Vous ne pouvez pas utiliser cette commande");
            return false;
        }

        if (args.length != 2) {
            player.sendMessage(PrefixMessage.erreur() + "/pay [player] [money]");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PrefixMessage.erreur() + "Le joueur n'est pas en ligne");
            return false;
        }

        int sendMoney;
        try {
            sendMoney = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(PrefixMessage.erreur() + "/pay [player] [money]");
            return false;
        }

        PlayerClass senderClass = PlayerClass.getPlayerClass(player);
        PlayerClass targetClass = PlayerClass.getPlayerClass(target);

        if (senderClass.getMoney() < sendMoney) {
            player.sendMessage(PrefixMessage.erreur() + "Pas assez de Akoins");
            return false;
        }

        senderClass.addMoney(-sendMoney);
        targetClass.addMoney(sendMoney);

        player.sendMessage(PrefixMessage.serveur() + "Vous avez envoyé §b" + sendMoney + " §aAkoins à §b" + target.getName());
        target.sendMessage(PrefixMessage.serveur() + "Vous avez reçu §b" + sendMoney + " §aAkoins de §b" + player.getName());

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return new ArrayList<>();
    }
}
