package Rezeptteile;

import qrcodegen.QrBufferedImage;
import qrcodegen.QrCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Rezeptkopf {

    private String rKoRezeptname;
    private final String rKoID;
    private int rKoPersonenzahl;
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
                        rezeptzutat.setrZuRezeptkopf(null); //TODO update Datenbank
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void qrGenerieren(){
        try {
            QrCode.Ecc errCorLvl = QrCode.Ecc.LOW;
            QrCode qr = QrCode.encodeText(this.zutatenToString(), errCorLvl);

            BufferedImage img = QrBufferedImage.toImage(qr, 20, 8, 0xffffff, 0x000000);
            File imgFile = new File("QR_Test_generiert.png");
            ImageIO.write(img, "png", imgFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Rezeptkopf(String rKoRezeptname, String rKoID) {
        this.rKoRezeptname = rKoRezeptname; //TODO update Datenbank
        this.rKoID = rKoID; //TODO Wie berechnen wir unsere IDs?
        rKoRezeptzutat = new ArrayList<>();
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
                    ret = rezeptzutat.getrZuZutat().getZutName() + ": " + rezeptzutat.getrZuMenge() + " " + rezeptzutat.getrZuEinheit() + "\n";
                }
                return ret;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
