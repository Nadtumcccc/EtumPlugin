package be.nadtum.etum.Listeners;

import be.nadtum.etum.Vanilla.City.menu.MenuCity;
import be.nadtum.etum.Vanilla.City.menu.MenuCityGestionMembre;
import be.nadtum.etum.Vanilla.Economie.Métier.*;
import be.nadtum.etum.Vanilla.Economie.shop.Shop_Serveur;
import be.nadtum.etum.Vanilla.Economie.shop.Shop_Commerçant;
import be.nadtum.etum.Vanilla.City.Fonctionnalité.Claim;
import be.nadtum.etum.Vanilla.Fonctionnalité.Spawner;
import be.nadtum.etum.Vanilla.MenuGui.*;
import be.nadtum.etum.Lobby.event.PlayerGestionConnection;
import be.nadtum.etum.Main;
import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuChoiseVoie;
import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuCompétenceMétier;
import be.nadtum.etum.Vanilla.Player.JoueurGestionCombat;
import be.nadtum.etum.Vanilla.Player.JoueurGestionDeath;
import be.nadtum.etum.Region.RegionGestion;
import be.nadtum.etum.Staff.ACC.events.PlayerJoinQuit;
import be.nadtum.etum.Utility.Modules.Chat;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.ServerList;
import org.bukkit.plugin.PluginManager;

public class events {

    protected PluginManager pm;
    public events(Main main) {
        pm = main.getServer().getPluginManager();
        //fichier load server
        FichierGestion.CreateFiles();
        FichierGestion.LoadConfig();

        //MOTD
        pm.registerEvents(new ServerList(), main);

        //Fonctionnalité
        pm.registerEvents(new Spawner(), main);

        //Player
        pm.registerEvents(new Chat(), main);
        pm.registerEvents(new JoueurGestionDeath(), main);
        pm.registerEvents(new JoueurGestionCombat(), main);




            //compétence métier
        pm.registerEvents(new MenuChoiseVoie(), main);
        pm.registerEvents(new MenuCompétenceMétier(), main);

        //menu
        //menu gestion de base
        pm.registerEvents(new MenuPrincipal(), main);
        pm.registerEvents(new MenuJob(), main);
        pm.registerEvents(new MenuMondeRessource(), main);
        pm.registerEvents(new MenuHome(), main);
        pm.registerEvents(new MenuProfil(), main);

        //menu city
        pm.registerEvents(new MenuCity(), main);
        pm.registerEvents(new MenuCityGestionMembre(), main);

        //Economie
        pm.registerEvents(new Shop_Commerçant(), main);
        pm.registerEvents(new Shop_Serveur(), main);

        pm.registerEvents(new Mineur(), main);
        pm.registerEvents(new Bucheron(), main);
        pm.registerEvents(new Fermier(), main);
        pm.registerEvents(new Chasseur(), main);
        pm.registerEvents(new Pêcheur(), main);

        //City
        pm.registerEvents(new Claim(), main);

        pm.registerEvents(new PlayerGestionConnection(), main);


        //staff
            //modération
        pm.registerEvents(new PlayerJoinQuit(), main);
        pm.registerEvents(new RegionGestion(), main);



    }


}
