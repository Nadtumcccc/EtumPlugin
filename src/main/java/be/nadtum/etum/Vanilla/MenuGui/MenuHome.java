package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MenuHome implements Listener {

    private static String nameMenu = "Menu : Home";

    public static void menu(Player player) {
        YamlConfiguration cfg_profil = FichierGestion.getCfgPlayers();
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 45);

        inv.setupTemplate();

        int nbCase = 0;
        ConfigurationSection homesSection = cfg_profil.getConfigurationSection("Profil." + player.getUniqueId() + ".Home.homes");
        if (homesSection != null) {
            for (String homeName : Objects.requireNonNull(homesSection.getKeys(false))) {
                ItemBuilder home = new ItemBuilder(Material.OAK_SIGN, homeName, 1);

                while (nbCase < inv.getInventory().getSize() && inv.getInventory().getItem(nbCase) != null) {
                    nbCase++;
                }

                if (nbCase >= inv.getInventory().getSize()) {
                    break;
                }

                inv.getInventory().setItem(nbCase, home.getItem());
                nbCase++;
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

        event.setCancelled(true);

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case OAK_SIGN:
                    player.closeInventory();
                    String home_name = event.getCurrentItem().getItemMeta().getDisplayName();
                    player.performCommand("home " + home_name);
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
