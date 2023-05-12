package be.nadtum.etum.Vanilla.MenuGui.City;

import be.nadtum.etum.Vanilla.City.Fonctionnalité.Claim;
import be.nadtum.etum.Vanilla.MenuGui.MenuPrincipal;
import be.nadtum.etum.Utility.Modules.*;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuCity implements Listener {

    private static String nameMenu = "Menu : Cité";
    private static String nameMenuSettings = "Menu : Cité | Settings";
    private static ItemMeta meta;

    public static void menu(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 9);

        ItemBuilder spawn = new ItemBuilder(Material.RED_BED,"§4Spawn",1);
        meta = spawn.getItem().getItemMeta();
        List<String> loreSpawn = new ArrayList<String>();
        loreSpawn.add(!FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".spawn.world") ? "§4le spawn n'est pas placé" : "§2spawn placé");
        meta.setLore(loreSpawn);
        spawn.getItem().setItemMeta(meta);

        ItemBuilder settings = new ItemBuilder(Material.COMPARATOR,"§9Settings",1);

        inv.getInventory().setItem(0, spawn.getItem());
        inv.getInventory().setItem(7, settings.getItem());
        inv.getInventory().setItem(8, MenuGestion.back.getItem());
        for(int i =0; i < 9; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.fill.getItem());
            }
        }

        player.openInventory(inv.getInventory());
    }
    public static void menuSettings(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenuSettings, 9);
        ItemBuilder membres = new ItemBuilder(Material.PLAYER_HEAD, "§dMembres", 1);

        ItemBuilder spawn = new ItemBuilder(Material.RED_BED,"§6set-spawn",1);
        inv.getInventory().setItem(0, spawn.getItem());
        inv.getInventory().setItem(1, membres.getItem());
        inv.getInventory().setItem(8, MenuGestion.back.getItem());

        for(int i =0; i < 9; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.fill.getItem());
            }
        }
        player.openInventory(inv.getInventory());
    }


    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        if (event.getCurrentItem() == null) return;


        if (event.getCurrentItem().getType().equals(Material.MAGENTA_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            return;
        }
        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()){
                case COMPARATOR:
                    if(CityGestion.hasPermission(player.getName(), "admin")){
                        player.closeInventory();
                        MenuCity.menuSettings(player);
                    }else{
                        event.setCancelled(true);
                    }
                    break;
                case RED_BED:
                    //si le spawn de cité placé téléporter le joueur à ce spawn
                    if(CityGestion.hasCitySpawn(player.getName())){
                        CityGestion.tpToSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                    }else{
                        //si il est admin il peut placé le spawn de cité via l'item de ce menu
                        if(CityGestion.hasPermission(player.getName(), "admin")){
                            //on vérifie qu'il est bien dans sa cité
                            if(!Claim.canBuild(player, player.getLocation().getX(),player.getLocation().getZ())){
                                player.closeInventory();
                                player.sendMessage(PrefixMessage.erreur() + "vous n'êtes pas dans votre cité");
                                return;
                            }
                            //on met le nouveau spawn de la cité
                            player.closeInventory();
                            CityGestion.setSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                        }else{
                            //si pas admin on ne fait rien
                            event.setCancelled(true);
                        }
                    }
                    break;
                case DARK_OAK_DOOR:
                    player.closeInventory();
                    MenuPrincipal.menu(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }


        if (event.getView().getTitle().equalsIgnoreCase(nameMenuSettings)) {
            switch (event.getCurrentItem().getType()){
                case PLAYER_HEAD:
                    player.closeInventory();
                    MenuCityGestionMembre.menu(player);
                    break;
                case RED_BED:
                    if(!Claim.canBuild(player, player.getLocation().getX(),player.getLocation().getZ())){
                        player.closeInventory();
                        player.sendMessage(PrefixMessage.erreur() + "vous n'êtes pas dans votre cité");
                        return;
                    }
                    CityGestion.setSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                    event.setCancelled(true);
                    break;
                case DARK_OAK_DOOR:
                    player.closeInventory();
                    MenuCity.menu(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }

    }

}
