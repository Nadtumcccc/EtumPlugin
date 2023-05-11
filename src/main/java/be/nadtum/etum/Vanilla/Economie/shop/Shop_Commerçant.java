package be.nadtum.etum.Vanilla.Economie.shop;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop_Commerçant implements Listener {

    private static ItemMeta meta;


    @EventHandler
    public void OnClickSignShop(PlayerInteractEvent e){

        Action action = e.getAction();
        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_AIR))return;
        if(!(e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) || e.getClickedBlock().getType().equals(Material.OAK_SIGN)))return;

        Sign sign = (Sign) e.getClickedBlock().getState();

        if(!(Bukkit.getPlayer(sign.getLine(0)) instanceof Player))return;

        String commerçant = sign.getLine(0);
        Player player = e.getPlayer();
        //YamlConfiguration cfg_commerçant = YamlConfiguration.loadConfiguration(FichierGestion.getProfilFile(sign.getLine(0)));;

        YamlConfiguration cfg_file = FichierGestion.getCfgPermission();
        //vérifier si il à perms

        if(!cfg_file.contains("Grade." + PlayerGestion.getPlayerGrade(commerçant) + ".permission.commerçant")
                && !player.isOp()){
            //rajouter que le métier marchant permet le shop
            player.sendMessage( PrefixMessage.erreur() + " le joueur §4" + sign.getLine(0) + "§c n'est pas commerçant ou marchand");
            return;
        }

        for(int i = 0; i < 1; i++){
            if(sign.getLine(i).isEmpty())return;
        }
        if(Material.getMaterial(sign.getLine(1)).equals(null))return;
        Integer prix = 0;
        Material material = Material.getMaterial(sign.getLine(1));

        if(action.equals(Action.RIGHT_CLICK_BLOCK)){
            if(sign.getLine(2).isEmpty()){
                player.sendMessage( PrefixMessage.erreur() + " le commerçant ne vent pas §4" + material);
                return;
            }
        }
        if(action.equals(Action.LEFT_CLICK_BLOCK)){
            if(sign.getLine(3).isEmpty()){
                player.sendMessage( PrefixMessage.erreur() + " le commerçant ne n'achète pas §4" + material);
                return;
            }
        }

        if(action.equals(Action.RIGHT_CLICK_BLOCK)){
            String[] prixstr = sign.getLine(2).split(" ");
            prix = Integer.valueOf(prixstr[1]);
        }

        if(action.equals(Action.LEFT_CLICK_BLOCK)){
            String[] prixstr = sign.getLine(3).split(" ");
            prix = Integer.valueOf(prixstr[1]);
        }

        if(!sign.getLine(3).isEmpty()){
            if(!sign.getLine(2).isEmpty()){
                String[] prixstrv = sign.getLine(3).split(" ");
                String[] prixstra = sign.getLine(2).split(" ");
                if(Double.parseDouble(prixstrv[1]) > Double.parseDouble(prixstra[1])){
                    player.sendMessage( PrefixMessage.erreur() + "le prix de vente est trop élevé par rapport au prix de l'achat");
                    return;
                }
            }
        }


        org.bukkit.material.Sign sign_material = (org.bukkit.material.Sign) e.getClickedBlock().getState().getData();

        Block attached = null;//e.getClickedBlock().getRelative(sign_material.getAttachedFace());

        //il faut pouvoir passer au if suivant si ce n'est pas un coffre

        if(e.getClickedBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.CHEST)){
            attached = e.getClickedBlock().getRelative(BlockFace.SOUTH);
        }else{
            if(e.getClickedBlock().getRelative(BlockFace.NORTH).getType().equals(Material.CHEST)){
                attached = e.getClickedBlock().getRelative(BlockFace.NORTH);
            }else{
                if(e.getClickedBlock().getRelative(BlockFace.EAST).getType().equals(Material.CHEST)){
                    attached = e.getClickedBlock().getRelative(BlockFace.EAST);
                }else{
                    if(e.getClickedBlock().getRelative(BlockFace.WEST).getType().equals(Material.CHEST)){
                        attached = e.getClickedBlock().getRelative(BlockFace.WEST);
                    }
                }
            }
        }

        if((attached == null || attached.getType() != Material.CHEST))return;

        Chest chest = (org.bukkit.block.Chest) attached.getLocation().getBlock().getState();
        Integer quantité = 1;
        if(player.isSneaking()){
            quantité = 64;
        }


        //acheter
        if(action.equals(Action.RIGHT_CLICK_BLOCK)){
            //vérifie si le coffre contient assez

            if(player.isSneaking()){

                int compteur = 0;
                for(ItemStack itemStack : chest.getBlockInventory().getStorageContents()){
                    if(itemStack != null && itemStack.getType().equals(material)){
                        compteur = compteur + itemStack.getAmount();
                    }
                }

                if(compteur < 64){
                    player.sendMessage(PrefixMessage.erreur() + " le coffre ne contient pas de §4" + material);
                    return;
                }
            }else{
                if(!(chest.getBlockInventory().contains(material))){
                    player.sendMessage(PrefixMessage.erreur() + " le coffre ne contient pas de §4" + material);
                    return;
                }
            }


            if(PlayerGestion.getPlayerMoney(player.getName()) < prix * quantité){
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de Akons");
                return;
            }

            PlayerGestion.setPlayerMoney(player.getName(),(PlayerGestion.getPlayerMoney(player.getName()) - (prix * quantité)));
            player.getInventory().addItem(new ItemStack(material, quantité));
            player.sendMessage(PrefixMessage.serveur() + " vous avez acheter un stack de §b" + material + " §epour §b" + prix * quantité);
            //étape du commerçant
            PlayerGestion.setPlayerMoney(commerçant,(PlayerGestion.getPlayerMoney(commerçant) + (prix * quantité)));

            chest.getBlockInventory().removeItem(new ItemStack(material, quantité));


        }
        //vendre
        if(action.equals(Action.LEFT_CLICK_BLOCK)) {

            //vérifie si le coffre est pas plein
            boolean hasEmptySlot = false;
            for (ItemStack stack : chest.getBlockInventory().getContents()) {
                if (stack == null) {
                    hasEmptySlot = true;
                    break;
                }
            }

            if (!hasEmptySlot) {
                player.sendMessage(PrefixMessage.erreur() + " le coffre est plein");
                return;
            }

            if (!(player.getInventory().getItemInMainHand().getAmount() >= quantité)) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de §4" + material);
                return;
            }


            if (PlayerGestion.getPlayerMoney(commerçant) < prix * quantité) {
                player.sendMessage(PrefixMessage.erreur() + " le commerçant n'a pas assez d'argent");
                return;
            }


            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - quantité);
            PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + (prix * quantité));
            player.sendMessage(PrefixMessage.serveur() + " vous avez vendu un stack de §b" + material + " §epour §b" + prix * quantité);
            PlayerGestion.setPlayerMoney(commerçant, PlayerGestion.getPlayerMoney(commerçant) - (prix * quantité));



            chest.getBlockInventory().addItem(new ItemStack(material, quantité));


        }
    }

    @EventHandler
    public void PlacedSignShop(SignChangeEvent e){


        /*
            if(!Objects.requireNonNull(e.getLine(2)).isEmpty()){
                String[] line = Objects.requireNonNull(e.getLine(2)).split(" ");
                e.setLine(2, "§a" + line[0] + " " + line[1]);
            }

            if(!Objects.requireNonNull(e.getLine(3)).isEmpty()){
                String[] line = Objects.requireNonNull(e.getLine(3)).split(" ");
                e.setLine(3, "§c" + line[0] + "  " + line[1]);
            }
             */
    }
}
