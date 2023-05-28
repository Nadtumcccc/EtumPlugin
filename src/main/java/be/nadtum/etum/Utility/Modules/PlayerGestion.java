package be.nadtum.etum.Utility.Modules;

import be.nadtum.etum.Vanilla.City.Management.Claim;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PlayerGestion {


    public static void createNewProfil(Player player){

        if(PlayerGestion.hasData(player.getName())){
            return;
        }
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".name", player.getName());
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".grade", "Akien");
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".money", 500);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".mana", 0);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".soul", 0);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".job.name", "nojob");
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".job.niveau", 1);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".job.xp", 0);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".Home.nb", 2);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".claim", 200);
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".city", "NoCity");
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".staff.grade", "NoStaff");
        FichierGestion.getCfgPlayers().set("Profil." + PlayerGestion.getUUIDFromName(player.getName()) + ".staff.puissance", 0);

        player.sendMessage(PrefixMessage.serveur() + "votre profil a bien été créé");
    }



    public static String getPlayerGrade(String playerName){
        return FichierGestion.getCfgPlayers().getString("Profil." + getUUIDFromName(playerName) + ".grade");
    }
    public static void setPlayerGrade(String playerName, String grade){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".grade", grade);
    }

    public static Integer getPlayerMana(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName) + ".mana");
    }
    public static void setPlayerMana(String playerName, Integer mana){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".mana", mana);
    }

    public static Long getPlayerMoney(String playerName){
        return FichierGestion.getCfgPlayers().getLong("Profil." + getUUIDFromName(playerName) + ".money");
    }
    public static void setPlayerMoney(String playerName, Long somme){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".money", somme);
    }

    public static void addPlayerMoney(String playerName, Long somme){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".money", getPlayerMoney(playerName) + somme);
    }

    public static String getPlayerJobName(String playerName){
        return FichierGestion.getCfgPlayers().getString("Profil." + getUUIDFromName(playerName) + ".job.name");
    }
    public static void setPlayerJobName(String playerName, String jobName){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".job.name", jobName);
    }

    public static Integer getPlayerJobNiveau(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName) + ".job.niveau");
    }
    public static void setPlayerJobNiveau(String playerName, Integer niveau){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".job.niveau", niveau);
    }

    public static void addPlayerJobNiveau(String playerName, Integer niveau){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".job.niveau", getPlayerJobNiveau(playerName) + niveau);
    }

    public static Integer getPlayerJobXp(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".job.xp");
    }
    public static void setPlayerJobXp(String playerName, Integer xp){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".job.xp", xp);
    }

    public static void addPlayerJobXp(String playerName, Integer xp){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".job.xp", getPlayerJobXp(playerName) + xp);
    }

    public static Integer getPlayerHomeCount(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".Home.nb");
    }
    public static void setPlayerHomeCount(String playerName, Integer count){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".Home.nb", count);
    }

    public static String getPlayerCityName(String playerName){
        return FichierGestion.getCfgPlayers().getString("Profil." + getUUIDFromName(playerName).toString() + ".city");
    }
    public static void setPlayerCityName(String playerName, String city){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".city", city);
    }

    public static Integer getPlayerClaimCount(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".claim");
    }
    public static void setPlayerClaimCount(String playerName, Integer claim){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".claim", claim);
    }

    public static String getPlayerStaffGrade(String playerName){
        return FichierGestion.getCfgPlayers().getString("Profil." + getUUIDFromName(playerName).toString() + ".staff.grade");
    }
    public static void setPlayerStaffGrade(String playerName, String gradeStaff){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".staff.grade", gradeStaff);
    }

    public static Integer getPlayerStaffPuissance(String playerName){
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".staff.puissance");
    }
    public static void setPlayerStaffPuissance(String playerName, Integer puissance){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".staff.puissance", puissance);
    }

    //compétence métier
    public static String getPlayerJobsVoie(String playerName){
        if(!FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.voie")){
            return "pas de voie";
        }
        return FichierGestion.getCfgPlayers().getString("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.voie");
    }

    public static void setPlayerJobsVoie(String playerName, String voie){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName) + ".compétence.métier.voie", voie);
    }

    public static Integer getPlayerJobsComp1(String playerName){
        if(!FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName) + ".compétence.métier.Un")){
            return null;
        }
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Un");
    }

    public static void setPlayerJobsComp1(String playerName, Integer nb){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Un", nb);
    }

    public static Integer getPlayerJobsCompDeux(String playerName){
        if(!FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Deux")){
            return null;
        }
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Deux");
    }

    public static void setPlayerJobsComp2(String playerName, Integer nb){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Deux", nb);
    }

    public static Integer getPlayerJobsComp3(String playerName){
        if(!FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trois")){
            return null;
        }
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trois");
    }

    public static void setPlayerJobsComp3(String playerName, Integer nb){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trois", nb);
    }

    public static Integer getPlayerJobsTrancendance(String playerName){
        if(!FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trancendance")){
            return null;
        }
        return FichierGestion.getCfgPlayers().getInt("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trancendance");
    }

    public static void setPlayerJobsTrancendance(String playerName, Integer nb){
        FichierGestion.getCfgPlayers().set("Profil." + getUUIDFromName(playerName).toString() + ".compétence.métier.Trancendance", nb);
    }




    //fonction :
    public static Boolean isPlayer(String player){
        return Bukkit.getPlayer(player) instanceof Player;
    }

    public static String getGradeDesign(String grade){
        return FichierGestion.getCfgPermission().getString("Grade."+ grade + ".design");
    }

    public static Boolean hasData(String playerName){
        return FichierGestion.getCfgPlayers().contains("Profil." + getUUIDFromName(playerName));
    }

    public static boolean hasPermission(Player player, String permission) {
        String playerGradePermission = "Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission." + permission;

        if (!FichierGestion.getCfgPermission().contains(playerGradePermission) && !player.isOp()) {
            //player.sendMessage(PrefixMessage.erreur() + " Vous n'avez pas la permission d'utiliser cette commande");
            return false;
        }

        return true;
    }

    public static boolean hasStaffPermission(Player player, String permission) {
        String playerGradePermission = "Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission." + permission;

        if (!FichierGestion.getCfgPermission().contains(playerGradePermission) && !player.isOp()) {
            //player.sendMessage(PrefixMessage.erreur() + " Vous n'avez pas la permission d'utiliser cette commande");
            return false;
        }

        return true;
    }

    public static Boolean canFly(Player player){
        if (player.isOp() || FichierGestion.getCfgPermission().contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.fly")) {
            return true;
        }
        if(!Claim.isInDefaultWorld(player))return false;
        if(Claim.getNameCityOfClaim(player, player.getLocation().getX(), player.getLocation().getZ()) == null)return false;
        if(!Claim.getNameCityOfClaim(player, player.getLocation().getX(), player.getLocation().getZ()).equals(PlayerGestion.getPlayerCityName(player.getName()))){
            return false;
        }
        return true;
    }

    public static UUID getUUIDFromName(String name){

        Player player = Bukkit.getPlayer(name);

        if (player != null) {
            return player.getUniqueId();
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        return offlinePlayer.getUniqueId();
    }




}
