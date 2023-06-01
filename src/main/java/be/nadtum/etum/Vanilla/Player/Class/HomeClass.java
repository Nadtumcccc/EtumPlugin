package be.nadtum.etum.Vanilla.Player.Class;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeClass {

    private Location location;
    private String name;
    private boolean visible;

    public HomeClass(Location location, String name, boolean visible) {
        this.location = location;
        this.name = name;
        this.visible = visible;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void delete(PlayerClass playerClass) {
        // Code pour supprimer un home
        playerClass.getListHomes().remove(this);
    }

    public void teleport(Player player) {
        // Code pour téléporter le joueur à l'emplacement du home
        player.teleport(getLocation());
    }
}
