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

public class Mineur implements Listener {
    private Player player;
    private Block block;

    Double petitx;
    Double petitz;
    Double grandx;
    Double grandz;

    Double x1;
    Double z1;
    Double x2;
    Double z2;

    @EventHandler
    public void MineurMine(BlockBreakEvent event){

        player = event.getPlayer();
        block = event.getBlock();

        if(!Claim.canBuild(player, block.getX(),block.getZ())){
            return;
        }else{
            if(Claim.cityCoinShow.containsKey(PlayerGestion.getPlayerCityName(player.getName()))){
                return;
            }
        }

        if(!PlayerGestion.getPlayerJobName(player.getName()).equalsIgnoreCase("Mineur"))return;

        if(HashMapGestion.blockPlaced.containsKey(block)){
            HashMapGestion.blockPlaced.remove(block);
            return;
        }

        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if(!cfg.contains("Jobs.Mineur." + block.getType()))return;

        int bonusMoney = 0;
        int bonusXp = 0;

        //gestion des compétences du mineur de l'ancienneté
        if(PlayerGestion.getPlayerJobsVoie(player.getName()) != null
                && (PlayerGestion.getPlayerJobsVoie(player.getName()).equalsIgnoreCase("ancienneté"))){
            //on profite d'avoir fait la vérification pour pouvoir ajouter les bonus lier au mineur ancienneté

            //vérifier si il a débloqué le bonus de gain en money
            if(PlayerGestion.getPlayerJobsComp1(player.getName()) != null) {
                bonusMoney = PlayerGestion.getPlayerJobsComp1(player.getName());
            }
            //vérifier si il a débloqué le bonus de gain en xp
            if(PlayerGestion.getPlayerJobsCompDeux(player.getName()) != null) {
                bonusXp = PlayerGestion.getPlayerJobsCompDeux(player.getName());
            }

        }
        int gainMoney = cfg.getInt("Jobs.Mineur." + block.getType() + ".gain.money") + bonusMoney;
        Integer gainXp = cfg.getInt("Jobs.Mineur." + block.getType() + ".gain.xp") + bonusXp;

        //on ajoute la money et l'xp au joueur
        PlayerGestion.setPlayerMoney(player.getName(), PlayerGestion.getPlayerMoney(player.getName()) + gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15));
        PlayerGestion.setPlayerJobXp(player.getName(), PlayerGestion.getPlayerJobXp(player.getName()) + gainXp);

        //on envoie un message quand il gagne de l'argent
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§7vous avez gagné §f" + (gainMoney + gainMoney * (PlayerGestion.getPlayerJobNiveau(player.getName()) / 15)) + " §7Akoins et §f" + gainXp + " §7xp")));


        if(PlayerGestion.getPlayerJobXp(player.getName()) >= (500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2)){

            PlayerGestion.setPlayerJobXp(player.getName(),0);
            PlayerGestion.setPlayerJobNiveau(player.getName() , PlayerGestion.getPlayerJobNiveau(player.getName()) + 1);
            player.sendMessage(PrefixMessage.serveur() + " vous avez atteind le niveau §b" + PlayerGestion.getPlayerJobNiveau(player.getName()) + "" +
                    "\n§aIl vous faut §b" + 500 * PlayerGestion.getPlayerJobNiveau(player.getName()) * 2 + " §axp pour passer au niveau suivant");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,2.0f, 1.0f);

        }


    }

    @EventHandler
    public void BlockPlaced(BlockPlaceEvent event){
        block = event.getBlock();

        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if(!cfg.contains("Jobs.Mineur." + block.getType()))return;
        HashMapGestion.blockPlaced.put(event.getBlockPlaced(),event.getBlockPlaced());
    }
}
