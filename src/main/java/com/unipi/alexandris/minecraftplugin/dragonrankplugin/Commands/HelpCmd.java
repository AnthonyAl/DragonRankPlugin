package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers.CommandsHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HelpCmd implements SubCommand {

    private final CommandsHandler cmdHandler;

    public HelpCmd(CommandsHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Command list:");

        for (SubCommand cmd : cmdHandler.getCommands()) {
            if (cmd.inGameOnly() && !(sender instanceof Player)) {
                continue;
            }

            sender.sendMessage(ChatColor.GRAY + "  -" + ChatColor.AQUA + "/dragon_keeper " + cmd.getUsage() + ChatColor.GRAY + " - " + cmd.getDescription());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
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
}
