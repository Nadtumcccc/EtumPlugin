package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.MenuGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuHome implements Listener {

    private static String nameMenu = "Menu : Home";

    public static void menu(Player player){

        YamlConfiguration cfg_profil = FichierGestion.getCfgPlayers();
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 18);

        Integer nbCase = 9;
        if(cfg_profil.contains("Profil." + player.getUniqueId().toString() + ".Home.homes")){
            for (String home_name : cfg_profil.getConfigurationSection("Profil." + player.getUniqueId().toString() + ".Home.homes").getKeys(false)) {
                ItemBuilder home = new ItemBuilder(Material.OAK_SIGN, home_name , 1);
                inv.getInventory().setItem(nbCase, home.getItem());
                nbCase = nbCase + 1;
            }
        }

        inv.getInventory().setItem(8, MenuGestion.back.getItem());

        for(int i =0; i < 18; i++){
            if(inv.getInventory().getItem(i) == null){
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

        if (event.getCurrentItem().getType().equals(Material.MAGENTA_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            return;
        }
        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case OAK_SIGN:
                    player.closeInventory();
                    String home_name = event.getCurrentItem().getItemMeta().getDisplayName();
                    player.performCommand("home " + home_name);
                    break;
                case STRUCTURE_VOID:
                    event.setCancelled(true);
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
