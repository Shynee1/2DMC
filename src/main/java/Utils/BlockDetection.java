package Utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockDetection {

    public static boolean checkCollisions(Location loc){
        return !(loc.getBlock().isPassable() && Util.addLocation(loc, 0, 1, 0).getBlock().isPassable());
    }

    //Gets all blocks in a 11x11 radius of the character
    public static List<Block> getBlocksInRadius(Location location){

        double x = location.getX()-0.5;
        double y = location.getY()-0.5;
        double z = location.getZ()-0.5;
        World w = location.getWorld();

        List<Block> blockList = new ArrayList<>();

        float rotation = location.getYaw();

        for (double j = 5; j >= -5; j--){
            for (double i = 5; i >= -5; i--){
                switch ((int) rotation){
                    case 0, 180 -> blockList.add(new Location(w, x, y + j, z-i).getBlock());
                    case 90, -90 -> blockList.add(new Location(w, x-i, y + j, z).getBlock());
                }
            }
        }
        return blockList;
    }

    //Gets all entities in an 11x11 radius of the character
    public static List<Entity> getEntitiesInRadius(Location playerLocation){

        List<Entity> finalList = new ArrayList<>();

        Chunk c = playerLocation.getChunk();

        //Get all entities in the armorstand's chunk
        Entity[] entities = c.getEntities();

        Location blockLocation = new Location(playerLocation.getWorld(), playerLocation.getX()-0.500, playerLocation.getY()-0.500, playerLocation.getZ()-0.500);

        //Create a box where the grid is
        BoundingBox b = new BoundingBox(blockLocation.getX()+5, blockLocation.getY()+5, blockLocation.getZ()+5, blockLocation.getX()-5, blockLocation.getY()-5, blockLocation.getZ()-5);

        //Check if entities are inside box
        for (Entity e : entities){
            if (e instanceof LivingEntity && b.contains(new Vector(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ()))){
                finalList.add(e);
            }
        }

        return finalList;

    }



}
