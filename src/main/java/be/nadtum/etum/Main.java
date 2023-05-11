package be.nadtum.etum;


import be.nadtum.etum.Listeners.Commands;
import be.nadtum.etum.Listeners.events;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new Commands(this);
        new events(this);

    }

    @Override
    public void onDisable() {

        //sauvegardes des fichiers
        FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
        FichierGestion.saveFile(FichierGestion.getCfgPermission(), FichierGestion.getPermissionFiles());
        FichierGestion.saveFile(FichierGestion.getCfgSettings(), FichierGestion.getSettingsFile());
        FichierGestion.saveFile(FichierGestion.getCfgJobs(), FichierGestion.getFichierJobs());
        FichierGestion.saveFile(FichierGestion.getCfgCity(), FichierGestion.getFichierCity());
        FichierGestion.saveFile(FichierGestion.getCfgRegion(), FichierGestion.getFichierRegion());

    }


}
