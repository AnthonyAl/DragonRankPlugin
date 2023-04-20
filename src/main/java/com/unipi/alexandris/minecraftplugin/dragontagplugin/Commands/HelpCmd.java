package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers.CommandsHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class HelpCmd implements SubCommand {

    private final CommandsHandler cmdHandler;
    private final String prefix;

    public HelpCmd(CommandsHandler cmdHandler, String prefix) {
        this.cmdHandler = cmdHandler;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(prefix + "Command list:");

        for (SubCommand cmd : cmdHandler.getCommands()) {
            if (cmd.inGameOnly() && !(sender instanceof Player))
                continue;
            if(!sender.hasPermission(cmd.getPermission()))
                continue;

            sender.sendMessage(ChatColor.GRAY + "  -" + ChatColor.AQUA + "/dragontag " + cmd.getUsage() + ChatColor.GRAY + " - " + cmd.getDescription());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows this page.";
    }

    @Override
    public String getPermission() {
        return "dragontag.access";
    }
}
