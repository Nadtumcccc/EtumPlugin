package be.nadtum.etum.Vanilla.Economie.Métier;

import be.nadtum.etum.Utility.Modules.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class Pêcheur implements Listener {
    private Player player;

    @EventHandler
    public void pêcheurPêche(PlayerFishEvent event){

        player = event.getPlayer();
        Entity cauhgt = event.getCaught();
        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if(!PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("Pêcheur"))return;
        if(cauhgt == null)return;
        if(!cfg.contains("Jobs.Pêcheur." + cauhgt.getName().toUpperCase()))return;

        int gainMoney = cfg.getInt("Jobs.Pêcheur." + cauhgt.getName().toUpperCase() + ".gain.money");
        Integer gainXp = cfg.getInt("Jobs.Pêcheur." + cauhgt.getName().toUpperCase() + ".gain.xp");


        PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15));
        PlayerGestion.setPlayerJobXp(player.getName(), PlayerGestion.getPlayerJobXp(player.getName()) + gainXp);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§7vous avez gagné §f" + (gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15)) + " §7Akoins et §f" + gainXp + " §7xp")));

        if(PlayerGestion.getPlayerJobXp(player.getName()) >= (500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2)){

            PlayerGestion.setPlayerJobXp(player.getName(),0);
            PlayerGestion.setPlayerJobNiveau(player.getName() , PlayerGestion.getPlayerJobNiveau(player.getName()) + 1);

            player.sendMessage(PrefixMessage.serveur() + " vous avez atteind le niveau §b" + PlayerGestion.getPlayerJobNiveau(player.getName()) + "" +
                    "\n§aIl vous faut §b" + 500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2 + " §axp pour passer au niveau suivant");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,2.0f, 1.0f);

        }
    }
}
