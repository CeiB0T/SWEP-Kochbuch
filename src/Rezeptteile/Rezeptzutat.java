package Rezeptteile;

public class Rezeptzutat {

    private double rZuMenge;
    private String rZuEinheit;
    private Zutat rZuZutat;

    public Rezeptzutat(double rZuMenge, String rZuEinheit, Zutat rZuZutat) {
        this.rZuMenge = rZuMenge; //TODO update Datenbank
        this.rZuZutat = rZuZutat;
        this.rZuEinheit = rZuEinheit;
    }

    public double getrZuMenge() {
        return rZuMenge;
    }

    public void setrZuMenge(double rZuMenge) {
        this.rZuMenge = rZuMenge; //TODO update Datenbank
    }

    public Zutat getrZuZutat() {
        return rZuZutat;
    }

    public void setrZuZutat(Zutat rZuZutat) {
        this.rZuZutat = rZuZutat; //TODO update Datenbank
    }

    public String getrZuEinheit() {
        return rZuEinheit;
    }

    public void setrZuEinheit(String rZuEinheit) {
        this.rZuEinheit = rZuEinheit; //TODO update Datenbank
    }

    @Override
    public String toString() {
        return "Rezeptzutat{" +
                "rZuMenge=" + rZuMenge +
                ", rZuEinheit='" + rZuEinheit + '\'' +
                ", rZuZutat=" + rZuZutat +
                '}';
    }

    public String listViewString() {
        return rZuZutat.getZutName() + ": " + rZuMenge + " " + rZuEinheit;
    }
}