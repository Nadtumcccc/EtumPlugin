package be.nadtum.etum.Vanilla.MenuGui.City;

import be.nadtum.etum.Utility.Modules.*;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuCityGestionMembre implements Listener {

    private static String nameMenu = "Menu : Cité | [Liste Membres]";
    private static String nameMenuMembre = "Menu : Cité | Settings Membre";
    private static ItemMeta meta;
    private static List<String> lore;

    public static void menu(Player player){

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);

        int compteur = 0;

        for(String membre : FichierGestion.getCfgCity().getConfigurationSection
                ("City." + PlayerGestion.getPlayerCityName(player.getName()) + ".membres").getKeys(false)){

            ItemBuilder profil = new ItemBuilder(Material.PLAYER_HEAD,Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName(), 1);
            SkullMeta skullMeta = (SkullMeta) profil.getItem().getItemMeta();
            skullMeta.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName());
            List<String> lore = new ArrayList<String>();
            lore.add("§6Grade  §e: " + Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName()))));
            lore.add("§6Claim §e: §b" + PlayerGestion.getPlayerClaimCount(Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName()));
            lore.add("§6Money  §e: §b" + PlayerGestion.getPlayerMoney(Bukkit.getOfflinePlayer(UUID.fromString(membre)).getName()));
            lore.add(!Bukkit.getOfflinePlayer(UUID.fromString(membre)).isOnline() ? "§4or ligne" : "§2en ligne");
            skullMeta.setLore(lore);
            profil.getItem().setItemMeta(skullMeta);

            inv.getInventory().setItem(compteur, profil.getItem());
            compteur++;
        }


        inv.getInventory().setItem(53, MenuGestion.back.getItem());

        for(int i =0; i < 53; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.contour.getItem());
            }
        }


        player.openInventory(inv.getInventory());
    }

    public static void menuMembre(Player player,String membre){

        InventoryBuilder inv = new InventoryBuilder(nameMenuMembre, 54);

        ItemBuilder profil = new ItemBuilder(Material.PLAYER_HEAD, membre, 1);
        SkullMeta skullMeta = (SkullMeta) profil.getItem().getItemMeta();
        skullMeta.setOwner(membre);
        List<String> lore = new ArrayList<String>();
        lore.add("§6Grade  §e: " + Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(membre))));
        lore.add("§6Claim §e: §b" + PlayerGestion.getPlayerClaimCount(membre));
        lore.add("§6Money  §e: §b" + PlayerGestion.getPlayerMoney(membre));
        lore.add(!Bukkit.getOfflinePlayer(membre).isOnline() ? "§4or ligne" : "§2en ligne");
        skullMeta.setLore(lore);
        profil.getItem().setItemMeta(skullMeta);

        //shop: true
        //upgrade: true
        ItemBuilder build = new ItemBuilder(Material.GRASS_BLOCK,"§2BUILD",1);
        meta = build.getItem().getItemMeta();
        lore = new ArrayList<String>();
        lore.add(CityGestion.hasPermission(membre,"build") ? "§2oui" : "§4non");
        meta.setLore(lore);
        build.getItem().setItemMeta(meta);


        ItemBuilder admin = new ItemBuilder(Material.IRON_HOE,"§cADMIN",1);
        meta = admin.getItem().getItemMeta();
        lore = new ArrayList<String>();
        lore.add(CityGestion.hasPermission(membre,"admin") ? "§2oui" : "§4non");
        meta.setLore(lore);
        admin.getItem().setItemMeta(meta);

        ItemBuilder claim = new ItemBuilder(Material.GOLDEN_SHOVEL,"§6CLAIM",1);
        meta = claim.getItem().getItemMeta();
        lore = new ArrayList<String>();
        lore.add(CityGestion.hasPermission(membre,"claim") ? "§2oui" : "§4non");
        meta.setLore(lore);
        claim.getItem().setItemMeta(meta);

        ItemBuilder invite = new ItemBuilder(Material.NAME_TAG,"§bINVITE",1);
        meta = invite.getItem().getItemMeta();
        lore = new ArrayList<String>();
        lore.add(CityGestion.hasPermission(membre,"invite") ? "§2oui" : "§4non");
        meta.setLore(lore);
        invite.getItem().setItemMeta(meta);

        ItemBuilder modération = new ItemBuilder(Material.IRON_AXE,"§bMODERATION",1);
        meta = modération.getItem().getItemMeta();
        lore = new ArrayList<String>();
        lore.add(CityGestion.hasPermission(membre,"modération") ? "§2oui" : "§4non");
        meta.setLore(lore);
        modération.getItem().setItemMeta(meta);

        ItemBuilder kick = new ItemBuilder(Material.DIAMOND_AXE, "§4kick le membre de la guilde",1);



        inv.getInventory().setItem(8, MenuGestion.back.getItem());
        inv.getInventory().setItem(10, profil.getItem());
        inv.getInventory().setItem(19, build.getItem());
        inv.getInventory().setItem(20, admin.getItem());
        inv.getInventory().setItem(21, invite.getItem());
        inv.getInventory().setItem(22, claim.getItem());
        inv.getInventory().setItem(23, modération.getItem());
        inv.getInventory().setItem(43, kick.getItem());

        for(int i =0; i < 53; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.contour.getItem());
            }
        }

        player.openInventory(inv.getInventory());
    }

    @EventHandler
    public void PlayerMenu(InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        if (event.getCurrentItem() == null) return;


        if (event.getCurrentItem().getType().equals(Material.MAGENTA_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            return;
        }
        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            switch (event.getCurrentItem().getType()) {
                case PLAYER_HEAD:
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(player.getDisplayName())){
                        event.setCancelled(true);
                    }else{
                        player.closeInventory();
                        menuMembre(player, event.getCurrentItem().getItemMeta().getDisplayName());
                    }
                    break;
                case BARRIER:
                    player.closeInventory();
                    MenuCity.menu(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }

            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(nameMenuMembre)) {
            String membre = event.getClickedInventory().getItem(10).getItemMeta().getDisplayName();
            switch (event.getCurrentItem().getType()) {
                case GRASS_BLOCK:
                    if(CityGestion.hasPermission(membre,"build")){
                        CityGestion.removePermission(membre,"build");
                    }else{
                        CityGestion.setPermission(membre,"build");
                    }

                    player.closeInventory();
                    menuMembre(player, membre);
                    break;
                case IRON_AXE:
                    if(CityGestion.hasPermission(membre,"modération")){
                        CityGestion.removePermission(membre,"modération");
                    }else{
                        CityGestion.setPermission(membre,"modération");
                    }

                    player.closeInventory();
                    menuMembre(player, membre);
                    break;
                case NAME_TAG:
                    if(CityGestion.hasPermission(membre,"invite")){
                        CityGestion.removePermission(membre,"invite");
                    }else{
                        CityGestion.setPermission(membre,"invite");
                    }
                    player.closeInventory();
                    menuMembre(player, membre);
                    break;
                case GOLDEN_SHOVEL:
                    if(CityGestion.hasPermission(membre,"claim")){
                        CityGestion.removePermission(membre,"claim");
                    }else{
                        CityGestion.setPermission(membre,"claim");
                    }

                    player.closeInventory();
                    menuMembre(player, membre);
                    break;
                case IRON_HOE:
                    if(CityGestion.hasPermission(membre,"admin")){
                        CityGestion.removePermission(membre,"admin");
                    }else{
                        CityGestion.setPermission(membre,"admin");
                    }

                    player.closeInventory();
                    menuMembre(player, membre);
                    break;
                case BARRIER:
                    player.closeInventory();
                    menu(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }
    }

}
