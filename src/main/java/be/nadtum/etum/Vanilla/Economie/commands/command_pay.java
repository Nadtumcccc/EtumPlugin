package be.nadtum.etum.Vanilla.Economie.commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_pay implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.pay")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 2){

            if(!(Bukkit.getPlayer(args[0]) instanceof Player)){
                player.sendMessage(PrefixMessage.erreur() + "/pay [player] [money]");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(!target.isOnline()){
                player.sendMessage(PrefixMessage.erreur() + "le joueur n'est plus en ligne");
                return false;
            }

            if(!(Integer.valueOf(args[1]) instanceof Integer)){
                player.sendMessage(PrefixMessage.erreur() + "/pay [player] [money]");
                return false;
            }
            Integer sendmoney = Integer.valueOf(args[1]);

            if(PlayerGestion.getPlayerMoney(player.getName()) < sendmoney){
                player.sendMessage(PrefixMessage.erreur() + "pas assez de Akoins");
                return false;
            }

            PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - sendmoney);
            PlayerGestion.setPlayerMoney(target.getName(), PlayerGestion.getPlayerMoney(target.getName()) + sendmoney);

            player.sendMessage( PrefixMessage.serveur() + "vous avez envoyé §b" + sendmoney + " §aAkoins à §b" + target.getName());
            target.sendMessage( PrefixMessage.serveur() + "vous avez reçu §b" + sendmoney + " §aAkoins de §b" + player.getName());



        }else{
            player.sendMessage(PrefixMessage.erreur() + "/pay [player] [money]");
            return false;
        }

        return false;
    }
}
