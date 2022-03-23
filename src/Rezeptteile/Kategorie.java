package Rezeptteile;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Kategorie {

    private String katName;
    private ArrayList<Rezeptkopf> katRezeptkopf;

    public void rezeptHinzufügen(Rezeptkopf rezeptkopf){
        try {
            if (rezeptkopf != null) {
                katRezeptkopf.add(rezeptkopf); //TODO update Datenbank
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rezeptLöschen(Rezeptkopf rezeptkopf){
        try {
            if (rezeptkopf != null) {
                katRezeptkopf.remove(rezeptkopf); //TODO update Datenbank
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Kategorie(String katName) {
        this.katName = katName; //TODO update Datenbank
        katRezeptkopf = new ArrayList<>();
    }

    public String getKatName() {
        return katName;
    }

    public void setKatName(String katName) {
        this.katName = katName; //TODO update Datenbank
    }

    public String toJSON(){
        return new Gson().toJson(this);
    }

    public static Kategorie fromJSON(String json){
        Kategorie result = new Gson().fromJson(json, Kategorie.class); //String json wird zu einem kategorie Objekt konvertiert
        for (Rezeptkopf kopf: result.katRezeptkopf) {
            for (Rezeptzutat zutat : kopf.getrKoRezeptzutat()) { //Alle Zutaten werden dem Rezeptkopf result zugeordnet
                zutat.setrZuRezeptkopf(kopf);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Kategorie{" +
                "katName='" + katName + '\'' +
                ", katRezeptkopf=" + katRezeptkopf +
                '}';
    }
}
