package be.nadtum.etum.Vanilla.Player.Events;

import be.nadtum.etum.Utility.Modules.PlayerGestion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Fight implements Listener {



    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        // Prevent PvP between players of the same guild
        if (PlayerGestion.getPlayerCityName(player.getName()).equalsIgnoreCase(PlayerGestion.getPlayerCityName(target.getName()))) {
            event.setCancelled(true);
        }
    }



}
