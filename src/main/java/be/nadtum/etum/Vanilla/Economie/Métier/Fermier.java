package be.nadtum.etum.Vanilla.Economie.Métier;

import be.nadtum.etum.Vanilla.City.Fonctionnalité.Claim;
import be.nadtum.etum.Utility.Modules.*;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Fermier implements Listener {


    private Player player;
    private Block block;
    @EventHandler
    public void Farmer(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Block block = e.getBlock();

        YamlConfiguration cfg = FichierGestion.getCfgJobs();
        if(!Claim.canBuild(player, block.getX(),block.getZ()))return;
        if (!PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("Fermier")) return;

        if(HashMapGestion.blockPlaced.containsKey(block)){
            HashMapGestion.blockPlaced.remove(block);
            return;
        }

        if (!cfg.contains("Jobs.Fermier." + block.getType())) return;

        if(cfg.contains("Jobs.Fermier." + block.getType() + ".gain.age")){

            int growthStatus = block.getData();
            if(growthStatus < 7){
                return;
            }
        }

        int gainMoney = cfg.getInt("Jobs.Fermier." + block.getType() + ".gain.money");
        Integer gainXp = cfg.getInt("Jobs.Fermier." + block.getType() + ".gain.xp");


        PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15));
        PlayerGestion.setPlayerJobXp(player.getName(), PlayerGestion.getPlayerJobXp(player.getName()) + gainXp);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§7vous avez gagné §f" + (gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15)) + " §7Akoins et §f" + gainXp + " §7xp")));

        if (PlayerGestion.getPlayerJobXp(player.getName()) >= (500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2)) {

            PlayerGestion.setPlayerJobXp(player.getName(), 0);
            PlayerGestion.setPlayerJobNiveau(player.getName(), PlayerGestion.getPlayerJobNiveau(player.getName()) + 1);

            player.sendMessage(PrefixMessage.serveur() + " vous avez atteind le niveau §b" + PlayerGestion.getPlayerJobNiveau(player.getName()) + "" +
                    "\n§aIl vous faut §b" + 500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2 + " §axp pour passer au niveau suivant");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 1.0f);

        }

    }

    @EventHandler
    public void BlockPlaced(BlockPlaceEvent event){
        block = event.getBlock();

        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if(!cfg.contains("Jobs.Fermier." + block.getType()))return;
        HashMapGestion.blockPlaced.put(event.getBlockPlaced(),event.getBlockPlaced());
    }


}
