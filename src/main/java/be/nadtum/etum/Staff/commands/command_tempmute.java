package be.nadtum.etum.Staff.commands;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Utility.Objets.DataPunish;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_tempmute implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.modération")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        //tempmute player chiffre format reason

        if(args.length == 0 || args.length == 1 || args.length == 2){
            player.sendMessage( PrefixMessage.erreur() + "/tempmute [player] [duration] (reason)");
            return false;
        }

        // 1d
        String format = args[2];

        int duration = Integer.valueOf(args[1]);

        long time = 0;
        String timesrt = "";

        switch (format){

            case "s":
                time = duration * 1000;
                timesrt = "secondes";
                break;
            case "m":
                time = duration * 1000 * 60;
                timesrt = "minutes";
                break;
            case "h":
                time = duration * 1000 * 60 * 60;
                timesrt = "heure(s)";
                break;
            case "d":
                time = duration * 1000 * 60 * 60 * 24;
                timesrt = "jour(s)";
                break;
            case "w":
                time = duration * 1000 * 60 * 60 * 24 * 7;
                timesrt = "semaine(s)";
                break;
            default:
                player.sendMessage(PrefixMessage.erreur() + "mauvais format de temps");
                return false;
        }

        if(args.length >= 3){
            String reason = "";
            for (int i = 3; i < args.length; i++){
                reason = reason + args[i] + " ";
            }
            reason = reason.trim();

            DataPunish data  = new DataPunish(PlayerGestion.getUUIDFromName(args[0]));

            if(!data.exist()){
                player.sendMessage(PrefixMessage.erreur() + "fichier joueur inexistant contactez un membre de l'administation au plus vite !");
                return false;
            }

            String target = (args[0]);

            if (cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(target) + ".permission.modération")) {
                if(!player.isOp()){
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission de mute un membre du staff");
                    return false;
                }

            }


            if(data.isTempmuted()){
                player.sendMessage(PrefixMessage.erreur() + "le joueur est déjà mute !");
                return false;
            }

            data.setTempmuted(player.getName(), reason.isEmpty() ? "§emute temporaire" : reason, time, duration ,timesrt);
            player.sendMessage(PrefixMessage.admin() + "vous avez mute le joueur §b" + target);
            if(Bukkit.getPlayer(target) == null)return false;

            Bukkit.getPlayer(target).sendMessage(PrefixMessage.admin() + "vous avez été mute pendant §b" + data.getTempmutedChiffre() + " §e" + data.getTempmutedFormat()
                    + " pour §b" + data.getTempmutedReason());

        }

        return false;
    }

}
