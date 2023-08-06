package be.nadtum.etum.Vanilla.City.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CityBuilder {

    //--------------------------------------------------------

    /*
     * STRUCTURE FICHIER
     *
     * City:
     *   ID: UUID de la ville
     *       Name: Nom de la ville
     *       Claim: Objet pour la revendication de la ville : objet ClaimBuilder
     *       Members: Liste des membres de la ville
     *           - ID: UUID de l'utilisateur : objet MemberBuilder
     *       SurfaceMax: Surface maximale de la ville
     *
     *
     */

    //--------------------------------------------------------


    private Integer ID;                   // UUID de la ville
    private String name;                  // Nom de la ville
    private ClaimBuilder claim;           // Objet pour la revendication de la ville
    private List<MemberBuilder> members;  // Liste des membres de la ville

    private Integer surfaceMax;           // Surface maximale de la ville

    // Constructeur de la classe CityBuilder
    public CityBuilder(Integer uuid, String name, ClaimBuilder claim) {
        this.ID = uuid;
        this.name = name;
        this.claim = claim;
    }

    // Méthode pour récupérer l'objet de revendication de la ville
    public ClaimBuilder getClaim() {
        return claim;
    }

    // Méthode pour définir l'objet de revendication de la ville
    public void setClaim(ClaimBuilder claim) {
        this.claim = claim;
    }

    // Méthode pour récupérer le nom de la ville
    public String getName() {
        return name;
    }

    // Méthode pour définir le nom de la ville
    public void setName(String name) {
        this.name = name;
    }

    // Méthode pour récupérer l'UUID de la ville
    public Integer getID() {
        return ID;
    }

    // Méthode pour définir l'UUID de la ville
    public void setID(Integer ID) {
        this.ID = ID;
    }

    //--------------------------------------------------------

    // Méthode pour récupérer la liste des membres de la ville
    public List<MemberBuilder> getListMembers() {
        return members;
    }

    // Méthode pour définir la liste des membres de la ville
    public void setListMembers(List<MemberBuilder> members) {
        this.members = members;
    }

    // Méthode pour ajouter un membre à la ville
    public void addMember(UUID uuid) {
        getListMembers().add(new MemberBuilder(uuid, new ArrayList<>()));
    }

    public Boolean removeMember(UUID uuid) {
        for (MemberBuilder builder : getListMembers()) {
            if (builder.getUUID().equals(uuid)) {
                getListMembers().remove(builder);
                return true;
            }
        }
        return false;
    }

    //--------------------------------------------------------


    //--------------------------------------------------------

    // Méthode pour charger toutes les informations de toutes les villes

}