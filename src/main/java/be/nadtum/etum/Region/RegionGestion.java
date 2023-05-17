package be.nadtum.etum.Region;

import be.nadtum.etum.Vanilla.City.Management.Claim;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerBucketFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class RegionGestion implements Listener {

    public static HashMap<Player, Integer> cachecoordonnéex1 = new HashMap<>();
    public static HashMap<Player, Integer> cachecoordonnéez1 = new HashMap<>();
    public static HashMap<Player, Integer> cachecoordonnéex2 = new HashMap<>();
    public static HashMap<Player, Integer> cachecoordonnéez2 = new HashMap<>();

    private static final HashMap<Player, Boolean> isActionOnRegion = new HashMap<>();

    @EventHandler
    public void setPoseRegion(PlayerInteractEvent event) {


        //on vérifie si les conditions de départ sont bonnes
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        //on vérifie s'il à la permission de gérer les regions
        if (!FichierGestion.getCfgPermission().contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.region")) {
            if (!player.isOp()) {
                return;
            }
        }



        switch (event.getPlayer().getInventory().getItemInMainHand().getType()){
            case GOLDEN_AXE:
                if (!isActionOnRegion.containsKey(player)) {

                    if(player.isSneaking())return;
                    cachecoordonnéex1.put(player, block.getX());
                    cachecoordonnéez1.put(player, block.getZ());
                    player.sendMessage(PrefixMessage.admin() + "le premier point de la zone a bien été placé");
                    isActionOnRegion.put(player, true);
                }else{
                    //deuxième pose
                    //on vérifie si la pose finale ne se trouve pas sur un autre claim

                    if(!player.isSneaking())return;

                    cachecoordonnéex2.put(player, block.getX());
                    cachecoordonnéez2.put(player, block.getZ());

                    isActionOnRegion.remove(player);

                    player.sendMessage(PrefixMessage.admin() + "le deuxième point de claim a bien été placé");
                    player.sendMessage(PrefixMessage.admin() + "utilisé /rg create [name of region] pour définir la region");
                }
            default:
                break;
        }
    }
    @EventHandler
    public void PlaceBucketEmpty(PlayerBucketEmptyEvent event){
        if(!Claim.canBuild(event.getPlayer(), event.getBlock().getX(), event.getBlock().getZ())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void Fire(BlockBurnEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void PlaceBucketFish(PlayerBucketFishEvent event){
        if(!Claim.canBuild(event.getPlayer(), event.getEntity().getLocation().getX(), event.getEntity().getLocation().getZ())){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlaceBucketFill(PlayerBucketFillEvent event){
        if(!Claim.canBuild(event.getPlayer(), event.getBlock().getX(), event.getBlock().getZ())){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void protectPlayer(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player player = (Player) event.getEntity();
        if(getNameOfRegion(player, player.getLocation().getX(),player.getLocation().getZ()) != null){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void protectEntity(EntityDamageByEntityEvent event){
        Player player = null;
        if(event.getEntity() instanceof Player){
            player = (Player) event.getEntity();
        }else{
            if(event.getDamager() instanceof Player){
                player = (Player) event.getDamager();
                if(Claim.canBuild((Player) event.getDamager(), player.getLocation().getX(), player.getLocation().getZ()))return;
            }
        }
        if (player == null)return;
        if(getNameOfRegion(player, player.getLocation().getX(),player.getLocation().getZ()) != null){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void cancelSpawnMonsters(CreatureSpawnEvent event) {
        Location entityLocation = event.getEntity().getLocation();
        World entityWorld = entityLocation.getWorld();
        YamlConfiguration cfg = FichierGestion.getCfgRegion();

        if (!cfg.contains("Region")) {
            return;
        }

        String regionPrefix = "Region.";

        for (String region : cfg.getConfigurationSection(regionPrefix).getValues(false).keySet()) {
            String regionWorld = cfg.getString(regionPrefix + region + ".coordonnées.world");

            if (entityWorld == null || !entityWorld.getName().equalsIgnoreCase(regionWorld)) {
                continue;
            }

            double x1 = cfg.getDouble(regionPrefix + region + ".coordonnées.x1");
            double z1 = cfg.getDouble(regionPrefix + region + ".coordonnées.z1");
            double x2 = cfg.getDouble(regionPrefix + region + ".coordonnées.x2");
            double z2 = cfg.getDouble(regionPrefix + region + ".coordonnées.z2");

            double grandx = Math.max(x1, x2);
            double petitx = Math.min(x1, x2);
            double grandz = Math.max(z1, z2);
            double petitz = Math.min(z1, z2);

            if (entityLocation.getX() >= petitx && entityLocation.getX() <= grandx
                    && entityLocation.getZ() >= petitz && entityLocation.getZ() <= grandz) {
                if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void setRegion(String region, Player player){
        FichierGestion.getCfgRegion().set("Region." + region + ".coordonnées.x1", cachecoordonnéex1.get(player));
        FichierGestion.getCfgRegion().set("Region." + region + ".coordonnées.z1", cachecoordonnéez1.get(player));
        FichierGestion.getCfgRegion().set("Region." + region + ".coordonnées.x2", cachecoordonnéex2.get(player));
        FichierGestion.getCfgRegion().set("Region." + region + ".coordonnées.z2", cachecoordonnéez2.get(player));
        FichierGestion.getCfgRegion().set("Region." + region + ".coordonnées.world", player.getWorld().getName());
    }

    public static void deleteRegion(String region){
        FichierGestion.getCfgRegion().set("Region." + region, null);
    }



    public static Boolean verifIfClaimIsOnOtherRegion(Player player, YamlConfiguration cfg, String cityname, Double xBlock, Double zBlock){

        double x1Player = 0;
        double z1Player = 0;

        if(Claim.isActionOnClaim.containsKey(player) && player.isSneaking()){
            x1Player = FichierGestion.getCfgCity().getDouble("City." + cityname + ".zone.coordonnées.x1");
            z1Player = FichierGestion.getCfgCity().getDouble("City." + cityname + ".zone.coordonnées.z1");
        }else{
            return true;
        }

        if(cfg.contains("Region")) {

            for (String region : cfg.getConfigurationSection("Region.").getKeys(false)) {
                double x1 = cfg.getDouble("Region." + region + ".coordonnées.x1");
                double z1 = cfg.getDouble("Region." + region + ".coordonnées.z1");
                double x2 = cfg.getDouble("Region." + region + ".coordonnées.x2");
                double z2 = cfg.getDouble("Region." + region + ".coordonnées.z2");

                double grandx, petitx, grandz, petitz;


                grandx = Math.max(x1, x2);
                petitx = Math.min(x1, x2);
                grandz = Math.max(z1, z2);
                petitz = Math.min(z1, z2);

                double playergrandx, playerpetitx, playergrandz, playerpetitz;

                playergrandx = Math.max(x1Player, xBlock);
                playerpetitx = Math.min(x1Player, xBlock);
                playergrandz = Math.max(z1Player, zBlock);
                playerpetitz = Math.min(z1Player, zBlock);

                if (((grandz > playerpetitz && grandz < playergrandz) || (petitz > playerpetitz && petitz < playergrandz))
                        && ((grandx > playerpetitx && grandx < playergrandx) || (petitx > playerpetitx && petitx < playergrandx))) {
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la région " + region);
                    Claim.cancelActionClaim(player);
                    return true;
                }

                if ((playergrandx > petitx && playergrandx < grandx)
                        && (playerpetitz > petitz && playerpetitz < grandz)) {
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la région " + region);
                    Claim.cancelActionClaim(player);
                    return true;
                }
            }

            String regionname = getNameOfRegion(player, xBlock, zBlock);

            if(regionname != null){
                if(!regionname.equals(PlayerGestion.getPlayerCityName(player.getName()))){
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la region " + regionname);
                    Claim.cancelActionClaim(player);
                    return true;
                }
            }


        }
        return false;
    }

    public static String getNameOfRegion(Player player, double xBlock, double zBlock){
        YamlConfiguration cfg = FichierGestion.getCfgRegion();
        if(cfg.contains("Region")) {
            for(String regions : cfg.getConfigurationSection("Region.").getKeys(false)){
                if(cfg.contains("Region." + regions + ".coordonnées.z2")){
                    Double x1 = cfg.getDouble("Region." + regions + ".coordonnées.x1");
                    Double z1 = cfg.getDouble("Region." + regions + ".coordonnées.z1");
                    Double x2 = cfg.getDouble("Region." + regions + ".coordonnées.x2");
                    Double z2 = cfg.getDouble("Region." + regions + ".coordonnées.z2");


                    Double petitx;
                    Double grandx;
                    if(x1 > x2){
                        grandx = x1;
                        petitx = x2;
                    }else{
                        grandx = x2;
                        petitx = x1;
                    }

                    Double petitz;
                    Double grandz;
                    if(z1 > z2){
                        grandz = z1;
                        petitz = z2;
                    }else{
                        grandz = z2;
                        petitz = z1;
                    }
                    if(player.getWorld().getName().equalsIgnoreCase(cfg.getString("Region." + regions + ".coordonnées.world"))){
                        if((xBlock  >= petitx && xBlock  <= grandx) && (zBlock  >= petitz && zBlock  <= grandz)) {
                            return regions;
                        }else{
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void removeCache(Player player){
        cachecoordonnéex1.remove(player);
        cachecoordonnéez1.remove(player);
        cachecoordonnéex2.remove(player);
        cachecoordonnéez2.remove(player);
    }

}
