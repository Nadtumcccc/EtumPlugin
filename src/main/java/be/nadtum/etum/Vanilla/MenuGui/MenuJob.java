package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.*;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuJob implements Listener {

    private static String nameMenu = "Menu : Métier";

    public static void menu(Player player) {
        ItemMeta meta;
        List<String> lore;

        // Création de l'inventaire
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);
        inv.setupTemplate();

        if (PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")) {
            // Menu pour choisir un métier
            ItemBuilder mineur = new ItemBuilder(Material.IRON_PICKAXE, "§7Mineur", 1);
            ItemBuilder chasseur = new ItemBuilder(Material.IRON_SWORD, "§4Chasseur", 1);
            ItemBuilder bucheron = new ItemBuilder(Material.IRON_AXE, "§2Bûcheron", 1);
            ItemBuilder fermier = new ItemBuilder(Material.IRON_HOE, "§aFermier", 1);
            ItemBuilder pecheur = new ItemBuilder(Material.FISHING_ROD, "§bPêcheur", 1);

            inv.getInventory().setItem(10, mineur.getItem());
            inv.getInventory().setItem(11, chasseur.getItem());
            inv.getInventory().setItem(12, bucheron.getItem());
            inv.getInventory().setItem(13, fermier.getItem());
            inv.getInventory().setItem(14, pecheur.getItem());
        } else {
            // Affichage du métier choisi et options associées
            ItemBuilder jobs = new ItemBuilder(Material.ENCHANTED_BOOK, PlayerGestion.getPlayerJobName(player.getName()), 1);
            jobs.addLore("§6Voie§e : §b" + PlayerGestion.getPlayerJobsVoie(player.getName()));
            jobs.addLore("§6Niveau§e : §b" + PlayerGestion.getPlayerJobNiveau(player.getName()));
            jobs.addLore("§6XP§e : §b" + PlayerGestion.getPlayerJobXp(player.getName()) + "§e/"
                    + 500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2);

            ItemBuilder quitterMétier = new ItemBuilder(Material.BOOK, "Quitter le métier", 1);
            quitterMétier.addLore(PlayerGestion.getPlayerMoney(player.getName()) < 500 ? "§4vous n'avez pas assez de money" : "§2vous avez assez de money");

            ItemBuilder ability = new ItemBuilder(Material.SPYGLASS, "compétence métier", 1);

            inv.getInventory().setItem(10, jobs.getItem());
            inv.getInventory().setItem(11, ability.getItem());
            inv.getInventory().setItem(37, quitterMétier.getItem());
        }

        player.openInventory(inv.getInventory());
    }

    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {
        // Vérification de l'événement de clic sur l'inventaire
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE))
            return;
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null)
            return;

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            // Gestion des actions du menu
            if (event.getCurrentItem().getType().equals(Material.DARK_OAK_DOOR)) {
                player.closeInventory();
                MenuPrincipal.menu(player);
                return;
            }

            if (PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")) {
                // Sélection du métier
                switch (event.getCurrentItem().getType()) {
                    case IRON_PICKAXE:
                        PlayerGestion.setPlayerJobName(player.getName(), "Mineur");
                        break;
                    case IRON_AXE:
                        PlayerGestion.setPlayerJobName(player.getName(), "Bûcheron");
                        break;
                    case IRON_SWORD:
                        PlayerGestion.setPlayerJobName(player.getName(), "Chasseur");
                        break;
                    case IRON_HOE:
                        PlayerGestion.setPlayerJobName(player.getName(), "Fermier");
                        break;
                    case FISHING_ROD:
                        PlayerGestion.setPlayerJobName(player.getName(), "Pêcheur");
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }

                player.closeInventory();
                player.sendMessage(PrefixMessage.serveur() + "vous êtes maintenant §b" + PlayerGestion.getPlayerJobName(player.getName()));
                menu(player);
                return;
            }

            switch (event.getCurrentItem().getType()) {
                case BOOK:
                    // Quitter le métier
                    if (PlayerGestion.getPlayerMoney(player.getName()) < 500) {
                        event.setCancelled(true);
                        return;
                    }

                    PlayerGestion.setPlayerJobXp(player.getName(), 0);
                    PlayerGestion.setPlayerJobNiveau(player.getName(), 1);
                    PlayerGestion.setPlayerJobName(player.getName(), "nojob");
                    PlayerGestion.setPlayerJobsVoie(player.getName(), null);

                    player.sendMessage(PrefixMessage.serveur() + "vous avez quitté votre métier");
                    PlayerGestion.addPlayerMoney(player.getName(), (long) -500);
                    menu(player);
                    break;
                case SPYGLASS:
                    // Compétence du métier
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }
}
