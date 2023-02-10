package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Core;

import org.bukkit.Location;
import org.bukkit.World;

public class Config {

    private boolean dragon_keeper;
    private boolean spoil_world;
    private boolean spoil_location;
    private boolean egg_drop;
    private boolean place_crystals;
    private World world;
    private Location location;
    private int strength = 0;
    private int speed = 0;
    private int haste = 0;
    private int jump = 0;
    private int regen = 0;
    private int resist = 0;
    private int fire_resist = 0;
    private int water_breath = 0;
    private int night_vision = 0;
    private int luck = 0;
    private int fatigue = 0;
    private int weakness = 0;
    private int blindness = 0;

    public boolean isDragon() {
        return dragon_keeper;
    }

    public void setDragon(boolean dragon_keeper) {
        this.dragon_keeper = dragon_keeper;
    }

    public boolean isSpoil_world() {
        return spoil_world;
    }

    public void setSpoil_world(boolean spoil_world) {
        this.spoil_world = spoil_world;
    }

    public boolean isSpoil_location() {
        return spoil_location;
    }

    public void setSpoil_location(boolean spoil_location) {
        this.spoil_location = spoil_location;
    }

    public boolean isEgg_drop() {
        return egg_drop;
    }

    public void setEgg_drop(boolean egg_drop) {
        this.egg_drop = egg_drop;
    }

    public boolean isPlace_crystals() {
        return place_crystals;
    }

    public void setPlace_crystals(boolean place_crystals) {
        this.place_crystals = place_crystals;
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
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHaste() {
        return haste;
    }

    public void setHaste(int haste) {
        this.haste = haste;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getRegen() {
        return regen;
    }

    public void setRegen(int regen) {
        this.regen = regen;
    }

    public int getResist() {
        return resist;
    }

    public void setResist(int resist) {
        this.resist = resist;
    }

    public int getFire_resist() {
        return fire_resist;
    }

    public void setFire_resist(int fire_resist) {
        this.fire_resist = fire_resist;
    }

    public int getWater_breath() {
        return water_breath;
    }

    public void setWater_breath(int water_breath) {
        this.water_breath = water_breath;
    }

    public int getNight_vision() {
        return night_vision;
    }

    public void setNight_vision(int night_vision) {
        this.night_vision = night_vision;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getWeakness() {
        return weakness;
    }

    public void setWeakness(int weakness) {
        this.weakness = weakness;
    }

    public int getBlindness() {
        return blindness;
    }

    public void setBlindness(int blindness) {
        this.blindness = blindness;
    }
}
