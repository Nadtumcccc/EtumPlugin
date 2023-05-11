package be.nadtum.etum.Utility.Objets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

    private ItemStack stack;

    public ItemBuilder(Material material, String name, Integer quantité){
        stack = new ItemStack(material,quantité);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
    }

    public ItemStack getItem(){
        return stack;
    }
}
