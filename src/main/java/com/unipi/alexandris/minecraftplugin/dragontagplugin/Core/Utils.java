package com.unipi.alexandris.minecraftplugin.dragontagplugin.Core;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Utils {

    private static final DragonTag plugin = DragonTag.getPlugin();

    private static final HashMap<UUID, String> scheduled_commands = loadScheduledCommands();

    public static final String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "DragonTag" + ChatColor.GRAY + "] ";

    public static final String DRAGON_PRE = "&8<&r{#4d0057>}D{#4d0057<}{#480053>}r{#480053<}{#42014f>}a{#42014f<}{#3d014c>}g{#3d014c<}{#380248>}o{#380248<}{#320244>}n{#320244<}{#2d0340>}K{#2d0340<}{#27033c>}e{#27033c<}{#220438>}e{#220438<}{#1d0435>}p{#1d0435<}{#170531>}e{#170531<}{#12052d>}r{#12052d<}&8>&r";
    public static final String DRAGON_SUF = "&8<&r{#4d0057>}D{#4d0057<}{#480053>}r{#480053<}{#42014f>}a{#42014f<}{#3d014c>}g{#3d014c<}{#380248>}o{#380248<}{#320244>}n{#320244<}&8>";

    public static void DRAGON_CLEAR(Player player) {
        revokePerks(player);
        removeDragon(player);

        if(plugin.config.isPlace_crystals()) fountain_crystallize();
    }

    public static void DRAGON_REASSIGN(Player player) {
        if(plugin.getDRAGON() != null) {
            revokePerks(player);
            removeDragon(player);
        }
        passPerks(player);
        assignDragon(player);

        saveData();
    }

    public static void fountain_crystallize() {
        Random r = new Random();
        Location base_loc = plugin.config.getLocation();
        Location crys1 = new Location(base_loc.getWorld(), base_loc.getX(), base_loc.getY() - 3, base_loc.getZ() + 3);
        Location crys2 = new Location(base_loc.getWorld(), base_loc.getX(), base_loc.getY() - 3, base_loc.getZ() - 3);
        Location crys3 = new Location(base_loc.getWorld(), base_loc.getX() + 3, base_loc.getY() - 3, base_loc.getZ());

        if(r.nextInt(100) < 65) {
            EnderCrystal EC1 = (EnderCrystal) base_loc.getWorld().spawnEntity(crys1.toCenterLocation(), EntityType.ENDER_CRYSTAL);
            EC1.setShowingBottom(false);
        }
        if(r.nextInt(100) < 35) {
            EnderCrystal EC2 = (EnderCrystal) base_loc.getWorld().spawnEntity(crys2.toCenterLocation(), EntityType.ENDER_CRYSTAL);
            EC2.setShowingBottom(false);
        }
        if(r.nextInt(100) < 5) {
            EnderCrystal EC3 = (EnderCrystal) base_loc.getWorld().spawnEntity(crys3.toCenterLocation(), EntityType.ENDER_CRYSTAL);
            EC3.setShowingBottom(false);
        }
    }

    public static void passPerks(Player p) {
        for(PotionEffectType type : plugin.config.getEffects().keySet()) {
            if(plugin.config.getEffects().get(type) > -1)
                p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, plugin.config.getEffects().get(type), false, false));
        }


    }

    private static void revokePerks(Player p) {
        for(PotionEffectType type : plugin.config.getEffects().keySet()) p.removePotionEffect(type);
    }

    private static void assignDragon(Player player) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(player.getUniqueId());
        user.ifPresent(value -> value.setPrefix(DRAGON_PRE));
        user.ifPresent(value -> value.setSuffix(DRAGON_SUF));

        plugin.setDRAGON(player.getUniqueId());
    }

    private static void removeDragon(Player player) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(player.getUniqueId());
        user.ifPresent(value -> value.setPrefix(null));
        user.ifPresent(value -> value.setSuffix(null));
        if(plugin.getDRAGON() != null && player.getUniqueId() == plugin.getDRAGON())
            plugin.setDRAGON(null);
    }

    public static void assign(Player p, CommandSender sender) {
        String name = p.getName();
        Inventory i = p.getInventory();

        String message = "[DragonTag] Assigned the dragon rank to player " + name + "!";

        if(i.contains(Material.DRAGON_EGG)) {
            i.remove(Material.DRAGON_EGG);

            i.setItem(i.firstEmpty(), i.getItem(9));
            i.setItem(9, new ItemStack(Material.DRAGON_EGG));
        }
        else if(i.firstEmpty() == -1) {
            if(sender instanceof Player) sender.sendMessage(prefix + ChatColor.YELLOW + "This player has a full Inventory! They cannot pick up the Egg!");
            plugin.getServer().getLogger().severe("[DragonTag] ABORTING COMMAND The player " +
                    name + " had a full inventory while attempting to execute command /dragon_tag current assign " + name + ".");
            return;
        }
        else {
            i.setItem(i.firstEmpty(), i.getItem(9));
            i.setItem(9, new ItemStack(Material.DRAGON_EGG));
        }

        if(plugin.getDRAGON() != null) {
            Player dragon = plugin.getServer().getPlayer(plugin.getDRAGON());
            if(dragon != null) {
                message = "[DragonTag] Assigned the dragon rank to player " + name + "! Rank removed from previous Keeper: " + dragon.getName();

                Utils.DRAGON_CLEAR(dragon);

                Inventory dragon_i = dragon.getInventory();

                if (dragon_i.contains(Material.DRAGON_EGG)) {
                    dragon_i.remove(Material.DRAGON_EGG);
                }
            }
            else if(plugin.config.isOffline_mode()) {
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(plugin.getDRAGON());
                schedule_command(offlinePlayer, "remove");
            }
        }

        plugin.EGG_SPAWNED = false;
        Utils.DRAGON_REASSIGN(p);

        if(sender != null) sender.sendMessage(prefix + "The current DragonTag game got manually started!");
        if (sender == null || sender instanceof Player)
            plugin.getServer().getLogger().info(message);
    }

    public static void reset(Player p, CommandSender sender) {
        String name = p.getName();
        Inventory i = p.getInventory();

        Utils.DRAGON_CLEAR(p);

        if(i != null && i.contains(Material.DRAGON_EGG)) {
            Utils.DRAGON_REASSIGN(p);
            if (i.getItem(9).getType() != Material.DRAGON_EGG) {
                i.setItem(i.first(Material.DRAGON_EGG), i.getItem(9));
                i.setItem(9, new ItemStack(Material.DRAGON_EGG));
            }
        }

        //TODO - RESET TIME

        if (sender instanceof Player)
            sender.sendMessage(prefix + name + "'s Tag stats got reset.");
        plugin.getServer().getLogger().info("[DragonTag] The current DragonKeeper's stats were reset: " + name);
    }

    public static void remove(Player p, CommandSender sender) {
        String name = p.getName();
        Inventory i = p.getInventory();

        Utils.DRAGON_CLEAR(p);


        if(i.contains(Material.DRAGON_EGG)) {
            i.remove(Material.DRAGON_EGG);
        }

        //TODO - RESET TIME

        if (sender instanceof Player)
            sender.sendMessage(prefix + "The current DragonTag game got reset!");
        plugin.getServer().getLogger().info("[DragonTag] The current DragonTag game got reset! Rank removed from: " + name);
    }

    public static void schedule_command(OfflinePlayer offlinePlayer, String command) {
        List<String> accepted_commands = List.of(new String[]{"reset", "remove", "assign"});
        if(accepted_commands.contains(command)) {
            scheduled_commands.put(offlinePlayer.getUniqueId(), command);
        }
        saveScheduledCommands();
    }

    public static String getScheduledCommand(UUID key) {
        return scheduled_commands.get(key);
    }

    public static void removeScheduledCommand(UUID key, String command) {
        scheduled_commands.remove(key, command);
        saveScheduledCommands();
    }

    private static void saveScheduledCommands() {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(plugin.getDataFolder().getPath()+"/data/ScheduledCommands.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(scheduled_commands);

            out.close();
            file.close();

        } catch (FileNotFoundException e) {
            plugin.getServer().getLogger().severe("[DragonTag] File not found : " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            plugin.getServer().getLogger().severe("[DragonTag] Error while writing data : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static HashMap<UUID, String> loadScheduledCommands() {
        HashMap data = new HashMap<>();
        try {
            File file = new File(plugin.getDataFolder().getPath()+"/data/ScheduledCommands.ser");
            if(!file.exists()) {
                plugin.getServer().getLogger().warning("[DragonTag] No save file found to load scheduled commands from.");
                return data;
            }

            ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()));
            Object obj = in.readObject();
            in.close();
            data = (HashMap) obj;
            return data;

        } catch (IOException | ClassNotFoundException e) {
            plugin.getServer().getLogger().severe("[DragonTag] Error while retrieving scheduled command data: " + e.getMessage());
        }

        return data;

    }

    private static void saveData() {
        List<String> data = new ArrayList<>();
        if(plugin.getDRAGON() != null) {
            UUID uuid = plugin.getDRAGON();
            String s = uuid.toString();
            data.add(s);
        }
        if(plugin.EGG_SPAWNED)
            data.add("t");
        else
            data.add("f");

        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(plugin.getDataFolder().getPath()+"/data/TagData.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(data);

            out.close();
            file.close();

        } catch (FileNotFoundException e) {
            plugin.getServer().getLogger().severe("[DragonTag] File not found : " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            plugin.getServer().getLogger().severe("[DragonTag] Error while writing data : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<String> loadData() {
        List<String> data = new ArrayList<>();
        File dataF = new File(plugin.getDataFolder(), "data");

        if(!dataF.exists()) {
            dataF.mkdir();

            data.add("f");
        }
        data = new ArrayList<>();

        try {
            File file = new File(plugin.getDataFolder().getPath()+"/data/TagData.ser");
            if(!file.exists()) {
                plugin.getServer().getLogger().warning("[DragonTag] No save file found to load data from.");
                return data;
            }

            ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()));
            Object obj = in.readObject();
            in.close();
            data = (ArrayList<String>) obj;
            return data;

        } catch (IOException | ClassNotFoundException e) {
            plugin.getServer().getLogger().severe("[DragonTag] Error while retrieving data: " + e.getMessage());
        }

        return data;
    }
}
