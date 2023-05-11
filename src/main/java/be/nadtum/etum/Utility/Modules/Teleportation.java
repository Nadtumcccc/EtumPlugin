package be.nadtum.etum.Utility.Modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Teleportation {



    public static void PlayerTpToSpawn(Player player, String spawn){

        YamlConfiguration cfg = FichierGestion.getCfgSettings();

        Double x = cfg.getDouble("Warps." + spawn + ".x");
        Double z = cfg.getDouble("Warps." + spawn + ".z");
        Double y = cfg.getDouble("Warps." + spawn + ".y");
        float yaw = (float) cfg.getDouble("Warps." + spawn + ".yaw");
        float pitch = (float) cfg.getDouble("Warps." + spawn + ".pitch");
        String worldname = cfg.getString("Warps." + spawn + ".world");

        if(worldname == null){
            player.sendMessage(PrefixMessage.erreur() + " le warp §4" + spawn + " §cn'existe pas");
            return;
        }

        World world = Bukkit.getWorld(worldname);
        player.teleport(new Location(world, x,y,z,yaw,pitch));


    }

}
