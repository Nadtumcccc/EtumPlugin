package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Vanilla.City.menu.MenuCity;

import be.nadtum.etum.Utility.Modules.*;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;



public class MenuPrincipal implements Listener {

    /*

    MenuPlayer :
    - Description :
        cette série de menu va permettre aux joueurs de facilité et d'offire des options lié au serveur
    -Listes des menus :
        - Menu Princale
        - Jobs
        - Mondes Ressources
        - Profil
        - Menu Home
     */

    private static String nameMenu = "Menu : Principal";



    public static void menu(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);

        ItemBuilder profil = new ItemBuilder(Material.PLAYER_HEAD, "§eProfil : §b" + player.getName(), 1);
        SkullMeta skullMeta = (SkullMeta) profil.getItem().getItemMeta();
        skullMeta.setOwner(player.getName());
        List<String> lore = new ArrayList<String>();
        lore.add("§6Grade  §e: " + Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName()))));
        lore.add("§6Métier  §e: §b" + PlayerGestion.getPlayerJobName(player.getName()));
        lore.add("§6Money  §e: §b" + PlayerGestion.getPlayerMoney(player.getName()));
        skullMeta.setLore(lore);
        profil.getItem().setItemMeta(skullMeta);

        ItemBuilder home = new ItemBuilder(Material.RED_BED, "§4Home", 1);
        ItemBuilder mressource = new ItemBuilder(Material.GOLD_ORE, "§eMondes §6Ressources", 1);
        ItemBuilder spawn = new ItemBuilder(Material.BEACON, "§bSpawn", 1);
        ItemBuilder random = new ItemBuilder(Material.GHAST_TEAR, "§4Random §cLocation", 1);
        ItemBuilder jobs = new ItemBuilder(Material.IRON_AXE, "§6Jobs", 1);
        ItemBuilder guilde = new ItemBuilder(Material.TOTEM_OF_UNDYING, "§5City", 1);


        inv.getInventory().setItem(10, profil.getItem());
        inv.getInventory().setItem(11, home.getItem());
        inv.getInventory().setItem(16, jobs.getItem());
        inv.getInventory().setItem(25, guilde.getItem());
        inv.getInventory().setItem(37, mressource.getItem());
        inv.getInventory().setItem(38, random.getItem());
        inv.getInventory().setItem(39, spawn.getItem());
        player.updateInventory();

        for(int i =0; i < 54; i++){
            if(inv.getInventory().getItem(i) == null){
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

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case PLAYER_HEAD:
                    player.closeInventory();
                    MenuProfil.menu(player);
                    break;
                case RED_BED:
                    player.closeInventory();
                    MenuHome.menu(player);
                    break;
                case TOTEM_OF_UNDYING:
                    if(PlayerGestion.getPlayerCityName(player.getName()).equals("NoCity")){
                        event.setCancelled(true);
                        return;
                    }
                    player.closeInventory();
                    MenuCity.menu(player);
                    break;
                case IRON_AXE:
                    player.closeInventory();
                    MenuJob.menu(player);
                    break;
                case GOLD_ORE:
                    player.closeInventory();
                    MenuMondeRessource.menu(player);
                    break;
                case BEACON:
                    player.closeInventory();
                    Teleportation.PlayerTpToSpawn(player, "Spawn");
                    break;
                case GHAST_TEAR:
                    player.closeInventory();
                    player.sendMessage("random téléporte en développement");
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }
    }

}
