package be.nadtum.etum.Utility.Modules;



import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FichierGestion {

    private static File fichier = new File("plugins//fichiers");
    private static File configuration = new File("plugins//fichiers//configurations");
    private static File players = new File("plugins//fichiers//players");
    private static File ranks = new File("plugins//fichiers//configurations//ranks.yml");
    private static File settings = new File("plugins//fichiers//configurations//settings.yml");
    private static File economie = new File("plugins//fichiers//configurations//economie.yml");
    private static File city = new File("plugins//fichiers//configurations//city.yml");
    private static File region = new File("plugins//fichiers//configurations//region.yml");


    public static File getRanksFile() {
        return ranks;
    }
    public static File getSettingsFile(){
        return settings;
    }
    public static File getCityFile(){
        return city;
    }
    public static File getFichierEconomie(){
        return economie;
    }
    public static File getFichierRegion(){
        return region;
    }

    public static void CreateFiles(){

        if(!fichier.exists()){
            fichier.mkdirs();
        }

        if(!configuration.exists()){
            configuration.mkdirs();
        }

        if(!players.exists()){
            players.mkdirs();
        }

        if(!ranks.exists()){
            createNewFile(ranks);
        }
        if(!settings.exists()){
            createNewFile(settings);
        }

        if(!region.exists()){
            createNewFile(region);
        }

        if(!economie.exists()){
            createNewFile(economie);
        }

        if(!city.exists()){
            createNewFile(city);
        }
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


}
