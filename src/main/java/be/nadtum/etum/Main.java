package be.nadtum.etum;

import be.nadtum.etum.Listeners.Commands;
import be.nadtum.etum.Listeners.events;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Vanilla.Economie.Depot;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        registerListeners();
        Depot.initializeJobMenus();
    }

    @Override
    public void onDisable() {
        saveFiles();
    }

    private void registerListeners() {
        new Commands(this);
        new events(this);
    }

    private void saveFiles() {
        FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
        FichierGestion.saveFile(FichierGestion.getCfgPermission(), FichierGestion.getPermissionFiles());
        FichierGestion.saveFile(FichierGestion.getCfgSettings(), FichierGestion.getSettingsFile());
        FichierGestion.saveFile(FichierGestion.getCfgJobs(), FichierGestion.getFichierJobs());
        FichierGestion.saveFile(FichierGestion.getCfgCity(), FichierGestion.getFichierCity());
        FichierGestion.saveFile(FichierGestion.getCfgRegion(), FichierGestion.getFichierRegion());
    }
}