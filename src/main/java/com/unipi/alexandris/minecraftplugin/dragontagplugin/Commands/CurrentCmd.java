package com.unipi.alexandris.minecraftplugin.dragontagplugin.Commands;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Utils;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CurrentCmd implements SubCommand{


    private final DragonTag plugin;
    private final String prefix;

    public CurrentCmd(DragonTag plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        HashMap<String, String> subCommandPermissions = getSubPermissions();
        if(args.length > 0 && !sender.hasPermission(subCommandPermissions.get(args[0]))) {
            sender.sendMessage(Utils.prefix + ChatColor.RED + "You do not fulfill the required permissions to use this command.");
            return true;
        }

        if(args.length == 2 && Objects.equals(args[0], "assign")) {
            try {
                final Player p = plugin.getServer().getPlayer(args[1]);
                final Player dragon = plugin.getServer().getPlayer(plugin.getDRAGON());

                if(p == null || (dragon == null && plugin.getDRAGON() != null)) {
                    if(plugin.config.isOffline_mode()) {
                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                        if(offlinePlayer == null)
                            for(OfflinePlayer o : plugin.getServer().getOfflinePlayers())
                                if(Objects.equals(o.getName().toUpperCase(), args[1].toUpperCase())) offlinePlayer = o;
                        if(offlinePlayer == null) {
                            sender.sendMessage(prefix + ChatColor.YELLOW + "Invalid player.");
                            return true;
                        }
                        Utils.schedule_command(offlinePlayer, "assign");
                        sender.sendMessage(prefix + ChatColor.YELLOW + "Player was offline, scheduling command.");
                    }
                    else if (sender instanceof Player) {
                        sender.sendMessage(prefix + ChatColor.YELLOW + "ONLINE MODE: " + ChatColor.WHITE + "Both the requested player and the existing Dragon Keeper must be online.");
                        return true;
                    }
                }
                else {
                    Utils.assign(p, sender);
                }

            }
            catch(Exception e) {
                if (sender instanceof Player) {
                    sender.sendMessage(prefix + ChatColor.RED + "A fatal error has occurred! Check the console for details.");
                }
                plugin.getServer().getLogger().severe("[DragonTag] " + e.getMessage());
            }
        }


        else if (plugin.getDRAGON() == null) {
            sender.sendMessage(prefix + ChatColor.YELLOW + "There is no player internally registered as Dragon Keeper.");
            if(sender instanceof Player)
                plugin.getServer().getLogger().info("[DragonTag] There is no player internally registered as Dragon Keeper. If an online player still has the rank, this is possibly a bug.");
        }
        else {
            if (args.length == 0 || Objects.equals(args[0], "name")) {
                Player DRAGON = plugin.getServer().getPlayer(plugin.getDRAGON());
                OfflinePlayer ODRAGON = plugin.getServer().getOfflinePlayer(plugin.getDRAGON());
                if (sender instanceof Player) {
                    if(DRAGON != null) {
                        sender.sendMessage(prefix + ChatColor.BOLD + ChatColor.YELLOW + DRAGON.getName() +
                                ChatColor.RESET + " is the current" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Dragon Keeper" + ChatColor.RESET + "!");
                        plugin.getServer().getLogger().info("[DragonTag] The current DragonKeeper is: " + DRAGON.getName());
                    }
                    else {
                        sender.sendMessage(prefix + ChatColor.BOLD + ChatColor.YELLOW + ODRAGON.getName() +
                                ChatColor.RESET + " is the current" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Dragon Keeper" + ChatColor.RESET + "!");
                        plugin.getServer().getLogger().info("[DragonTag] The current DragonKeeper is: " + ODRAGON.getName());
                    }
                }
                else
                    if(DRAGON != null) sender.sendMessage(DRAGON.getName());
                    else sender.sendMessage(ODRAGON.getName());

            } else if (Objects.equals(args[0], "reset")) {
                try {
                    Player p = plugin.getServer().getPlayer(plugin.getDRAGON());

                    if (p == null) {
                        if(plugin.config.isOffline_mode()) {
                            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(plugin.getDRAGON());
                            if(offlinePlayer == null) {
                                sender.sendMessage(prefix + ChatColor.YELLOW + "Invalid player.");
                                return true;
                            }
                            Utils.schedule_command(offlinePlayer, "reset");
                            sender.sendMessage(prefix + ChatColor.YELLOW + "Player was offline, scheduling command.");
                        }
                        else if (sender instanceof Player) {
                            sender.sendMessage(prefix + ChatColor.YELLOW + "ONLINE MODE: " + ChatColor.WHITE + "The requested player must be online.");
                            return true;
                        }
                    } else {
                        Utils.reset(p, sender);
                    }
                }
                catch(Exception e) {
                    if (sender instanceof Player) {
                        sender.sendMessage(prefix + ChatColor.RED + "A fatal error has occurred! Check the console for details.");
                    }
                    plugin.getServer().getLogger().severe("[DragonTag] " + e.getMessage());
                }
            } else if (Objects.equals(args[0], "remove")) {
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
                    }
                    plugin.getServer().getLogger().severe("[DragonTag] " + e.getMessage());
                }

            } else if (Objects.equals(args[0], "prefix")) {
                if (sender instanceof Player) {
                    sender.sendMessage(prefix + Utils.DRAGON_PRE);
                    plugin.getServer().getLogger().info("[DragonTag] " + Utils.DRAGON_PRE);
                }
                else
                    sender.sendMessage(Utils.DRAGON_PRE);

            } else if (Objects.equals(args[0], "suffix")) {
                if (sender instanceof Player) {
                    sender.sendMessage(prefix + Utils.DRAGON_SUF);
                    plugin.getServer().getLogger().info("[DragonTag] " + Utils.DRAGON_SUF);
                }
                else
                    sender.sendMessage(Utils.DRAGON_SUF);

            }else if (Objects.equals(args[0], "time")) {
                if (sender instanceof Player) {
                    sender.sendMessage(prefix + ChatColor.YELLOW + "DRAGON TIME has not yet been implemented. Stay tuned for a future update!");
                }

            } else if (Objects.equals(args[0], "locate")) {
                if(plugin.getDRAGON() != null) {
                    Player p = plugin.getServer().getPlayer(plugin.getDRAGON());
                    if (p != null) {
                        String world = p.getWorld().getName();
                        world.toUpperCase().replaceAll("THE", "").replaceAll("__", "_").replaceAll("_", " ");
                        if(p.getWorld().getName().equals("world"))
                            world = "OVER WORLD";
                        if(p.getWorld().getName().equals("world_nether"))
                            world = "NETHER DIMENSION";
                        if(p.getWorld().getName().equals("world_the_end"))
                            world = "END DIMENSION";

                        double multiplier = p.getWorld().getCoordinateScale();

                        String loc = Utils.calc_coords(p, multiplier);
                        if (!plugin.config.isSpoil_location()) loc = "!";

                        if (plugin.config.isSpoil_world())
                            sender.sendMessage(prefix + ChatColor.WHITE + "The current " + ChatColor.DARK_GRAY + ChatColor.BOLD + "Dragon Keeper " +
                                    ChatColor.RESET + "is located in the " + ChatColor.YELLOW + world + loc);
                        else
                            sender.sendMessage(prefix + ChatColor.YELLOW + "The Dragon Keeper's location is hidden!");
                    }
                    else {
                        sender.sendMessage(prefix + ChatColor.YELLOW + "The Dragon Keeper is not online.");
                    }
                }
                else {
                    sender.sendMessage(prefix + ChatColor.YELLOW + "There is no Dragon Keeper at this time. This is your chance to claim glory!");
                }
            } else if (Objects.equals(args[0], "effects")) {
                StringBuilder effects = new StringBuilder();
                Player p = plugin.getServer().getPlayer(plugin.getDRAGON());
                if(p == null) {
                    sender.sendMessage(prefix + ChatColor.YELLOW + "The requested player is not online.");
                    return true;
                }
                for(PotionEffect e : p.getActivePotionEffects())
                    effects.append(e.getType().getName()).append(" ");

                if (sender instanceof Player) {
                    if(effects.isEmpty()) {
                        sender.sendMessage(prefix + ChatColor.YELLOW + "The DragonKeeper has no Active Potion Effects.");
                        plugin.getServer().getLogger().info("[DragonTag] The DragonKeeper has no Active Potion Effects.");

                    }
                    else {
                        sender.sendMessage(prefix + "The DragonKeeper's Active Effects are: " + effects);
                        plugin.getServer().getLogger().info("[DragonTag] The DragonKeeper's Active Effects are: " + effects);
                    }
                }
                else
                    sender.sendMessage(String.valueOf(effects));

            } else {
                sender.sendMessage(ChatColor.RED + "Command usage: /dragontag " + ChatColor.GRAY + getUsage() + ChatColor.RED + ".");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "current";
    }

    @Override
    public String getDescription() {
        return "Returns the name of the current DragonKeeper if a player has the rank.";
    }

    @Override
    public String getPermission() {
        return "dragontag.info";
    }

    @Override
    public HashMap<String, String> getSubPermissions() {
        HashMap<String, String> subCommandPerms = new HashMap<>();
        subCommandPerms.put("name", "dragontag.info");
        //subCommandPerms.put("time", "dragontag.access");
        subCommandPerms.put("locate", "dragontag.info");
        subCommandPerms.put("prefix", "dragontag.access");
        subCommandPerms.put("suffix", "dragontag.access");
        subCommandPerms.put("effects", "dragontag.access");
        subCommandPerms.put("assign", "dragontag.admin");
        subCommandPerms.put("remove", "dragontag.admin");
        subCommandPerms.put("reset", "dragontag.admin");

        return subCommandPerms;
    }
}
