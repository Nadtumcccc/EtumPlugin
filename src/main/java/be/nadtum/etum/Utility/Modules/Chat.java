package be.nadtum.etum.Utility.Modules;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void PlayerChat(PlayerChatEvent event){

        event.setCancelled(true);

        Player player = event.getPlayer();

        //gérer le muted


        for (Player player1 : Bukkit.getOnlinePlayers()){
            //chat avec un grade staff : affichage du grade + chat color
            if (!PlayerGestion.getPlayerStaffGrade(player.getName()).equalsIgnoreCase("NoStaff")) {
                player1.sendMessage(colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerStaffGrade(player.getName())) + " " + player.getName() + " §e: §f" + colorString(event.getMessage())));
            }else{
                //chat sans grade staff mais avec un grade ayant la permission chat color : affichage du grade de jeu + chat color
                if (FichierGestion.getCfgPermission().contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.colorchat")) {
                    player1.sendMessage(colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName())) + " " + player.getName() + " §e: §f" + colorString(event.getMessage())));
                }else{
                    //chat sans grade staff et sans chat color : affichage du grade de jeu
                    player1.sendMessage(colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName()))) + " " + player.getName() + " §e: §f" + event.getMessage());
                }
            }
        }


    }

    public static String colorString(String message){
        if(message == null)return "error var is null";
        return ChatColor.translateAlternateColorCodes('&', message);
    }


}
