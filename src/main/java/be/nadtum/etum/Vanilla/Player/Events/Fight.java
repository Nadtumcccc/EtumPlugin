package be.nadtum.etum.Vanilla.Player.Events;

import be.nadtum.etum.Utility.Modules.PlayerGestion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Fight implements Listener {

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player target) || !(event.getDamager() instanceof Player player)) {
            return;
        }

        // Prevent PvP between players of the same guild
        if (PlayerGestion.isSameCity(player, target)) {
            event.setCancelled(true);
        }
    }
}
