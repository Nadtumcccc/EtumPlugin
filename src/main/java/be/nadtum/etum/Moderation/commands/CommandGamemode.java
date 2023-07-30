package be.nadtum.etum.Moderation.commands;

import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandGamemode implements CommandExecutor, TabExecutor {

    private static String GAMEMODE_STRING;
    private static GameMode GAMEMODE;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrefixMessage.erreur() + "Vous ne pouvez pas utiliser cette commande");
            return false;
        }

        if (!PlayerGestion.hasPermission(player,"gamemode")) {
            return false;
        }

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(PrefixMessage.erreur() + "/gm [0,1,2,3] (joueur)");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "survival":
            case "0":
                GAMEMODE = GameMode.SURVIVAL;
                GAMEMODE_STRING = "survie";
                break;
            case "creative":
            case "1":
                GAMEMODE = GameMode.CREATIVE;
                GAMEMODE_STRING = "créative";
                break;
            case "adventure":
            case "2":
                GAMEMODE = GameMode.ADVENTURE;
                GAMEMODE_STRING = "aventure";
                break;
            case "spectator":
            case "3":
                GAMEMODE = GameMode.SPECTATOR;
                GAMEMODE_STRING = "spectateur";
                break;
            default:
                player.sendMessage(PrefixMessage.erreur() + "/gm [0,1,2,3] (joueur)");
                return false;
        }

        if (args.length == 1) {
            player.sendMessage(PrefixMessage.serveur() + "Votre mode de jeu est maintenant en §b" + GAMEMODE_STRING);
            player.setGameMode(GAMEMODE);
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PrefixMessage.erreur() + "Le joueur §4" + args[1] + " §cn'est pas en ligne !");
            return false;
        }

        target.setGameMode(GAMEMODE);
        target.sendMessage(PrefixMessage.serveur() + "Votre mode de jeu a été mis en §b" + GAMEMODE_STRING + " §apar §b" + player.getName());
        player.sendMessage(PrefixMessage.serveur() + "Le mode de jeu a été mis en §b" + GAMEMODE_STRING + " §apour §b" + target.getName());

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("survival");
            list.add("creative");
            list.add("adventure");
            list.add("spectator");
            return list;
        }

        if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            return list;
        }

        return null;
    }
}
