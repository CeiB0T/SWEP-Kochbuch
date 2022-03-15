package Rezeptteile;

import qrcodegen.QrBufferedImage;

public class Test {

    public static void main(String[] args) {
        Rezeptkopf rez = new Rezeptkopf("Test", "ID");
        rez.zutatHinzufügen(new Rezeptzutat(200, new Zutat("Zwiebel"), rez));
        System.out.println(rez.zutatenToString());
        QrBufferedImage.qrGenerieren(rez);
    }
}
