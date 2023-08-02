package be.nadtum.etum.Vanilla.City.Builder;

import java.util.List;
import java.util.UUID;

public class MemberBuilder {

    private UUID uuid;                      // UUID du membre
    private List<String> permissions;       // Liste des permissions du membre

    // Constructeur de la classe MemberBuilder
    public MemberBuilder(UUID uuid, List<String> permissions) {
        this.uuid = uuid;
        this.permissions = permissions;
    }

    // Méthode pour récupérer l'UUID du membre
    public UUID getUUID() {
        return uuid;
    }

    // Méthode pour définir l'UUID du membre
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    // Méthode pour récupérer la liste des permissions du membre
    public List<String> getPermissions() {
        return permissions;
    }

    // Méthode pour définir la liste des permissions du membre
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    // Méthode pour ajouter une permission à la liste des permissions du membre
    public void addPermission(String permission) {
        if(hasPermission(permission)){
            permissions.add(permission);
        }else{
            System.out.print(getUUID() + " possède la permission : " + permission);
        }
    }

    // Méthode pour supprimer une permission de la liste des permissions du membre
    public void removePermission(String permission) {
        if(hasPermission(permission)){
            permissions.remove(permission);
        }else{
            System.out.print(getUUID() + " ne possède pas la permission : " + permission);
        }
    }

    // Méthode pour vérifier si le membre possède une permission donnée
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
