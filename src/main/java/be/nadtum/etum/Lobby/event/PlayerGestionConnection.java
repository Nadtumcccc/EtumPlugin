package be.nadtum.etum.Lobby.event;

import be.nadtum.etum.Utility.Modules.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerGestionConnection implements Listener {


    @EventHandler
    public void PlayerJoinServer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerGestion.createNewProfil(player);
        event.setJoinMessage("§6[§a+§6] §4"
                + (PlayerGestion.getPlayerStaffGrade(player.getName()).equalsIgnoreCase("NoStaff")
                ? Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName()))) : Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerStaffGrade(player.getName())))) + " " + player.getName());


        if(!player.hasPlayedBefore()){
            player.sendTitle("§6Bienvenue sur §bAkia","§6pour commencer ton aventure fait §b/menu", 20, 40, 30);
            for (Player players : Bukkit.getOnlinePlayers()){
                players.sendMessage(PrefixMessage.serveur() + "Bienvenue à " + player.getName() + " sur le serveur §bAkia ");
                players.playSound(players.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F);
            }

            Teleportation.PlayerTpToSpawn(player, "Spawn");

        }


        for(Player players :  Bukkit.getOnlinePlayers()) {
            players.setPlayerListHeaderFooter("§bAkia\n§eEn Jeu : §b" + Bukkit.getOnlinePlayers().size() + "§e/§b"
                            + Bukkit.getServer().getMaxPlayers() + "\n§7==================",
                    "§7==================\n§6Site Web §e: §bakia.be \n§6Discord §e: §bhttps://discord.gg/n2VSKWGb7n");

        }


        if (PlayerGestion.getPlayerStaffGrade(player.getName()).equalsIgnoreCase("NoStaff")) {
            onNameTag(player, Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName()))) + " ");
        }else{
            onNameTag(player, Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerStaffGrade(player.getName()))) + " ");
        }


        player.sendTitle("§aBienvenue sur §bAkia","Nous te souhaitons une bonne session", 20, 30, 30);



        player.sendMessage(
                "§a-------------------------------" +
                        "\n§aMoney §e: §b" + PlayerGestion.getPlayerMoney(player.getName()) +
                        "\n§aGrade §e: §b" + PlayerGestion.getPlayerGrade(player.getName()) +
                        "\n§aMétier §e: §b" + PlayerGestion.getPlayerJobName(player.getName()) + "§e, §aniveau §e: §b" + PlayerGestion.getPlayerJobNiveau(player.getName()) +
                "\n§a-------------------------------");





        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()).toString() + ".lastconnection", new SimpleDateFormat("dd.MM.yyyy HH.mm").format(new Date()));



        player.setGameMode(GameMode.SURVIVAL);


    }

    @EventHandler
    public void PlayerQuitServer(PlayerQuitEvent event){

        Player player = event.getPlayer();
        event.setQuitMessage("§6[§4-§6] "
                + (PlayerGestion.getPlayerStaffGrade(player.getName()).equalsIgnoreCase("NoStaff") ? Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerGrade(player.getName()))) : Chat.colorString(PlayerGestion.getGradeDesign(PlayerGestion.getPlayerStaffGrade(player.getName())))) + " " + player.getName());

        for(Player players :  Bukkit.getOnlinePlayers()){
            players.setPlayerListHeaderFooter("§bAkia\n§eEn Jeu : §b" + (Bukkit.getOnlinePlayers().size() - 1) + "§e/§b"
                            + Bukkit.getServer().getMaxPlayers() + "\n§7==================",
                    "§7==================\n§6Site Web §e: §bakia.be \n§6Discord §e: §bhttps://discord.gg/n2VSKWGb7n");


        }
        FichierGestion.saveFile(FichierGestion.getCfgPlayers(), FichierGestion.getFichierPlayers());
    }

    public void onNameTag(Player player, String prefix){

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam(prefix);
        if(team == null){
            team = scoreboard.registerNewTeam(prefix);
        }
        team.setPrefix(prefix);
        team.addPlayer(player);
        for (Player all : Bukkit.getOnlinePlayers()){
            all.setScoreboard(scoreboard);
        }
    }
}
