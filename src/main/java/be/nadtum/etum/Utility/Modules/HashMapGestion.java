package be.nadtum.etum.Utility.Modules;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class HashMapGestion {
    //player
    public static HashMap<Player, Player> Tpa = new HashMap<>();
    public static HashMap<Block, Block> blockPlaced = new HashMap<>();

    //staff
    private static HashMap<Player, ItemStack[]> Inventaire = new HashMap<>();
    private static HashMap<Player, ItemStack[]> Armor = new HashMap<>();
    private static HashMap<Player, Player> vanish = new HashMap<>();
    public static HashMap<Player, ItemStack[]> getInventaire() {
        return Inventaire;
    }
    public static HashMap<Player, ItemStack[]> getArmor() {
        return Armor;
    }

    public static HashMap<Player, Player> getTpa() {
        return Tpa;
    }

    public static HashMap<Player, Player> getVanish() {
        return vanish;
    }
}
