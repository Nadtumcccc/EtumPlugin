package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.Teleportation;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuMondeRessource implements Listener {

    private static String nameMenu = "Menu : Monde Ressource";

    public static void menu(Player player) {



        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);

        inv.setupTemplate();

        ItemBuilder overworld = new ItemBuilder(Material.GRASS_BLOCK, "§2OverWorld", 1);
        ItemBuilder nether = new ItemBuilder(Material.NETHER_BRICK, "§4Nether", 1);
        ItemBuilder end = new ItemBuilder(Material.END_CRYSTAL, "§5End", 1);

        inv.getInventory().setItem(20, overworld.getItem());
        inv.getInventory().setItem(22, nether.getItem());
        inv.getInventory().setItem(24, end.getItem());


        player.openInventory(inv.getInventory());

    }


    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        event.setCancelled(true);

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case GRASS_BLOCK:
                    Teleportation.PlayerTpToSpawn(player, "OverWorld");
                    break;
                case NETHER_BRICK:
                    Teleportation.PlayerTpToSpawn(player, "Nether");
                    break;
                case END_CRYSTAL:
                    Teleportation.PlayerTpToSpawn(player, "End");
                    break;
                case DARK_OAK_DOOR:
                    MenuPrincipal.menu(player);
                    break;
                default:
                    break;
            }

        }
    }
}


