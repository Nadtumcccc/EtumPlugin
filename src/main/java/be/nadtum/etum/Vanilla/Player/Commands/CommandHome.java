package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHome implements CommandExecutor, TabExecutor {





    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println("vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player)sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(args.length == 0){
            player.sendMessage(PrefixMessage.erreur() + " /(del)(set)home [home]");
            return false;
        }

        if(PlayerBuilder.hasPermission(player, "admin")){
            if(args[0].equals("add")){
                if(Bukkit.getPlayer(args[1]) != null){
                    PlayerBuilder.setPlayerHomeCount(Bukkit.getPlayer(args[1]).getName(), PlayerBuilder.getPlayerHomeCount(Bukkit.getPlayer(args[1]).getName()) + Integer.valueOf(args[2]));
                    player.sendMessage(PrefixMessage.admin() + "le joueur §b" + args[1] + "§a a maintenant §b" + PlayerBuilder.getPlayerHomeCount(Bukkit.getPlayer(args[1]).getName()) + " §ahomes");
                    return false;
                }
            }
        }


        //gestion des homes
        if(cmd.getName().equals("sethome")) {

            //on vérifie s'il peut encore créer des homes
            Integer nbHome = 0;
            if(FichierGestion.getCfgPlayers().contains("Profil." + player.getUniqueId() + ".Home.homes")){
                for (String home : FichierGestion.getCfgPlayers().getConfigurationSection("Profil." + player.getUniqueId() + ".Home.homes").getKeys(false)) {
                    nbHome = nbHome + 1;
                }
                if (nbHome >= PlayerBuilder.getPlayerHomeCount(player.getName())) {
                    player.sendMessage(PrefixMessage.erreur() + " vous avez trop de home " +
                            "\nvous avez §c[" + nbHome + "§c] home");


                    return false;
                }
            }

            Location loc = player.getLocation();

            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".x", loc.getX());
            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".z", loc.getZ());
            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".y", loc.getY());
            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".yaw", loc.getYaw());
            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".pitch", loc.getPitch());
            FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".world", loc.getWorld().getName());
            player.sendMessage(PrefixMessage.serveur() + " le home §b" + args[0] + " §aa été créé");


            return false;
        }


        if(cmd.getName().equals("delhome")){
            //vérifie si le home n'est pas encore créé
            if (FichierGestion.getCfgPlayers().contains("Profil." + player.getUniqueId().toString() + ".Home.homes." + args[0])) {
                FichierGestion.getCfgPlayers().set("Profil." + player.getUniqueId().toString() + ".Home.homes." + args[0], null);
                player.sendMessage(PrefixMessage.serveur() + " le home §b" + args[0] + " §aa été supprimé");
            }else{
                player.sendMessage( PrefixMessage.erreur() + " le home n'a pas été créé");
            }
            return false;
        }

        if(cmd.getName().equals("home")) {
            if (!FichierGestion.getCfgPlayers().contains("Profil." + player.getUniqueId() + ".Home.homes." + args[0])) {
                player.sendMessage( PrefixMessage.erreur() + " le home n'a pas été créé");
                return false;
            }
            Double x = FichierGestion.getCfgPlayers().getDouble("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".x");
            Double z = FichierGestion.getCfgPlayers().getDouble("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".z");
            Double y = FichierGestion.getCfgPlayers().getDouble("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".y");
            float yaw = (float) FichierGestion.getCfgPlayers().getDouble("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".yaw");
            float pitch = (float) FichierGestion.getCfgPlayers().getDouble("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".pitch");
            String worldname = FichierGestion.getCfgPlayers().getString("Profil." + player.getUniqueId() + ".Home.homes." + args[0] + ".world");

            World world = Bukkit.getWorld(worldname);

            player.teleport(new Location(world, x,y,z,yaw,pitch));

            player.sendMessage(PrefixMessage.serveur() + " vous avez été téléporté au home §b" + args[0]);

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 1) {

            ConfigurationSection homesSection = FichierGestion.getCfgPlayers().getConfigurationSection("Profil." + player.getUniqueId() + ".Home.homes");
            if (homesSection != null) {
                list.addAll(homesSection.getKeys(false));
            }

        }

        return list;
    }
}