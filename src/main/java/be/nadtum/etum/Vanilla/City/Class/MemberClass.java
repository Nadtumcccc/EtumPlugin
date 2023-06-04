package be.nadtum.etum.Vanilla.City.Class;

import java.util.List;
import java.util.UUID;

public class MemberClass {

    private List<String> permissions;
    private UUID uuid;

    public MemberClass(List<String> permissions, UUID uuid) {
        this.permissions = permissions;
        this.uuid = uuid;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
