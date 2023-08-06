package be.nadtum.etum.Utility.Modules;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class ChatManage implements Listener {



    public static String colorString(String message) {
        return message == null ? "error var is null" : ChatColor.translateAlternateColorCodes('&', message);
    }

}
