package be.nadtum.etum.Vanilla.Commands;

import be.nadtum.etum.Utility.Modules.CityGestion;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandClaim implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.logs() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;
        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!PlayerGestion.hasPermission(player, "default")) return true;

        if (cmd.getName().equalsIgnoreCase("claim")) {
            if (args.length > 0) {
                if(PlayerGestion.hasPermission(player, "admin")) return true;


                if (!isInteger(args[2])) {
                    player.sendMessage(PrefixMessage.erreur() + "la valeur n'est pas un nombre");
                    return false;
                }

                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas en ligne");
                    return false;
                }

                int amount = Integer.parseInt(args[2]);
                switch (args[0]) {
                    case "add":
                        PlayerGestion.setPlayerClaimCount(args[1], PlayerGestion.getPlayerClaimCount(args[1]) + amount);
                        break;
                    case "set":
                        PlayerGestion.setPlayerClaimCount(args[1], amount);
                        break;
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("unclaim")) {
            if (!CityGestion.hasPermission(player.getName(), "claim")) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission de gérer le claim de la cité");
                return false;
            }

            if (!FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone")) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas de claim");
                return false;
            }

            FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone", null);

            player.sendMessage(PrefixMessage.serveur() + "le claim de votre cité à été supprimé");
            return false;
        }

        player.sendMessage(PrefixMessage.serveur() + "§b" + PlayerGestion.getPlayerClaimCount(player.getName()) + " §aclaim");

        return false;
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
