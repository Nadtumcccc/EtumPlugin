package be.nadtum.etum.Vanilla.City.commands;

import be.nadtum.etum.Utility.Modules.CityManage;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandCity implements CommandExecutor, TabCompleter {

    /*
        /city :
            create [city : String]
            delete
            leave
            invite [Player : String(Type Player)]
     */
    private static final HashMap<Player, Player> invite = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Vous ne pouvez pas utiliser cette commande en tant que console.");
            return false;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "spawn":
                    if (CityManage.hasCitySpawn(player.getName())) {
                        CityManage.tpToSpawn(player, PlayerBuilder.getPlayerCityName(player.getName()));
                        player.sendMessage(PrefixMessage.serveur() + "Tu as été téléporté au spawn de ta cité.");
                    } else {
                        player.sendMessage(PrefixMessage.erreur() + "Ta cité n'a pas encore de spawn défini.");
                    }
                    break;
                case "delete":
                    if (!CityManage.hasCity(player.getName())) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas de cité.");
                        return false;
                    }

                    if (!CityManage.hasPermission(player.getName(), "owner")) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas la permission de supprimer la cité.");
                        return false;
                    }

                    if (PlayerBuilder.getPlayerMoney(player.getName()) < 10000) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas assez d'Akoins [10 000].");
                        return false;
                    }

                    PlayerBuilder.setPlayerMoney(player.getName(), PlayerBuilder.getPlayerMoney(player.getName()) - 10000);
                    CityManage.deleteCity(player);
                    player.sendMessage(PrefixMessage.serveur() + "Votre cité a été supprimée.");

                    break;
                case "leave":

                    // On vérifie si le joueur a une cité pour effectuer une action
                    if (!CityManage.hasCity(player.getName())) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas de cité.");
                        return false;
                    }

                    if (CityManage.hasPermission(player.getName(), "owner")) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous ne pouvez pas quitter votre propre cité.");
                        return false;
                    }

                    FichierGestion.getCfgCity().set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".membres." + PlayerBuilder.getUUIDFromName(player.getName()), null);
                    PlayerBuilder.setPlayerCityName(player.getName(), "NoCity");

                    player.sendMessage(PrefixMessage.serveur() + "Vous avez bien quitté votre cité.");

                    break;
            }
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    if (CityManage.hasCity(player.getName())) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous avez déjà une cité.");
                        return false;
                    }

                    if (args[1].equalsIgnoreCase("nocity")) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous ne pouvez pas mettre NoCity comme nom de cité.");
                        return false;
                    }

                    if (PlayerBuilder.getPlayerMoney(player.getName()) < 10000) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas assez d'Akoins [10 000].");
                        return false;
                    }

                    if (FichierGestion.getCfgCity().contains("City." + args[1].toLowerCase())) {
                        player.sendMessage(PrefixMessage.erreur() + "Le nom de cité existe déjà.");
                        return false;
                    }
                    PlayerBuilder.setPlayerMoney(player.getName(), PlayerBuilder.getPlayerMoney(player.getName()) - 10000);
                    CityManage.createCity(player, args[1].toLowerCase());
                    player.sendMessage(PrefixMessage.serveur() + "Votre cité a été créée.");
                    break;
                case "kick":
                    // On vérifie si le joueur a une cité pour effectuer une action
                    if (!CityManage.hasCity(player.getName())) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas de cité.");
                        return false;
                    }

                    if (!CityManage.hasCity(args[1])) {
                        player.sendMessage(PrefixMessage.erreur() + args[1] + " n'a pas de cité.");
                        return false;
                    }

                    if (!FichierGestion.getCfgCity().contains("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".membres."
                            + PlayerBuilder.getUUIDFromName(args[1]).toString())) {
                        player.sendMessage(PrefixMessage.erreur() + "Le joueur n'est pas dans votre cité.");
                        return false;
                    }

                    if (!CityManage.hasPermission(player.getName(), "modération")) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas la permission.");
                        return false;
                    }

                    if (player.getName().equals(args[1])) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous ne pouvez pas vous exclure vous-même.");
                        return false;
                    }

                    if (!CityManage.hasPermission(player.getName(), "owner") && CityManage.hasPermission(args[1], "modération")) {
                        player.sendMessage(PrefixMessage.erreur() + "Vous ne pouvez pas exclure un membre qui a la permission [modération].");
                        return false;
                    }

                    FichierGestion.getCfgCity().set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".membres." + PlayerBuilder.getUUIDFromName(args[1]).toString(), null);
                    PlayerBuilder.setPlayerCityName(args[1], "NoCity");

                    player.sendMessage(PrefixMessage.serveur() + "Vous avez bien exclu §b" + args[1] + ".");

                    break;
                case "invite":

                    if (Bukkit.getPlayer(args[1]) != null) {

                        if (!CityManage.hasCity(player.getName())) {
                            player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas de cité.");
                            return false;
                        }


                        int compteur = 0;
                        for (String member : FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".membres").getKeys(false)) {
                            compteur++;
                        }
                        if (FichierGestion.getCfgCity().getInt("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".settings.maxMember") == compteur) {
                            player.sendMessage(PrefixMessage.erreur() + "Vous avez atteint la limite de membres pour votre cité.");
                            return false;
                        }

                        if (!CityManage.hasPermission(player.getName(), "invite")) {
                            player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas la permission [invite].");
                            return false;
                        }

                        Player target = Bukkit.getPlayer(args[1]);

                        if (CityManage.hasCity(target.getName())) {
                            player.sendMessage(PrefixMessage.erreur() + "Le joueur est déjà dans une cité.");
                            return false;
                        }

                        if (invite.containsKey(target)) {
                            player.sendMessage(PrefixMessage.erreur() + "Le joueur §4" + target.getName() + " §ca déjà une invitation en cours.");
                            return false;
                        }

                        invite.put(target, player);

                        target.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a vous a envoyé une demande d'invitation.");
                        player.sendMessage(PrefixMessage.serveur() + "Le joueur §b" + target.getName() + "§a a reçu votre demande d'invitation.");
                        return false;
                    }

                    if (args[1].equalsIgnoreCase("accepter")) {


                        if (!invite.containsKey(player)) {
                            player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas d'invitation.");
                            return false;
                        }

                        if (invite.get(player) == null) {
                            player.sendMessage(PrefixMessage.erreur() + "Le joueur §4 " + invite.get(player).getName() + " §cn'est plus en ligne.");
                            invite.remove(player);
                            return false;
                        }

                        int compteur = 0;

                        for (String member : FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerBuilder.getPlayerCityName(invite.get(player).getName()) + ".membres").getKeys(false)) {
                            compteur++;
                        }

                        if (FichierGestion.getCfgCity().getInt("City." + PlayerBuilder.getPlayerCityName(invite.get(player).getName()) + ".settings.maxMember") == compteur) {
                            player.sendMessage(PrefixMessage.erreur() + "Il n'y a plus de place.");
                            invite.remove(player);
                            return false;
                        }
                        // Procédure
                        PlayerBuilder.setPlayerCityName(player.getName(), PlayerBuilder.getPlayerCityName(invite.get(player).getName()));
                        FichierGestion.getCfgCity().set("City." + PlayerBuilder.getPlayerCityName(invite.get(player).getName()) + ".membres." + player.getUniqueId().toString() + ".rôle", "Membre");

                        invite.get(player).sendMessage(PrefixMessage.serveur() + " Le joueur §b" + player.getName() + "§a a accepté votre invitation.");
                        player.sendMessage(PrefixMessage.serveur() + "Vous avez accepté l'invitation.");
                        invite.remove(player);
                        return false;

                    }
                    if (args[1].equalsIgnoreCase("rejeter")) {
                        if (!invite.containsKey(player)) {
                            player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas d'invitation.");
                            return false;
                        }
                        invite.get(player).sendMessage(PrefixMessage.serveur() + "Le joueur §b" + player.getName() + "§a a refusé votre invitation.");
                        player.sendMessage(PrefixMessage.serveur() + "Demande d'invitation refusée.");
                        invite.remove(player);
                    }
                    break;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            List<String> options = new ArrayList<>(Arrays.asList("create", "delete", "leave", "invite"));
            if (player.isOp()) {
                options.add("spawn");
            }
            return options;
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create", "delete", "leave":
                    return null;
                case "invite":
                    List<String> playerNames = new ArrayList<>();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!invite.containsKey(onlinePlayer) || invite.get(onlinePlayer) == null || !invite.get(onlinePlayer).equals(player)) {
                            playerNames.add(onlinePlayer.getName());
                        }
                    }
                    return playerNames;
            }
        }
        return null;
    }
}
