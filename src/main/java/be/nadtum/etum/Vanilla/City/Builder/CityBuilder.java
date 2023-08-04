package be.nadtum.etum.Vanilla.City.Builder;

import java.util.List;

public class CityBuilder {

    private Integer ID;                 // UUID de la ville
    private String name;                  // Nom de la ville
    private ClaimBuilder claim;           // Objet pour la revendication de la ville
    private List<MemberBuilder> members;  // Liste des membres de la ville

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
    public List<MemberBuilder> getMembers() {
        return members;
    }

    // Méthode pour définir la liste des membres de la ville
    public void setMembers(List<MemberBuilder> members) {
        this.members = members;
    }

    public void addMember(MemberBuilder member) {

    }
}

//--------------------------------------------------------
