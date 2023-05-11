package be.nadtum.etum.Utility.Modules;



import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class FichierGestion {

    private static File fichier = new File("plugins//fichiers");
    private static File configuration = new File("plugins//fichiers//configurations");
    private static File permission = new File("plugins//fichiers//configurations//permissions.yml");
    private static File settings = new File("plugins//fichiers//configurations//settings.yml");
    private static File jobs = new File("plugins//fichiers//configurations//jobs.yml");
    private static File city = new File("plugins//fichiers//configurations//city.yml");
    private static File players= new File("plugins//fichiers//players.yml");
    private static File region = new File("plugins//fichiers//configurations//region.yml");


    public static File getPermissionFiles() {
        return permission;
    }
    public static File getSettingsFile(){
        return settings;
    }
    public static File getFichierCity(){
        return city;
    }
    public static File getFichierPlayers(){
        return players;
    }
    public static File getFichierJobs(){
        return jobs;
    }
    public static File getFichierRegion(){
        return region;
    }


    //configuration
    private static YamlConfiguration cfgPermission;
    private static YamlConfiguration cfgSettings;
    private static YamlConfiguration cfgPlayers;
    private static YamlConfiguration cfgJobs;
    private static YamlConfiguration cfgCity;
    private static YamlConfiguration cfgRegion;


    public static YamlConfiguration getCfgSettings(){
        return cfgSettings;
    }
    public static YamlConfiguration getCfgPlayers(){
        return cfgPlayers;
    }
    public static YamlConfiguration getCfgPermission(){
        return cfgPermission;
    }
    public static YamlConfiguration getCfgJobs(){
        return cfgJobs;
    }
    public static YamlConfiguration getCfgCity(){
        return cfgCity;
    }
    public static YamlConfiguration getCfgRegion(){
        return cfgRegion;
    }

    public static void CreateFiles(){

        if(!fichier.exists()){
            fichier.mkdirs();
        }

        if(!configuration.exists()){
            configuration.mkdirs();
        }

        if(!permission.exists()){
            createNewFile(permission);
        }
        if(!settings.exists()){
            createNewFile(settings);
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(settings);
            cfg.set("Settings.Module.MOTD", "changer le motd de ce serveur");
            saveFile(cfg, settings);
        }

        if(!region.exists()){
            createNewFile(region);
        }

        if(!players.exists()){
            createNewFile(players);
        }

        if(!jobs.exists()){
            createNewFile(jobs);
        }

        if(!city.exists()){
            createNewFile(city);
        }
        LoadConfig();
        cfgCity.set("City.nocity.cantake", false);
        FichierGestion.saveFile(cfgCity, city);
    }



    public static void createNewFile(File file){
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(YamlConfiguration yamlConfiguration, File file){
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadConfig(){

        cfgSettings = YamlConfiguration.loadConfiguration(settings);
        cfgPermission = YamlConfiguration.loadConfiguration(permission);
        cfgPlayers = YamlConfiguration.loadConfiguration(players);
        cfgJobs = YamlConfiguration.loadConfiguration(jobs);
        cfgCity = YamlConfiguration.loadConfiguration(city);
        cfgRegion = YamlConfiguration.loadConfiguration(region);

    }

}
