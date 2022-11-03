package Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Util {
    public enum Rotation{
        LEFT,
        RIGHT
    }
    public enum Movement{
        FOWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    public static void adjustToRotation(Location location, float rotation){
        switch ((int) rotation) {
            case 90 -> location.add(-1, 0, 0);
            case 180 -> location.add(0, 0, -1);
            case -90 -> location.add(1, 0, 0);
            default -> location.add(0, 0, 1);
        }
    }

    public static void adjustToLocation(Location location, Movement movementType, float rotation){
        switch ((int) rotation){
            case 0:
                if (movementType == Util.Movement.FOWARD){
                    location.add(0, 0, 1);
                }
                else if (movementType == Util.Movement.BACKWARD){
                    location.add(0, 0, -1);
                }
                else if (movementType == Util.Movement.RIGHT){
                    location.add(-1, 0, 0);
                }
                else {
                    location.add(1, 0, 0);
                }
                break;
            case 90:
                if (movementType == Util.Movement.FOWARD){
                    location.add(-1, 0, 0);
                }
                else if (movementType == Util.Movement.BACKWARD){
                    location.add(1, 0, 0);
                }
                else if (movementType == Util.Movement.RIGHT){
                    location.add(0, 0, -1);
                }
                else {
                    location.add(0, 0, 1);
                }
                break;
            case 180:
                if (movementType == Util.Movement.FOWARD){
                    location.add(0, 0, -1);
                }
                else if (movementType == Util.Movement.BACKWARD){
                    location.add(0, 0, 1);
                }
                else if (movementType == Util.Movement.RIGHT){
                    location.add(1, 0, 0);
                }
                else {
                    location.add(-1, 0, 0);
                }
                break;
            case -90:
                if (movementType == Util.Movement.FOWARD){
                    location.add(1, 0, 0);
                }
                else if (movementType == Util.Movement.BACKWARD){
                    location.add(-1, 0, 0);
                }
                else if (movementType == Util.Movement.RIGHT){
                    location.add(0, 0, 1);
                }
                else {
                    location.add(0, 0, -1);
                }
                break;
        }
    }

    public static ItemStack getPlayerSkull(Player player){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static Location addLocation(Location location, int x, int y, int z){
        return new Location(location.getWorld(), location.getX()+x, location.getY()+y, location.getZ()+z);
    }

}
