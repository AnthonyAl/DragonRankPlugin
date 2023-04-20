package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Utils;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ResetCmd implements SubCommand{

    private final DragonTag plugin;
    private final String prefix;

    public ResetCmd(DragonTag plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        try {
            Player p = plugin.getServer().getPlayer(plugin.getDRAGON());

            if(p == null) {
                if(plugin.config.isOffline_mode()) {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(plugin.getDRAGON());
                    if(offlinePlayer == null) {
                        sender.sendMessage(prefix + ChatColor.YELLOW + "Invalid player.");
                        return true;
                    }
                    Utils.schedule_command(offlinePlayer, "remove");
                    sender.sendMessage(prefix + ChatColor.YELLOW + "Player was offline, scheduling command.");
                }
                else if (sender instanceof Player) {
                    sender.sendMessage(prefix + ChatColor.YELLOW + "ONLINE MODE: " + ChatColor.WHITE + "The requested player must be online.");
                    return true;
                }
            }
            else {
                Utils.remove(p, sender);
            }
        }
        catch(Exception e) {
            if (sender instanceof Player) {
                sender.sendMessage(prefix + ChatColor.RED + "A fatal error has occurred! Check the console for details.");
                sender.sendMessage(prefix + ChatColor.RED + "Abborting command.");
                return true;
            }
            plugin.getServer().getLogger().severe("[DragonTag] " + e.getMessage());
        }
        plugin.EGG_SPAWNED = false;
        plugin.setDRAGON(null);

        List<UUID> importantSchedules = Utils.getScheduledCommands("remove");
        sender.sendMessage(prefix + ChatColor.YELLOW + "Retrieved important scheduled commands.");
        sender.sendMessage(prefix + ChatColor.YELLOW + "Killing Tag Data.");
        Utils.killData();
        sender.sendMessage(prefix + ChatColor.YELLOW + "Killing Scheduler Data.");
        Utils.killScheduler();

        sender.sendMessage(prefix + ChatColor.YELLOW + "Rescheduling important commands.");
        for(UUID uuid : importantSchedules)
            Utils.schedule_command(plugin.getServer().getOfflinePlayer(uuid), "remove");

        sender.sendMessage(prefix + ChatColor.YELLOW + "Plugin reset completed.");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return null;
    }

    @Override
    public String getUsage() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Forcefully reset the plugin's data.";
    }

    @Override
    public String getPermission() {
        return "dragontag.admin";
    }
}
