package be.nadtum.etum.Vanilla.Economie;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class depot implements Listener {

    private static String nameMenu = "Menu : Dépôt";

    public static void menu(Player player) {
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);
        inv.setupTemplate();

        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        // Récupérer les données des éléments avec le paramètre "depot" du fichier de configuration
        ConfigurationSection element = cfg.getConfigurationSection("Jobs." + PlayerGestion.getPlayerJobName(player.getName()));

        int nbCase = 0;
        if (element != null) {
            for (String target : element.getKeys(false)) {

                if(cfg.contains("Jobs." + PlayerGestion.getPlayerJobName(player.getName()) + "." + target + ".depot") ){

                    while (nbCase < inv.getInventory().getSize() && inv.getInventory().getItem(nbCase) != null) {
                        nbCase++;
                    }

                    if (nbCase >= inv.getInventory().getSize()) {
                        break;
                    }

                    ItemBuilder stack = new ItemBuilder(Material.valueOf(target), target, 1);

                }

            }
        }

        // Ouvrir l'inventaire pour le joueur

    }

}
