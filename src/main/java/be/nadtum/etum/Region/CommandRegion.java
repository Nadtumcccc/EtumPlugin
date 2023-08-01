package be.nadtum.etum.Region;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class CommandRegion implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (PlayerGestion.hasPermission(player,"admin")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 2){
            switch (args[0]){
                case "create":
                    RegionManage.setRegion(args[1], player);
                    RegionManage.removeCache(player);
                    player.sendMessage(PrefixMessage.admin() + "création de la region " + args[1]);
                    break;
                case "delete":
                    RegionManage.deleteRegion(args[1]);
                    player.sendMessage(PrefixMessage.admin() + "suppression de la region " + args[1]);
                    break;
                default:
                    player.sendMessage(PrefixMessage.erreur() + "/rg [create/delete] name of region");
                    break;
            }

        }

        return false;
    }
}
