package Rezeptteile;

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
}
