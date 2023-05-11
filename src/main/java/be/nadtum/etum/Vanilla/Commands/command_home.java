package be.nadtum.etum.Vanilla.Commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
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
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class command_home implements CommandExecutor, TabExecutor {




    /**
     * Gère l'exécution des commandes.
     *
     * @param sender L'expéditeur de la commande.
     * @param cmd    La commande exécutée.
     * @param label  L'étiquette de la commande.
     * @param args   Les arguments de la commande.
     * @return {@code true} si la commande s'est exécutée correctement, sinon {@code false}.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            System.out.println("Vous ne pouvez pas utiliser cette commande");
        } else {

            if (args.length == 0) {
                player.sendMessage(PrefixMessage.erreur() + " /(del)(set)home [home]");
            } else if (PlayerGestion.hasPermission(player, "admin") && args.length >= 3 && args[0].equals("add")) {
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer instanceof Player) {
                    String playerName = targetPlayer.getName();
                    int currentHomeCount = PlayerGestion.getPlayerHomeCount(playerName);
                    int additionalHomeCount = Integer.valueOf(args[2]);
                    int newHomeCount = currentHomeCount + additionalHomeCount;

                    PlayerGestion.setPlayerHomeCount(playerName, newHomeCount);

                    player.sendMessage(PrefixMessage.admin() + "Le joueur §b" + playerName + "§a a maintenant §b" + newHomeCount + "§a homes");
                }
            }
        }


        if(!PlayerGestion.hasPermission(player, "default")) return false;


        if (cmd.getName().equals("sethome")) {
            String homePath = "Profil." + player.getUniqueId().toString() + ".Home.homes." + args[0];

            if (FichierGestion.getCfgPlayers().isSet(homePath)) {
                player.sendMessage(PrefixMessage.erreur() + " Le home §4" + args[0] + " §c a déjà été créé");
            } else {
                ConfigurationSection homesSection = FichierGestion.getCfgPlayers().getConfigurationSection("Profil." + player.getUniqueId() + ".Home.homes");
                if (homesSection != null) {
                    int nbHome = homesSection.getKeys(false).size();
                    if (nbHome >= PlayerGestion.getPlayerHomeCount(player.getName())) {
                        player.sendMessage(PrefixMessage.erreur() + " Vous avez trop de homes." +
                                "\nVous avez §c[" + nbHome + "] homes");
                        return false;
                    }
                }

                FichierGestion.getCfgPlayers().set(homePath, player.getLocation());
                FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());

                player.sendMessage(PrefixMessage.serveur() + " Le home §b" + args[0] + " §a a été créé");
            }
        } else if (cmd.getName().equals("delhome")) {
            String homePath = "Profil." + player.getUniqueId().toString() + ".Home.homes." + args[0];

            if (FichierGestion.getCfgPlayers().isSet(homePath)) {
                FichierGestion.getCfgPlayers().set(homePath, null);
                player.sendMessage(PrefixMessage.serveur() + " le home §b" + args[0] + " §aa été supprimé");
                FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
            } else {
                player.sendMessage(PrefixMessage.erreur() + " le home n'a pas été créé");
            }
        } else if (cmd.getName().equals("home")) {
            String homePath = "Profil." + player.getUniqueId() + ".Home.homes." + args[0];

            if (!FichierGestion.getCfgPlayers().isSet(homePath)) {
                player.sendMessage(PrefixMessage.erreur() + " le home n'a pas été créé");
            } else {
                player.teleport(FichierGestion.getCfgPlayers().getLocation(homePath));
                player.sendMessage(PrefixMessage.serveur() + " vous avez été téléporté au home §b" + args[0]);
            }
        }

        return false;


    }

    /**
     * Renvoie la liste des suggestions d'auto-complétion pour la commande.
     *
     * @param commandSender L'expéditeur de la commande.
     * @param command       La commande en cours.
     * @param s             L'étiquette de la commande.
     * @param args          Les arguments de la commande.
     * @return La liste des suggestions d'auto-complétion.
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            String homePath = "Profil." + player.getUniqueId() + ".Home.homes";
            ConfigurationSection homesSection = FichierGestion.getCfgPlayers().getConfigurationSection(homePath);

            if (homesSection != null) {
                list.addAll(homesSection.getKeys(false));
            }
        }

        return list;
    }
}
