package be.nadtum.etum.Utility.Modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CityGestion {

    //fonction
    public static Boolean hasCity(String player){
        return !PlayerGestion.getPlayerCityName(player).equals("NoCity");
    }

    public static Boolean hasPermission(String player, String permission){
        return FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player) + ".membres." + PlayerGestion.getUUIDFromName(player).toString() + ".permission." + permission);
    }
    public static void setPermission(String player, String permission){
        FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player) + ".membres." + PlayerGestion.getUUIDFromName(player).toString() + ".permission." + permission, permission);
    }


    public static void removePermission(String player, String permission){
        FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player) + ".membres." + PlayerGestion.getUUIDFromName(player).toString() + ".permission." + permission, null);
    }
    public static Boolean hasCitySpawn(String player){
        return FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player) + ".spawn.world");
    }

    //scrypt
    public static void createCity(Player player, String cityName){

        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".rôle", "Propriétaire");
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.owner", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.build", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.admin", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.claim", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.shop", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.upgrade", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.invite", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".membres." + player.getUniqueId().toString() + ".permission.modération", true);
        FichierGestion.getCfgCity().set("City." + cityName + ".settings.maxMember", 6);
        FichierGestion.getCfgCity().set("City." + cityName + ".settings.maxSurfaceClaim", 1000);
        PlayerGestion.setPlayerCityName(player.getName(), cityName);

        FichierGestion.saveFile(FichierGestion.getCfgCity(), FichierGestion.getFichierCity());
        FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
    }

    public static void deleteCity(Player player){

        for (String uuidstr : FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres").getKeys(false)){
            if(!uuidstr.equals(player.getUniqueId().toString())){
                FichierGestion.getCfgPlayers().set("Profil." + uuidstr + ".city", "NoCity");
            }
        }
        FichierGestion.getCfgCity().set("City." + PlayerGestion.getPlayerCityName(player.getName()), null);
        PlayerGestion.setPlayerCityName(player.getName(), "NoCity");
        FichierGestion.saveFile(FichierGestion.getCfgCity(), FichierGestion.getFichierCity());
        FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
    }

    public static void setSpawn(Player player, String cityName){

        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.x", player.getLocation().getX());
        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.y", player.getLocation().getY());
        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.z", player.getLocation().getZ());
        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.pitch", player.getLocation().getPitch());
        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.yaw", player.getLocation().getYaw());
        FichierGestion.getCfgCity().set("City." + cityName + ".spawn.world", player.getLocation().getWorld().getName());

        player.sendMessage(PrefixMessage.serveur() + "votre spawn de cité à été positionné");
    }

    public static void tpToSpawn(Player player,String cityName){

        YamlConfiguration cfg = FichierGestion.getCfgCity();

        Double x = cfg.getDouble("City." + cityName + ".spawn.x");
        Double z = cfg.getDouble("City." + cityName + ".spawn.z");
        Double y = cfg.getDouble("City." + cityName + ".spawn.y");
        float yaw = (float) cfg.getDouble("City." + cityName + ".spawn.yaw");
        float pitch = (float) cfg.getDouble("City." + cityName + ".spawn.pitch");
        String worldname = cfg.getString("City." + cityName + ".spawn.world");

        World world = Bukkit.getWorld(worldname);
        player.teleport(new Location(world, x,y,z,yaw,pitch));

    }

    //méthode

}
