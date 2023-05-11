package be.nadtum.etum.Vanilla.Economie.Métier;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Chasseur implements Listener {

    @EventHandler
    public void ChasseurDamage(EntityDeathEvent e){

        if(!(e.getEntity().getKiller() instanceof Player))return;
        Player player = e.getEntity().getKiller();
        Entity entity = e.getEntity();

        if(!PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("Chasseur"))return;

        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if(!cfg.contains("Jobs.Chasseur." + entity.getType()))return;

        int gainMoney = cfg.getInt("Jobs.Chasseur." + entity.getType() + ".gain.money");
        Integer gainXp = cfg.getInt("Jobs.Chasseur." + entity.getType() + ".gain.xp");


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
