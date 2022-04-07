package controller;

import Persistenz.FileUtil;
import Rezeptteile.Zutat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZutatenController {

    private HashMap<String , Zutat> zutaten = new HashMap<>();

    private ZutatenController(){}

    private static ZutatenController instance = new ZutatenController();

    public static ZutatenController getInstance() {
        return instance;
    }

    public Zutat neueZutat(String name){
        if (existiertZutat(name)) return null;

        Zutat Zutat = new Zutat(name);
        zutaten.put(name, Zutat);
        return Zutat;
    }

    public Zutat löschenZutat(String name){
        return zutaten.remove(name);
    }

    public Zutat getZutat(String name){
        return zutaten.get(name);
    }

    public boolean existiertZutat(String name){
        return zutaten.containsKey(name);
    }

    public List<Zutat> getAlleZutaten(){
        return List.copyOf(zutaten.values()); //Kapseln der Dateien indem nur eine Kopie weitergegeben wird
    }

    public void speichernDatei() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(zutaten);
        FileUtil.writeToFile("Zutaten.json", json);
    }

    public void leseDatei() throws IOException {
        String json = FileUtil.readFromFile("Zutaten.json");
        Type type = new TypeToken<Map<String, Zutat>>(){}.getType();
        HashMap map = new HashMap(new Gson().fromJson(json, type));
        this.zutaten = map;
    }
}
