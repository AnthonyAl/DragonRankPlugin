package com.unipi.alexandris.minecraftplugin.dragontagplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragontagplugin.Core.Utils;
import com.unipi.alexandris.minecraftplugin.dragontagplugin.DragonTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public final class EventsHandler implements Listener {

    private final DragonTag plugin;

    private final TextComponent component_prefix = Component.text("[", NamedTextColor.GOLD)
            .append(Component.text("Broadcast", NamedTextColor.DARK_RED))
            .append(Component.text("] ", NamedTextColor.GOLD));

    private final TextComponent component_egg_despawn = component_prefix.append(Component.text("The ", NamedTextColor.RED))
            .append(Component.text("DRAGON EGG ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("is obtainable in the END!", NamedTextColor.RED));

    private final TextComponent component_lost_natural = component_prefix.append(Component.text("The ", NamedTextColor.RED))
            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("has perished! The egg was thus summoned back to the End.", NamedTextColor.RED));

    private final TextComponent component_dropped = component_prefix.append(Component.text("The ", NamedTextColor.RED))
            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("has given up the egg! It has returned in the END!", NamedTextColor.RED));


    public EventsHandler(DragonTag plugin) {
        this.plugin = plugin;

        if(plugin.config.isVoid_protection()) new BukkitRunnable() {

            @Override
            public void run() {
                for(Entity e : plugin.config.getWorld().getEntitiesByClasses(Item.class, FallingBlock.class)) {
                    if(e instanceof Item item) {
                        if(item.getItemStack().getType() == Material.DRAGON_EGG) {
                            if(item.getLocation().getY() < 0) {
                                item.remove();

                                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_egg_despawn));

                                plugin.EGG_SPAWNED = false;
                            }
                        }
                    }
                    else if(e instanceof FallingBlock fb) {
                        if(fb.getBlockData().getMaterial() == Material.DRAGON_EGG) {
                            if(fb.getLocation().getY() < 0) {
                                fb.remove();

                                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_egg_despawn));

                                plugin.EGG_SPAWNED = false;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggMove(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p)
            if(p.hasPermission("op")) return;
        try {
            if (e.getCurrentItem().getType() == Material.DRAGON_EGG) {
                if(e.getAction() != InventoryAction.DROP_ONE_SLOT) {
                    if(e.getWhoClicked() instanceof Player p) Utils.passPerks(p);
                    e.setCancelled(true);
                }
            }
            if(e.getInventory().contains(Material.DRAGON_EGG))
                if(e.getWhoClicked() instanceof Player p) {
                    if(p.getInventory().getItemInOffHand().getType() == Material.DRAGON_EGG) {

                        p.getInventory().setItemInOffHand(p.getInventory().getItem(9));
                        p.getInventory().setItem(9, new ItemStack(Material.DRAGON_EGG, 1));
                    }

                    try {
                        if (p.getInventory().getItem(9).getType() != Material.DRAGON_EGG) {
                            int i = p.getInventory().first(Material.DRAGON_EGG);
                            p.getInventory().setItem(i, p.getInventory().getItem(9));
                            p.getInventory().setItem(9, new ItemStack(Material.DRAGON_EGG, 1));
                        }
                    }
                    catch(NullPointerException ignored) {
                        p.getInventory().remove(Material.DRAGON_EGG);
                        p.getInventory().setItem(9, new ItemStack(Material.DRAGON_EGG, 1));
                    }
                }
        }
        catch(NullPointerException ignored) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggRightClick(PlayerInteractEvent e) {
        try {
            if (e.getItem().getType() == Material.DRAGON_EGG) {
                e.setCancelled(true);
            }
        }
        catch(NullPointerException ignored) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggDespawn(ItemDespawnEvent e) {
        if(e.getEntity().getType() == EntityType.DROPPED_ITEM) {
            if (e.getEntity().getItemStack().getType() == Material.DRAGON_EGG) {

                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_egg_despawn));

                if(plugin.config.isPlace_crystals()) Utils.fountain_crystallize();
                plugin.EGG_SPAWNED = false;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noDragonEggDie(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM) {
            Item i = (Item) event.getEntity();
            if(i.getItemStack().getType() == Material.DRAGON_EGG) {
                i.setInvulnerable(true);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggDropByDeath(PlayerDeathEvent e) {
        for(ItemStack is : e.getDrops()) {
            if(is.getType() == Material.DRAGON_EGG) {

                if(!plugin.config.isEgg_drop()) e.getDrops().remove(is);

                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_lost_natural));

                Utils.DRAGON_CLEAR(e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggDropByChoice(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.DRAGON_EGG) {
            if(!plugin.config.isEgg_drop()) e.getItemDrop().remove();

            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_dropped));

            Utils.DRAGON_CLEAR(e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnderDragonDeath(EntityDeathEvent e) {
        if(plugin.getDRAGON() == null && e.getEntity() instanceof EnderDragon && !plugin.config.isEgg_drop() && !plugin.EGG_SPAWNED) {
            Location loc = plugin.config.getLocation();
            loc.getBlock().setType(Material.DRAGON_EGG);
            plugin.EGG_SPAWNED = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggPlayerPickup(EntityPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if(!(e.getEntity() instanceof Player)) e.setCancelled(true);

            Player player = (Player) e.getEntity();
            player.getInventory().remove(Material.DRAGON_EGG);
            try {
                if (player.getInventory().getItem(9).getType() != Material.DRAGON_EGG) {
                    int i = player.getInventory().firstEmpty();
                    player.getInventory().setItem(i, player.getInventory().getItem(9));
                    player.getInventory().setItem(9, new ItemStack(Material.DRAGON_EGG));
                    e.setCancelled(true);
                }
            }
            catch(NullPointerException ignored) {
                player.getInventory().remove(Material.DRAGON_EGG);
                player.getInventory().setItem(9, new ItemStack(Material.DRAGON_EGG));
                e.setCancelled(true);
            }

            TextComponent component = component_prefix.append(Component.text(player.getName() + " has awakened as the ", NamedTextColor.RED))
                    .append(Component.text("DRAGON KEEPER!").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true));
            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component));

            plugin.EGG_SPAWNED = false;
            Utils.DRAGON_REASSIGN(player);

            e.getItem().remove();
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonEggPickup(InventoryPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if (e.getInventory().getType() != InventoryType.PLAYER &&
                    e.getInventory().getType() != InventoryType.CREATIVE) e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonKeeperTravel(PlayerChangedWorldEvent e) {
        if(e.getPlayer().getUniqueId() == plugin.getDRAGON()) {
            String world = "OVER WORLD";
            if(e.getPlayer().getWorld().getName().equals("world_nether"))
                world = "NETHER DIMENSION";
            if(e.getPlayer().getWorld().getName().equals("world_the_end"))
                world = "END DIMENSION";

            List<Integer> borders = plugin.config.getBorders();
            float multiplier = 1;
            if(e.getPlayer().getWorld().getName().equals("world_nether"))
                multiplier = 8;

            String loc = ", over " + Math.round(borders.get(0) / multiplier) + " blocks away from the center!";

            for(int i : borders) {
                if(e.getPlayer().getLocation().getX() <= i &&
                        e.getPlayer().getLocation().getZ() <= i)
                    loc = ", within " + Math.round(i / multiplier) + " blocks away from the center!";
                else break;
            }
            if(!plugin.config.isSpoil_location()) loc = "!";

            TextComponent component = component_prefix.append(Component.text("The ", NamedTextColor.RED))
                    .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("has escaped to the " + world + loc, NamedTextColor.RED));
            if(plugin.config.isSpoil_world()) Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        List<String> scheduled_commands = new ArrayList<>();
        String command = Utils.getScheduledCommand(player.getUniqueId());
        while(command != null) {
            scheduled_commands.add(0, command);
            Utils.removeScheduledCommand(player.getUniqueId(), command);
            command = Utils.getScheduledCommand(player.getUniqueId());
        }

       for(String c : scheduled_commands) {
            switch (c) {
                case "reset" -> Utils.reset(player, null);
                case "remove" -> Utils.remove(player, null);
                case "assign" -> Utils.assign(player, null);
            }
        }
    }

}
