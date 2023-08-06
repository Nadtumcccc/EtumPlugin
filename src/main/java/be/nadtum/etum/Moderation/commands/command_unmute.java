package be.nadtum.etum.Moderation.commands;


import be.nadtum.etum.Moderation.DataPunish;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_unmute implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player player)){
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!cfg.contains("Grade." + PlayerBuilder.getPlayerStaffGrade(player.getName()) + ".permission.modération") ){
            if(!player.isOp()){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(!(args.length == 1)){
            player.sendMessage(PrefixMessage.erreur() + "/unmute [player]");
            return false;
        }

        DataPunish data = new DataPunish(PlayerBuilder.getUUIDFromName(args[0]));

        if(data.exist()){
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
