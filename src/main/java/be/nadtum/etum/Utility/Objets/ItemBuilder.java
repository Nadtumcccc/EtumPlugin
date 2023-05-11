package be.nadtum.etum.Utility.Objets;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material, String displayName, Integer amount) {
        itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text(displayName));
        itemStack.setItemMeta(meta);
    }

    public void setDisplayName(String displayName) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text(displayName));
        itemStack.setItemMeta(meta);
    }

    public void addLore(String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loreList = meta.getLore();
        if (loreList == null) {
            loreList = new ArrayList<>();
        }
        loreList.add(lore);
        meta.setLore(loreList);
        itemStack.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return itemStack;
    }
}
