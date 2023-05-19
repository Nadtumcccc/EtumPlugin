package be.nadtum.etum.Staff.ACC.events;

import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Staff.DataPunish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player modos : HashMapGestion.getVanish().keySet()) {
            player.hidePlayer(modos);
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        DataPunish data = new DataPunish(player.getUniqueId());

        if (!data.exist()) {
            return;
        }

        if (data.isTempbanned()) {
            if (data.getTempbannedMilliseconds() <= System.currentTimeMillis()) {
                data.setUnTempbanned();
            } else {
                String kickMessage = "§evous avez été ban par §b§l" + data.getTempbannedFrom() +
                        "\n §eà partir de " + data.getTempbannedTimestamp() + " §ependant §b" + data.getTempbannedFormat() +
                        "\n §eraison : §b§l" + data.getTempbannedReason();
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessage);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (HashMapGestion.getInventaire().containsKey(player)) {
            player.getInventory().clear();

            // Remettre l'inventaire de jeu
            player.getInventory().setContents(HashMapGestion.getInventaire().get(player));
            player.getInventory().setArmorContents(HashMapGestion.getArmor().get(player));

            player.sendMessage(PrefixMessage.admin() + " vous avez quitté le mode staff");
            HashMapGestion.getInventaire().remove(player);
            HashMapGestion.getVanish().remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
