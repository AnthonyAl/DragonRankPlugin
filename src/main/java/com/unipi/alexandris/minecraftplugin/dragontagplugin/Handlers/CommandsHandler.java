package com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands.*;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Utils;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class CommandsHandler implements TabExecutor {

    private final HashMap<String, SubCommand> commands = new HashMap<>();
    private final DragonTag plugin;

    public CommandsHandler(DragonTag plugin) {
        this.plugin = plugin;
        commands.put("help", new HelpCmd(this, Utils.prefix));
        commands.put("reload", new ReloadCmd(plugin, Utils.prefix));
        commands.put("current", new CurrentCmd(plugin, Utils.prefix));
        commands.put("scheduler", new SchedulerCmd(plugin, Utils.prefix));
        commands.put("reset", new ResetCmd(plugin, Utils.prefix));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.prefix + ChatColor.AQUA + "DragonTag" + ChatColor.GRAY + " is a plugin developed by Antitonius " +
                    "to implement a Dragon Egg Tag game and the Dragon Keeper Rank. The plugin was developed for play.theNRK.net. To check the help page, type "
                    + ChatColor.YELLOW + "/dragontag help" + ChatColor.GRAY + ".");
            return true;
        }

        SubCommand command = commands.get(args[0].toLowerCase());

        if (command == null) {
            sender.sendMessage(Utils.prefix + ChatColor.RED + "Unknown command. To check out the help page, type " + ChatColor.GRAY + "/dragontag help" + ChatColor.RED + ".");
            return true;
        }

        if(!sender.hasPermission(command.getPermission())) {
            sender.sendMessage(Utils.prefix + ChatColor.RED + "You do not fulfill the required permissions to use this command.");
            return true;
        }

        if (command.inGameOnly() && !(sender instanceof Player)) {
            sender.sendMessage(Utils.prefix + ChatColor.RED + "Console cannot run this command.");
            return true;
        }

        String[] subCmdArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCmdArgs, 0, subCmdArgs.length);

        if (!command.onCommand(sender, subCmdArgs)) {
            sender.sendMessage(Utils.prefix + ChatColor.RED + "Command usage: /dragontag " + ChatColor.GRAY + command.getUsage() + ChatColor.RED + ".");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        //create new array
        final List<String> completions = new ArrayList<>();

        //create arrays of allowed commands
        List<String> commands = new ArrayList<>();
        if(args.length == 1) {
            commands = new ArrayList<>();
            for(String cmd : this.commands.keySet().stream().toList())
                if(sender.hasPermission(this.commands.get(cmd).getPermission()))
                    commands.add(cmd);
        }
        else if(args.length == 2) {
            commands = new ArrayList<>();
            SubCommand sb = this.commands.get(args[0]);
            if(!sb.getSubPermissions().isEmpty())
                for(String cmd : sb.getSubPermissions().keySet().stream().toList())
                    if(sender.hasPermission(sb.getSubPermissions().get(cmd)))
                        commands.add(cmd);
        }

        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        if(args.length == 1)
            StringUtil.copyPartialMatches(args[0], commands, completions);
        else if(args.length == 2 && Objects.equals(args[0], "current")) {
            //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if(args.length == 3 && Objects.equals(args[0], "current") && Objects.equals(args[1], "assign")
                && sender.hasPermission("dragontag.admin")) {
            List<String> players = new ArrayList<>();
            for(Player p : plugin.getServer().getOnlinePlayers())
                players.add(p.getName());

            //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
            StringUtil.copyPartialMatches(args[2], players, completions);
        }

        //sort the list
        Collections.sort(completions);
        return completions;
    }

    public Collection<SubCommand> getCommands() {
        return commands.values();
    }
}
