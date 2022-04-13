package controller;

import Persistenz.FileUtil;
import Rezeptteile.Rezeptkopf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RezeptkopfController {

    private HashMap<String, Rezeptkopf> köpfe = new HashMap<>();

    private RezeptkopfController() {
    }

    private static RezeptkopfController instance = new RezeptkopfController();

    public static RezeptkopfController getInstance() {
        return instance;
    }

    public Rezeptkopf neuerRezeptkopf(String name) {
        String id = null;
        while (true) {
            id = generierenID();
            if (!existiertRezeptkopf(id)) break;
        }

        Rezeptkopf rezeptkopf = new Rezeptkopf(name, id);
        köpfe.put(id, rezeptkopf);
        return rezeptkopf;
    }

    public Rezeptkopf löschenRezeptkopf(String id) {
        return köpfe.remove(id);
    }

    public Rezeptkopf getRezeptkopf(String id) {
        return köpfe.get(id);
    }

    public Rezeptkopf getRezeptkopfByName(String name) {
        for (Rezeptkopf rez : getAlleRezeptkopf()) {
            if (rez.getrKoRezeptname().equals(name)) {
                return rez;
            }
        }
        return null;
    }

    public boolean existiertRezeptkopf(String id) {
        return köpfe.containsKey(id);
    }

    public List<Rezeptkopf> getAlleRezeptkopf() {
        return List.copyOf(köpfe.values()); //Kapseln der Dateien indem nur eine Kopie weitergegeben wird
    }

    private String generierenID() {
        SecureRandom r = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 16) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 16);
    }

    public void speichernDatei() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(köpfe);
        FileUtil.writeToFile("Rezeptköpfe.json", json);
    }

    public void leseDatei() throws IOException {
        String json = FileUtil.readFromFile("Rezeptköpfe.json");
        Type type = new TypeToken<Map<String, Rezeptkopf>>() {
        }.getType();
        HashMap map = new HashMap(new Gson().fromJson(json, type));
        this.köpfe = map;
    }
}