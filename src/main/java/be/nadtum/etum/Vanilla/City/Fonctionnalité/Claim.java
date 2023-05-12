package be.nadtum.etum.Vanilla.City.Fonctionnalité;

import be.nadtum.etum.Region.RegionGestion;
import be.nadtum.etum.Utility.Modules.CityGestion;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    private static Double petitx;
    private static Double petitz;
    private static Double grandx;
    private static Double grandz;

    private static Double x1;
    private static Double z1;
    private static Double x2;
    private static Double z2;

    public static HashMap<Player, Boolean> isActionOnClaim = new HashMap<>();

    private static String cityName;
    public static HashMap<Player, String> cityOnWalk = new HashMap<>();

    private static HashMap<Player, Double> cachecoordonnéex1 = new HashMap<>();
    private static HashMap<Player, Double> cachecoordonnéez1 = new HashMap<>();
    private static HashMap<Player, Double> cachecoordonnéex2 = new HashMap<>();
    private static HashMap<Player, Double> cachecoordonnéez2 = new HashMap<>();

    public static HashMap<String, Boolean> cityCoinShow = new HashMap<>();

    //hashmap pour récupérer les block coin d'une cité qui seraii montrer
    private static HashMap<String, Block> coinMontrerx1z1 = new HashMap<>();
    private static HashMap<String, Block> coinMontrerx1z2 = new HashMap<>();
    private static HashMap<String, Block> coinMontrerx2z1 = new HashMap<>();
    private static HashMap<String, Block> coinMontrerx2z2 = new HashMap<>();

    //hashmap pour récupérer les block coin des block montrer
    private static HashMap<String, Material> cacheCoinMontrerx1z1 = new HashMap<>();
    private static HashMap<String, Material> cacheCoinMontrerx1z2 = new HashMap<>();
    private static HashMap<String, Material> cacheCoinMontrerx2z1 = new HashMap<>();
    private static HashMap<String, Material> cacheCoinMontrerx2z2= new HashMap<>();


    /*
    Event :
        - Joueur qui casse un block :
            description :
                le joueur ne peut casser quand dans le claim de cité dans laquelle il appartient
        - Joueur qui pose un block :
            description :
                le joueur ne peut poser quand dans le claim de cité dans laquelle il appartient
        - Joueur qui intéragi :
            description :
                le joueur ne peut intéragir quand dans le claim de cité dans laquelle il appartient (certains block sont exempté de cette règle)
        - joueur qui bouge :
            description :
                affiche un message poursavoir s'il est dans une cité

     Méthode : qu'est ce qui ce retrouve dans la plus part des events
        canBuild : type Booléen
            desciption :
                vérification de s'il peut construire dans le monde où il se trouve
        isInClaim : type String
            description :
                vérification pour récupérer l'information s'il se trouve dans une cité ou non,
                si c'est vrai on renvoie l'information nom de la cité, si ce n'est pas le cas on renvoie null
     */

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
            if (!CityGestion.hasPermission(player.getName(), "claim")) {

                return;
            }
        }

        //on vérifie si le joueur a une cité
        String cityname = PlayerGestion.getPlayerCityName(player.getName());
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

                if(cityCoinShow.containsKey(PlayerGestion.getPlayerCityName(player.getName()))){
                    //modification
                    modificationClaim(player, FichierGestion.getCfgCity(), cityname, block ,xBlock,zBlock);
                }else{
                    //pose
                    poseClaim(player, FichierGestion.getCfgCity(), cityname, xBlock,zBlock);
                }

                break;
            //le montrer (que son claim)
            case STICK:
                //on utilise la méthode canBuild pour vérifier qu'il est dans son claim
                if(!player.isSneaking()){
                    if(!cityCoinShow.containsKey(PlayerGestion.getPlayerCityName(player.getName()))){
                        x1 = FichierGestion.getCfgCity().getDouble("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.x1");
                        z1 = FichierGestion.getCfgCity().getDouble("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.z1");
                        x2 = FichierGestion.getCfgCity().getDouble("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.x2");
                        z2 = FichierGestion.getCfgCity().getDouble("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.z2");

                        getCoinClaim(PlayerGestion.getPlayerCityName(player.getName()), player, x1,x2,z1,z2);
                        coinMontrerx1z1.get(PlayerGestion.getPlayerCityName(player.getName())).setType(Material.BEDROCK);
                        coinMontrerx1z2.get(PlayerGestion.getPlayerCityName(player.getName())).setType(Material.BEDROCK);
                        coinMontrerx2z1.get(PlayerGestion.getPlayerCityName(player.getName())).setType(Material.BEDROCK);
                        coinMontrerx2z2.get(PlayerGestion.getPlayerCityName(player.getName())).setType(Material.BEDROCK);
                        cityCoinShow.put(PlayerGestion.getPlayerCityName(player.getName()), true);
                        return;
                    }
                }else{
                    if(cityCoinShow.containsKey(PlayerGestion.getPlayerCityName(player.getName()))){
                        setLastBlockTypeCoinClaim(PlayerGestion.getPlayerCityName(player.getName()));
                    }
                }

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
    public void playerMoveInClaim(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isInDefaultWorld(player)) {
            return;
        }

        if (player.getAllowFlight() && !PlayerGestion.canFly(player)) {
            Location highestBlockLocation = new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getWorld().getHighestBlockYAt((int) player.getLocation().getX(),
                            (int) player.getLocation().getZ()), player.getLocation().getZ());
            player.teleport(highestBlockLocation);
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        String currentCityName = getNameCityOfClaim(player, player.getLocation().getX(), player.getLocation().getZ());

        if (cityOnWalk.containsKey(player)) {
            if (currentCityName == null) {
                cityOnWalk.remove(player);
                return;
            }
        }

        if (currentCityName != null && !currentCityName.equals(cityOnWalk.get(player))) {
            cityOnWalk.put(player, currentCityName);
            String playerCityName = PlayerGestion.getPlayerCityName(player.getName());

            if (currentCityName.equalsIgnoreCase(playerCityName)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§avous êtes dans votre cité")));
            } else {
                if (player.getAllowFlight() && !PlayerGestion.canFly(player)) {
                    Location highestBlockLocation = new Location(player.getWorld(), player.getLocation().getX(),
                            player.getLocation().getWorld().getHighestBlockYAt((int) player.getLocation().getX(),
                                    (int) player.getLocation().getZ()), player.getLocation().getZ());
                    player.teleport(highestBlockLocation);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§cvous êtes dans la cité §4" + cityOnWalk.get(player))));
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
        if(cityCoinShow.containsKey(PlayerGestion.getPlayerCityName(event.getPlayer().getName()))
                && CityGestion.hasPermission(event.getPlayer().getName(), "claim")){
            setLastBlockTypeCoinClaim(PlayerGestion.getPlayerCityName(event.getPlayer().getName()));
        }
    }

    //METHODES
    public static boolean canBuild(Player player, double xBlock, double zBlock) {
        YamlConfiguration cfg_perms = FichierGestion.getCfgPermission();

        if (cfg_perms.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.build")) {
            return true;
        }

        if (RegionGestion.getNameOfRegion(player, xBlock, zBlock) != null) {
            return false;
        }

        if (!isInDefaultWorld(player)) {
            return true;
        }

        String playerCityName = PlayerGestion.getPlayerCityName(player.getName());
        if (playerCityName.equals("NoCity")) {
            return false;
        }

        if (!CityGestion.hasPermission(player.getName(), "build")) {
            return false;
        }

        String cityName = getNameCityOfClaim(player, xBlock, zBlock);
        if (cityName != null) {
            if (cityName.equals(playerCityName)) {
                if (cityCoinShow.containsKey(cityName) && !CityGestion.hasPermission(player.getName(), "claim")) {
                    player.sendMessage(PrefixMessage.erreur() + "le claim est en mode modification veuillez attendre");
                }
                return false;
            } else {
                player.sendMessage(PrefixMessage.erreur() + "tu es dans la cité " + cityName);
            }
        }

        return false;
    }

    public static String getNameCityOfClaim(Player player, double xBlock, double zBlock) {
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

    public void getCoinClaim(String city, Player player, double x1, double x2, double z1, double z2) {
        World world = player.getWorld();
        int highestYAtX1Z1 = world.getHighestBlockYAt((int) x1, (int) z1);
        int highestYAtX1Z2 = world.getHighestBlockYAt((int) x1, (int) z2);
        int highestYAtX2Z1 = world.getHighestBlockYAt((int) x2, (int) z1);
        int highestYAtX2Z2 = world.getHighestBlockYAt((int) x2, (int) z2);

        Block blockX1Z1 = world.getBlockAt(new Location(world, x1, highestYAtX1Z1, z1));
        Block blockX1Z2 = world.getBlockAt(new Location(world, x1, highestYAtX1Z2, z2));
        Block blockX2Z1 = world.getBlockAt(new Location(world, x2, highestYAtX2Z1, z1));
        Block blockX2Z2 = world.getBlockAt(new Location(world, x2, highestYAtX2Z2, z2));

        cacheCoinMontrerx1z1.put(city, blockX1Z1.getType());
        cacheCoinMontrerx1z2.put(city, blockX1Z2.getType());
        cacheCoinMontrerx2z1.put(city, blockX2Z1.getType());
        cacheCoinMontrerx2z2.put(city, blockX2Z2.getType());

        coinMontrerx1z1.put(city, blockX1Z1);
        coinMontrerx1z2.put(city, blockX1Z2);
        coinMontrerx2z1.put(city, blockX2Z1);
        coinMontrerx2z2.put(city, blockX2Z2);
    }

    public static void setLastBlockTypeCoinClaim(String city) {
        Block blockX1Z1 = coinMontrerx1z1.remove(city);
        Block blockX1Z2 = coinMontrerx1z2.remove(city);
        Block blockX2Z1 = coinMontrerx2z1.remove(city);
        Block blockX2Z2 = coinMontrerx2z2.remove(city);

        blockX1Z1.setType(cacheCoinMontrerx1z1.remove(city));
        blockX1Z2.setType(cacheCoinMontrerx1z2.remove(city));
        blockX2Z1.setType(cacheCoinMontrerx2z1.remove(city));
        blockX2Z2.setType(cacheCoinMontrerx2z2.remove(city));

        cityCoinShow.remove(city);
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
                cfg.set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.x1", cachecoordonnéex1.get(player));
                cfg.set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.x2", cachecoordonnéex2.get(player));
                cfg.set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.z1", cachecoordonnéez1.get(player));
                cfg.set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone.coordonnées.z2", cachecoordonnéez2.get(player));
                player.sendMessage(PrefixMessage.erreur() + "claim annulé, l'ancienne zone a été remise");
                removeCacheCoordonnée(player);
            }else{
                cfg.set("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".zone", null);
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

        double x1Player = 0;
        double z1Player = 0;

        if(isActionOnClaim.containsKey(player) && player.isSneaking()){
            x1Player = cfg.getDouble("City." + cityname + ".zone.coordonnées.x1");
            z1Player = cfg.getDouble("City." + cityname + ".zone.coordonnées.z1");
        }else{
            return;
        }


        if(cfg.contains("City")) {


            for(String City : cfg.getConfigurationSection("City.").getKeys(false)){

                x1 = cfg.getDouble("City." + City + ".zone.coordonnées.x1");
                z1 = cfg.getDouble("City." + City + ".zone.coordonnées.z1");
                x2 = cfg.getDouble("City." + City + ".zone.coordonnées.x2");
                z2 = cfg.getDouble("City." + City + ".zone.coordonnées.z2");



                if(x1 > x2){
                    grandx = x1;
                    petitx = x2;
                }else{
                    grandx = x2;
                    petitx = x1;
                }

                if(z1 > z2){
                    grandz = z1;
                    petitz = z2;
                }else{
                    grandz = z2;
                    petitz = z1;
                }


                //vérifier si claim sur un autre claim

                //si on est dans le cas d'une pose de claim on effectue le code qui execute des éléments en rapport avec une pose

                Double playergrandx;
                Double playerpetitx;
                if (x1Player > xBlock) {
                    playergrandx = x1Player;
                    playerpetitx = xBlock;
                } else {
                    playergrandx = xBlock;
                    playerpetitx = x1Player;
                }

                Double playergrandz;
                Double playerpetitz;
                if (z1Player > zBlock) {
                    playergrandz = z1Player;
                    playerpetitz = zBlock;
                } else {
                    playergrandz = zBlock;
                    playerpetitz = z1Player;
                }


                // Les coin
                boolean isCoinOverlap = ((grandz > playerpetitz && grandz < playergrandz) && (grandx > playerpetitx && grandx < playergrandx))
                        || ((petitz > playerpetitz && petitz < playergrandz) && (petitx > playerpetitx && petitx < playergrandx))
                        || ((petitz > playerpetitz && petitz < playergrandz) && (grandx > playerpetitx && grandx < playergrandx))
                        || ((grandz > playerpetitz && grandz < playergrandz) && (petitx > playerpetitx && petitx < playergrandx));

                // Entre les points du claim
                boolean isClaimOverlap = (playergrandx > grandx && playergrandx > petitx) && (playerpetitx < petitx && playerpetitx < grandx)
                        && (playergrandz < grandz && playergrandz > petitz) && (playerpetitz < grandz && playerpetitz > petitz)
                        || (playergrandz > grandz && playergrandz > petitz) && (playerpetitz < grandz && playerpetitz < petitz)
                        && (playergrandx < grandx && playergrandx > petitx) && (playerpetitx < grandx && playerpetitx > petitx);

                if (isCoinOverlap || isClaimOverlap) {
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la cité " + City);
                    cancelActionClaim(player);
                    return;
                }

            }

            cityName = getNameCityOfClaim(player, xBlock, zBlock);

            if(cityName != null){
                if(!cityName.equals(PlayerGestion.getPlayerCityName(player.getName()))){
                    player.sendMessage(PrefixMessage.erreur() + "tu es dans la cité " + cityName);
                    cancelActionClaim(player);
                }
            }



        }
    }

    //à vérifier
    public int calculTotalClaimOfCity(String Cityname){

        Integer claim = 0;
        for(String membre : FichierGestion.getCfgCity().getConfigurationSection
                ("City." + Cityname + ".membres.").getKeys(false)){
            claim = claim + PlayerGestion.getPlayerClaimCount(String.valueOf(Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName()));

        }

        return claim;

    }

    public Double calculAirClaim(Double x1, Double x2 ,Double z1,Double z2){

        if(Math.abs(x1) > Math.abs(x2)){


            grandx = Math.abs(x1);
            petitx = Math.abs(x2);
        }else{
            grandx = Math.abs(x2);
            petitx = Math.abs(x1);
        }

        if(Math.abs(z1) > Math.abs(z2)){
            grandz = Math.abs(z1);
            petitz = Math.abs(z2);
        }else{
            grandz = Math.abs(z2);
            petitz = Math.abs(z1);
        }


        Double longueur = grandx - petitx;
        Double largeur = grandz - petitz;

        if(longueur == 0){
            longueur = 1.0;
        }

        if(largeur == 0){
            largeur = 1.0;
        }

        return longueur * largeur;
    }

    public void poseClaim(Player player, YamlConfiguration cfg, String cityname, Double xBlock, Double zBlock){

        /*
        vérifier si on est dans le cas de la première position ou de la deuxième sachant que la deuxième pose si les conditions sont bonnes va permettre la définition du claim de cité
        la première pose est définie par l'absence de valeur pour le joueur de la hashmap isActionOnClaim

        NOTES:
        il ne sert à rien de savoir si c'est sa cité car dès la première pose on ne le saura jamais vue qu'il repose son claim
         */

        //par sécurité nous allons enregistrer son ancien claim de cité s'il en a un



        if (!isActionOnClaim.containsKey(player)) {


            //première pose
            // on veut vérifier si sa première pose ne se trouve pas dans une cité
            //on remet à zéro le claim
            if(player.isSneaking())return;


            saveOldClaim(cfg, PlayerGestion.getPlayerCityName(player.getName()), player);
            cfg.set("City." + cityname + ".zone", null);
            //checkcity est la variable qui va contenir le nom de la cité sur laquel on est si on est sur une cité

            cfg.set("City." + cityname + ".zone.coordonnées.x1", xBlock);
            cfg.set("City." + cityname + ".zone.coordonnées.z1", zBlock);
            player.sendMessage(PrefixMessage.serveur() + "le premier point de claim a bien été placé");
            isActionOnClaim.put(player, true);
        }else{


            //deuxième pose
            //on vérifie si la pose finale ne se trouve pas sur un autre claim

            if(!player.isSneaking())return;
            cfg.set("City." + cityname + ".zone.coordonnées.x2", xBlock);
            cfg.set("City." + cityname + ".zone.coordonnées.z2", zBlock);
            verifIfClaimIsOnOtherClaim(player, cfg, cityname, xBlock, zBlock);
            if(RegionGestion.verifIfClaimIsOnOtherRegion(player,FichierGestion.getCfgRegion(), cityname, xBlock, zBlock)){
                return;
            }


            FichierGestion.saveFile(cfg, FichierGestion.getFichierCity());

            x1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.x1");
            z1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.z1");
            x2 = cfg.getDouble("City." + cityname + ".zone.coordonnées.x2");
            z2 = cfg.getDouble("City." + cityname + ".zone.coordonnées.z2");

            grandx = Math.max(Math.abs(x1), Math.abs(x2));
            petitx = Math.min(Math.abs(x1), Math.abs(x2));

            grandz = Math.max(Math.abs(z1), Math.abs(z2));
            petitz = Math.min(Math.abs(z1), Math.abs(z2));

            Double longueur = Math.max(grandx - petitx, 1.0);
            Double largeur = Math.max(grandz - petitz, 1.0);

            if(!(((longueur * largeur) * 15) <= PlayerGestion.getPlayerMoney(player.getName()))){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas assez d'Akoins");
                cancelActionClaim(player);
                return;
            }



            if((longueur * largeur) > calculTotalClaimOfCity(PlayerGestion.getPlayerCityName(player.getName()))){
                player.sendMessage(PrefixMessage.erreur() + "l'air de la cité est trop grande");
                cancelActionClaim(player);
                return;
            }
            isActionOnClaim.remove(player);
            PlayerGestion.setPlayerMoney(player.getName(), (long) (PlayerGestion.getPlayerMoney(player.getName()) - ((longueur * largeur) * 15)));
            player.sendMessage(PrefixMessage.serveur() + "le deuxième point de claim a bien été placé");
            player.sendMessage(PrefixMessage.serveur() + "votre cité a bien sa zone posé");
        }
    }

    public void modificationClaim(Player player, YamlConfiguration cfg, String cityname, Block block ,Double xBlock, Double zBlock){

        //il faut vérifier s'il le joueur clique sur un des coins, car c'est avec ça qu'on va pouvoir définir la première position

        int premièrePositionX;
        int premièrePositionZ;
        //si on n'a pas la première position pour définir la modification il faut la prendre
        if(!isActionOnClaim.containsKey(player)){
            //première pose
            if(player.isSneaking())return;
            if (coinMontrerx1z1.get(cityname).equals(block)) {
                premièrePositionX = coinMontrerx2z2.get(cityname).getX();
                premièrePositionZ = coinMontrerx2z2.get(cityname).getZ();
            }else{

                if (coinMontrerx1z2.get(cityname).equals(block)) {
                    premièrePositionX = coinMontrerx2z1.get(cityname).getX();
                    premièrePositionZ = coinMontrerx2z1.get(cityname).getZ();
                }else {
                    if (coinMontrerx2z1.get(cityname).equals(block)) {
                        premièrePositionX = coinMontrerx1z2.get(cityname).getX();
                        premièrePositionZ = coinMontrerx1z2.get(cityname).getZ();
                    }else{
                        if (coinMontrerx2z2.get(cityname).equals(block)) {
                            premièrePositionX = coinMontrerx1z1.get(cityname).getX();
                            premièrePositionZ = coinMontrerx1z1.get(cityname).getZ();
                        }else{
                            player.sendMessage(PrefixMessage.erreur() + "il faut cliquez sur un des coins de votre claim");
                            return;
                        }
                    }
                }

            }

            //on enregistre la première position qu'on a récupérer
            //on remet à zéro le claim
            saveOldClaim(cfg, PlayerGestion.getPlayerCityName(player.getName()), player);
            cfg.set("City." + cityname + ".zone", null);
            //pour vérifier ensuite si la première pose ne se fait pas sur un autre claim

            cfg.set("City." + cityname + ".zone.coordonnées.x1", premièrePositionX);
            cfg.set("City." + cityname + ".zone.coordonnées.z1", premièrePositionZ);

            player.sendMessage(PrefixMessage.serveur() + "la zone peut commencé à être modifié");
            isActionOnClaim.put(player, true);

        }else{
            //deuxième pose

            //on vérifie si la pose finale ne se trouve pas sur un autre claim

            if (!player.isSneaking()) {
                return;
            }

            cfg.set("City." + cityname + ".zone.coordonnées.x2", xBlock);
            cfg.set("City." + cityname + ".zone.coordonnées.z2", zBlock);
            verifIfClaimIsOnOtherClaim(player, cfg, cityname, xBlock, zBlock);

            if (RegionGestion.verifIfClaimIsOnOtherRegion(player, FichierGestion.getCfgRegion(), cityname, xBlock, zBlock)) {
                return;
            }

            x1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.x1");
            z1 = cfg.getDouble("City." + cityname + ".zone.coordonnées.z1");
            x2 = xBlock;
            z2 = zBlock;

            double airClaim = calculAirClaim(x1, x2, z1, z2);
            double totalClaimOfCity = calculTotalClaimOfCity(PlayerGestion.getPlayerCityName(player.getName()));

            if (airClaim > totalClaimOfCity) {
                player.sendMessage(PrefixMessage.erreur() + "l'air de la cité est trop grande [" + airClaim + "]");
                isActionOnClaim.remove(player);
                cancelActionClaim(player);
                return;
            }

            double cachedAirClaim = calculAirClaim(cachecoordonnéex1.get(player), cachecoordonnéex2.get(player),
                    cachecoordonnéez1.get(player), cachecoordonnéez2.get(player));

            if (airClaim > cachedAirClaim) {
                double difference = airClaim - cachedAirClaim;
                long requiredMoney = (long) (difference * 15);

                if (requiredMoney > PlayerGestion.getPlayerMoney(player.getName())) {
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas assez d'Akoins");
                    cancelActionClaim(player);
                    isActionOnClaim.remove(player);
                    return;
                }

                PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - requiredMoney);
                player.sendMessage(PrefixMessage.serveur() + "votre cité a bien sa zone modifié");
            }


            isActionOnClaim.remove(player);
            setLastBlockTypeCoinClaim(cityname);

        }



    }



}

