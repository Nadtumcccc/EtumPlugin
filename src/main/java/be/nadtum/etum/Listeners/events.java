package be.nadtum.etum.Listeners;

import be.nadtum.etum.Vanilla.MenuGui.City.*;
import be.nadtum.etum.Vanilla.Player.Jobs.*;
import be.nadtum.etum.Vanilla.Economie.shop.*;
import be.nadtum.etum.Vanilla.City.Fonctionnalité.Claim;
import be.nadtum.etum.Vanilla.Fonctionnalité.Spawner;
import be.nadtum.etum.Main;
import be.nadtum.etum.Region.RegionGestion;
import be.nadtum.etum.Staff.ACC.events.PlayerJoinQuit;
import be.nadtum.etum.Utility.Modules.*;

import be.nadtum.etum.Vanilla.MenuGui.MenuHome;
import be.nadtum.etum.Vanilla.MenuGui.MenuJob;
import be.nadtum.etum.Vanilla.MenuGui.MenuMondeRessource;
import be.nadtum.etum.Vanilla.MenuGui.MenuPrincipal;
import be.nadtum.etum.Vanilla.Player.event.Connection;
import be.nadtum.etum.Vanilla.Player.event.Fight;
import be.nadtum.etum.Vanilla.Player.event.Death;
import org.bukkit.plugin.PluginManager;

public class events {

    PluginManager pluginManager;

    public void evens(Main main){

        this.pluginManager = main.getServer().getPluginManager();

        // Fichier load server
        FichierGestion.CreateFiles();
        FichierGestion.LoadConfig();

        registerServerEvents(main);
        registerFunctionalityEvents(main);
        registerPlayerEvents(main);
        registerMenuEvents(main);
        registerEconomyEvents(main);
        registerCityEvents(main);
        registerStaffEvents(main);
    }


    private void registerServerEvents(Main main) {
        pluginManager.registerEvents(new ServerList(), main);
    }

    private void registerFunctionalityEvents(Main main) {
        pluginManager.registerEvents(new Spawner(), main);
    }

    private void registerPlayerEvents(Main main) {
        pluginManager.registerEvents(new Chat(), main);
        pluginManager.registerEvents(new Death(), main);
        pluginManager.registerEvents(new Fight(), main);
    }

    private void registerMenuEvents(Main main) {
        pluginManager.registerEvents(new MenuPrincipal(), main);
        pluginManager.registerEvents(new MenuJob(), main);
        pluginManager.registerEvents(new MenuMondeRessource(), main);
        pluginManager.registerEvents(new MenuHome(), main);
        pluginManager.registerEvents(new MenuCity(), main);
        pluginManager.registerEvents(new MenuCityGestionMembre(), main);
    }

    private void registerEconomyEvents(Main main) {
        pluginManager.registerEvents(new Shop_Commerçant(), main);
        pluginManager.registerEvents(new Shop_Serveur(), main);
    }

    private void registerCityEvents(Main main) {
        pluginManager.registerEvents(new Claim(), main);
        pluginManager.registerEvents(new Connection(), main);
    }

    private void registerStaffEvents(Main main) {
        pluginManager.registerEvents(new PlayerJoinQuit(), main);
        pluginManager.registerEvents(new RegionGestion(), main);
    }

}