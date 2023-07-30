package be.nadtum.etum;

import be.nadtum.etum.Listeners.Commands;
import be.nadtum.etum.Listeners.Events;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Vanilla.Player.Economy.Depot;
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
        FichierGestion.CreateFiles();

        registerListeners();
        Depot.initializeJobMenus();
    }

    @Override
    public void onDisable() {
        FichierGestion.saveAllFiles();
    }

    private void registerListeners() {
        new Commands(this);
        new Events(this);
    }

}