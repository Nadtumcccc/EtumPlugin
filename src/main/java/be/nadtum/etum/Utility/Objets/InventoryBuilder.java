package be.nadtum.etum.Utility.Objets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {

    Inventory inventory;

    private final ItemBuilder back = new ItemBuilder(Material.DARK_OAK_DOOR, "§cRetour", 1);
    private final ItemBuilder contour = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, " ", 1);

    public InventoryBuilder(String name, Integer nbCase){
        inventory = Bukkit.createInventory(null, nbCase, name);
    }



    public void setupTemplate() {
        int inventorySize = inventory.getSize();
        int numRows = (inventorySize + 8) / 9; // Calculate the number of rows in the inventory

        ItemStack contourItem = contour.getItem();
        ItemStack backItem = back.getItem();

        // Set the contour items for the top row
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, contourItem);
        }

        int bottomRowIndex = (numRows - 1) * 9;
        // Set the contour items for the bottom row
        for (int i = 0; i < 9; i++) {
            inventory.setItem(bottomRowIndex + i, contourItem);
        }

        // Set the contour items for the left and right sides
        for (int row = 1; row < numRows - 1; row++) {
            inventory.setItem(row * 9, contourItem); // Contour gauche
            inventory.setItem(row * 9 + 8, contourItem); // Contour droit
        }

        inventory.setItem(inventorySize - 5, backItem);
    }



    public Inventory getInventory(){
        return inventory;
    }

}
