package be.nadtum.etum.Utility.Modules;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatManage implements Listener {

    @EventHandler
    public void onPlayerChat(@NotNull PlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerGrade = PlayerGestion.getPlayerGrade(playerName);
        String staffGrade = PlayerGestion.getPlayerStaffGrade(playerName);
        String message = event.getMessage();

        String chatFormat;

        if (!staffGrade.equalsIgnoreCase("NoStaff")) {
            chatFormat = PlayerGestion.getGradeDesign(staffGrade) + " " + playerName + " §e: §7" + colorString(message);
        } else if (FichierGestion.getCfgPermission().contains("Grade." + playerGrade + ".permission.colorchat")) {
            chatFormat = PlayerGestion.getGradeDesign(playerGrade) + " " + playerName + " §e: §7" + colorString(message);
        } else {
            chatFormat = PlayerGestion.getGradeDesign(playerGrade) + " " + playerName + " §e: §7" + message;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(colorString(chatFormat));
        }
    }

    public static String colorString(String message){
        return message == null ? "error var is null" : ChatColor.translateAlternateColorCodes('&', message);
    }




}
