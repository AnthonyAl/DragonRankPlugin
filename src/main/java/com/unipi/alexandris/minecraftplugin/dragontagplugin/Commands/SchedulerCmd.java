package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SchedulerCmd implements SubCommand{

    private DragonTag plugin;

    private String prefix;

    public SchedulerCmd(DragonTag plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        HashMap<String, String> subCommandPermissions = getSubPermissions();
        if(args.length > 0 && !sender.hasPermission(subCommandPermissions.get(args[0]))) {
            sender.sendMessage(prefix + ChatColor.RED + "You do not fulfill the required permissions to use this command.");
            return true;
        }

        if(args.length == 0) {
            if(plugin.config.isOffline_mode())
                sender.sendMessage(prefix + ChatColor.YELLOW + "/dt scheduler" + ChatColor.RESET + " is an experimental command that allows the admin to edit the scheduled " +
                        "commands assigned to offline players by the " + ChatColor.YELLOW + "/dt current <assign | remove | reset>" + ChatColor.RESET + " admin commands.");
            else
                sender.sendMessage(prefix + ChatColor.YELLOW + "This command is not available in online mode " + ChatColor.RESET + " (Switch offline_mode in DragonTag config).");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "scheduler";
    }

    @Override
    public String getDescription() {
        return "Shows and manages the scheduled commands for offline players.";
    }

    @Override
    public String getPermission() {
        return "dragontag.admin";
    }

    @Override
    public HashMap<String, String> getSubPermissions() {
        HashMap<String, String> subCommandPerms = new HashMap<>();
        subCommandPerms.put("get", "dragontag.admin");
        subCommandPerms.put("remove", "dragontag.admin");

        return subCommandPerms;
    }
}
