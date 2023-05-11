package be.nadtum.etum.Staff.ACC.events;

import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Utility.Objets.DataPunish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {


    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for(Player modos : HashMapGestion.getVanish().keySet()){
            player.hidePlayer(modos);
        }


    }

    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event){
        Player p = event.getPlayer();

        DataPunish data = new DataPunish(p.getUniqueId());

        if(!data.exist())return;

        if(data.isTempbanned()){
            if(data.getTempbannedMilliseconds() <= System.currentTimeMillis()){
                data.setUnTempbanned();
            }else{
                //au lieu de dire à partir de, utiliser jusqu'au et reprendre le bout de code du projet staffmode
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§evous avez été ban par §b§l" + data.getTempbannedFrom() +
                        "\n §eà partir de " + data.getTempbannedTimestamp() + " §ependant §b" + data.getTempmutedChiffre() + " §e" + data.getTempbannedFormat() +
                        "\n §eraison : §b§l" + data.getTempbannedReason());
            }
        }
    }





    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if(HashMapGestion.getInventaire().containsKey(player)){
            player.getInventory().clear();

            //remettre l'inventaire de jeu
            for (int i = 0; i < 35;i++){
                player.getInventory().setItem(i,HashMapGestion.getInventaire().get(player)[i]);
            }

            player.getInventory().setBoots(HashMapGestion.getArmor().get(player)[0]);
            player.getInventory().setLeggings(HashMapGestion.getArmor().get(player)[1]);
            player.getInventory().setChestplate(HashMapGestion.getArmor().get(player)[2]);

            player.getInventory().setHelmet(HashMapGestion.getArmor().get(player)[3]);

            player.sendMessage(PrefixMessage.admin() + " vous avez quitté le mode staff");
            HashMapGestion.getInventaire().remove(player);
            if(HashMapGestion.getVanish().containsKey(player)){
                HashMapGestion.getVanish().remove(player);
            }
            player.setAllowFlight(false);
            player.setFlying(false);

        }

    }




}
