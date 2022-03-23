package Rezeptteile;


import Persistenz.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import qrcodegen.QrBufferedImage;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Rezeptkopf rez = new Rezeptkopf("Test", "ID");
        rez.zutatHinzufügen(new Rezeptzutat(200, new Zutat("Zwiebel"), rez));
        Rezeptkopf rez2 = new Rezeptkopf("2Test", "ID");
        rez.zutatHinzufügen(new Rezeptzutat(400, new Zutat("Gurke"), rez));

        //QrBufferedImage.qrGenerieren(rez);

        String res = rez.toJSON();
        String res2 = rez2.toJSON();

        //System.out.println(res);
        //System.out.println(res2);
        //Rezeptkopf loaded = Rezeptkopf.fromJSON(res);
        //System.out.println(loaded.toString());

        FileUtil.writeToFile("result.json", res); //Geht das auch als finale jar
        FileUtil.writeToFile("result.json", res2);

        String fromFile = FileUtil.readFromFile("result.json");

        Rezeptkopf loaded = Rezeptkopf.fromJSON(fromFile);
        loaded.setrKoRezeptname("Positiv");
        loaded.toString();
    }
}
