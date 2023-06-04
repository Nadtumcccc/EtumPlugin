package be.nadtum.etum;

import be.nadtum.etum.Listeners.Commands;
import be.nadtum.etum.Listeners.Events;
import be.nadtum.etum.Vanilla.Player.Economy.Depot;
import be.nadtum.etum.Vanilla.Player.Class.RankClass;
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
        RankClass.initializeRanks();

    }

    @Override
    public void onDisable() {
        saveData();
    }

    private void registerListeners() {
        new Commands(this);
        new Events(this);
    }

    private void saveData() {
        RankClass.saveConfigRanks();

    }
}