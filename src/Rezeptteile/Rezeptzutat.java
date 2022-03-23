package Rezeptteile;

public class Rezeptzutat {

    private double rZuMenge;
    private String rZuEinheit;
    private Zutat rZuZutat;
    private transient Rezeptkopf rZuRezeptkopf;

    public Rezeptzutat(double rZuMenge, String rZuEinheit, Zutat rZuZutat, Rezeptkopf rZuRezeptkopf) {
        this.rZuMenge = rZuMenge; //TODO update Datenbank
        this.rZuZutat = rZuZutat;
        this.rZuEinheit = rZuEinheit;
        this.rZuRezeptkopf = rZuRezeptkopf;
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

    public Rezeptkopf getrZuRezeptkopf() {
        return rZuRezeptkopf;
    }

    public void setrZuRezeptkopf(Rezeptkopf rZuRezeptkopf) {
        this.rZuRezeptkopf = rZuRezeptkopf; //TODO update Datenbank
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
}
