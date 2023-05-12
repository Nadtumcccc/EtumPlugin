package be.nadtum.etum.Utility.Objets;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryBuilder {

    Inventory inventory;

    public InventoryBuilder(String name, Integer nbCase){
        inventory = Bukkit.createInventory(null, nbCase, name);
    }



    public Inventory getInventory(){
        return inventory;
    }

}
