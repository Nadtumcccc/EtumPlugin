package be.nadtum.etum.Vanilla.Player.Economy.shop;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
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

public class ShopTrader implements Listener {

    private static final String AKON_CURRENCY = "Akons";
    private static final int MAX_QUANTITY = 64;

    @EventHandler
    public void OnClickSignShop(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
            return;
        }

        Block clickedBlock = e.getClickedBlock();
        Material blockType = clickedBlock.getType();
        if (blockType != Material.OAK_WALL_SIGN && blockType != Material.OAK_SIGN) {
            return;
        }

        Sign sign = (Sign) clickedBlock.getState();
        String trader = sign.getLine(0);
        Player player = e.getPlayer();

        YamlConfiguration cfg_file = FichierGestion.getCfgPermission();
        if (!cfg_file.contains("Grade." + PlayerGestion.getPlayerGrade(trader) + ".permission.trader")
                && !player.isOp()) {
            player.sendMessage(PrefixMessage.erreur() + " le joueur §4" + sign.getLine(0) + "§c n'est pas trader ou marchand");
            return;
        }

        if (sign.getLine(0).isEmpty()) {
            return;
        }

        Material material = Material.getMaterial(sign.getLine(1));
        if (material == null) {
            return;
        }

        int prix = 0;

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (sign.getLine(2).isEmpty()) {
                player.sendMessage(PrefixMessage.erreur() + " le trader ne vend pas §4" + material);
                return;
            }
            prix = Integer.parseInt(sign.getLine(2).split(" ")[1]);
        }

        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (sign.getLine(3).isEmpty()) {
                player.sendMessage(PrefixMessage.erreur() + " le trader n'achète pas §4" + material);
                return;
            }
            prix = Integer.parseInt(sign.getLine(3).split(" ")[1]);
        }

        String line2 = sign.getLine(2);
        String line3 = sign.getLine(3);

        if (!line3.isEmpty() && !line2.isEmpty()) {
            double prixVente = Double.parseDouble(line3.split(" ")[1]);
            double prixAchat = Double.parseDouble(line2.split(" ")[1]);
            if (prixVente > prixAchat) {
                player.sendMessage(PrefixMessage.erreur() + "le prix de vente est trop élevé par rapport au prix de l'achat");
                return;
            }
        }

        if (!isChestNextToSign(clickedBlock)) {
            return;
        }

        Chest chest = (Chest) getAttachedBlock(clickedBlock).getState();
        int amount = player.isSneaking() ? MAX_QUANTITY : 1;

        // Acheter
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            int chestItemCount = getItemCountInChest(chest, material);

            if (player.isSneaking() && chestItemCount < MAX_QUANTITY) {
                player.sendMessage(PrefixMessage.erreur() + " le coffre ne contient pas de §4" + material);
                return;
            } else if (!player.isSneaking() && !chest.getBlockInventory().contains(material)) {
                player.sendMessage(PrefixMessage.erreur() + " le coffre ne contient pas de §4" + material);
                return;
            }

            int totalCost = prix * amount;
            if (PlayerGestion.getPlayerMoney(player.getName()) < totalCost) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de " + AKON_CURRENCY);
                return;
            }

            PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) - totalCost);
            player.getInventory().addItem(new ItemStack(material, amount));
            player.sendMessage(PrefixMessage.serveur() + " vous avez acheté un stack de §b" + material + " §epour §b" + totalCost);
            PlayerGestion.setPlayerMoney(trader, PlayerGestion.getPlayerMoney(trader) + totalCost);

            removeItemFromChest(chest, material, amount);
        }
        // Vendre
        else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (isChestFull(chest)) {
                player.sendMessage(PrefixMessage.erreur() + " le coffre est plein");
                return;
            }

            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (handItem.getAmount() < amount) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas assez de §4" + material);
                return;
            }

            int totalCost = prix * amount;
            if (PlayerGestion.getPlayerMoney(trader) < totalCost) {
                player.sendMessage(PrefixMessage.erreur() + " le trader n'a pas assez d'argent");
                return;
            }

            handItem.setAmount(handItem.getAmount() - amount);
            PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + totalCost);
            player.sendMessage(PrefixMessage.serveur() + " vous avez vendu un stack de §b" + material + " §epour §b" + totalCost);
            PlayerGestion.setPlayerMoney(trader, PlayerGestion.getPlayerMoney(trader) - totalCost);

            addItemToChest(chest, new ItemStack(material, amount));
        }
    }

    private boolean isChestNextToSign(Block signBlock) {
        BlockFace[] blockFaces = {BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST};
        Material chestMaterial = Material.CHEST;

        for (BlockFace face : blockFaces) {
            Block relativeBlock = signBlock.getRelative(face);
            if (relativeBlock.getType() == chestMaterial) {
                return true;
            }
        }

        return false;
    }

    private Block getAttachedBlock(Block signBlock) {
        BlockFace[] blockFaces = {BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST};

        for (BlockFace face : blockFaces) {
            Block relativeBlock = signBlock.getRelative(face);
            if (relativeBlock.getType() == Material.CHEST) {
                return relativeBlock;
            }
        }

        return null;
    }

    private int getItemCountInChest(Chest chest, Material material) {
        int itemCount = 0;
        for (ItemStack itemStack : chest.getBlockInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == material) {
                itemCount += itemStack.getAmount();
            }
        }
        return itemCount;
    }

    private boolean isChestFull(Chest chest) {
        for (ItemStack stack : chest.getBlockInventory().getContents()) {
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    private void addItemToChest(Chest chest, ItemStack itemStack) {
        chest.getBlockInventory().addItem(itemStack);
    }

    private void removeItemFromChest(Chest chest, Material material, int quantity) {
        ItemStack[] contents = chest.getBlockInventory().getContents();
        int remaining = quantity;
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == material) {
                int stackAmount = stack.getAmount();
                if (stackAmount <= remaining) {
                    remaining -= stackAmount;
                    contents[i] = null;
                } else {
                    stack.setAmount(stackAmount - remaining);
                    remaining = 0;
                }
                if (remaining <= 0) {
                    break;
                }
            }
        }
        chest.getBlockInventory().setContents(contents);
    }

    @EventHandler
    public void PlacedSignShop(SignChangeEvent e) {
        // Your sign shop setup code if needed
    }
}
