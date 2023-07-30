package be.nadtum.etum.Vanilla.Player.Events;

import be.nadtum.etum.Utility.Modules.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class Connection implements Listener {

    @EventHandler
    public void onPlayerJoinServer(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerGestion.createNewProfil(player);

        String playerName = player.getName();
        String playerStaffGrade = PlayerGestion.getPlayerStaffGrade(playerName);
        String playerGrade = PlayerGestion.getPlayerGrade(playerName);

        String joinMessage = "§6[§a+§6] " + Chat.colorString(playerStaffGrade.equalsIgnoreCase("NoStaff") ? PlayerGestion.getGradeDesign(playerGrade) : PlayerGestion.getGradeDesign(playerStaffGrade)) + " " + playerName;
        event.joinMessage(Component.text(joinMessage));

        if (!player.hasPlayedBefore()) {
            player.sendTitle("§6Bienvenue sur §bAkia", "§6pour commencer ton aventure fait §b/menu", 20, 40, 30);

            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            String welcomeMessage = PrefixMessage.serveur() + "Bienvenue à " + playerName + " sur le serveur §bEtum ";
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(welcomeMessage);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F);
            }

            Teleportation.PlayerTpToSpawn(player, "Spawn");
        }

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getServer().getMaxPlayers();
        String header = "§bEtum\n§eEn Jeu : §b" + onlinePlayers + "§e/§b" + maxPlayers + "\n§7==================";
        String footer = "§7==================\n§6Site Web §e: §betum.fr";

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setPlayerListHeaderFooter(header, footer);
        }

        String nameTag = Chat.colorString(playerStaffGrade.equalsIgnoreCase("NoStaff") ? PlayerGestion.getGradeDesign(playerGrade) : PlayerGestion.getGradeDesign(playerStaffGrade)) + " ";
        onNameTag(player, nameTag);

        player.sendTitle("§aBienvenue sur §bEtum", "Nous te souhaitons une bonne session", 20, 30, 30);

        player.sendMessage("§a-------------------------------");
        player.sendMessage("§aMoney §e: §b" + PlayerGestion.getPlayerMoney(playerName));
        player.sendMessage("§aGrade §e: §b" + PlayerGestion.getPlayerGrade(playerName));
        player.sendMessage("§aMétier §e: §b" + PlayerGestion.getPlayerJobName(playerName) + "§e, §aniveau §e: §b" + PlayerGestion.getPlayerJobNiveau(playerName));
        player.sendMessage("§a-------------------------------");

        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onPlayerQuitServer(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerStaffGrade = PlayerGestion.getPlayerStaffGrade(playerName);
        String playerGrade = PlayerGestion.getPlayerGrade(playerName);
        String quitMessage = "§6[§4-§6] " + Chat.colorString(playerStaffGrade.equalsIgnoreCase("NoStaff") ? PlayerGestion.getGradeDesign(playerGrade) : PlayerGestion.getGradeDesign(playerStaffGrade)) + " " + playerName;

        event.quitMessage(Component.text(quitMessage));

        int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
        int maxPlayers = Bukkit.getServer().getMaxPlayers();
        String header = "§bEtum\n§eEn Jeu : §b" + onlinePlayers + "§e/§b" + maxPlayers + "\n§7==================";
        String footer = "§7==================\n§6Site Web §e: §betum.fr";

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setPlayerListHeaderFooter(header, footer);
        }


    }

    public void onNameTag(Player player, String prefix) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(prefix);

        if (team == null) {
            team = scoreboard.registerNewTeam(prefix);
            team.setPrefix(prefix);
        }

        if (!team.hasPlayer(player)) {
            team.addPlayer(player);
            player.setScoreboard(scoreboard);
        }
    }
}
