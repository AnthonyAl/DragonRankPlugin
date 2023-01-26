package com.unipi.alexandris.minecraftplugin.dragonrankplugin;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers.CommandsHandler;
import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers.ConfigHandler;
import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers.EventsHandler;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.UltraPermissionsAPI;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DragonRank extends JavaPlugin {

    public ConfigHandler config;

    private Player DRAGON;
    private double[] originalAttributeValues;

    private UltraPermissionsAPI upAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        upAPI = UltraPermissions.getAPI();

        config = new ConfigHandler(this);
        Objects.requireNonNull(getCommand("dragon_keeper")).setExecutor(new CommandsHandler(this));
        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Player getDRAGON() {
        return DRAGON;
    }

    public void setDRAGON(Player DRAGON) {
        this.DRAGON = DRAGON;
    }

    public double[] getOriginalAttributeValues() {
        return originalAttributeValues;
    }

    public void setOriginalAttributeValues(double[] originalAttributeValues) {
        this.originalAttributeValues = originalAttributeValues;
    }

    public UltraPermissionsAPI getUpAPI() {
        return upAPI;
    }

    public void setUpAPI(UltraPermissionsAPI upAPI) {
        this.upAPI = upAPI;
    }
}
