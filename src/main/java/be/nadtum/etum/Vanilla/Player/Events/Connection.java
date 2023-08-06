package be.nadtum.etum.Vanilla.Player.Events;

import be.nadtum.etum.Utility.Modules.ChatManage;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Vanilla.Player.Commands.CommandWarp;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Connection implements Listener {

    @EventHandler
    public void onPlayerJoinServer(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerBuilder.createNewProfil(player);

        String playerName = player.getName();
        String playerStaffGrade = PlayerBuilder.getPlayerStaffGrade(playerName);
        String playerGrade = PlayerBuilder.getPlayerGrade(playerName);

        String joinMessage = "§6[§a+§6] " + ChatManage.colorString(playerStaffGrade.equalsIgnoreCase("NoStaff") ? PlayerBuilder.getGradeDesign(playerGrade) : PlayerBuilder.getGradeDesign(playerStaffGrade)) + " " + playerName;
        event.joinMessage(Component.text(joinMessage));

        if (!player.hasPlayedBefore()) {
            player.sendTitle("§6Bienvenue sur §bAkia", "§6pour commencer ton aventure fait §b/menu", 20, 40, 30);

            String welcomeMessage = PrefixMessage.serveur() + "Bienvenue à " + playerName + " sur le serveur §bEtum ";
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(welcomeMessage);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F);
            }
        }

        player.sendTitle("§aBienvenue sur §bEtum", "Nous te souhaitons une bonne session", 20, 30, 30);

        player.sendMessage("§a-------------------------------");
        player.sendMessage("§aMoney §e: §b" + PlayerBuilder.getPlayerMoney(playerName));
        player.sendMessage("§aGrade §e: §b" + PlayerBuilder.getPlayerGrade(playerName));
        player.sendMessage("§aMétier §e: §b" + PlayerBuilder.getPlayerJobName(playerName) + "§e, §aniveau §e: §b" + PlayerBuilder.getPlayerJobNiveau(playerName));
        player.sendMessage("§a-------------------------------");

        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onPlayerQuitServer(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerStaffGrade = PlayerBuilder.getPlayerStaffGrade(playerName);
        String playerGrade = PlayerBuilder.getPlayerGrade(playerName);
        String quitMessage = "§6[§4-§6] " + ChatManage.colorString(playerStaffGrade.equalsIgnoreCase("NoStaff") ? PlayerBuilder.getGradeDesign(playerGrade) : PlayerBuilder.getGradeDesign(playerStaffGrade)) + " " + playerName;

        event.quitMessage(Component.text(quitMessage));
    }

}
