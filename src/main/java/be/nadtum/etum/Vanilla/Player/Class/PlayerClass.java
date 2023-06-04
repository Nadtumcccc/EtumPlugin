package be.nadtum.etum.Vanilla.Player.Class;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerClass {

    public static HashMap<Player, PlayerClass> playerClassList = new HashMap<>();

    public static void createFile(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        String playerName = player.getName();
        String fileName = uuid + ".yml";

        File playerFile = new File("plugins/fichier", fileName);
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();

                YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
                playerConfig.set("uuid", uuid.toString());
                playerConfig.set("name", playerName);
                playerConfig.set("mana", 0);
                playerConfig.set("money", 0);
                playerConfig.set("homes", new ArrayList<>());
                playerConfig.set("ranks", new ArrayList<>());
                playerConfig.set("homeAmount", 0);
                playerConfig.set("cityId", "");
                playerConfig.set("claimAmount", 0);
                playerConfig.set("hearth", 0.0);

                playerConfig.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            loadPlayerClass(player);
        }
    }

    public static void removePlayerClass(UUID uniqueId) {
        playerClassList.remove(uniqueId);
    }

    public static PlayerClass getPlayerClass(Player uniqueId) {
        return playerClassList.get(uniqueId);
    }

    public void savePlayerData() {
        String fileName = uuid.toString() + ".yml";
        File playerFile = new File("plugins/fichier", fileName);

        if (playerFile.exists()) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

            playerConfig.set("mana", mana);
            playerConfig.set("money", money);
            playerConfig.set("homes", listHomes);
            playerConfig.set("ranks", listRanks);
            playerConfig.set("homeAmount", homeAmount);
            playerConfig.set("cityId", cityId);
            playerConfig.set("claimAmount", claimAmount);
            playerConfig.set("hearth", hearth);

            try {
                playerConfig.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void loadPlayerClass(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        String fileName = uuid + ".yml";
        File playerFile = new File("plugins/fichier", fileName);

        if (playerFile.exists()) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

            int mana = playerConfig.getInt("mana");
            int money = playerConfig.getInt("money");
            List<?> homes = playerConfig.getList("homes");
            List<?> ranks = playerConfig.getList("ranks");
            int homeAmount = playerConfig.getInt("homeAmount");
            int cityId = playerConfig.getInt("cityId");
            int claimAmount = playerConfig.getInt("claimAmount");
            double hearth = playerConfig.getDouble("hearth");

            PlayerClass playerClass = new PlayerClass(uuid, player.getName());
            playerClass.setMana(mana);
            playerClass.setMoney(money);

            List<HomeClass> homeList = new ArrayList<>();
            if (homes != null) {
                for (Object home : homes) {
                    if (home instanceof HomeClass homeData) {
                        Location location = homeData.getLocation();
                        String name = homeData.getName();
                        boolean visible = homeData.isVisible();
                        HomeClass homeClass = new HomeClass(location, name, visible);
                        homeList.add(homeClass);
                    }
                }
            }
            playerClass.setListHomes(homeList);

            List<String> rankList = new ArrayList<>();
            if (ranks != null) {
                for (Object rank : ranks) {
                    if (rank instanceof String) {
                        rankList.add((String) rank);
                    }
                }
            }
            playerClass.setListRanks(rankList);

            playerClass.setHomeAmount(homeAmount);
            playerClass.setCityId(cityId);
            playerClass.setClaimAmount(claimAmount);
            playerClass.setHearth(hearth);

            // Ajouter playerClass à la liste playerClassList
            playerClassList.put(player, playerClass);
        }
    }

    public Boolean hasPermission(String permission){
        if(Objects.requireNonNull(Bukkit.getPlayer(uuid)).isOp()) return true;
        for(String rankClass : getListRanks()){
            if(RankClass.rankClassList.get(rankClass).hasPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private final UUID uuid;
    private final String name;
    private int mana;
    private int money;
    private double hearth;
    private List<HomeClass> listHomes;
    private List<String> listRanks;
    private int homeAmount;
    private Integer cityId;
    private int claimAmount;

    public PlayerClass(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.mana = 0;
        this.money = 0;
        this.listHomes = new ArrayList<>();
        this.listRanks = new ArrayList<>();
        this.homeAmount = 0;
        this.cityId = 0;
        this.claimAmount = 0;
        this.hearth = 0.0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public void addMoney(int money) {
        this.money = getMoney() + money;
    }

    public List<HomeClass> getListHomes() {
        return listHomes;
    }

    public void setListHomes(List<HomeClass> listHomes) {
        this.listHomes = listHomes;
    }

    public List<String> getListRanks() {
        return listRanks;
    }

    public void setListRanks(List<String> listRanks) {
        this.listRanks = listRanks;
    }

    public int getHomeAmount() {
        return homeAmount;
    }

    public void setHomeAmount(Integer homeAmount) {
        this.homeAmount = homeAmount;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public int getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(int claimAmount) {
        this.claimAmount = claimAmount;
    }

    public double getHearth() {
        return hearth;
    }

    public void setHearth(double hearth) {
        this.hearth = hearth;
    }
}
