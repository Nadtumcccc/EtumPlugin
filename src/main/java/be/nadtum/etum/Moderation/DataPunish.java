package be.nadtum.etum.Moderation;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DataPunish {

    private final File fb;
    private final UUID uuid;
    YamlConfiguration cfg = FichierGestion.getCfgPlayers();

    public DataPunish(UUID uuid){
        fb = FichierGestion.getPlayersFile();
        this.uuid = uuid;
    }

    public boolean exist(){
        return !fb.exists();
    }

    public void setTempbanned(String from, String reason, long time, Integer chiffre,String format){
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.istempbanned", true);
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.from", from);
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.reason", reason);
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.duration", time + System.currentTimeMillis());
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.format", format);
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.chiffre", chiffre);

        cfg.set("Profil." + uuid.toString() + ".modération.tempban.timestamp", new SimpleDateFormat("dd.MM.yyyy HH.mm").format(new Date()));
    }

    public void setUnTempbanned(){
        cfg.set("Profil." + uuid.toString() + ".modération.tempban", null);
    }

    public String getTempbannedFrom(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempban.from");
    }

    public  String getTempbannedFormat(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempban.format");
    }

    public  Integer getTempbannedchiffre(){
        return cfg.getInt("Profil." + uuid.toString() + ".modération.tempban.chiffre");
    }

    public String getTempbannedReason(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempban.reason");
    }

    public long getTempbannedMilliseconds(){
        return cfg.getLong("Profil." + uuid.toString() + ".modération.tempban.duration");
    }

    public String getTempbannedTimestamp(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempban.timestamp");
    }

    public boolean isTempbanned(){
        return cfg.getBoolean("Profil." + uuid.toString() + ".modération.tempban.istempbanned");
    }



    //Muted


    public void setTempmuted(String from, String reason, long time, Integer chiffre ,String format){
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.istempmuted", true);
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.from", from);
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.reason", reason);
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.duration", time + System.currentTimeMillis());
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.format", format);
        cfg.set("Profil." + uuid.toString() + ".modération.tempban.chiffre", chiffre);

        cfg.set("Profil." + uuid.toString() + ".modération.tempmute.timestamp", new SimpleDateFormat("dd.MM.yyyy HH.mm").format(new Date()));
    }

    public void setUnTempmuted(){
        cfg.set("Profil." + uuid.toString() + ".modération.tempmute", null);
    }

    public String getTempmutedFrom(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempmute.from");
    }

    public  String getTempmutedFormat(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempmute.format");
    }

    public  Integer getTempmutedChiffre(){
        return cfg.getInt("Profil." + uuid.toString() + ".modération.tempban.chiffre");
    }

    public String getTempmutedReason(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempmute.reason");
    }

    public long getTempmutedMilliseconds(){
        return cfg.getLong("Profil." + uuid.toString() + ".modération.tempmute.duration");
    }

    public String getTempmutedTimestamp(){
        return cfg.getString("Profil." + uuid.toString() + ".modération.tempmute.timestamp");
    }

    public boolean isTempmuted(){
        return cfg.getBoolean("Profil." + uuid.toString() + ".modération.tempmute.istempmuted");
    }

}
