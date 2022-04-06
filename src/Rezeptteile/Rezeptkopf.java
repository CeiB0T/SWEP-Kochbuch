package Rezeptteile;

import com.google.gson.Gson;
import java.util.ArrayList;

public class Rezeptkopf {

    private String rKoRezeptname;
    private final String rKoID;
    private int rKoPersonenzahl;
    private String rKoRezeptinhalt;
    private ArrayList<Rezeptzutat> rKoRezeptzutat;

    public void zutatHinzufügen(Rezeptzutat rezeptzutat){
        try {
            if(rezeptzutat != null){
            rKoRezeptzutat.add(rezeptzutat); //TODO update Datenbank
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void zutatLöschen(String zutatName){
        try {
            if (zutatName != null && zutatName != ""){
                for (Rezeptzutat rezeptzutat: rKoRezeptzutat) {
                    if (rezeptzutat.getrZuZutat().getZutName() == zutatName){
                        rezeptzutat.setrZuZutat(null); //TODO update Datenbank
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Rezeptkopf(String rKoRezeptname, String rKoID) {
        this.rKoRezeptname = rKoRezeptname; //TODO update Datenbank
        this.rKoID = rKoID; //TODO Wie berechnen wir unsere IDs?
        rKoRezeptzutat = new ArrayList<>();
        rKoPersonenzahl = 0;
    }

    public String getrKoRezeptname() {
        return rKoRezeptname;
    }

    public void setrKoRezeptname(String rKoRezeptname) {
        this.rKoRezeptname = rKoRezeptname; //TODO update Datenbank
    }

    public int getrKoPersonenzahl() {
        return rKoPersonenzahl;
    }

    public void setrKoPersonenzahl(int rKoPersonenzahl) {
        this.rKoPersonenzahl = rKoPersonenzahl; //TODO update Datenbank
    }


    public String zutatenToString() {
        try {
            if(rKoRezeptzutat.size() > 0) {
                String ret = "";
                for (Rezeptzutat rezeptzutat : rKoRezeptzutat) {
                    if(rezeptzutat.getrZuEinheit() == null){
                        ret = rezeptzutat.getrZuZutat().getZutName() + ": " + rezeptzutat.getrZuMenge() + "\n";
                    }else ret = rezeptzutat.getrZuZutat().getZutName() + ": " + rezeptzutat.getrZuMenge() + " " + rezeptzutat.getrZuEinheit() + "\n";
                }
                return ret;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Rezeptzutat> getrKoRezeptzutat() {
        return rKoRezeptzutat;
    }

    public void setrKoRezeptzutat(ArrayList<Rezeptzutat> rKoRezeptzutat) {
        this.rKoRezeptzutat = rKoRezeptzutat;
    }

    public String getrKoRezeptinhalt() {
        return rKoRezeptinhalt;
    }

    public void setrKoRezeptinhalt(String rKoRezeptinhalt) {
        this.rKoRezeptinhalt = rKoRezeptinhalt;
    }

    public String toJSON(){
        return new Gson().toJson(this);
    }

    public static Rezeptkopf fromJSON(String json){
        Rezeptkopf result = new Gson().fromJson(json, Rezeptkopf.class); //String json wird zu einem Rezeptkopf Objekt konvertiert
        return result;
    }

    public String getrKoID() {
        return rKoID;
    }

    @Override
    public String toString() {
        return "Rezeptkopf{" +
                "rKoRezeptname='" + rKoRezeptname + '\'' +
                ", rKoID='" + rKoID + '\'' +
                ", rKoPersonenzahl=" + rKoPersonenzahl +
                ", rKoRezeptzutat=" + rKoRezeptzutat +
                '}';
    }

    public String listViewString(){
        String rezeptInhaltAusgabe = "nein";
        if (getrKoRezeptinhalt() != null){
            if (!getrKoRezeptinhalt().isEmpty()) rezeptInhaltAusgabe = "ja";
        }
        return rKoRezeptname +", anzahl Zutaten: "+ rKoRezeptzutat.size() + ", Anzahl Personen: " + rKoPersonenzahl + ", Inhalt? " + rezeptInhaltAusgabe;
    }
}
