package be.nadtum.etum.Vanilla.City.Claim;


import be.nadtum.etum.Utility.Modules.CityManage;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Vanilla.Region.RegionManage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class Claim implements Listener {

    public static HashMap<Player, Boolean> isActionOnClaim = new HashMap<>();

    private static final HashMap<Player, Double> cachecoordonnéex1 = new HashMap<>();
    private static final HashMap<Player, Double> cachecoordonnéez1 = new HashMap<>();
    private static final HashMap<Player, Double> cachecoordonnéex2 = new HashMap<>();
    private static final HashMap<Player, Double> cachecoordonnéez2 = new HashMap<>();

    public static HashMap<String, Boolean> cityCoinShow = new HashMap<>();


    @EventHandler
    public void GestionClaim(PlayerInteractEvent event) {


        //on vérifie si les conditions de départ sont bonnes
        Player player = event.getPlayer();


        if(!isInDefaultWorld(player))return;
        Block block = event.getClickedBlock();
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        //on vérifie s'il à la permission de gérer le claim de la cité

        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SHOVEL)
                || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
            if (!CityManage.hasPermission(player.getName(), "claim")) {

                return;
            }
        }

        //on vérifie si le joueur a une cité
        String cityname = PlayerBuilder.getPlayerCityName(player.getName());
        if(cityname.equals("NoCity")){

            return;
        }
        switch (event.getPlayer().getInventory().getItemInMainHand().getType()){
            /*
            positionner le claim
            le modifier
            */
            case GOLDEN_SHOVEL:
                /*
                on va devoir d'abord vérifier sur quel action sur le claim on est, soit une pose soit une modification
                la différence est que si la cité à son claim montrer alors il est en modification
                 */

                Double xBlock = block.getLocation().getX();
                Double zBlock = block.getLocation().getZ();

                poseClaim(player, FichierGestion.getCfgCity(), cityname, xBlock,zBlock);


                break;
            default:
                break;
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event){
        if(!canBuild(event.getPlayer(), event.getBlock().getX(), event.getBlock().getZ())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event){
        if(!canBuild(event.getPlayer(), event.getBlock().getX(), event.getBlock().getZ())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void InteractBlock(PlayerInteractEvent event){
        if(event.getClickedBlock() == null)return;
        switch (event.getClickedBlock().getType()) {
            case DISPENSER:
            case LEVER:
            case CHEST:
            case CHEST_MINECART:
            case FURNACE:
            case STONE_BUTTON:
            case OAK_BUTTON:
            case LAVA_BUCKET:
            case WATER_BUCKET:
            case JUNGLE_BUTTON:
            case BIRCH_BUTTON:
            case ACACIA_BUTTON:
            case SPRUCE_BUTTON:
            case DARK_OAK_BUTTON:
            case CRIMSON_BUTTON:
            case WARPED_BUTTON:
            case POLISHED_BLACKSTONE_BUTTON:
            case OAK_TRAPDOOR:
            case SMOKER:
            case BLAST_FURNACE:
            case SPRUCE_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case ACACIA_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case WARPED_TRAPDOOR:
            case OAK_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case CRIMSON_FENCE_GATE:
            case WARPED_FENCE_GATE:
            case BARREL:
            case ITEM_FRAME:
            case ANVIL:
            case HOPPER:
                if(!canBuild(event.getPlayer(), event.getClickedBlock().getX(), event.getClickedBlock().getZ())){
                    event.setCancelled(true);
                }
        }
    }

    @EventHandler
    public void PlayerMoveWithNotGoldenShovel(PlayerMoveEvent event){
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SHOVEL)){
            cancelActionClaim(event.getPlayer());
        }
    }

    @EventHandler
    public void PlayerLeaveInActionOnClaim(PlayerQuitEvent event){
        cancelActionClaim(event.getPlayer());
    }

    public static boolean canBuild(Player player, double xBlock, double zBlock) {
        YamlConfiguration cfg_perms = FichierGestion.getCfgPermission();
        String playerCityName = PlayerBuilder.getPlayerCityName(player.getName());
        String cityName = getNameCityOfClaim(xBlock, zBlock);

        // Vérifie si le grade du joueur a la permission "build" dans la configuration.
        if (cfg_perms.contains("Grade." + PlayerBuilder.getPlayerStaffGrade(player.getName()) + ".permission.build")) {
            return true; // Le joueur a la permission de construire.
        }

        // Vérifie si le joueur se trouve dans une région aux coordonnées spécifiées.
        if (RegionManage.getNameOfRegion(player, xBlock, zBlock) != null) {
            return false; // Le joueur ne peut pas construire dans cette région.
        }

        // Vérifie si le claim aux coordonnées spécifiées appartient à la même ville que celle du joueur.
        if (cityName != null) {
            if(cityName.equals(playerCityName)){
                // Le joueur peut construire dans cette ville s'il a la permission "claim" ou si le claim n'est pas en mode modification.
                if (!CityManage.hasPermission(player.getName(), "build")) {
                    return false; // Le joueur n'a pas la permission de construire dans cette ville.
                }
            }else{
                return false;
            }

        }

        return true; // Le joueur peut construire à l'emplacement spécifié car il ne se trouve pas dans un claim de ville ou dans une ville où il peut construire.
    }



    public static String getNameCityOfClaim(double xBlock, double zBlock) {
        ConfigurationSection citySection = FichierGestion.getCfgCity().getConfigurationSection("City");
        if (citySection != null) {
            for (String cityName : citySection.getKeys(false)) {
                ConfigurationSection cityZoneSection = FichierGestion.getCfgCity().getConfigurationSection("City." + cityName + ".zone.coordonnées");
                if (cityZoneSection != null && cityZoneSection.contains("x2")) {
                    double x1 = cityZoneSection.getDouble("x1");
                    double z1 = cityZoneSection.getDouble("z1");
                    double x2 = cityZoneSection.getDouble("x2");
                    double z2 = cityZoneSection.getDouble("z2");

                    double grandx = Math.max(x1, x2);
                    double petitx = Math.min(x1, x2);
                    double grandz = Math.max(z1, z2);
                    double petitz = Math.min(z1, z2);

                    if ((xBlock >= petitx && xBlock <= grandx) && (zBlock >= petitz && zBlock <= grandz)) {
                        return cityName;
                    }
                }
            }
        }
        return null;
    }

    public static void removeCacheCoordonnée(Player player){
        cachecoordonnéex1.remove(player);
        cachecoordonnéez1.remove(player);
        cachecoordonnéex2.remove(player);
        cachecoordonnéez2.remove(player);
    }


    /*
    Méthode isInDEfaultWorld :
        description : permet de savoir s'il de trouve dans le monde principal du serveur
        arguments :
            - player : Type Player, description : variable qui contient le joueur dont on veut savoir s'il est dans le monde principal
     */
    public static Boolean isInDefaultWorld(Player player){
        return player.getLocation().getWorld().getName().equals("Etum");
    }

    public static void cancelActionClaim(Player player){
        if(isActionOnClaim.containsKey(player)){
            YamlConfiguration cfg = FichierGestion.getCfgCity();
            isActionOnClaim.remove(player);
            if(cachecoordonnéez2.containsKey(player)){
                cfg.set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".zone.coordonnées.x1", cachecoordonnéex1.get(player));
                cfg.set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".zone.coordonnées.x2", cachecoordonnéex2.get(player));
                cfg.set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".zone.coordonnées.z1", cachecoordonnéez1.get(player));
                cfg.set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".zone.coordonnées.z2", cachecoordonnéez2.get(player));
                player.sendMessage(PrefixMessage.erreur() + "claim annulé, l'ancienne zone a été remise");
                removeCacheCoordonnée(player);
            }else{
                cfg.set("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".zone", null);
            }
        }
    }

    public void saveOldClaim(YamlConfiguration cfg, String cityname, Player player){
        if(cfg.contains("City." + cityname + ".zone.coordonnées.x2")){
            cachecoordonnéex1.put(player, cfg.getDouble("City." + cityname + ".zone.coordonnées.x1"));
            cachecoordonnéez1.put(player, cfg.getDouble("City." + cityname + ".zone.coordonnées.z1"));
            cachecoordonnéex2.put(player, cfg.getDouble("City." + cityname + ".zone.coordonnées.x2"));
            cachecoordonnéez2.put(player, cfg.getDouble("City." + cityname + ".zone.coordonnées.z2"));
        }
    }

    public void verifIfClaimIsOnOtherClaim(Player player,YamlConfiguration cfg, String cityname, Double xBlock, Double zBlock){

        double x1Player;
        double z1Player;

        // Check if the player is in claim modification mode
        if (isActionOnClaim.containsKey(player) && player.isSneaking()) {
            x1Player = cfg.getDouble("City." + cityname + ".zone.coordonnées.x1");
            z1Player = cfg.getDouble("City." + cityname + ".zone.coordonnées.z1");
        } else {
            return;
        }

        if (cfg.contains("City")) {
            for (String cityName : cfg.getConfigurationSection("City.").getKeys(false)) {
                double x1Claim = cfg.getDouble("City." + cityName + ".zone.coordonnées.x1");
                double z1Claim = cfg.getDouble("City." + cityName + ".zone.coordonnées.z1");
                double x2Claim = cfg.getDouble("City." + cityName + ".zone.coordonnées.x2");
                double z2Claim = cfg.getDouble("City." + cityName + ".zone.coordonnées.z2");

                // Determine the greater and lesser coordinates of the claim
                double grandxClaim = Math.max(x1Claim, x2Claim);
                double petitxClaim = Math.min(x1Claim, x2Claim);
                double grandzClaim = Math.max(z1Claim, z2Claim);
                double petitzClaim = Math.min(z1Claim, z2Claim);

                // Determine the greater and lesser coordinates of the player's claim action
                double playergrandx;
                double playerpetitx;
                if (x1Player > xBlock) {
                    playergrandx = x1Player;
                    playerpetitx = xBlock;
                } else {
                    playergrandx = xBlock;
                    playerpetitx = x1Player;
                }

                double playergrandz;
                double playerpetitz;
                if (z1Player > zBlock) {
                    playergrandz = z1Player;
                    playerpetitz = zBlock;
                } else {
                    playergrandz = zBlock;
                    playerpetitz = z1Player;
                }

                // Check if the player's claim action overlaps with other claims
                boolean isCoinOverlap = ((grandzClaim > playerpetitz && grandzClaim < playergrandz) && (grandxClaim > playerpetitx && grandxClaim < playergrandx))
                        || ((petitzClaim > playerpetitz && petitzClaim < playergrandz) && (petitxClaim > playerpetitx && petitxClaim < playergrandx))
                        || ((petitzClaim > playerpetitz && petitzClaim < playergrandz) && (grandxClaim > playerpetitx && grandxClaim < playergrandx))
                        || ((grandzClaim > playerpetitz && grandzClaim < playergrandz) && (petitxClaim > playerpetitx && petitxClaim < playergrandx));

                boolean isClaimOverlap = (playergrandx > grandxClaim && playergrandx > petitxClaim) && (playerpetitx < petitxClaim && playerpetitx < grandxClaim)
                        && (playergrandz < grandzClaim && playergrandz > petitzClaim) && (playerpetitz < grandzClaim && playerpetitz > petitzClaim)
                        || (playergrandz > grandzClaim && playergrandz > petitzClaim) && (playerpetitz < grandzClaim && playerpetitz < petitzClaim)
                        && (playergrandx < grandxClaim && playergrandx > petitxClaim) && (playerpetitx < grandxClaim && playerpetitx > petitxClaim);

                if (isCoinOverlap || isClaimOverlap) {
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la cité " + cityName);
                    cancelActionClaim(player);
                    return;
                }
            }

            String cityName = getNameCityOfClaim(xBlock, zBlock);
            if (cityName != null && !cityName.equals(PlayerBuilder.getPlayerCityName(player.getName()))) {
                player.sendMessage(PrefixMessage.erreur() + "tu es dans la cité " + cityName);
                cancelActionClaim(player);
            }
        }
    }

    //à vérifier
    public int calculTotalClaimOfCity(String cityName) {
        int claim = 0;
        ConfigurationSection membersSection = FichierGestion.getCfgCity().getConfigurationSection("City." + cityName + ".membres.");
        if (membersSection != null) {
            for (String memberId : membersSection.getKeys(false)) {
                UUID playerUUID = UUID.fromString(memberId);
                claim += PlayerBuilder.getPlayerClaimCount(Bukkit.getOfflinePlayer(playerUUID).getName());
            }
        }
        return claim;
    }


    public void poseClaim(Player player, YamlConfiguration cfg, String cityname, Double xBlock, Double zBlock) {
        if (!isActionOnClaim.containsKey(player)) {
            // Première pose
            if (player.isSneaking()) {
                return;
            }

            // Sauvegarde de l'ancien claim de la cité s'il en a un
            saveOldClaim(cfg, PlayerBuilder.getPlayerCityName(player.getName()), player);

            // Remise à zéro du claim
            cfg.set("City." + cityname + ".zone", null);

            // Enregistrement des coordonnées du premier point de claim
            cfg.set("City." + cityname + ".zone.coordonnées.x1", xBlock);
            cfg.set("City." + cityname + ".zone.coordonnées.z1", zBlock);

            player.sendMessage(PrefixMessage.serveur() + "le premier point de claim a bien été placé");
            isActionOnClaim.put(player, true);
        } else {
            // Deuxième pose
            if (!player.isSneaking()) {
                return;
            }

            // Enregistrement des coordonnées du deuxième point de claim
            cfg.set("City." + cityname + ".zone.coordonnées.x2", xBlock);
            cfg.set("City." + cityname + ".zone.coordonnées.z2", zBlock);

            // Vérification si la pose finale ne se trouve pas sur un autre claim
            verifIfClaimIsOnOtherClaim(player, cfg, cityname, xBlock, zBlock);
            if (RegionManage.verifIfClaimIsOnOtherRegion(player, FichierGestion.getCfgRegion(), cityname, xBlock, zBlock)) {
                return;
            }

            // Sauvegarde des modifications dans le fichier
            FichierGestion.saveFile(cfg, FichierGestion.getCityFile());

            // Calcul des dimensions du claim
            Double x1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.x1");
            Double z1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.z1");
            Double x2 = cfg.getDouble("City." + cityname + ".zone.coordonnées.x2");
            Double z2 = cfg.getDouble("City." + cityname + ".zone.coordonnées.z2");

            Double grandx = Math.max(Math.abs(x1), Math.abs(x2));
            Double petitx = Math.min(Math.abs(x1), Math.abs(x2));
            Double grandz = Math.max(Math.abs(z1), Math.abs(z2));
            Double petitz = Math.min(Math.abs(z1), Math.abs(z2));

            Double longueur = Math.max(grandx - petitx, 1.0);
            Double largeur = Math.max(grandz - petitz, 1.0);


            // Vérification si l'aire du claim n'est pas trop grande par rapport aux autres claims de la cité
            if ((longueur * largeur) > calculTotalClaimOfCity(PlayerBuilder.getPlayerCityName(player.getName()))) {
                player.sendMessage(PrefixMessage.erreur() + "l'aire de la cité est trop grande");
                cancelActionClaim(player);
                return;
            }

            // Confirmation de la pose du claim et réduction des Akoins du joueur
            isActionOnClaim.remove(player);
            PlayerBuilder.setPlayerMoney(player.getName(), (long) (PlayerBuilder.getPlayerMoney(player.getName()) - ((longueur * largeur) * 15)));
            player.sendMessage(PrefixMessage.serveur() + "le deuxième point de claim a bien été placé");
            player.sendMessage(PrefixMessage.serveur() + "votre cité a bien sa zone posée");
        }
    }
}