package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_back implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = null;

        try {
            FichierGestion.getCfgPermission();
        }catch (Exception exception){
            Bukkit.getLogger().severe(String.valueOf(exception));
        }

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.back")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        //cooldown

        if(CooldownManager.inCooldowns(player)){
            player.sendMessage(PrefixMessage.erreur() + "vous êtes en cooldown [" + CooldownManager.getCooldowns(player) + "]");
            return false;
        }

        if(!HashMapGestion.back.containsKey(player)){
            player.sendMessage(PrefixMessage.erreur() + " pas de location enregistré");
            return false;
        }

        player.teleport(HashMapGestion.back.get(player));
        HashMapGestion.back.remove(player);
        player.sendMessage(PrefixMessage.serveur() + "vous vous êtes téléporté au lieu de votre mort");

        switch (PlayerGestion.getPlayerGrade(player.getName())){
            case "Bronze":
                CooldownManager.setCooldowns(player, 60 * 60);
                break;
            case "Iron":
                CooldownManager.setCooldowns(player, 60 * 45);
                break;
            case "Commerçant":
                CooldownManager.setCooldowns(player, 60 * 30);
                break;
            case "Gold":
                CooldownManager.setCooldowns(player, 60 * 15);
                break;
            case "Diamond":
                CooldownManager.setCooldowns(player, 60 * 10);
                break;
            case "Netherite":
                CooldownManager.setCooldowns(player, 60 * 5);
                break;
        }

        return false;
    }
}
