package be.nadtum.etum.Vanilla.MenuGui.City;

import be.nadtum.etum.Utility.Modules.ChatManage;
import be.nadtum.etum.Utility.Modules.CityManage;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuCityGestionMembre implements Listener {

    private static final String nameMenu = "Menu : Cité | [Liste Membres]";
    private static final String nameMenuMembre = "Menu : Cité | Settings Membre";

    public static void menu(Player player) {
        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);
        inv.setupTemplate();

        int nbCase = 0;
        ConfigurationSection membresSection = FichierGestion.getCfgCity().getConfigurationSection("City." + PlayerBuilder.getPlayerCityName(player.getName()) + ".membres");
        if (membresSection != null) {
            for (String membre : membresSection.getKeys(false)) {
                UUID membreUUID = UUID.fromString(membre);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(membreUUID);

                ItemBuilder profil = new ItemBuilder(Material.PLAYER_HEAD, offlinePlayer.getName() == null ? "null": offlinePlayer.getName(), 1);
                SkullMeta skullMeta = (SkullMeta) profil.getItem().getItemMeta();

                skullMeta.setOwner(offlinePlayer.getName());

                List<String> lore = new ArrayList<>();
                lore.add("§6Grade  §e: " + ChatManage.colorString(PlayerBuilder.getGradeDesign(PlayerBuilder.getPlayerGrade(offlinePlayer.getName()))));
                lore.add("§6Claim §e: §b" + PlayerBuilder.getPlayerClaimCount(offlinePlayer.getName()));
                lore.add("§6Money  §e: §b" + PlayerBuilder.getPlayerMoney(offlinePlayer.getName()));
                lore.add(offlinePlayer.isOnline() ? "§2en ligne" : "§4hors ligne");

                skullMeta.setLore(lore);
                profil.getItem().setItemMeta(skullMeta);

                while (nbCase < inv.getInventory().getSize() && inv.getInventory().getItem(nbCase) != null) {
                    nbCase++;
                }

                inv.getInventory().setItem(nbCase, profil.getItem());
                nbCase++;
            }
        }

        player.openInventory(inv.getInventory());
    }

    public static void menuMembre(Player player,String membre){

        InventoryBuilder inv = new InventoryBuilder(nameMenuMembre, 54);

        inv.setupTemplate();

        ItemBuilder profil = new ItemBuilder(Material.PLAYER_HEAD, membre, 1);
        SkullMeta skullMeta = (SkullMeta) profil.getItem().getItemMeta();
        skullMeta.setOwner(membre);
        List<String> lore = new ArrayList<String>();
        lore.add("§6Grade  §e: " + ChatManage.colorString(PlayerBuilder.getGradeDesign(PlayerBuilder.getPlayerGrade(membre))));
        lore.add("§6Claim §e: §b" + PlayerBuilder.getPlayerClaimCount(membre));
        lore.add("§6Money  §e: §b" + PlayerBuilder.getPlayerMoney(membre));
        lore.add(!Bukkit.getOfflinePlayer(membre).isOnline() ? "§4hors ligne" : "§2en ligne");
        skullMeta.setLore(lore);
        profil.getItem().setItemMeta(skullMeta);

        //shop: true
        //upgrade: true
        ItemBuilder build = new ItemBuilder(Material.GRASS_BLOCK,"§2BUILD",1);
        build.addLore(CityManage.hasPermission(membre,"build") ? "§2oui" : "§4non");



        ItemBuilder admin = new ItemBuilder(Material.IRON_HOE,"§cADMIN",1);
        admin.addLore(CityManage.hasPermission(membre,"admin") ? "§2oui" : "§4non");


        ItemBuilder claim = new ItemBuilder(Material.GOLDEN_SHOVEL,"§6CLAIM",1);
        claim.addLore(CityManage.hasPermission(membre,"claim") ? "§2oui" : "§4non");


        ItemBuilder invite = new ItemBuilder(Material.NAME_TAG,"§bINVITE",1);
        invite.addLore(CityManage.hasPermission(membre,"invite") ? "§2oui" : "§4non");

        ItemBuilder modération = new ItemBuilder(Material.IRON_AXE,"§bMODERATION",1);
        modération.addLore(CityManage.hasPermission(membre,"modération") ? "§2oui" : "§4non");


        ItemBuilder kick = new ItemBuilder(Material.DIAMOND_AXE, "§4kick le membre de la guilde",1);


        inv.getInventory().setItem(10, profil.getItem());
        inv.getInventory().setItem(19, build.getItem());
        inv.getInventory().setItem(20, admin.getItem());
        inv.getInventory().setItem(21, invite.getItem());
        inv.getInventory().setItem(22, claim.getItem());
        inv.getInventory().setItem(23, modération.getItem());
        inv.getInventory().setItem(43, kick.getItem());



        player.openInventory(inv.getInventory());
    }

    @EventHandler
    public void onPlayerMenu(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE || event.getClickedInventory().getType() == InventoryType.PLAYER || event.getCurrentItem() == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            handleMainMenuClick(event, player);
        } else if (event.getView().getTitle().equalsIgnoreCase(nameMenuMembre)) {
            handleMembreMenuClick(event, player);
        }
    }

    private void handleMainMenuClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Material itemType = event.getCurrentItem().getType();
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (itemType == Material.PLAYER_HEAD) {
            if (displayName.equalsIgnoreCase(player.getDisplayName())) {
                event.setCancelled(true);
            } else {
                if (CityManage.hasPermission(player.getName(), "admin") && !CityManage.hasPermission(displayName, "owner")) {
                    menuMembre(player, displayName);
                }
            }
        } else if (itemType == Material.DARK_OAK_DOOR) {
            player.closeInventory();
            MenuCity.menu(player);
        }
    }

    private void handleMembreMenuClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Material itemType = event.getCurrentItem().getType();
        String membre = event.getClickedInventory().getItem(10).getItemMeta().getDisplayName();

        switch (itemType) {
            case GRASS_BLOCK:
                togglePermission(player, membre, "build");
                break;
            case IRON_AXE:
                togglePermission(player, membre, "modération");
                break;
            case NAME_TAG:
                togglePermission(player, membre, "invite");
                break;
            case GOLDEN_SHOVEL:
                togglePermission(player, membre, "claim");
                break;
            case IRON_HOE:
                togglePermission(player, membre, "admin");
                break;
            case DARK_OAK_DOOR:
                menu(player);
                break;
            default:
                event.setCancelled(true);
                break;
        }
    }

    private void togglePermission(Player player, String membre, String permission) {
        if (CityManage.hasPermission(membre, permission)) {
            CityManage.removePermission(membre, permission);
        } else {
            CityManage.setPermission(membre, permission);
        }
        menuMembre(player, membre);
    }

}
