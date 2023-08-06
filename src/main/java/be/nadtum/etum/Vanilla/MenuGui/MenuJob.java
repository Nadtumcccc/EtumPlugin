package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
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

        if (PlayerBuilder.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")) {
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
            ItemBuilder jobs = new ItemBuilder(Material.ENCHANTED_BOOK, PlayerBuilder.getPlayerJobName(player.getName()), 1);
            jobs.addLore("§6Voie§e : §b" + PlayerBuilder.getPlayerJobsVoie(player.getName()));
            jobs.addLore("§6Niveau§e : §b" + PlayerBuilder.getPlayerJobNiveau(player.getName()));
            jobs.addLore("§6XP§e : §b" + PlayerBuilder.getPlayerJobXp(player.getName()) + "§e/"
                    + 500 * PlayerBuilder.getPlayerJobNiveau(player.getName()) * 2);

            ItemBuilder quitterMétier = new ItemBuilder(Material.BOOK, "Quitter le métier", 1);
            quitterMétier.addLore(PlayerBuilder.getPlayerMoney(player.getName()) < 500 ? "§4vous n'avez pas assez de money" : "§2vous avez assez de money");

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
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {

            event.setCancelled(true);

            // Gestion des actions du menu
            if (event.getCurrentItem().getType().equals(Material.DARK_OAK_DOOR)) {
                player.closeInventory();
                MenuPrincipal.menu(player);
                return;
            }

            if (PlayerBuilder.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")) {
                // Sélection du métier
                switch (event.getCurrentItem().getType()) {
                    case IRON_PICKAXE:
                        PlayerBuilder.setPlayerJobName(player.getName(), "MINER");
                        break;
                    case IRON_AXE:
                        PlayerBuilder.setPlayerJobName(player.getName(), "LUMBERJACK");
                        break;
                    case IRON_SWORD:
                        PlayerBuilder.setPlayerJobName(player.getName(), "HUNTER");
                        break;
                    case IRON_HOE:
                        PlayerBuilder.setPlayerJobName(player.getName(), "FARMER");
                        break;
                    case FISHING_ROD:
                        PlayerBuilder.setPlayerJobName(player.getName(), "FISHERMAN");
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }

                player.closeInventory();
                player.sendMessage(PrefixMessage.serveur() + "vous êtes maintenant §b" + PlayerBuilder.getPlayerJobName(player.getName()));
                menu(player);
                return;
            }

            switch (event.getCurrentItem().getType()) {
                case BOOK:
                    // Quitter le métier
                    if (PlayerBuilder.getPlayerMoney(player.getName()) < 500) {
                        event.setCancelled(true);
                        return;
                    }

                    PlayerBuilder.setPlayerJobXp(player.getName(), 0);
                    PlayerBuilder.setPlayerJobNiveau(player.getName(), 1);
                    PlayerBuilder.setPlayerJobName(player.getName(), "nojob");
                    PlayerBuilder.setPlayerJobsVoie(player.getName(), null);

                    player.sendMessage(PrefixMessage.serveur() + "vous avez quitté votre métier");
                    PlayerBuilder.addPlayerMoney(player.getName(), (long) -500);
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
