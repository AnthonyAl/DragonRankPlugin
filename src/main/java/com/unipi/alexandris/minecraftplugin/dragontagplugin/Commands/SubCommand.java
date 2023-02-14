package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public interface SubCommand {

    boolean onCommand(CommandSender sender, String[] args);

    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args);


        String getUsage();

    String getDescription();

    default boolean inGameOnly() {
        return false;
    }

    default String getPermission() {
        return "dragontag.info";
    }

    default HashMap<String, String> getSubPermissions() {
        return new HashMap<>();
    }
}
