package Rezeptteile;

public class Zutat {

    private String zutName;

    public Zutat(String zutName) {
        this.zutName = zutName; //TODO update Datenbank
    }

    public String getZutName() {
        return zutName;
    }

    public void setZutName(String zutName) {
        this.zutName = zutName; //TODO update Datenbank
    }
}