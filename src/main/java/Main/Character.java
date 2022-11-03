package Main;

import Utils.BlockDetection;
import Utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Character {

    private ArmorStand armorStand = null;

    //Set the armorstand
    public Character(String uuid, Location location) {;
        location.getChunk().load();

        for (Entity e : location.getChunk().getEntities()){
            if (e.getUniqueId().toString().equals(uuid) && e.getType().equals(EntityType.ARMOR_STAND)){
                armorStand = (ArmorStand) e;
            }
        }

    }

    //Adjust rotation to even values
    public float equalizeRotation(float rot){

        if (rot == 360 || rot == -360){
            rot = 0;
        }
        else if (rot == -180){
            rot = 180;
        }
        else if (rot == -270){
            rot = 90;
        }
        else if (rot == 270){
            rot = -90;
        }

        return rot;
    }

    public void mine(){
        if (armorStand != null){
            Location location = armorStand.getLocation();
            float rotation = location.getYaw();
            Block blockOne = null;

            Util.adjustToRotation(location, rotation);

            blockOne = location.getBlock();

            Block blockTwo = location.add(0,1,0).getBlock();

            blockOne.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
            blockTwo.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
        }
    }

    public void attack(){
        if (armorStand != null){
            Collection<Entity> entityList = BlockDetection.getEntitiesInRadius(armorStand.getLocation());

            for (Entity e : entityList){
                if (e instanceof LivingEntity && !(e instanceof ArmorStand)){
                    LivingEntity lE = (LivingEntity) e;
                    lE.setHealth(0);
                }
            }        }
    }
    public void placeBlock(){
        if (armorStand != null){
            Location loc = armorStand.getLocation();
            float rotation = loc.getYaw();

            Util.adjustToRotation(loc, rotation);
            loc.subtract(0, 1, 0);
            loc.getBlock().setType(Material.STONE);
        }
    }
    public void placeFloorBlock(){
        if (armorStand != null){
            Location loc = armorStand.getLocation();
            loc.add(0, 1, 0);
            if (!BlockDetection.checkCollisions(loc)){
                armorStand.teleport(loc);
                loc.subtract(0, 1, 0).getBlock().setType(Material.STONE);
            }
        }
    }
    public void placeOneBlock(){
        if (armorStand != null){
            Location loc = armorStand.getLocation();
            float rotation = loc.getYaw();

            Util.adjustToRotation(loc, rotation);
            loc.getBlock().setType(Material.STONE);
        }
    }
    public void placeTwoBlocks(){
        if (armorStand != null){
            Location loc = armorStand.getLocation();
            float rotation = loc.getYaw();

            Util.adjustToRotation(loc, rotation);
            loc.getBlock().setType(Material.STONE);
            loc.add(0, 1, 0).getBlock().setType(Material.STONE);
        }
    }

    public void rotate(Util.Rotation enumRotation){
        Location loc = armorStand.getLocation();
        float rotation = armorStand.getLocation().getYaw();

        if (enumRotation == Util.Rotation.RIGHT){
            rotation = rotation + 90f;
        }
        else {
            rotation = rotation - 90f;
        }

        loc.setYaw(equalizeRotation(rotation));
        armorStand.teleport(loc);

    }

    public void move(Util.Movement movementType){

        Location location = armorStand.getLocation();
        float rotation = location.getYaw();

        Util.adjustToLocation(location, movementType, rotation);

        if (!BlockDetection.checkCollisions(location)){
            armorStand.teleport(location);
        }
        else{
            location.add(0,1,0);
            if (!BlockDetection.checkCollisions(location)){
                armorStand.teleport(location);
            }
        }

        if (!location.getChunk().isLoaded()){
            location.getChunk().load();
        }

    }

    public void jump(){

        Location location = armorStand.getLocation();
        float rotation = location.getYaw();

        location.add(0, 1, 0);
        Util.adjustToRotation(location, rotation);

        if (!BlockDetection.checkCollisions(location)){
            armorStand.teleport(location);
        }
    }

    public void addGravity(ArmorStand armorStand){

        new BukkitRunnable(){
            @Override
            public void run() {
                Material b = Util.addLocation(armorStand.getLocation(), 0, -1, 0).getBlock().getType();
                if (b == Material.AIR || b == Material.CAVE_AIR || b == Material.WATER || b == Material.COBWEB || b == Material.LAVA || b == Material.LADDER){
                    armorStand.teleport(Util.addLocation(armorStand.getLocation(), 0, -1, 0));
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(CharacterControll.instance, 0, 5);
    }

    public ArmorStand getArmorStand(){
        return armorStand;
    }
}

