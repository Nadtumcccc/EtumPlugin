package be.nadtum.etum.Utility.Modules;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FichierGestion {

    // File paths
    private static final String PLUGIN_PATH = "plugins//fichiers";
    private static final String CONFIG_PATH = PLUGIN_PATH + "//configurations";
    private static final String PERMISSION_FILE_PATH = CONFIG_PATH + "//permissions.yml";
    private static final String SETTINGS_FILE_PATH = CONFIG_PATH + "//settings.yml";
    private static final String JOBS_FILE_PATH = CONFIG_PATH + "//jobs.yml";
    private static final String CITY_FILE_PATH = CONFIG_PATH + "//city.yml";
    private static final String PLAYERS_FILE_PATH = PLUGIN_PATH + "//players.yml";
    private static final String REGION_FILE_PATH = CONFIG_PATH + "//region.yml";

    // Configuration objects
    private static YamlConfiguration cfgPermission;
    private static YamlConfiguration cfgSettings;
    private static YamlConfiguration cfgPlayers;
    private static YamlConfiguration cfgJobs;
    private static YamlConfiguration cfgCity;
    private static YamlConfiguration cfgRegion;

    // Files
    private static final File permission = new File(PERMISSION_FILE_PATH);
    private static final File settings = new File(SETTINGS_FILE_PATH);
    private static final File jobs = new File(JOBS_FILE_PATH);
    private static final File city = new File(CITY_FILE_PATH);
    private static final File players = new File(PLAYERS_FILE_PATH);
    private static final File region = new File(REGION_FILE_PATH);

    // Getters for files
    public static File getPermissionFile() {
        return permission;
    }

    public static File getSettingsFile() {
        return settings;
    }

    public static File getCityFile() {
        return city;
    }

    public static File getPlayersFile() {
        return players;
    }

    public static File getJobsFile() {
        return jobs;
    }

    public static File getRegionFile() {
        return region;
    }

    // Getters for configurations
    public static YamlConfiguration getCfgSettings() {
        return cfgSettings;
    }

    public static YamlConfiguration getCfgPlayers() {
        return cfgPlayers;
    }

    public static YamlConfiguration getCfgPermission() {
        return cfgPermission;
    }

    public static YamlConfiguration getCfgJobs() {
        return cfgJobs;
    }

    public static YamlConfiguration getCfgCity() {
        return cfgCity;
    }

    public static YamlConfiguration getCfgRegion() {
        return cfgRegion;
    }

    // Creates necessary files and loads configurations
    public static void CreateFiles() {
        if (!permission.exists()) {
            createNewFile(permission);
        }
        if (!settings.exists()) {
            createNewFile(settings);
        }
        if (!region.exists()) {
            createNewFile(region);
        }
        if (!players.exists()) {
            createNewFile(players);
        }
        if (!jobs.exists()) {
            createNewFile(jobs);
        }
        if (!city.exists()) {
            createNewFile(city);
        }
        loadConfig();
        getCfgCity().set("City.nocity.cantake", false);
    }

    // Create a new file if it does not exist
    private static void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save configuration to the file
    public static void saveFile(YamlConfiguration yamlConfiguration, File file) {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load configurations from files
    private static void loadConfig() {
        cfgSettings = YamlConfiguration.loadConfiguration(settings);
        cfgPermission = YamlConfiguration.loadConfiguration(permission);
        cfgPlayers = YamlConfiguration.loadConfiguration(players);
        cfgJobs = YamlConfiguration.loadConfiguration(jobs);
        cfgCity = YamlConfiguration.loadConfiguration(city);
        cfgRegion = YamlConfiguration.loadConfiguration(region);
    }

    // Method to save all configurations to their respective files
    public static void saveAllFiles() {
        saveFile(cfgPlayers, players);
        saveFile(cfgPermission, permission);
        saveFile(cfgSettings, settings);
        saveFile(cfgJobs, jobs);
        saveFile(cfgCity, city);
        saveFile(cfgRegion, region);
    }
}
