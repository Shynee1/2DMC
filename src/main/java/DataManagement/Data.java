package DataManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Data {

    private String id, uuid, location;
    private boolean isNull = false;

    public Data(String id, String uuid, String location){
        if (id != null && uuid != null && location != null) {
            this.id = id;
            this.uuid = uuid;
            this.location = location;
        } else {
            isNull = true;
        }
    }

    public Data(String[] value){
        if (value.length == 3){
            id = value[0];
            uuid = value[1];
            location = value[2];
        } else {
            isNull = true;
        }
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId(){
        return id;
    }

    public String getUuid(){
        return uuid;
    }

    public String getLocationString(){
        return location;
    }

    public Location getLocation() {
        String[] loc = location.split(":");
        return new Location(Bukkit.getServer().getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
    }

    public boolean isNull(){
        return isNull;
    }
}

