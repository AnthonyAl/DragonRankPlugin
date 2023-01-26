package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Core;

import org.bukkit.Location;
import org.bukkit.World;

public class Config {

    private boolean dragon_keeper;

    private World world;

    private Location location;

    public boolean isDragon() {
        return dragon_keeper;
    }

    public void setDragon(boolean dragon_keeper) {
        this.dragon_keeper = dragon_keeper;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
