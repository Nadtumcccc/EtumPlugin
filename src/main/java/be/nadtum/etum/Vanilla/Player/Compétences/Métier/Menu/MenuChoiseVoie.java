package be.nadtum.etum.Vanilla.Player.Compétences.Métier.Menu;

import be.nadtum.etum.Vanilla.MenuGui.MenuJob;
import be.nadtum.etum.Utility.Modules.MenuGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuChoiseVoie implements Listener {

    private static String nameMenu = "Menu : choix de ta voie";

    public static void menuChoixDeLaVoix(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);
        /*

        Compétence :
            - Voix du néant
            - Voix de l'ancienneté
            - Voix de l'essence
         */

        ItemBuilder néant = new ItemBuilder(Material.END_CRYSTAL, "§avoix du §5néant", 1);
        ItemBuilder ancienneté = new ItemBuilder(Material.DIAMOND_AXE, "§avoix de §6l'ancienneté", 1);
        ItemBuilder essence = new ItemBuilder(Material.EXPERIENCE_BOTTLE, "§avoix de §bl'essence", 1);

        inv.getInventory().setItem(20, néant.getItem());
        inv.getInventory().setItem(22, ancienneté.getItem());
        inv.getInventory().setItem(24, essence.getItem());
        inv.getInventory().setItem(8, MenuGestion.back.getItem());

        for(int i =0; i < 54; i++){
            if (inv.getInventory().getItem(i) == null) {
                inv.getInventory().setItem(i, MenuGestion.contour.getItem());
            }
        }
        player.openInventory(inv.getInventory());
    }

    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;


        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case END_CRYSTAL:
                    player.closeInventory();
                    PlayerGestion.setPlayerJobsVoie(player.getName(), "néant");
                    break;
                case DIAMOND_AXE:
                    player.closeInventory();
                    PlayerGestion.setPlayerJobsVoie(player.getName(), "ancienneté");
                    break;
                case EXPERIENCE_BOTTLE:
                    player.closeInventory();
                    PlayerGestion.setPlayerJobsVoie(player.getName(), "essence");
                    break;
                case BARRIER:
                    player.closeInventory();
                    break;
                default:
                    event.setCancelled(true);
                    return;
            }

            MenuJob.menu(player);
            return;
        }
    }

}
