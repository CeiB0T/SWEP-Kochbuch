package Rezeptteile;

public class Zubereitungsmethode {

    private String zMeName;
    private String zMeDefinition;
    private final String zMeID;

    public Zubereitungsmethode(String zMeName, String zMeID) {
        this.zMeName = zMeName; //TODO update Datenbank
        this.zMeID = zMeID;
    }

    public String getzMeID() {
        return zMeID;
    }

    public String getzMeName() {
        return zMeName;
    }

    public void setzMeName(String zMeName) {
        this.zMeName = zMeName; //TODO update Datenbank
    }

    public String getzMeDefinition() {
        return zMeDefinition;
    }

    public void setzMeDefinition(String zMeDefinition) {
        this.zMeDefinition = zMeDefinition; //TODO update Datenbank
    }
}
