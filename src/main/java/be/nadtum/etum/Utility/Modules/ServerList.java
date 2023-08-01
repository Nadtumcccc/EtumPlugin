package be.nadtum.etum.Utility.Modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerList implements Listener {

    @EventHandler
    public void ServerListEvent(ServerListPingEvent event){
        event.setMotd(
                ChatManage.colorString(ChatManage.colorString(
                          FichierGestion.getCfgSettings().getString("Settings.Module.MOTD.ligne_1"))
                        + "\n"
                        + ChatManage.colorString(FichierGestion.getCfgSettings().getString("Settings.Module.MOTD.ligne_2"))));
    }

}
