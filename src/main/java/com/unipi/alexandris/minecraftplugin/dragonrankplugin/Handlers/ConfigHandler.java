package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Core.Config;
import com.unipi.alexandris.minecraftplugin.dragonrankplugin.DragonRank;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class ConfigHandler {

    private final Config config = new Config();

    private final HashMap<PotionEffectType, Integer> effects = new HashMap<>();

    public ConfigHandler(DragonRank plugin) {
        FileConfiguration fileConfiguration = plugin.getConfig();

        config.setDragon(fileConfiguration.getBoolean("dragon_keeper"));
        config.setWorld(plugin.getServer().getWorld(fileConfiguration.getString("world_name")));
        List<Integer> xyz = (List<Integer>) fileConfiguration.getList("egg_location");
        config.setLocation(new Location(config.getWorld(), xyz.get(0), xyz.get(1), xyz.get(2)));
        config.setEgg_drop(fileConfiguration.getBoolean("egg_drop"));
        config.setPlace_crystals(fileConfiguration.getBoolean("place_crystals"));
        config.setSpoil_world(fileConfiguration.getBoolean("spoil_world"));
        config.setSpoil_location(fileConfiguration.getBoolean("spoil_location"));

        int amplifier;
        if(fileConfiguration.contains("effects.strength")) {
            amplifier = fileConfiguration.getInt("effects.strength") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.speed")) {
            amplifier = fileConfiguration.getInt("effects.speed") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.haste")) {
            amplifier = fileConfiguration.getInt("effects.haste") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.jump")) {
            amplifier = fileConfiguration.getInt("effects.jump") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.regen")) {
            amplifier = fileConfiguration.getInt("effects.regen") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.resist")) {
            amplifier = fileConfiguration.getInt("effects.resist") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.fire_resist")) {
            amplifier = fileConfiguration.getInt("effects.fire_resist") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.water_breath")) {
            amplifier = fileConfiguration.getInt("effects.water_breath") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.night_vision")) {
            amplifier = fileConfiguration.getInt("effects.night_vision") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.luck")) {
            amplifier = fileConfiguration.getInt("effects.luck") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.fatigue")) {
            amplifier = fileConfiguration.getInt("effects.fatigue") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.weakness")) {
            amplifier = fileConfiguration.getInt("effects.weakness") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }


        if(fileConfiguration.contains("effects.blindness")) {
            amplifier = fileConfiguration.getInt("effects.blindness") - 1;
            if(amplifier < 0) amplifier = 0;
            if(amplifier > 254) amplifier = 254;

            config.setStrength(amplifier);
        }

        init_effects();

        if(plugin.getDRAGON() != null) {
            for(PotionEffectType type : effects.keySet()) {
                plugin.getDRAGON().removePotionEffect(type);

                if(effects.get(type) > 0)
                    plugin.getDRAGON().addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, effects.get(type) -1, false, false));
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

    public boolean isDragon() {
        return config.isDragon();
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

    public boolean isEgg_drop() {
        return config.isEgg_drop();
    }

    public boolean isPlace_crystals() {
        return config.isPlace_crystals();
    }

    public PotionEffect getStrength() {
        PotionEffectType type = PotionEffectType.INCREASE_DAMAGE;
        if(config.getStrength() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getStrength() -1, false, false);
    }

    public PotionEffect getSpeed() {
        PotionEffectType type = PotionEffectType.SPEED;
        if(config.getSpeed() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getSpeed() -1, false, false);
    }

    public PotionEffect getHaste() {
        PotionEffectType type = PotionEffectType.FAST_DIGGING;
        if(config.getHaste() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getHaste() -1, false, false);
    }

    public PotionEffect getJump() {
        PotionEffectType type = PotionEffectType.JUMP;
        if(config.getJump() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getJump() -1, false, false);
    }

    public PotionEffect getRegen() {
        PotionEffectType type = PotionEffectType.REGENERATION;
        if(config.getRegen() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getRegen() -1, false, false);
    }

    public PotionEffect getResist() {
        PotionEffectType type = PotionEffectType.DAMAGE_RESISTANCE  ;
        if(config.getResist() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getResist() -1, false, false);
    }

    public PotionEffect getFire_resist() {
        PotionEffectType type = PotionEffectType.FIRE_RESISTANCE;
        if(config.getFire_resist() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getFire_resist() -1, false, false);
    }

    public PotionEffect getWater_breath() {
        PotionEffectType type = PotionEffectType.WATER_BREATHING;
        if(config.getWater_breath() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getWater_breath() -1, false, false);
    }

    public PotionEffect getNight_vision() {
        PotionEffectType type = PotionEffectType.NIGHT_VISION;
        if(config.getNight_vision() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getNight_vision() -1, false, false);
    }

    public PotionEffect getLuck() {
        PotionEffectType type = PotionEffectType.LUCK;
        if(config.getLuck() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getLuck() -1, false, false);
    }

    public PotionEffect getFatigue() {
        PotionEffectType type = PotionEffectType.SLOW_DIGGING;
        if(config.getFatigue() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getFatigue() -1, false, false);
    }

    public PotionEffect getWeakness() {
        PotionEffectType type = PotionEffectType.WEAKNESS;
        if(config.getWeakness() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getWeakness() -1, false, false);
    }

    public PotionEffect getBlindness() {
        PotionEffectType type = PotionEffectType.BLINDNESS;
        if(config.getBlindness() < 1)
            return null;

        return new PotionEffect(type, Integer.MAX_VALUE, config.getBlindness() -1, false, false);
    }
}
