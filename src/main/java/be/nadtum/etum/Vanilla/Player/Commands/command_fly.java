package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_fly implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerBuilder.getPlayerGrade(player.getName()) + ".permission.fly")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(!player.getGameMode().equals(GameMode.SURVIVAL)){
            player.sendMessage(PrefixMessage.erreur() + " vous devez être en survie pour utiliser cette commande");
            return false;
        }


        if(!PlayerBuilder.canFly(player)){
            player.sendMessage(PrefixMessage.erreur() + "vous devez être dans votre cité pour voler");
            return false;
        }

        if(player.getAllowFlight()){
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(PrefixMessage.serveur()+ "fly désactivé");
        }else{
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(PrefixMessage.serveur()+ "fly activé");
        }



        return false;
    }
}
