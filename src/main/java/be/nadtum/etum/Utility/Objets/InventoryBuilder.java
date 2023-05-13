package be.nadtum.etum.Utility.Objets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryBuilder {

    Inventory inventory;

    private ItemBuilder fill = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§5Contour", 1);
    private ItemBuilder back = new ItemBuilder(Material.DARK_OAK_DOOR, "§cRetour", 1);

    public InventoryBuilder(String name, Integer nbCase){
        inventory = Bukkit.createInventory(null, nbCase, name);
    }


    public void setupTemplate() {

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fill.getItem());
        }
        inventory.setItem(inventory.getSize() - 5, back.getItem());
    }


    public Inventory getInventory(){
        return inventory;
    }

}
