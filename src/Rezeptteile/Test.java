package Rezeptteile;

import controller.RezeptkopfController;
import controller.ZutatenController;
//import javafx.application.Application;

import java.io.IOException;

public class Test{

    public static void main(String[] args) throws IOException {
        ZutatenController zutatenController = ZutatenController.getInstance();
        Zutat zutat = null;

        Rezeptkopf rez = RezeptkopfController.getInstance().neuerRezeptkopf("Pustekuchen");
        if (zutatenController.existiertZutat("Gurke")){
            zutat = zutatenController.getZutat("Gurke");
        }else{
            zutat = zutatenController.neueZutat("Gurke");
        }
        rez.zutatHinzufügen(new Rezeptzutat(200, "gramm", zutat));

        Rezeptkopf rez2 = RezeptkopfController.getInstance().neuerRezeptkopf("Zwiebelsuppe");
        Zutat zut2 = ZutatenController.getInstance().neueZutat("Zwiebel");
        if (zutatenController.existiertZutat("Zwiebel")){
            zutat = zutatenController.getZutat("Zwiebel");
        }else{
            zutat = zutatenController.neueZutat("Zwiebel");
        }
        rez2.zutatHinzufügen(new Rezeptzutat(1, "stück", zutat));
        rez2.setrKoRezeptinhalt("Zwiebel in wasser kochen und servieren");

        Rezeptkopf rez3 = RezeptkopfController.getInstance().neuerRezeptkopf("Gurkensalat");
        if (zutatenController.existiertZutat("Gurke")){
            zutat = zutatenController.getZutat("Gurke");
        }else{
            zutat = zutatenController.neueZutat("Gurke");
        }
        rez3.zutatHinzufügen(new Rezeptzutat(1, "stück", zutat));

        RezeptkopfController.getInstance().speichenDatei();

        RezeptkopfController.getInstance().leseDatei();
        for (Rezeptkopf rezeptkopf: RezeptkopfController.getInstance().getAlleRezeptkopf()) {
            System.out.println(rezeptkopf.toString());
        }

        //QrBufferedImage.qrGenerieren(rez);
    }
}