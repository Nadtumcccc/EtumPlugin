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


public class MenuCity implements Listener {

    private static String nameMenu = "Menu : Cité";
    private static String nameMenuSettings = "Menu : Cité | Settings";

    public static void menu(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 45);

        inv.setupTemplate();

        ItemBuilder spawn = new ItemBuilder(Material.RED_BED,"§4Spawn",1);
        spawn.addLore(!FichierGestion.getCfgCity().contains("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".spawn.world") ? "§4le spawn n'est pas placé" : "§2spawn placé");

        ItemBuilder settings = new ItemBuilder(Material.COMPARATOR,"§9Settings",1);

        inv.getInventory().setItem(20, spawn.getItem());
        inv.getInventory().setItem(24, settings.getItem());


        player.openInventory(inv.getInventory());
    }
    public static void menuSettings(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenuSettings, 27);

        inv.setupTemplate();

        ItemBuilder membres = new ItemBuilder(Material.PLAYER_HEAD, "§dMembres", 1);

        ItemBuilder spawn = new ItemBuilder(Material.RED_BED,"§6set-spawn",1);
        inv.getInventory().setItem(11, spawn.getItem());
        inv.getInventory().setItem(15, membres.getItem());

        player.openInventory(inv.getInventory());
    }


    @EventHandler
    public void onPlayerMenu(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE || event.getClickedInventory().getType() == InventoryType.PLAYER || event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem().getType() == Material.MAGENTA_STAINED_GLASS_PANE) {
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            handleMainMenuClick(event, player);
        } else if (event.getView().getTitle().equalsIgnoreCase(nameMenuSettings)) {
            handleSettingsMenuClick(event, player);
        }
    }

    private void handleMainMenuClick(InventoryClickEvent event, Player player) {
        Material itemType = event.getCurrentItem().getType();

        switch (itemType) {
            case COMPARATOR:
                if (CityGestion.hasPermission(player.getName(), "admin")) {
                    player.closeInventory();
                    MenuCity.menuSettings(player);
                } else {
                    event.setCancelled(true);
                }
                break;
            case RED_BED:
                if (CityGestion.hasCitySpawn(player.getName())) {
                    CityGestion.tpToSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                } else if (CityGestion.hasPermission(player.getName(), "admin")) {
                    if (!Claim.canBuild(player, player.getLocation().getX(), player.getLocation().getZ())) {
                        player.closeInventory();
                        player.sendMessage(PrefixMessage.erreur() + "vous n'êtes pas dans votre cité");
                        return;
                    }
                    player.closeInventory();
                    CityGestion.setSpawn(player, PlayerGestion.getPlayerCityName(player.getName()));
                } else {
                    event.setCancelled(true);
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
    }

    private void handleSettingsMenuClick(InventoryClickEvent event, Player player) {
        Material itemType = event.getCurrentItem().getType();

        switch (itemType) {
            case PLAYER_HEAD:
                player.closeInventory();
                MenuCityGestionMembre.menu(player);
                break;
            case RED_BED:
                if (!Claim.canBuild(player, player.getLocation().getX(), player.getLocation().getZ())) {
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
    }

}
