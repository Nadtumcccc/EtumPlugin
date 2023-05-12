package be.nadtum.etum.Listeners;

import be.nadtum.etum.Vanilla.MenuGui.City.*;
import be.nadtum.etum.Vanilla.Economie.Métier.*;
import be.nadtum.etum.Vanilla.Economie.shop.*;
import be.nadtum.etum.Vanilla.City.Fonctionnalité.Claim;
import be.nadtum.etum.Vanilla.Fonctionnalité.Spawner;
import be.nadtum.etum.Main;
import be.nadtum.etum.Region.RegionGestion;
import be.nadtum.etum.Staff.ACC.events.PlayerJoinQuit;
import be.nadtum.etum.Utility.Modules.*;

import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuChoiseVoie;
import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuCompétenceMétier;
import be.nadtum.etum.Vanilla.MenuGui.MenuHome;
import be.nadtum.etum.Vanilla.MenuGui.MenuJob;
import be.nadtum.etum.Vanilla.MenuGui.MenuMondeRessource;
import be.nadtum.etum.Vanilla.MenuGui.MenuPrincipal;
import be.nadtum.etum.Vanilla.Player.event.Connection;
import be.nadtum.etum.Vanilla.Player.event.Fight;
import be.nadtum.etum.Vanilla.Player.event.Death;
import org.bukkit.plugin.PluginManager;

public class events {

    protected PluginManager pm;

    public events(Main main) {
        pm = main.getServer().getPluginManager();

        // Fichier load server
        FichierGestion.CreateFiles();
        FichierGestion.LoadConfig();

        // MOTD
        pm.registerEvents(new ServerList(), main);

        // Fonctionnalité
        pm.registerEvents(new Spawner(), main);

        // Player
        pm.registerEvents(new Chat(), main);
        pm.registerEvents(new Death(), main);
        pm.registerEvents(new Fight(), main);

        // Compétence métier
        pm.registerEvents(new MenuChoiseVoie(), main);
        pm.registerEvents(new MenuCompétenceMétier(), main);

        // Menu
        // Menu gestion de base
        pm.registerEvents(new MenuPrincipal(), main);
        pm.registerEvents(new MenuJob(), main);
        pm.registerEvents(new MenuMondeRessource(), main);
        pm.registerEvents(new MenuHome(), main);

        // Menu city
        pm.registerEvents(new MenuCity(), main);
        pm.registerEvents(new MenuCityGestionMembre(), main);

        // Economie
        pm.registerEvents(new Shop_Commerçant(), main);
        pm.registerEvents(new Shop_Serveur(), main);

        pm.registerEvents(new Mineur(), main);
        pm.registerEvents(new Bucheron(), main);
        pm.registerEvents(new Fermier(), main);
        pm.registerEvents(new Chasseur(), main);
        pm.registerEvents(new Pêcheur(), main);

        // City
        pm.registerEvents(new Claim(), main);

        pm.registerEvents(new Connection(), main);

        // Staff
        // Modération
        pm.registerEvents(new PlayerJoinQuit(), main);
        pm.registerEvents(new RegionGestion(), main);
    }
}