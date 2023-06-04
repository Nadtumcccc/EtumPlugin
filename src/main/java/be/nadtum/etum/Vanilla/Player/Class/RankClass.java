package be.nadtum.etum.Vanilla.Player.Class;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankClass {

    public static HashMap<String, RankClass> rankClassList = new HashMap<>();

    public static void initializeRanks() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(FichierGestion.getRanksFile());

        rankClassList.clear();

        if (conf.contains("ranks")) {
            for (String rankName : conf.getConfigurationSection("ranks").getKeys(false)) {
                String prefix = conf.getString("ranks." + rankName + ".prefix");
                String suffix = conf.getString("ranks." + rankName + ".suffix");
                List<String> permissions = conf.getStringList("ranks." + rankName + ".permissions");
                int priority = conf.getInt("ranks." + rankName + ".priority");

                RankClass rank = new RankClass(rankName, prefix, suffix, permissions, priority);
                rankClassList.put(rankName, rank);
            }
        }
    }

    public static void saveConfigRanks() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(FichierGestion.getRanksFile());

        conf.set("ranks", null);

        for (Map.Entry<String, RankClass> entry : rankClassList.entrySet()) {
            String rankName = entry.getKey();
            RankClass rank = entry.getValue();

            conf.set("ranks." + rankName + ".prefix", rank.getPrefix());
            conf.set("ranks." + rankName + ".suffix", rank.getSuffix());
            conf.set("ranks." + rankName + ".permissions", rank.getPermissions());
            conf.set("ranks." + rankName + ".priority", rank.getPriority());
        }

        FichierGestion.saveFile(conf, FichierGestion.getRanksFile());
    }

    private String name;
    private String prefix;
    private String suffix;
    private List<String> permissions;
    private int priority;

    public RankClass(String name, String prefix, String suffix, List<String> permissions, int priority) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }
}
