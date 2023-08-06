package be.nadtum.etum.Vanilla.City.Builder;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import org.bukkit.Location;

import java.util.List;

public class ClaimBuilder extends CityBuilder {

    private List<Location> locations; // Liste des emplacements de la revendication

    public ClaimBuilder(Integer uuid, String name, ClaimBuilder claim) {
        super(uuid, name, claim);
    }

    // Constructeur de la classe ClaimBuilder


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
