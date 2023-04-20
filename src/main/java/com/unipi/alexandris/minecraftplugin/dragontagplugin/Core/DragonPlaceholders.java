package com.unipi.alexandris.minecraftplugin.dragontagplugin.Core;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DragonPlaceholders extends PlaceholderExpansion {

    private DragonTag plugin;

    public DragonPlaceholders(DragonTag plugin) {
        this.plugin = plugin;
    }

    public String onPlaceholderRequest(Player p, String identifier) {
        // placeholder: %dragontag_dragon_name%
        if (identifier.equals("dragon_name")) {
            String DRAGON;
            if(plugin.getDRAGON() == null) DRAGON = "No Player";
            else {
                Player player = plugin.getServer().getPlayer(plugin.getDRAGON());
                OfflinePlayer oplayer = plugin.getServer().getOfflinePlayer(plugin.getDRAGON());
                if(player == null) DRAGON = oplayer.getName();
                else DRAGON = player.getName();
            }

            return DRAGON;
        }
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dragontag";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Antitonius";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.1 - SNAPSHOT";
    }
}
