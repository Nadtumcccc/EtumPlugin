package be.nadtum.etum.Moderation.commands;

import be.nadtum.etum.Moderation.DataPunish;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_tempban implements CommandExecutor {




    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerBuilder.getPlayerStaffGrade(player.getName()) + ".permission.modération")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        //tempban player chiffre format reason

        if(args.length == 0 || args.length == 1 || args.length == 2){
            player.sendMessage( PrefixMessage.erreur() + "/tempban [player] [duration] (reason)");
            return false;
        }

        // 1d
        String format = args[2];

        int duration = Integer.valueOf(args[1]);

        long time = 0;
        String timestr = "";

        switch (format){

            case "s":
                time = duration * 1000;
                timestr = "secondes";
                break;
            case "m":
                time = duration * 1000 * 60;
                timestr = "minutes";
                break;
            case "h":
                time = duration * 1000 * 60 * 60;
                timestr = "heure(s)";
                break;
            case "d":
                time = duration * 1000 * 60 * 60 * 24;
                timestr = "jour(s)";
                break;
            case "w":
                time = duration * 1000 * 60 * 60 * 24 * 7;
                timestr = "semaine(s)";
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

            DataPunish data  = new DataPunish(PlayerBuilder.getUUIDFromName(args[0]));

            if(data.exist()){
                player.sendMessage(PrefixMessage.erreur() + "fichier joueur inexistant contactez un membre de l'administation au plus vite !");
                return false;
            }

            String target = args[0];


            if (cfg.contains("Grade." + PlayerBuilder.getPlayerGrade(target) + ".permission.modération")) {
                if(!player.isOp()){
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission de ban un membre du staff");
                    return false;
                }

            }


            if(data.isTempbanned()){
                player.sendMessage(PrefixMessage.erreur() + "le joueur est déjà banni !");
                return false;
            }

            data.setTempbanned(player.getName(), reason.isEmpty() ? "§eBan temporaire" : reason, time, duration ,timestr);



            if(target != null){
                Bukkit.getPlayer(target).kickPlayer("§evous avez été ban par §b§l" + data.getTempbannedFrom() +
                        "\n §eà partir de " + data.getTempbannedTimestamp() + " §ependant §b" + duration + " §e" + timestr +
                        "\n §eraison : §b§l" + data.getTempbannedReason());
            }

            player.sendMessage(PrefixMessage.admin() + " vous avez banni le joueur §b" + args[0]);
            for (Player players : Bukkit.getOnlinePlayers()){
                players.sendMessage("§4[§cModération§4] §cle joueur §4" + args[0] + "§c à été banni pour §4" + reason);
            }
        }

        return false;
    }

}
