package com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Config;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@SuppressWarnings("unused")
public final class ConfigHandler {

    private final Config config = new Config();

    private final HashMap<PotionEffectType, Integer> effects = new HashMap<>();

    public ConfigHandler(DragonTag plugin) {
        FileConfiguration fileConfiguration = plugin.getConfig();

        config.setVoid_protection(fileConfiguration.getBoolean("void_protection"));
        config.setOffline_mode(fileConfiguration.getBoolean("offline_mode"));
        config.setWorld(plugin.getServer().getWorld(fileConfiguration.getString("world_name")));
        List<Integer> xyz = (List<Integer>) fileConfiguration.getList("egg_location");
        config.setLocation(new Location(config.getWorld(), xyz.get(0), xyz.get(1), xyz.get(2)));
        config.setEgg_drop(fileConfiguration.getBoolean("egg_drop"));
        config.setPlace_crystals(fileConfiguration.getBoolean("place_crystals"));
        config.setSpoil_world(fileConfiguration.getBoolean("spoil_world"));
        config.setSpoil_location(fileConfiguration.getBoolean("spoil_location"));

        List<Integer> borders = new ArrayList<>();
        List<?> temp = fileConfiguration.getList("borders");
        for(Object o : temp) {
            try {
                int i = Integer.parseInt(String.valueOf(o));
                borders.add(i);
            }
            catch(NumberFormatException ignored) {}
        }
        borders.sort(Collections.reverseOrder());

        config.setBorders(borders);

        int amplifier;
        if(fileConfiguration.contains("effects.strength")) {
            amplifier = fileConfiguration.getInt("effects.strength") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.speed")) {
            amplifier = fileConfiguration.getInt("effects.speed") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setSpeed(amplifier);
        }


        if(fileConfiguration.contains("effects.haste")) {
            amplifier = fileConfiguration.getInt("effects.haste") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setHaste(amplifier);
        }


        if(fileConfiguration.contains("effects.jump")) {
            amplifier = fileConfiguration.getInt("effects.jump") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setJump(amplifier);
        }


        if(fileConfiguration.contains("effects.regen")) {
            amplifier = fileConfiguration.getInt("effects.regen") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setRegen(amplifier);
        }


        if(fileConfiguration.contains("effects.resist")) {
            amplifier = fileConfiguration.getInt("effects.resist") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setResist(amplifier);
        }


        if(fileConfiguration.contains("effects.fire_resist")) {
            amplifier = fileConfiguration.getInt("effects.fire_resist") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setFire_resist(amplifier);
        }


        if(fileConfiguration.contains("effects.water_breath")) {
            amplifier = fileConfiguration.getInt("effects.water_breath") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setWater_breath(amplifier);
        }


        if(fileConfiguration.contains("effects.night_vision")) {
            amplifier = fileConfiguration.getInt("effects.night_vision") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setNight_vision(amplifier);
        }


        if(fileConfiguration.contains("effects.luck")) {
            amplifier = fileConfiguration.getInt("effects.luck") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setLuck(amplifier);
        }


        if(fileConfiguration.contains("effects.fatigue")) {
            amplifier = fileConfiguration.getInt("effects.fatigue") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setFatigue(amplifier);
        }


        if(fileConfiguration.contains("effects.weakness")) {
            amplifier = fileConfiguration.getInt("effects.weakness") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setWeakness(amplifier);
        }


        if(fileConfiguration.contains("effects.blindness")) {
            amplifier = fileConfiguration.getInt("effects.blindness") - 1;
            if(amplifier < -1) amplifier = -1;
            if(amplifier > 254) amplifier = 254;

            config.setBlindness(amplifier);
        }

        init_effects();

        if(plugin.getDRAGON() != null) {
            for(PotionEffectType type : effects.keySet()) {
                plugin.getServer().getPlayer(plugin.getDRAGON()).removePotionEffect(type);

                if(effects.get(type) > -1)
                    plugin.getServer().getPlayer(plugin.getDRAGON()).addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, effects.get(type), false, false));
            }
        }
    }

    private void init_effects() {
        effects.put(PotionEffectType.INCREASE_DAMAGE, config.getStrength());
        effects.put(PotionEffectType.SPEED, config.getSpeed());
        effects.put(PotionEffectType.FAST_DIGGING, config.getHaste());
        effects.put(PotionEffectType.JUMP, config.getJump());
        effects.put(PotionEffectType.REGENERATION, config.getRegen());
        effects.put(PotionEffectType.DAMAGE_RESISTANCE, config.getResist());
        effects.put(PotionEffectType.FIRE_RESISTANCE, config.getFire_resist());
        effects.put(PotionEffectType.WATER_BREATHING, config.getWater_breath());
        effects.put(PotionEffectType.NIGHT_VISION, config.getNight_vision());
        effects.put(PotionEffectType.LUCK, config.getLuck());
        effects.put(PotionEffectType.SLOW_DIGGING, config.getFatigue());
        effects.put(PotionEffectType.WEAKNESS, config.getWeakness());
        effects.put(PotionEffectType.BLINDNESS, config.getBlindness());

    }

    public HashMap<PotionEffectType, Integer> getEffects() {
        return effects;
    }

    public boolean isVoid_protection() {
        return config.isVoid_protection();
    }

    public boolean isOffline_mode() {
        return config.isOffline_mode();
    }

    public World getWorld() {
        return config.getWorld();
    }

    public Location getLocation() {
        return config.getLocation();
    }

    public boolean isSpoil_world() {
        return config.isSpoil_world();
    }

    public boolean isSpoil_location() {
        return config.isSpoil_location();
    }


    public List<Integer> getBorders() {
        return config.getBorders();
    }

    public boolean isEgg_drop() {
        return config.isEgg_drop();
    }

    public boolean isPlace_crystals() {
        return config.isPlace_crystals();
    }
}
