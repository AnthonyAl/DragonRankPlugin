package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Core.Config;
import com.unipi.alexandris.minecraftplugin.dragonrankplugin.DragonRank;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@SuppressWarnings("unused")
public final class ConfigHandler {

    private final Config config = new Config();

    public ConfigHandler(DragonRank plugin) {
        FileConfiguration fileConfiguration = plugin.getConfig();

        config.setDragon(fileConfiguration.getBoolean("dragon_keeper"));
        config.setWorld(plugin.getServer().getWorld(fileConfiguration.getString("world_name")));
        List<Integer> xyz = (List<Integer>) fileConfiguration.getList("egg_location");
        config.setLocation(new Location(config.getWorld(), xyz.get(0), xyz.get(1), xyz.get(2)));
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
}
