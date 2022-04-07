package controller;

import Persistenz.FileUtil;
import Rezeptteile.Kategorie;
import Rezeptteile.Rezeptkopf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategorieController {

    private HashMap<String ,Kategorie> kategorien = new HashMap<>();

    private KategorieController(){}

    private static KategorieController instance = new KategorieController();

    public static KategorieController getInstance() {
        return instance;
    }

    public Kategorie neueKategorie(String name){
        if (existiertKategorie(name)) return null;

        Kategorie kategorie = new Kategorie(name);
        kategorien.put(name, kategorie);
        return kategorie;
    }

    public void loeschenRezeptkopf(Kategorie kategorie, Rezeptkopf rezeptkopf) throws IOException {
        kategorie.rezeptLöschen(rezeptkopf);
        speichenDatei();
    }

    public Kategorie löschenKategorie(String name){
        return kategorien.remove(name);
    }

    public Kategorie getKategorie(String name){
        return kategorien.get(name);
    }

    public boolean existiertKategorie(String name){
        return kategorien.containsKey(name);
    }

    public List<Kategorie> getAlleKategorien(){
        return List.copyOf(kategorien.values()); //Kapseln der Dateien indem nur eine Kopie weitergegeben wird
    }

    public void speichenDatei() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(kategorien);
        FileUtil.writeToFile("Kategorien.json", json);
    }

    public void leseDatei() throws IOException {
        String json = FileUtil.readFromFile("Kategorien.json");
        Type type = new TypeToken<Map<String, Kategorie>>(){}.getType();
        HashMap map = new HashMap(new Gson().fromJson(json, type));
        this.kategorien = map;
    }
}
