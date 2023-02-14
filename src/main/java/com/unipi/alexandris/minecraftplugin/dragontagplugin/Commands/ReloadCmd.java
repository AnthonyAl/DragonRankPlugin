package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadCmd implements SubCommand{

    private final DragonTag plugin;
    private final String prefix;

    public ReloadCmd(DragonTag plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(prefix + "Reloading Dragon Egg Keeper.. .  . ");
        plugin.reloadConfig();
        plugin.config = new ConfigHandler(plugin);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
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

    @Override
    public String getPermission() {
        return "dragontag.admin";
    }
}
