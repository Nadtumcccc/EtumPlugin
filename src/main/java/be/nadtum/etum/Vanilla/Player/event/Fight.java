package be.nadtum.etum.Vanilla.Player.event;

import be.nadtum.etum.Utility.Modules.PlayerGestion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Fight implements Listener {



    @EventHandler
    public void PvP(EntityDamageByEntityEvent event){


        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = ((Player) event.getDamager()).getPlayer();
            Player target = ((Player) event.getEntity()).getPlayer();

            //empêcher le pvp entre joueur de même guilde
            if(PlayerGestion.getPlayerCityName(player.getName()).equalsIgnoreCase(PlayerGestion.getPlayerCityName(target.getName()))){
                event.setCancelled(true);
                return;
            }
        }




    }



}
