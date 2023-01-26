package com.unipi.alexandris.minecraftplugin.dragonrankplugin.Handlers;

import com.unipi.alexandris.minecraftplugin.dragonrankplugin.DragonRank;
import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public final class EventsHandler implements Listener {

    private final DragonRank plugin;
    private final InventoryType[] Inventories = {InventoryType.ANVIL, InventoryType.BARREL, InventoryType.BEACON, InventoryType.BREWING, InventoryType.BLAST_FURNACE, InventoryType.CARTOGRAPHY, InventoryType.CHEST, InventoryType.COMPOSTER, InventoryType.DISPENSER, InventoryType.DROPPER, InventoryType.ENCHANTING, InventoryType.ENDER_CHEST, InventoryType.FURNACE, InventoryType.GRINDSTONE, InventoryType.HOPPER, InventoryType.LECTERN, InventoryType.LOOM, InventoryType.MERCHANT, InventoryType.SHULKER_BOX, InventoryType.SMITHING, InventoryType.SMOKER, InventoryType.STONECUTTER};

    private final TextComponent prefix = Component.text("[", NamedTextColor.GOLD)
            .append(Component.text("Broadcast", NamedTextColor.DARK_RED))
            .append(Component.text("] ", NamedTextColor.GOLD));

    public EventsHandler(DragonRank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onDragonEggMove(InventoryClickEvent e) {
        try {
            if (Arrays.stream(Inventories).toList().contains((e.getInventory().getType())))
                if (e.getCurrentItem().getType() == Material.DRAGON_EGG) {
                    e.setCancelled(true);
                    return true;
                }
            return false;
        }
        catch(NullPointerException ex) {
            return false;
        }
    }

    @EventHandler
    public boolean onDragonEggPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
            return true;
        }
        return false;
    }

    @EventHandler
    public boolean onDragonEggDespawn(ItemDespawnEvent e) {
        if(e.getEntity().getType() == EntityType.DROPPED_ITEM) {
            if (e.getEntity().getItemStack().getType() == Material.DRAGON_EGG) {
                Location loc = plugin.config.getLocation();
                loc.getBlock().setType(Material.DRAGON_EGG);
                TextComponent component = prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
                        .append(Component.text("DRAGON EGG ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
                        .append(Component.text("has respawned in the END!", NamedTextColor.DARK_RED));
                plugin.getServer().broadcast(component);
                return true;
            }
        }
        return false;
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
    public boolean onDragonEggDropByDeath(PlayerDeathEvent e) {
        for(ItemStack is : e.getDrops()) {
            if(is.getType() == Material.DRAGON_EGG) {
                plugin.setDRAGON(null);
                if(e.getPlayer().getWorld() == plugin.config.getWorld()) {
                    e.getDrops().remove(is);
                    Location loc = plugin.config.getLocation();
                    loc.getBlock().setType(Material.DRAGON_EGG);
                    TextComponent component = prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
                            .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
                            .append(Component.text("was defeated in the END! The egg was thus summoned back.", NamedTextColor.DARK_RED));
                    plugin.getServer().broadcast(component);
                }
                removeDragon(e.getPlayer());
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public boolean OnDragonEggDropByChoice(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.DRAGON_EGG) {
            e.getItemDrop().remove();
            Location loc = plugin.config.getLocation();
            loc.getBlock().setType(Material.DRAGON_EGG);
            plugin.setDRAGON(null);
            TextComponent component = prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
                    .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("has given up the egg! It has returned in the END!", NamedTextColor.DARK_RED));
            plugin.getServer().broadcast(component);
            removeDragon(e.getPlayer());
            return true;
        }
        return false;
    }

    @EventHandler
    public boolean onDragonEggPlayerPickup(EntityPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if(!(e.getEntity() instanceof Player)) e.setCancelled(true);

            Player player = (Player) e.getEntity();
            plugin.setDRAGON(player);
            assignDragon(player);
            return true;
        }

        return false;
    }

    @EventHandler
    public boolean onDragonEggPickup(InventoryPickupItemEvent e) {
        if(e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
            if(Arrays.stream(Inventories).toList().contains((e.getInventory().getType()))) e.setCancelled(true);
            return true;
        }
        return false;
    }

    @EventHandler
    public boolean onDragonKeeperTravel(PlayerChangedWorldEvent e) {
        if(e.getPlayer() == plugin.getDRAGON()) {
            String world = "OVER WORLD!";
            if(e.getPlayer().getWorld().getName().equals("world_nether"))
                world = "NETHER DIMENSION!";
            if(e.getPlayer().getWorld().getName().equals("world_the_nether"))
                world = "END DIMENSION!";
            TextComponent component = prefix.append(Component.text("The ", NamedTextColor.DARK_RED))
                    .append(Component.text("DRAGON KEEPER ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("has escaped to the " + world, NamedTextColor.DARK_RED));
            plugin.getServer().broadcast(component);
            return true;
        }
        return false;
    }

    private void assignDragon(Player p) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(p.getUniqueId());
        user.ifPresent(value -> value.setPrefix("&8<{#4d0057>}D{#4d0057<}{#46004f>}r{#46004f<}{#3f0047>}a{#3f0047<}{#38003f>}g{#38003f<}{#310037>}o{#310037<}{#2a002f>}n{#2a002f<}&8>&r"));
        user.ifPresent(value -> value.setSuffix("&8<{#4d0057>}D{#4d0057<}{#46004f>}r{#46004f<}{#3f0047>}a{#3f0047<}{#38003f>}g{#38003f<}{#310037>}o{#310037<}{#2a002f>}n{#2a002f<}&8>&r"));

    }

    private void removeDragon(Player p) {
        if(plugin.getUpAPI() == null) plugin.setUpAPI(UltraPermissions.getAPI());
        Optional<User> user = plugin.getUpAPI().getUsers().uuid(p.getUniqueId());
        user.ifPresent(value -> value.setPrefix(null));
        user.ifPresent(value -> value.setSuffix(null));
    }

}