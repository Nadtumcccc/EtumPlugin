package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.MenuGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuProfil implements Listener {

    private static String nameMenu = "Menu : Profil";

    public static void menu(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 9);
        inv.getInventory().setItem(8, MenuGestion.back.getItem());

        for(int i =0; i < 9; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.contour.getItem());
            }
        }
        player.openInventory(inv.getInventory());
        player.updateInventory();
    }

    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;


        if (event.getCurrentItem().getType().equals(Material.MAGENTA_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            return;
        }
        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case BARRIER:
                    player.closeInventory();
                    MenuPrincipal.menu(player);
                    break;
                default:
                    break;
            }
            return;
        }

    }

}
