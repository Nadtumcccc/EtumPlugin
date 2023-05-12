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

public class command_mana implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.logs() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.mana")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length > 0){
            if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin")) {
                if (!player.isOp()) {
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande (faîtes /mana)");
                    return false;
                }
            }

            if(!(Integer.valueOf(args[2]) instanceof Integer)){
                player.sendMessage(PrefixMessage.erreur() + "la valeur n'est pas un nombre");
                return false;
            }

            if(!(Bukkit.getPlayer(args[1]) instanceof Player)){
                player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas en ligne");
                return false;
            }
            switch (args[0]){
                case "add":
                    PlayerGestion.setPlayerMana(args[1], PlayerGestion.getPlayerMana(args[1]) + Integer.valueOf(args[2]));
                    break;
                case "set":
                    PlayerGestion.setPlayerMana(args[1], Integer.valueOf(args[2]));
                    break;
            }
            player.sendMessage(PrefixMessage.serveur() + "le joueur a maintenant §b" + PlayerGestion.getPlayerMana(args[1]) + " §aMana");
            return false;
        }

        player.sendMessage(PrefixMessage.serveur() + "§b" + PlayerGestion.getPlayerMana(player.getName()) + " §aMana");


        return false;
    }
}
