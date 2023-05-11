package be.nadtum.etum.Vanilla.MenuGui.commands;


import be.nadtum.etum.Vanilla.MenuGui.MenuPrincipal;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_menu implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player)sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.default") ){
            if(!player.isOp()){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");

                return false;
            }
        }

        MenuPrincipal.menu(player);

        return false;
    }


}
