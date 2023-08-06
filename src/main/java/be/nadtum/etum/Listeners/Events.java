package be.nadtum.etum.Listeners;

import be.nadtum.etum.Main;
import be.nadtum.etum.Moderation.ACC.events.PlayerJoinQuit;
import be.nadtum.etum.Utility.Modules.ChatManage;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.ServerList;
import be.nadtum.etum.Vanilla.City.Claim.Claim;
import be.nadtum.etum.Vanilla.Feature.Spawner;
import be.nadtum.etum.Vanilla.MenuGui.City.MenuCity;
import be.nadtum.etum.Vanilla.MenuGui.City.MenuCityGestionMembre;
import be.nadtum.etum.Vanilla.MenuGui.MenuHome;
import be.nadtum.etum.Vanilla.MenuGui.MenuJob;
import be.nadtum.etum.Vanilla.MenuGui.MenuMondeRessource;
import be.nadtum.etum.Vanilla.MenuGui.MenuPrincipal;
import be.nadtum.etum.Vanilla.Player.Economy.Depot;
import be.nadtum.etum.Vanilla.Player.Economy.Jobs.ActionManager;
import be.nadtum.etum.Vanilla.Player.Economy.shop.Market;
import be.nadtum.etum.Vanilla.Player.Economy.shop.ShopTrader;
import be.nadtum.etum.Vanilla.Player.Events.Connection;
import be.nadtum.etum.Vanilla.Player.Events.Death;
import be.nadtum.etum.Vanilla.Player.Events.Fight;
import be.nadtum.etum.Vanilla.Region.RegionManage;
import org.bukkit.plugin.PluginManager;

public class Events {

    PluginManager pluginManager;

    public Events(Main main) {

        this.pluginManager = main.getServer().getPluginManager();

        // Fichier load server
        FichierGestion.CreateFiles();

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
        pluginManager.registerEvents(new ChatManage(), main);
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
        pluginManager.registerEvents(new ShopTrader(), main);
        pluginManager.registerEvents(new Market(), main);
        pluginManager.registerEvents(new Depot(), main);
        pluginManager.registerEvents(new ActionManager(), main);
    }



    private void registerCityEvents(Main main) {
        pluginManager.registerEvents(new Claim(), main);
        pluginManager.registerEvents(new RegionManage(), main);
        pluginManager.registerEvents(new Connection(), main);
    }

    private void registerStaffEvents(Main main) {
        pluginManager.registerEvents(new PlayerJoinQuit(), main);
    }

}