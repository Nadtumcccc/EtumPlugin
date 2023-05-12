package be.nadtum.etum.Utility.Modules;

import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MenuGestion {

    public static ItemBuilder fill = new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE, "§5Contour", 1);
    public static ItemBuilder back = new ItemBuilder(Material.DARK_OAK_DOOR, "§cRetour", 1);


    public static void setupTemplate(Inventory inv) {

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, fill.getItem());
        }

        inv.setItem(inv.getSize() - 5, back.getItem());
    }

}
