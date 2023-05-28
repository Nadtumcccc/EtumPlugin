package be.nadtum.etum.Vanilla.City.Builder;


import be.nadtum.etum.Utility.Modules.PrefixMessage;

public class ClaimBuilder {

    private int CityId;

    private int x1;
    private int y1;

    private int x2;
    private int y2;


    public ClaimBuilder(int cityId, int x1, int y1, int x2, int y2) {
        CityId = cityId;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public String place() {

        if(canPlaceClaim()){
            return "Claim";
        }

        return PrefixMessage.serveur() + "votre claim a bien été placé";
    }

    private Boolean canPlaceClaim(){

        return true;
    }
}
