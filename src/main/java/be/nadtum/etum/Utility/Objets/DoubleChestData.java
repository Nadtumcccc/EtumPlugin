package be.nadtum.etum.Utility.Objets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DoubleChestData {

    private Material chestType;
    private ItemStack[] contentsLeft;
    private ItemStack[] contentsRight;

    public DoubleChestData(Material chestType, ItemStack[] contentsLeft, ItemStack[] contentsRight) {
        this.chestType = chestType;
        this.contentsLeft = contentsLeft;
        this.contentsRight = contentsRight;
    }

    public Material getChestType() {
        return chestType;
    }

    public ItemStack[] getContentsLeft() {
        return contentsLeft;
    }

    public ItemStack[] getContentsRight() {
        return contentsRight;
    }

}
