package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_job implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.logs() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(args.length > 0){
            if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin")) {
                if (!player.isOp()) {
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande (faîtes /money)");
                    return false;
                }
            }

            if(!(Integer.valueOf(args[3]) instanceof Integer)){
                player.sendMessage(PrefixMessage.erreur() + "la valeur n'est pas un nombre");
                return false;
            }

            if(!(Bukkit.getPlayer(args[1]) instanceof Player)){
                player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas en ligne");
                return false;
            }
            switch (args[0]){
                case "add":
                    if(args[2].equals("niveau")){
                        PlayerGestion.setPlayerJobNiveau(args[1], PlayerGestion.getPlayerJobNiveau(args[1]) + Integer.valueOf(args[3]));
                        return false;
                    }

                    if(args[2].equals("xp")){
                        PlayerGestion.setPlayerJobXp(args[1], PlayerGestion.getPlayerJobXp(args[1]) + Integer.valueOf(args[3]));
                        return false;
                    }
                    break;
                case "set":
                    break;
            }
        }




        return false;
    }
}
