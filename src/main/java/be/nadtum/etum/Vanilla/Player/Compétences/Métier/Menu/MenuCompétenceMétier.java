package be.nadtum.etum.Vanilla.Player.Compétences.Métier.Menu;

import be.nadtum.etum.Vanilla.MenuGui.MenuJob;
import be.nadtum.etum.Utility.Modules.MenuGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuCompétenceMétier implements Listener {

    private static String nameMenu = "Menu : Compétence de Métier";

    //Menu qui affiches les compétences des métiers des joueurs
    public static void menu(Player player){
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 27);

        ItemBuilder compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "en développement", 1);
        ItemBuilder compétenceDeux = new ItemBuilder(Material.COPPER_INGOT, "en développement", 1);
        ItemBuilder compétenceTrois = new ItemBuilder(Material.COPPER_INGOT, "en développement", 1);
        ItemBuilder compétenceTrancendance = new ItemBuilder(Material.COPPER_INGOT, "en développement", 1);

        switch (PlayerGestion.getPlayerJobsVoie(player.getName())){
            case "néant":
                switch (PlayerGestion.getPlayerJobName(player.getName())){
                    case "Mineur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Endurance", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Double loot", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "Mending Surpuissant", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Générateur du néant", 1);
                        break;
                    case "Chasseur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Régénération", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Ombral", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "lien éternel", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Appropriation des âmes", 1);

                        break;
                    case "Pêcheur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Rassasié", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Marque du néant", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "Vitalité marine", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Malaise", 1);

                        break;
                    case "Bûcheron":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Déforestation", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "Aspiration forestière", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Rage du néant", 1);
                        break;
                    case "Fermier":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Récolte des âmes", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, " ", 1);
                        break;
                }
                break;
            case "ancienneté":
                switch (PlayerGestion.getPlayerJobName(player.getName())){
                    case "Mineur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "indexation du gain", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "expérience accrus", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "boost minage", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Affinité du Mineur ", 1);

                        break;
                    case "Chasseur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "indexation du gain", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "expérience accrus", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "volonté de tuer", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Affiliation au tueur", 1);

                        break;
                    case "Pêcheur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "indexation du gain", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "expérience accrus", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "bonne prise", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Augmentation des prises", 1);

                        break;
                    case "Bûcheron":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "indexation du gain", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "expérience accrus", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "boost du bûcheron", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Bon Bûcheron", 1);

                        break;
                    case "Fermier":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "indexation du gain", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "expérience accrus", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "écologie des champs", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Affinité du Fermier", 1);

                        break;
                }
                break;
            case "essence":
                switch (PlayerGestion.getPlayerJobName(player.getName())){
                    case "Mineur":

                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Endurance des cavernes", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Vision des cavernes", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Ressource Aléatoire", 1);
                        break;
                    case "Chasseur":
                        compétenceUn = new ItemBuilder(Material.COPPER_INGOT, "Force de la nuit", 1);
                        compétenceDeux = new ItemBuilder(Material.IRON_INGOT, "Bonus essence", 1);
                        compétenceTrois = new ItemBuilder(Material.GOLD_INGOT, "", 1);
                        compétenceTrancendance = new ItemBuilder(Material.DIAMOND, "Damage critique", 1);

                        break;
                    case "Pêcheur":

                        break;
                    case "Bûcheron":

                        break;
                    case "Fermier":

                        break;
                }
                break;
            default:

                break;
        }

        inv.getInventory().setItem(20, compétenceUn.getItem());
        inv.getInventory().setItem(22, compétenceDeux.getItem());
        inv.getInventory().setItem(24, compétenceTrois.getItem());
        inv.getInventory().setItem(4, compétenceTrancendance.getItem());

        ItemBuilder resetvoie = new ItemBuilder(Material.STRUCTURE_VOID, "§cchanger de voie", 1);

        inv.getInventory().setItem(8, MenuGestion.back.getItem());
        inv.getInventory().setItem(26, resetvoie.getItem());
        for(int i =0; i < 27; i++){
            if (inv.getInventory().getItem(i) == null) {
                inv.getInventory().setItem(i, MenuGestion.contour.getItem());
            }
        }

        player.openInventory(inv.getInventory());
    }

    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        if(event.getCurrentItem().getType().equals(Material.STRUCTURE_VOID)){
            PlayerGestion.setPlayerJobsVoie(player.getName(), null);
            MenuChoiseVoie.menuChoixDeLaVoix(player);
            return;
        }

        if(event.getCurrentItem().getType().equals(Material.BARRIER)){
            player.closeInventory();
            MenuJob.menu(player);
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {

            event.setCancelled(true);
            return;
        }
    }

}
