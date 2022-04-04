package controller;

import Persistenz.FileUtil;
import Rezeptteile.Rezeptkopf;
import Rezeptteile.Zubereitungsmethode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZubereitungsmethodeController {

    private HashMap<String , Zubereitungsmethode> zubereitungsmethoden = new HashMap<>();

    private ZubereitungsmethodeController(){}

    private static ZubereitungsmethodeController instance = new ZubereitungsmethodeController();

    public static ZubereitungsmethodeController getInstance() {
        return instance;
    }

    public Zubereitungsmethode neueZubereitungsmethode(String name){
        String id = null;
        while (true){
            id = generierenID();
            if (!existiertZubereitungsmethode(id)) break;
        }

        Zubereitungsmethode zubereitungsmethode = new Zubereitungsmethode(name, id);
        zubereitungsmethoden.put(id, zubereitungsmethode);
        return zubereitungsmethode;
    }

    public Zubereitungsmethode löschenZubereitungsmethode(String id){
        return zubereitungsmethoden.remove(id);
    }

    public Zubereitungsmethode getZubereitungsmethode(String id){
        return zubereitungsmethoden.get(id);
    }

    public boolean existiertZubereitungsmethode(String id){
        return zubereitungsmethoden.containsKey(id);
    }

    public List<Zubereitungsmethode> getAlleZubereitungsmethoden(){
        return List.copyOf(zubereitungsmethoden.values()); //Kapseln der Dateien indem nur eine Kopie weitergegeben wird
    }

    private String generierenID(){
        SecureRandom r = new SecureRandom ();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 16){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 16);
    }

    public void speichenDatei() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(zubereitungsmethoden);
        FileUtil.writeToFile("Zubereitungsmethoden.json", json);
    }

    public void leseDatei() throws IOException {
        String json = FileUtil.readFromFile("Zubereitungsmethoden.json");
        Type type = new TypeToken<Map<String, Zubereitungsmethode>>(){}.getType();
        HashMap map = new HashMap(new Gson().fromJson(json, type));
        this.zubereitungsmethoden = map;
    }
}
