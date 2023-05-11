package be.nadtum.etum.Vanilla.Economie.commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_money implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.logs() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;
        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!PlayerGestion.hasPermission(player, "default"))return false;

        if (args.length > 0) {
            if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin")) {
                if (!player.isOp()) {
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande (faîtes /money)");
                    return false;
                }
            }

            if (!isInteger(args[2])) {
                player.sendMessage(PrefixMessage.erreur() + "la valeur n'est pas un nombre");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas en ligne");
                return false;
            }

            Long amount = (long) Integer.parseInt(args[2]);
            switch (args[0]) {
                case "add":
                    PlayerGestion.setPlayerMoney(args[1], PlayerGestion.getPlayerMoney(args[1]) + amount);
                    break;
                case "set":
                    PlayerGestion.setPlayerMoney(args[1], amount);
                    break;
            }

            player.sendMessage(PrefixMessage.serveur() + "le joueur a maintenant §b" + PlayerGestion.getPlayerMoney(args[1]) + " §aAkoins");
            return true;
        }

        player.sendMessage(PrefixMessage.serveur() + "§b" + PlayerGestion.getPlayerMoney(player.getName()) + " §aAkoins");

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
