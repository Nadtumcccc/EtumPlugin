package be.nadtum.etum.Vanilla.MenuGui;

import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuChoiseVoie;
import be.nadtum.etum.Vanilla.MenuGui.CompJobs.MenuCompétenceMétier;
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

import java.util.ArrayList;
import java.util.List;

public class MenuJob implements Listener {

    private static String nameMenu = "Menu : Métier";

    public static void menu(Player player){

        ItemMeta meta;
        List<String> lore;

        InventoryBuilder inv = new InventoryBuilder(nameMenu, 54);

        if(PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")){
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
        }else{

            ItemBuilder métier = new ItemBuilder(Material.ENCHANTED_BOOK, PlayerGestion.getPlayerJobName(player.getName()), 1);
            meta = métier.getItem().getItemMeta();
            lore = new ArrayList<String>();
            lore.add("§6Voie§e : §b" + PlayerGestion.getPlayerJobsVoie(player.getName()));
            lore.add("§6Niveau§e : §b" + PlayerGestion.getPlayerJobNiveau(player.getName()));
            lore.add("§6XP§e : §b" + PlayerGestion.getPlayerJobXp(player.getName()) + "§e/"
                    + 500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2);
            meta.setLore(lore);
            métier.getItem().setItemMeta(meta);

            ItemBuilder quitterMétier = new ItemBuilder(Material.BOOK, "Quitter le métier", 1);
            meta = quitterMétier.getItem().getItemMeta();
            lore = new ArrayList<String>();
            lore.add(PlayerGestion.getPlayerMoney(player.getName()) < 500 ? "§4vous n'avez pas assez de money" : "§2vous avez assez de money");
            meta.setLore(lore);
            quitterMétier.getItem().setItemMeta(meta);

            ItemBuilder compétence = new ItemBuilder(Material.SPYGLASS, "compétence métier", 1);

            inv.getInventory().setItem(10, métier.getItem());
            inv.getInventory().setItem(11,compétence.getItem());
            inv.getInventory().setItem(37, quitterMétier.getItem());

        }

        inv.getInventory().setItem(8, MenuGestion.back.getItem());

        for(int i =0; i < 54; i++){
            if(inv.getInventory().getItem(i) == null){
                inv.getInventory().setItem(i, MenuGestion.fill.getItem());
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

            if(event.getCurrentItem().getType().equals(Material.DARK_OAK_DOOR)){
                player.closeInventory();
                MenuPrincipal.menu(player);
                return;
            }

            if(PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("nojob")){
                switch (event.getCurrentItem().getType()){
                    case IRON_PICKAXE:
                        PlayerGestion.setPlayerJobName(player.getName(),"Mineur");
                        break;
                    case IRON_AXE:
                        PlayerGestion.setPlayerJobName(player.getName(),"Bûcheron");
                        break;
                    case IRON_SWORD:
                        PlayerGestion.setPlayerJobName(player.getName(),"Chasseur");
                        break;
                    case IRON_HOE:
                        PlayerGestion.setPlayerJobName(player.getName(),"Fermier");
                        break;
                    case FISHING_ROD:
                        PlayerGestion.setPlayerJobName(player.getName(),"Pêcheur");
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


            switch (event.getCurrentItem().getType()){

                case BOOK:
                    if(PlayerGestion.getPlayerMoney(player.getName()) < 500){
                        event.setCancelled(true);
                        return;
                    }
                    player.closeInventory();
                    PlayerGestion.setPlayerJobXp(player.getName(), 0);
                    PlayerGestion.setPlayerJobNiveau(player.getName(),1);
                    PlayerGestion.setPlayerJobName(player.getName(), "nojob");
                    PlayerGestion.setPlayerJobsVoie(player.getName(), null);

                    player.sendMessage(PrefixMessage.serveur() + "vous avez quitté votre métier");
                    PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - 500);
                    menu(player);
                    break;
                case SPYGLASS:
                    if(PlayerGestion.getPlayerJobsVoie(player.getName()).equalsIgnoreCase("pas de voie")){
                        MenuChoiseVoie.menuChoixDeLaVoix(player);
                    }else{
                        MenuCompétenceMétier.menu(player);
                    }
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }


        }

    }

}
