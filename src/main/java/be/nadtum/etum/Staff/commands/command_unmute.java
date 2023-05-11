package be.nadtum.etum.Staff.commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Utility.Objets.DataPunish;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_unmute implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player)sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.modération") ){
            if(!player.isOp()){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(!(args.length == 1)){
            player.sendMessage(PrefixMessage.erreur() + "/unmute [player]");
            return false;
        }

        DataPunish data = new DataPunish(PlayerGestion.get_uuid_from_name(args[0]));

        if(!data.exist()){
            player.sendMessage(PrefixMessage.erreur() + "fichier joueur inexistant contactez un membre de l'administation au plus vite !");
            return false;
        }

        if(!data.isTempmuted()){
            player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas mute !");
            return false;
        }

        data.setUnTempmuted();

        player.sendMessage( PrefixMessage.admin() + "vous avez bien unmute §b" + args[0]);



        return false;
    }

}
