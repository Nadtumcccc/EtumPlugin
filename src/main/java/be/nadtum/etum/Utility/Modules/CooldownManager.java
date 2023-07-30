package be.nadtum.etum.Utility.Modules;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    public static HashMap<UUID, Double> cooldowns = new HashMap<>();

    public static void setupCooldown(){
        cooldowns = new HashMap<>();
    }

    public static void setCooldowns(Player player, int secondes){
        double delay = System.currentTimeMillis() + (secondes * 1000L);
        cooldowns.put(player.getUniqueId(), delay);

    }

    public static double getCooldowns(Player player){
        return (cooldowns.get(player.getUniqueId()) - (double) System.currentTimeMillis() /1000);
    }

    public static boolean inCooldowns(Player player){
        if(!cooldowns.containsKey(player.getUniqueId()) || cooldowns.get(player.getUniqueId()) <= System.currentTimeMillis()){
            return false;
        }
        return true;
    }





}
