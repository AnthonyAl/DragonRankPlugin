package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Core;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class Config {

    private boolean void_protection;
    private boolean spoil_world;
    private boolean spoil_location;
    private List<Integer> borders;
    private boolean egg_drop;
    private boolean place_crystals;
    private World world;
    private Location location;
    private int strength = -1;
    private int speed = -1;
    private int haste = -1;
    private int jump = -1;
    private int regen = -1;
    private int resist = -1;
    private int fire_resist = -1;
    private int water_breath = -1;
    private int night_vision = -1;
    private int luck = -1;
    private int fatigue = -1;
    private int weakness = -1;
    private int blindness = -1;

    public boolean isVoid_protection() {
        return void_protection;
    }

    public void setVoid_protection(boolean void_protection) {
        this.void_protection = void_protection;
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

    public List<Integer> getBorders() {
        return borders;
    }

    public void setBorders(List<Integer> borders) {
        this.borders = borders;
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
