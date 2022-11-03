package DataManagement;

import org.bukkit.Location;

import java.util.Collections;
import java.util.List;

public class DataManager {

    //Matches Discord id to Minecraft character
    public static boolean matchCharacter(String username, String id){
        List<String> list = FileCreator.get().getStringList("Users");
        for (String s : list) {
            Data data = new Data(s.split("/"));
            if (!data.isNull() && data.getId().equalsIgnoreCase(username)){
                data.setId(id);
                String w = String.format("%s/%s/%s", data.getId(), data.getUuid(), data.getLocationString());
                Collections.replaceAll(list, s, w);
                saveList(list);
                return true;
            }
        }
        return false;
    }

    //Saves character when first created in Minecraft
    public static boolean initalizeCharacter(String username, String uuid, Location location){
        List<String> list = FileCreator.get().getStringList("Users");
        if (!list.isEmpty()) {
            for (String s : list) {
                Data data = new Data(s.split("/"));
                if (!data.isNull() && !data.getId().equalsIgnoreCase(username)) {
                    list.add(String.format("%s/%s/%s", username, uuid, toLocationString(location)));
                    saveList(list);
                    return true;
                }
            }
            return false;
        } else {
            list.add(String.format("%s/%s/%s", username, uuid, toLocationString(location)));
            saveList(list);
            return true;
        }
    }

    //Deletes character from Database
    public static boolean deleteCharacter(String id){
        List<String> list = FileCreator.get().getStringList("Users");
        for (String s : list){
            Data data = new Data(s.split("/"));
            if (!data.isNull() && data.getId().equalsIgnoreCase(id)){
                list.remove(s);
                saveList(list);
                return true;
            }
        }
        return false;
    }

    //Saves character location
    public static boolean saveCharacter(String id, Location location){
        List<String> list = FileCreator.get().getStringList("Users");
        for (String s : list) {
            Data data = new Data(s.split("/"));
            if (!data.isNull() && data.getId().equalsIgnoreCase(id)) {
                Collections.replaceAll(list, s, String.format("%s/%s/%s", data.getId(), data.getUuid(), toLocationString(location)));
                saveList(list);
                return true;
            }
        }
        return false;
    }

    public static Data getCharacterData(String id){
        List<String> list = FileCreator.get().getStringList("Users");
        for (String s : list) {
            Data data = new Data(s.split("/"));
            if (!data.isNull() && data.getId().equalsIgnoreCase(id)) {
                return data;
            }
        }
        return null;
    }

    private static void saveList(List<String> list){
        FileCreator.get().set("Users", list);
        FileCreator.save();
    }

    private static String toLocationString(Location location){
        return String.format("%s:%s:%s:%s", location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }


}
