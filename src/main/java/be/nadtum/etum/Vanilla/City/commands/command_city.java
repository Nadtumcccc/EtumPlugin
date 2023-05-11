package be.nadtum.etum.Vanilla.City.commands;

import be.nadtum.etum.Utility.Modules.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class command_city implements CommandExecutor, TabExecutor {

    /*
        /city :
            create [city : String]
            delete
            leave
            invite [Player : String(Type Player)]
     */
    private static HashMap<Player, Player> invite = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player)sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.city") ){
            if(!player.isOp()){
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 1){
            switch (args[0]){
                case "spawn":
                    if(CityGestion.hasCitySpawn(player.getName())) {
                        CityGestion.tpToSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                        player.sendMessage(PrefixMessage.serveur() + "tu as été téléporté au spawn de ta cité");
                    }else{
                        player.sendMessage(PrefixMessage.erreur() + "ta cité n'a pas encore de spawn défini");
                    }
                    break;
                case "delete":
                    if(!CityGestion.hasCity(player.getName())){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas de cité");
                        return false;
                    }

                    if(!CityGestion.hasPermission(player.getName(), "owner")){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission de supprimer la cité");
                        return false;
                    }

                    if(PlayerGestion.getPlayerMoney(player.getName()) < 10000){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas assez d'Akoins [10 000]");
                        return false;
                    }

                    PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - 10000);
                    CityGestion.deleteCity(player);
                    player.sendMessage(PrefixMessage.serveur() + "votre cité à été supprimé");

                    break;
                case "leave":

                    //on vérifie si le joueur à une cité pour faire une action
                    if(!CityGestion.hasCity(player.getName())){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas de cité");
                        return false;
                    }

                    if(!CityGestion.hasPermission(player.getName(), "owner")){
                        player.sendMessage(PrefixMessage.erreur() + "tu ne peux pas quitter ta propre cité");
                        return false;
                    }

                    FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres." + PlayerGestion.getUUIDFromName(player.getName()), null);
                    PlayerGestion.setPlayerCityName(player.getName(), "NoCity");

                    player.sendMessage(PrefixMessage.serveur() + "vous avez bien quitté votre cité");

                    break;
            }
        }

        if(args.length == 2){
            switch (args[0]){
                case "create":
                    if(CityGestion.hasCity(player.getName())){
                        player.sendMessage(PrefixMessage.erreur() + "vous avez déjà une cité");
                        return false;
                    }

                    if (args[1].toLowerCase().equals("nocity")) {
                        player.sendMessage(PrefixMessage.erreur() + "vous ne pouvez pas mettre NoCity comme nom de cité");
                        return false;
                    }

                    if(PlayerGestion.getPlayerMoney(player.getName()) < 10000){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas assez d'Akoins  [10 000]");
                        return false;
                    }

                    if(FichierGestion.getCfgCity().contains("City." + args[1].toLowerCase())){
                        player.sendMessage(PrefixMessage.erreur() + "le nom de cité existe déjà");
                        return false;
                    }
                    PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - 10000);
                    CityGestion.createCity(player, args[1].toLowerCase());
                    player.sendMessage(PrefixMessage.serveur() + "votre cité à été créé");
                    break;
                case "kick":
                    //on vérifie si le joueur à une cité pour faire une action
                    if(!CityGestion.hasCity(player.getName())){
                        player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas de cité");
                        return false;
                    }

                    if(!CityGestion.hasCity(args[1])){
                        player.sendMessage(PrefixMessage.erreur() + args[1] + " n'a pas de cité");
                        return false;
                    }

                    if(!FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres."
                            + PlayerGestion.getUUIDFromName(args[1]).toString())){
                        player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas dans ta cité");
                        return false;
                    }

                    if(!CityGestion.hasPermission(player.getName(), "modération")){
                        player.sendMessage(PrefixMessage.erreur() + "tu n'as pas la permission");
                        return false;
                    }

                    if(player.getName().equals(args[1])){
                        player.sendMessage(PrefixMessage.erreur() + "vous ne pouvez pas vous kick vous même");
                        return false;
                    }


                    if(!CityGestion.hasPermission(player.getName(), "owner") && CityGestion.hasPermission(args[1], "modération")){
                        player.sendMessage(PrefixMessage.erreur() + "tu ne peux pas kick un membre qui a la permission [modération]");
                        return false;
                    }

                    FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres." + PlayerGestion.getUUIDFromName(args[1]).toString(), null);
                    PlayerGestion.setPlayerCityName(args[1], "NoCity");

                    player.sendMessage(PrefixMessage.serveur() + "vous avez bien kick §b" + args[1]);

                    break;
                case "invite":

                    if(Bukkit.getPlayer(args[1]) instanceof Player){

                        if(!CityGestion.hasCity(player.getName())){
                            player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas de cité");
                            return false;
                        }


                        int compteur = 0;
                        for (String member : FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres").getKeys(false)){
                            compteur++;
                        }
                        if(FichierGestion.getCfgCity().getInt("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".settings.maxMember") == compteur){
                            player.sendMessage(PrefixMessage.erreur() + "vous avez atteind la limite de membres pour votre cité");
                            return false;
                        }

                        if(!CityGestion.hasPermission(player.getName(), "invite")){
                            player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission [invite]");
                            return false;
                        }

                        Player target = Bukkit.getPlayer(args[1]);

                        if(CityGestion.hasCity(target.getName())){
                            player.sendMessage(PrefixMessage.erreur() + "le joueur est déjà une cité");
                            return false;
                        }

                        if(invite.containsKey(target)){
                            player.sendMessage(PrefixMessage.erreur() + "le joueur §4" + target.getName() + " §ca déjà une invitation en cours");
                            return false;
                        }

                        invite.put(target, player);

                        target.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a vous envoyé une demande d'invitation");
                        player.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + target.getName() + "§a a reçu votre demande d'invitation");
                        return false;
                    }

                    if(args[1].equals("accepter")){


                        if(!invite.containsKey(player)){
                            player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas d'invitation");
                            return false;
                        }

                        if(invite.get(player) == null){
                            player.sendMessage(PrefixMessage.erreur() + "le joueur §4 " + invite.get(player).getName() + " §cn'est plus en ligne");
                            invite.remove(player);
                            return false;
                        }

                        int compteur = 0;

                        for (String member : FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerGestion.getPlayerCityName(invite.get(player).getName()) + ".membres").getKeys(false)){
                            compteur++;
                        }

                        if(FichierGestion.getCfgCity().getInt("City." + PlayerGestion.getPlayerCityName(invite.get(player).getName()) + ".settings.maxMember") == compteur){
                            player.sendMessage(PrefixMessage.erreur() + "il n'y a plus de place");
                            invite.remove(player);
                            return false;
                        }
                        //proccédure
                        PlayerGestion.setPlayerCityName(player.getName(), PlayerGestion.getPlayerCityName(invite.get(player).getName()));
                        FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(invite.get(player).getName()) + ".membres." + player.getUniqueId().toString() + ".rôle", "Membre");

                        invite.get(player).sendMessage(PrefixMessage.serveur() + " Le joueur §b" + player.getName() + "§a a accepté votre invitation");
                        player.sendMessage(PrefixMessage.serveur() + "vous avez accepté l'invitation");
                        invite.remove(player);
                        return false;

                    }
                    if(args[1].equals("rejeter")){
                        if(!invite.containsKey(player)){
                            player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas d'invitation");
                            return false;
                        }
                        invite.get(player).sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a a refusé votre invitation");
                        player.sendMessage(PrefixMessage.serveur() + "demande d'invitation refusée");
                        invite.remove(player);
                    }
                    break;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(args.length == 1){
            List<String> list = new ArrayList<>();

            list.add("create");
            list.add("leave");
            list.add("spawn");
            list.add("invite");
            list.add("kick");
            return list;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("invite")){
            List<String> list = new ArrayList<>();

            list.add("accepter");
            list.add("rejeter");


            for (Player player : Bukkit.getOnlinePlayers()){
                list.add(player.getName());
            }
            return list;
        }

        return null;
    }
}
