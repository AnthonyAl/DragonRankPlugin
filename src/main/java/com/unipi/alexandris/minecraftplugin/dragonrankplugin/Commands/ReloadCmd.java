package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.DragonRank;
import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers.ConfigHandler;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCmd implements SubCommand{

    private final DragonRank plugin;

    public ReloadCmd(DragonRank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Reloading Dragon Egg Keeper.. .  . ");
        plugin.reloadConfig();
        plugin.config = new ConfigHandler(plugin);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the config.yml for the plugin.";
    }
}
