package be.nadtum.etum.Vanilla.Player.Events;


import be.nadtum.etum.Utility.Modules.Chat;
import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;

import org.bukkit.Sound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.PlayerDeathEvent;


public class Death implements Listener {

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player killer = null;
        if(event.getEntity().getKiller() instanceof Player){
            killer = event.getEntity().getKiller();
        }
        Player player = event.getEntity().getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 4, 1);

        String prefixName = null;
        if(!PlayerGestion.getPlayerStaffGrade(player.getName()).equalsIgnoreCase("NoStaff")){
            prefixName = Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerStaffGrade(player.getName())) + " " + player.getName());
        }else{
            prefixName = Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName())) + " " + player.getName());
        }
        if(killer != null){
            event.setDeathMessage(prefixName + " a été tué par " + Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName())) + " " + killer.getName()));
            return;
        }
        event.setDeathMessage(prefixName + " §avient de mourir ");
        if(PlayerGestion.hasPermission(player, "back")){
            HashMapGestion.back.put(player, player.getLocation());
        }
    }


}
