package be.nadtum.etum.Vanilla.Fonctionnalité;


import be.nadtum.etum.Utility.Modules.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class Spawner implements Listener {



    @EventHandler
    public void onBreakSpawner(BlockBreakEvent e){

        Player player = e.getPlayer();

        Block block = e.getBlock();

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!block.getType().equals(Material.SPAWNER))return;

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.spawner")) {
            if (!player.isOp()) {
                return;
            }
        }

        Location locSpawner = block.getLocation();

        CreatureSpawner cs = (CreatureSpawner) block.getState();

        World world = locSpawner.getWorld();

        //vérifier si ne pas etre légacier fonctionne
        ItemStack stack = new ItemStack(Material.SPAWNER, 1);

        BlockStateMeta meta = (BlockStateMeta) stack.getItemMeta();
        meta.setDisplayName("§f[§dGénérateur§f] §a§l" + cs.getCreatureTypeName());
        meta.setBlockState(cs);
        stack.setItemMeta(meta);

        world.dropItem(locSpawner, stack);


    }

    @EventHandler
    public void onBPlaceSpawner(BlockBreakEvent e){


        Player player = e.getPlayer();

        Block block = e.getBlock();
        if(!block.getType().equals(Material.SPAWNER))return;
        CreatureSpawner cs = (CreatureSpawner) block.getState();
        block.setBlockData(cs.getBlockData());


    }




}
