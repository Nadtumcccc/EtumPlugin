package be.nadtum.etum.Vanilla.Economie.shop;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Material;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class Shop_Serveur implements Listener {

    @EventHandler
    public void OnClickSignShop(PlayerInteractEvent e){

        Action action = e.getAction();
        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_AIR))return;
        if(!(e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) || e.getClickedBlock().getType().equals(Material.OAK_SIGN)))return;

        Sign sign = (Sign) e.getClickedBlock().getState();

        if(!sign.getLine(0).equals("AdminShop"))return;

        for(int i = 0; i < 2; i++){
            if(sign.getLine(i).isEmpty())return;
        }

        Player player = e.getPlayer();
        //posibilité de prendre des blocks de claim
        if(sign.getLine(1).equalsIgnoreCase("Claim")){

            String prixstr = sign.getLine(2);
            Integer prix = Integer.valueOf(prixstr);
            if(PlayerGestion.getPlayerMoney(player.getName()) < prix){
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de Akoins");
                return;
            }
            PlayerGestion.setPlayerMoney(player.getName(),(PlayerGestion.getPlayerMoney(player.getName()) - prix));
            PlayerGestion.setPlayerClaimCount(player.getName(), PlayerGestion.getPlayerClaimCount(player.getName()) + Integer.valueOf(sign.getLine(3)));
            player.sendMessage(PrefixMessage.serveur() + " vous avez acheté des blocs de claim §b §epour §b" + prix + " §eAkoins");
            return;
        }
        //Marché
        for(int i = 0; i < 1; i++){
            if(sign.getLine(i).isEmpty())return;
        }
        if(Material.getMaterial(sign.getLine(1)).equals(null))return;
        Integer prix = 0;
        Material material = Material.getMaterial(sign.getLine(1));

        if(action.equals(Action.RIGHT_CLICK_BLOCK)){
            if(sign.getLine(2).isEmpty()){
                player.sendMessage( PrefixMessage.erreur() + " le serveur ne vent pas §4" + material);
                return;
            }
        }
        if(action.equals(Action.LEFT_CLICK_BLOCK)){
            if(sign.getLine(3).isEmpty()){
                player.sendMessage( PrefixMessage.erreur() + " le serveur n'achète pas §4" + material);
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

        Integer quantité = 1;
        if(player.isSneaking()){
            quantité = 64;
        }


        //acheter
        if(action.equals(Action.RIGHT_CLICK_BLOCK)){


            if(PlayerGestion.getPlayerMoney(player.getName()) < prix * quantité){
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de Akons");
                return;
            }
            Boolean isFull = true;
            for (ItemStack stack : player.getInventory().getStorageContents()){
                if(stack == null || (stack.getAmount() + quantité) <= 64){
                    isFull = false;
                }
            }

            if (isFull){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez plus de place");
                return;
            }

            PlayerGestion.setPlayerMoney(player.getName(),(PlayerGestion.getPlayerMoney(player.getName()) - (prix * quantité)));
            player.getInventory().addItem(new ItemStack(material, quantité));
            player.sendMessage(PrefixMessage.serveur() + " vous avez acheter un stack de §b" + material + " §epour §b" + prix * quantité);

        }
        //vendre
        if(action.equals(Action.LEFT_CLICK_BLOCK)) {



            if (!(player.getInventory().getItemInMainHand().getAmount() >= quantité)) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de §4" + material);
                return;
            }

            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - quantité);
            PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + (prix * quantité));
            player.sendMessage(PrefixMessage.serveur() + " vous avez vendu un stack de §b" + material + " §epour §b" + prix * quantité);



        }





    }


    @EventHandler
    public void PlacedSignShop(SignChangeEvent e){

        if(e.getLine(0).equals("AdminShop")){

            YamlConfiguration cfg = FichierGestion.getCfgPermission();
            Player player = e.getPlayer();

            if(!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin") ){
                if(!player.isOp()){
                    e.setCancelled(true);
                    player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission !");
                    return;
                }
            }

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

}
