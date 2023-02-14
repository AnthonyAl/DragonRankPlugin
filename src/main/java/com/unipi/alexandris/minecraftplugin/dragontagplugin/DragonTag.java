package com.unipi.alexandris.minecraftplugin.dragontagplugin;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Utils;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers.CommandsHandler;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers.ConfigHandler;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers.EventsHandler;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.UltraPermissionsAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class DragonTag extends JavaPlugin {

    private static DragonTag plugin;

    public boolean EGG_SPAWNED = false;

    public ConfigHandler config;

    private UUID DRAGON = null;

    private long DRAGONTime = 0;

    private UltraPermissionsAPI upAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        saveDefaultConfig();

        upAPI = UltraPermissions.getAPI();

        config = new ConfigHandler(this);
        Objects.requireNonNull(getCommand("dragontag")).setExecutor(new CommandsHandler(this));
        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);

        List<String> data = Utils.loadData();
        if (data.size() == 1) {
            getServer().getLogger().warning(data.get(0));
            DRAGON = null;
            EGG_SPAWNED = Objects.equals(data.get(0), "t");
        }
        else if(data.size() == 2) {
            DRAGON = UUID.fromString(data.get(0));
            EGG_SPAWNED = Objects.equals(data.get(1), "t");
        }

        Utils.loadScheduledCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public UUID getDRAGON() {
        return DRAGON;
    }

    public void setDRAGON(UUID DRAGON) {
        this.DRAGON = DRAGON;
    }

    public UltraPermissionsAPI getUpAPI() {
        return upAPI;
    }

    public void setUpAPI(UltraPermissionsAPI upAPI) {
        this.upAPI = upAPI;
    }

    @SuppressWarnings("unused")
    public long getDRAGONTime() {
        return DRAGONTime;
    }

    public static DragonTag getPlugin() {
        return plugin;
    }
}
