package be.nadtum.etum.Utility.Modules;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerBuilder {
    private String playerName;
    private UUID uuid;

    public PlayerBuilder(Player player) {
        this.playerName = player.getName();
        this.uuid = player.getUniqueId();
    }

    public void create() {
        if (hasData(playerName)) {
            return;
        }

        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".name", playerName);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".grade", "Akien");
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".money", 500);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".mana", 0);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".soul", 0);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".job.name", "nojob");
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".job.niveau", 1);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".job.xp", 0);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".Home.nb", 2);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".claim", 200);
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".city", "NoCity");
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".staff.grade", "NoStaff");
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".staff.puissance", 0);
    }

    public String getPlayerGrade() {
        return FichierGestion.getCfgPlayers().getString("Profil." + uuid + ".grade");
    }

    public void setPlayerGrade(String grade) {
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".grade", grade);
    }

    public Integer getPlayerMana() {
        return FichierGestion.getCfgPlayers().getInt("Profil." + uuid + ".mana");
    }

    public void setPlayerMana(Integer mana) {
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".mana", mana);
    }

    public Long getPlayerMoney() {
        return FichierGestion.getCfgPlayers().getLong("Profil." + uuid + ".money");
    }

    public void setPlayerMoney(Long somme) {
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".money", somme);
    }

    public void addPlayerMoney(Long somme) {
        FichierGestion.getCfgPlayers().set("Profil." + uuid + ".money", getPlayerMoney() + somme);
    }

    private static boolean hasData(String playerName) {
        return FichierGestion.getCfgPlayers().getConfigurationSection("Profil").getKeys(false).stream()
                .map(key -> FichierGestion.getCfgPlayers().getString("Profil." + key + ".name"))
                .anyMatch(name -> name.equalsIgnoreCase(playerName));
    }
}
