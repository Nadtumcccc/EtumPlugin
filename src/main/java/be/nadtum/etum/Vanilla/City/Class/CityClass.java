package be.nadtum.etum.Vanilla.City.Class;



import be.nadtum.etum.Vanilla.Player.Class.RankClass;

import java.util.HashMap;
import java.util.List;

public class CityClass{

    public static HashMap<String, RankClass> cityClassCity = new HashMap<>();

    public static void initializeRanks() {

    }

    public static void saveConfigRanks() {

    }

    private int id;
    private String name;
    private List<MemberClass> listMembers;
    private ClaimClass claim;

    public CityClass(int id, String name, List<MemberClass> listMembers, ClaimClass claim) {
        this.id = id;
        this.name = name;
        this.listMembers = listMembers;
        this.claim = claim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberClass> getListMembers() {
        return listMembers;
    }

    public void setListMembers(List<MemberClass> listMembers) {
        this.listMembers = listMembers;
    }

    public ClaimClass getClaim() {
        return claim;
    }

    public void setClaim(ClaimClass claim) {
        this.claim = claim;
    }
}
