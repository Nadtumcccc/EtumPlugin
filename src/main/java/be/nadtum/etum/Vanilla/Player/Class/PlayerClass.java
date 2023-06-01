package be.nadtum.etum.Vanilla.Player.Class;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerClass {

    private UUID uuid;
    private String name;

    private Integer mana;
    private Long money;

    private List<HomeClass> listHomes;

    private Integer homeAmount;

    private String cityName;
    private Integer claimAmount;


    public PlayerClass(UUID uuid, String name, Integer mana, Long money, List<HomeClass> listHomes, Integer homeAmount, String cityName, Integer claimAmount) {
        this.uuid = uuid;
        this.name = name;
        this.mana = mana;
        this.money = money;
        this.listHomes = listHomes;
        this.homeAmount = homeAmount;
        this.cityName = cityName;
        this.claimAmount = claimAmount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public List<HomeClass> getListHomes() {
        return listHomes;
    }

    public void setListHomes(List<HomeClass> listHomes) {
        this.listHomes = listHomes;
    }

    public Integer getHomeAmount() {
        return homeAmount;
    }

    public void setHomeAmount(Integer homeAmount) {
        this.homeAmount = homeAmount;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Integer claimAmount) {
        this.claimAmount = claimAmount;
    }
}
