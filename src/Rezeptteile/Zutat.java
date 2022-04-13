package Rezeptteile;

import com.google.gson.Gson;

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

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static Zutat fromJSON(String json) {
        Zutat result = new Gson().fromJson(json, Zutat.class); //String json wird zu einem Zutat Objekt konvertiert
        return result;
    }

    @Override
    public String toString() {
        return "Zutat{" +
                "zutName='" + zutName + '\'' +
                '}';
    }
}