package be.nadtum.etum.Vanilla.Fonctionnalité;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

public class Spawner implements Listener {

    @EventHandler
    public void onBreakSpawner(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!block.getType().equals(Material.SPAWNER)) return;

        if (!PlayerGestion.hasPermission(player, "spawner")) return;

        CreatureSpawner cs = (CreatureSpawner) block.getState();
        World world = block.getWorld();

        ItemStack stack = new ItemStack(Material.SPAWNER, 1);
        BlockDataMeta meta = (BlockDataMeta) stack.getItemMeta();
        meta.setBlockData(cs.getBlockData());
        meta.setDisplayName("§f[§dGénérateur§f] §a§l" + cs.getSpawnedType().name());
        stack.setItemMeta(meta);

        world.dropItem(block.getLocation(), stack);
    }

    @EventHandler
    public void onPlaceSpawner(BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (!block.getType().equals(Material.SPAWNER))
            return;

        CreatureSpawner cs = (CreatureSpawner) block.getState();
        block.setBlockData(cs.getBlockData());
    }
}
