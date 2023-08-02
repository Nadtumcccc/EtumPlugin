package be.nadtum.etum.Vanilla.City.Builder;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ClaimBuilder extends CityBuilder {

    private List<Location> locations; // Liste des emplacements de la revendication

    // Constructeur de la classe ClaimBuilder
    public ClaimBuilder(Integer ID, String name, ClaimBuilder claim, List<Location> locations) {
        super(ID, name, claim);
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    // Méthode pour créer la revendication
    public void create() {
        // Code pour créer la revendication ici
        // Vous pouvez utiliser la liste des emplacements (locations) pour définir les limites de la revendication
    }

    // Méthode pour supprimer la revendication
    public void delete() {
        FichierGestion.getCfgCity().set("City." + getID() + ".zone", null);
    }


}
