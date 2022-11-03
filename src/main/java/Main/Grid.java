package Main;

import Utils.BlockDetection;
import Utils.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Grid {

    public static String getGrid(ArmorStand armorStand) {
        Location location = armorStand.getLocation();
        StringBuilder s = new StringBuilder();

        //Get all entities in a 11x11 box
        Collection<Entity> entities = BlockDetection.getEntitiesInRadius(location);

        //Store the entities locations and their corresponding emojis in a hashmap
        HashMap<Location, String> entityLocations = new HashMap<>();

        for (Entity e : entities) {

            //Round location to adjust for entities in the middle of blocks
            Location entityLoc = new Location(e.getWorld(), Math.floor(e.getLocation().getX()), Math.floor(e.getLocation().getY()), Math.floor(e.getLocation().getZ()));

            switch (e.getType()) {
                case ARMOR_STAND -> {
                    entityLocations.put(entityLoc, "\uD83D\uDE33");
                    entityLocations.put(Util.addLocation(entityLoc, 0, 1, 0), "\uD83D\uDE33");
                }
                case PLAYER -> {
                    entityLocations.put(entityLoc, "\uD83E\uDD28");
                    entityLocations.put(Util.addLocation(entityLoc, 0, 1, 0), "\uD83E\uDD28");
                }
                case ZOMBIE, CREEPER, SKELETON, SPIDER, CAVE_SPIDER, WITHER_SKELETON, ENDERMAN, ENDER_DRAGON, HUSK, DROWNED, PIGLIN, PIGLIN_BRUTE -> {
                    entityLocations.put(entityLoc, "\uD83D\uDE08");
                    entityLocations.put(Util.addLocation(entityLoc, 0, 1, 0), "\uD83D\uDE08");
                }
                default -> entityLocations.put(entityLoc, "\uD83D\uDC37");
            }
        }

        //Get all blocks in a 11x11 box and adjust for rotation
        List<Block> blockList = BlockDetection.getBlocksInRadius(location);

        for (int rows = 0; rows < 10; rows++) {
            for (int cols = 0; cols < 11; cols++) {

                Block b = blockList.get(0); //Use this instead of counter because of the nested loops

                //Display entities
                if (entityLocations.containsKey(b.getLocation())) {
                    s.append(entityLocations.get(b.getLocation()));
                    blockList.remove(0);
                    continue;
                }

                //Display blocks
                switch (b.getType()) {
                    case WATER -> s.append("\uD83D\uDFE6");
                    case LAVA -> s.append("\uD83D\uDFE5");
                    case AIR, CAVE_AIR, VOID_AIR -> s.append("⬛");
                    default -> s.append("⬜");
                }

                blockList.remove(0);

            }
            s.append("\n");
        }

        //Return grid with armorstand location
        return String.format(s + "\nx: %d, y: %d, z: %d", (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }
}
