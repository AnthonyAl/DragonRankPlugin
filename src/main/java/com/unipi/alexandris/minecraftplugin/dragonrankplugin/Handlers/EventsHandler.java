package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.DragonRank;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Random;

public final class EventsHandler implements Listener {

    private final DragonRank plugin;

    private final TextComponent component_prefix = Component.text("[", NamedTextColor.GOLD)
            .append(Component.text("Broadcast", NamedTextColor.DARK_RED))
            .append(Component.text("] ", NamedTextColor.GOLD));

    private final TextComponent component_generic_lost = component_prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("has lost the egg! It must have returned to the END!", NamedTextColor.DARK_RED));

    private final TextComponent component_egg_despawn = component_prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
            .append(Component.text("DRAGON EGG ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("is obtainable in the END!", NamedTextColor.DARK_RED));

    private final TextComponent component_lost_natural = component_prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("has perished! The egg was thus summoned back to the End.", NamedTextColor.DARK_RED));

    private final TextComponent component_dropped = component_prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
            .append(Component.text("has given up the egg! It has returned in the END!", NamedTextColor.DARK_RED));


    public EventsHandler(DragonRank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDragonEggMove(InventoryClickEvent e) {
        try {
            if (e.getInventory().getType() != InventoryType.PLAYER &&
                e.getInventory().getType() != InventoryType.CREATIVE)
                if (e.getCurrentItem().getType() == Material.DRAGON_EGG) {
                    e.setCancelled(true);
                }
            if(e.getInventory().getHolder() instanceof Player p) {
                if(p.getInventory().getItemInOffHand().getType() == Material.DRAGON_EGG) {
                    e.setCancelled(true);
                }
            }
        }
        catch(NullPointerException ignored) {}
    }

    @EventHandler
    public void inventoryOpenChecker(InventoryOpenEvent e) {
        if (e.getPlayer().getInventory().getItemInOffHand().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
        }
        if(e.getPlayer().getEnderChest().contains(Material.DRAGON_EGG)) {
            e.getPlayer().getEnderChest().remove(Material.DRAGON_EGG);

            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_generic_lost));

            try {
                DRAGON_CLEAR((Player) e.getPlayer());
            } catch (InputMismatchException ignored) {
                if(plugin.config.isPlace_crystals()) fountain_crystallize();
            }
        }
        if(!e.getPlayer().getInventory().contains(Material.DRAGON_EGG)) {

            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_generic_lost));

            try {
                DRAGON_CLEAR((Player) e.getPlayer());
            } catch (InputMismatchException ignored) {
                if(plugin.config.isPlace_crystals()) fountain_crystallize();
            }
        }
    }

    @EventHandler
    public void interactChecker(PlayerInteractEvent e) {
        if(plugin.getDRAGON() == null) return;
        if(plugin.getDRAGON().equals(e.getPlayer())) {
            if(!e.getPlayer().getInventory().contains(Material.DRAGON_EGG)) {
                DRAGON_CLEAR(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onDragonEggPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragonEggRightClick(PlayerInteractEvent e) {
        if(e.getItem().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragonEggDespawn(ItemDespawnEvent e) {
        if(e.getEntity().getType() == EntityType.DROPPED_ITEM) {
            if (e.getEntity().getItemStack().getType() == Material.DRAGON_EGG) {

                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_egg_despawn));

                if(plugin.config.isPlace_crystals()) fountain_crystallize();
            }
        }
    }

    @EventHandler
    public void noDragonEggDie(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM) {
            Item i = (Item) event.getEntity();
            if(i.getItemStack().getType() == Material.DRAGON_EGG) {
                i.setInvulnerable(true);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDragonEggDropByDeath(PlayerDeathEvent e) {
        for(ItemStack is : e.getDrops()) {
            if(is.getType() == Material.DRAGON_EGG) {

                if(!plugin.config.isEgg_drop()) e.getDrops().remove(is);

                Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_lost_natural));

                DRAGON_CLEAR(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onDragonEggDropByChoice(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.DRAGON_EGG) {
            if(!plugin.config.isEgg_drop()) e.getItemDrop().remove();

            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component_dropped));

            DRAGON_CLEAR(e.getPlayer());
        }
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent e) {
        if(plugin.getDRAGON() == null && e.getEntity() instanceof EnderDragon && !plugin.config.isEgg_drop()) {
            Location loc = plugin.config.getLocation();
            loc.getBlock().setType(Material.DRAGON_EGG);
        }
    }

    @EventHandler
    public void onDragonEggPlayerPickup(EntityPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if(!(e.getEntity() instanceof Player)) e.setCancelled(true);

            Player player = (Player) e.getEntity();

            TextComponent component = component_prefix.append(Component.text(player.getName() + " has awakened as the ", NamedTextColor.DARK_RED))
                    .append(Component.text("DRAGON KEEPER!").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true));
            Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component));

            DRAGON_REASSIGN(player);
        }
    }

    @EventHandler
    public void onDragonEggPickup(InventoryPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if (e.getInventory().getType() != InventoryType.PLAYER &&
                    e.getInventory().getType() != InventoryType.CREATIVE) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragonKeeperTravel(PlayerChangedWorldEvent e) {
        if(e.getPlayer() == plugin.getDRAGON()) {
            String world = "OVER WORLD";
            if(e.getPlayer().getWorld().getName().equals("world_nether"))
                world = "NETHER DIMENSION";
            if(e.getPlayer().getWorld().getName().equals("world_the_end"))
                world = "END DIMENSION";

            String loc = ", over 25 thousand blocks away from center!";
            if(e.getPlayer().getLocation().getX() <= 25000 &&
                    e.getPlayer().getLocation().getZ() <= 25000)
                loc = ", within 25 thousand blocks away from center!";
            if(!plugin.config.isSpoil_location()) loc = "!";

            TextComponent component = component_prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
                    .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("has escaped to the " + world + loc, NamedTextColor.DARK_RED));
            if(plugin.config.isSpoil_world()) Bukkit.getServer().getOnlinePlayers().forEach(pl -> pl.sendMessage(component));
        }
    }



    private void DRAGON_CLEAR(Player player) {
        revokePerks(player);
        removeDragon(player);

        if(plugin.config.isPlace_crystals()) fountain_crystallize();
    }

    private void DRAGON_REASSIGN(Player player) {
        if(plugin.getDRAGON() != null) {
            revokePerks(player);
            removeDragon(player);
        }
        passPerks(player);
        assignDragon(player);
    }


    private void fountain_crystallize() {
        Random r = new Random();
        Location base_loc = plugin.config.getLocation();
        Location crys1 = new Location(base_loc.getWorld(), base_loc.getX(), base_loc.getY() - 3, base_loc.getZ() + 3);
        Location crys2 = new Location(base_loc.getWorld(), base_loc.getX(), base_loc.getY() - 3, base_loc.getZ() - 3);
        Location crys3 = new Location(base_loc.getWorld(), base_loc.getX() + 3, base_loc.getY() - 3, base_loc.getZ());

        if(r.nextInt(100) < 65) crys1.getBlock().setType(Material.END_CRYSTAL);
        if(r.nextInt(100) < 35) crys2.getBlock().setType(Material.END_CRYSTAL);
        if(r.nextInt(100) < 5) crys3.getBlock().setType(Material.END_CRYSTAL);
    }

    private void passPerks(Player p) {
        for(PotionEffectType type : plugin.config.getEffects().keySet()) {
            if(plugin.config.getEffects().get(type) > 0)
                p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, plugin.config.getEffects().get(type) -1, false, false));
        }


    }

    private void revokePerks(Player p) {
       for(PotionEffectType type : plugin.config.getEffects().keySet()) p.removePotionEffect(type);
    }

    private void assignDragon(Player player) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(player.getUniqueId());
        user.ifPresent(value -> value.setPrefix("&8<&r{#4d0057>}D{#4d0057<}{#480053>}r{#480053<}{#42014f>}a{#42014f<}{#3d014c>}g{#3d014c<}{#380248>}o{#380248<}{#320244>}n{#320244<}{#2d0340>}K{#2d0340<}{#27033c>}e{#27033c<}{#220438>}e{#220438<}{#1d0435>}p{#1d0435<}{#170531>}e{#170531<}{#12052d>}r{#12052d<}&8>&r"));
        user.ifPresent(value -> value.setSuffix("&8<&r{#4d0057>}D{#4d0057<}{#480053>}r{#480053<}{#42014f>}a{#42014f<}{#3d014c>}g{#3d014c<}{#380248>}o{#380248<}{#320244>}n{#320244<}&8>"));

        plugin.setDRAGON(player);
    }

    private void removeDragon(Player player) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(player.getUniqueId());
        user.ifPresent(value -> value.setPrefix(null));
        user.ifPresent(value -> value.setSuffix(null));

        plugin.setDRAGON(null);
    }

}
