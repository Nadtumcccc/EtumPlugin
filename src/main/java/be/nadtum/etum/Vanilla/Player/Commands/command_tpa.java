package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class command_tpa implements CommandExecutor {




    private static HashMap<Player, Long> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.default")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 1){
            if(cmd.getName().equalsIgnoreCase("tpa")){

                Player target = Bukkit.getPlayer(args[0]);
                //on vérifie que le target est toujours en ligne
                if(!target.isOnline()){
                    player.sendMessage(PrefixMessage.erreur() + "le joueur §4 " + target.getName() + " §cn'est plus en ligne");
                    return false;
                }

                if(HashMapGestion.getTpa().containsKey(target)){
                    player.sendMessage(PrefixMessage.erreur() + "le joueur §4" + target.getName() + " §ca déjà une invitation en cours");
                    return false;
                }

                HashMapGestion.getTpa().put(target, player);

                target.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a vous envoyé une demande de tp");
                player.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + target.getName() + "§a a reçu votre demande de tp");

                return false;
            }
        }

        if(args.length == 0){
            if(cmd.getName().equalsIgnoreCase("tpaccept")){

                if(!HashMapGestion.getTpa().containsKey(player)){
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas d'invitation");
                    return false;
                }

                if(HashMapGestion.getTpa().get(player) == null){
                    player.sendMessage(PrefixMessage.erreur() + "le joueur §4 " + HashMapGestion.getTpa().get(player).getName() + " §cn'est plus en ligne");
                    HashMapGestion.getTpa().remove(player);
                    return false;
                }

                HashMapGestion.getTpa().get(player).teleport(player);

                HashMapGestion.getTpa().get(player).sendMessage(PrefixMessage.serveur() + " Le joueur §b" + player.getName() + "§a a accepté votre demande de tp");
                player.sendMessage(PrefixMessage.serveur() + "demande de tp accepté");
                HashMapGestion.getTpa().remove(player);
                return false;
            }

            if(cmd.getName().equalsIgnoreCase("tpreject")){
                if(!HashMapGestion.getTpa().containsKey(player)){
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas d'invitation");
                    return false;
                }

                HashMapGestion.getTpa().get(player).sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a a refusé votre demande de tp");
                player.sendMessage(PrefixMessage.serveur() + "demande de tp refusée");
                HashMapGestion.getTpa().remove(player);


                return false;
            }
        }

        return false;
    }
}
