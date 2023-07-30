package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class command_back implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Vous ne pouvez pas utiliser cette commande depuis la console.");
            return false;
        }

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.back")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + " Vous n'avez pas la permission d'utiliser cette commande.");
                return false;
            }
        }

        // Cooldown check
        if (CooldownManager.inCooldowns(player)) {
            player.sendMessage(PrefixMessage.erreur() + " Vous êtes en cooldown [" + CooldownManager.getCooldowns(player) + "]");
            return false;
        }

        if (!HashMapGestion.back.containsKey(player)) {
            player.sendMessage(PrefixMessage.erreur() + " Pas de location enregistrée.");
            return false;
        }

        player.teleport(HashMapGestion.back.get(player));
        HashMapGestion.back.remove(player);
        player.sendMessage(PrefixMessage.serveur() + " Vous vous êtes téléporté au lieu de votre mort.");

        String playerRank = PlayerGestion.getPlayerGrade(player.getName());


        return true;
    }
}
