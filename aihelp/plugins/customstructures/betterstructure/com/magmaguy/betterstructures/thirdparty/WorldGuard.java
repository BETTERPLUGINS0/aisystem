/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.magmaguy.elitemobs.api.EliteMobDeathEvent
 *  com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity
 *  com.magmaguy.elitemobs.mobconstructor.custombosses.RegionalBossEntity
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.WorldGuard
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.Flags
 *  com.sk89q.worldguard.protection.flags.StateFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.flags.registry.FlagConflictException
 *  com.sk89q.worldguard.protection.flags.registry.FlagRegistry
 *  com.sk89q.worldguard.protection.managers.RegionManager
 *  com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  com.sk89q.worldguard.protection.regions.RegionContainer
 *  com.sk89q.worldguard.protection.regions.RegionQuery
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.thirdparty;

import com.magmaguy.betterstructures.buildingfitter.FitAnything;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.elitemobs.api.EliteMobDeathEvent;
import com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity;
import com.magmaguy.elitemobs.mobconstructor.custombosses.RegionalBossEntity;
import com.magmaguy.magmacore.util.Logger;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class WorldGuard
implements Listener {
    private static StateFlag BETTERSTRUCTURES_PROTECTED = null;
    private static final StateFlag.State allow = StateFlag.State.ALLOW;
    private static final StateFlag.State deny = StateFlag.State.DENY;

    public static void initializeFlag() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            return;
        }
        FlagRegistry registry = null;
        try {
            registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
        } catch (Exception ex) {
            Logger.warn("Something went wrong while loading WorldGuard. Are you using the right WorldGuard version?");
            return;
        }
        if (BETTERSTRUCTURES_PROTECTED != null) {
            Logger.info("Flag betterstructures-protect is already registered, this is normal if the plugin or server have just been reloaded.");
            return;
        }
        Bukkit.getLogger().info("[BetterStructures] Enabling flags:");
        try {
            BETTERSTRUCTURES_PROTECTED = new StateFlag("betterstructures-protect", false);
            registry.register((Flag)BETTERSTRUCTURES_PROTECTED);
            Bukkit.getLogger().info("[BetteStructures] - betterstructures-protect");
        } catch (FlagConflictException | IllegalStateException e) {
            Bukkit.getLogger().warning("[EliteMobs] Warning: flag betterstructures-protect already exists! This is normal if you've just now reloaded BetterStructures.");
            BETTERSTRUCTURES_PROTECTED = (StateFlag)registry.get("betterstructures-protect");
        }
    }

    public static ProtectedRegion generateProtectedRegion(FitAnything fitAnything, String regionName) {
        Location lowestCorner = fitAnything.getLocation().clone().add(fitAnything.getSchematicOffset());
        Location highestCorner = lowestCorner.clone().add(new Vector(fitAnything.getSchematicClipboard().getRegion().getWidth() - 1, fitAnything.getSchematicClipboard().getRegion().getHeight(), fitAnything.getSchematicClipboard().getRegion().getLength() - 1));
        BlockVector3 min = BlockVector3.at((double)lowestCorner.getX(), (double)lowestCorner.getY(), (double)lowestCorner.getZ());
        BlockVector3 max = BlockVector3.at((double)highestCorner.getX(), (double)highestCorner.getY(), (double)highestCorner.getZ());
        return new ProtectedCuboidRegion(regionName, min, max);
    }

    public static void Protect(Location lowestCorner, Location highestCorner, String bossFilename, Location spawnLocation) {
        BlockVector3 min = BlockVector3.at((double)lowestCorner.getX(), (double)lowestCorner.getY(), (double)lowestCorner.getZ());
        BlockVector3 max = BlockVector3.at((double)highestCorner.getX(), (double)highestCorner.getY(), (double)highestCorner.getZ());
        WorldGuard.Protect(min, max, bossFilename, spawnLocation);
    }

    public static void Protect(BlockVector3 corner1, BlockVector3 corner2, String bossFilename, Location spawnLocation) {
        RegionContainer regionContainer = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt((World)spawnLocation.getWorld()));
        BlockVector3 min = corner1;
        BlockVector3 max = corner2;
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(WorldGuard.regionIDGenerator(bossFilename, spawnLocation), min, max);
        region.setFlag((Flag)BETTERSTRUCTURES_PROTECTED, (Object)allow);
        region.setFlag((Flag)Flags.PASSTHROUGH, (Object)allow);
        regionManager.addRegion((ProtectedRegion)region);
    }

    public static void Unprotect(CustomBossEntity customBossEntity) {
        if (!customBossEntity.getCustomBossesConfigFields().isRemoveAfterDeath()) {
            return;
        }
        ProtectedRegion protectedRegion = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((World)customBossEntity.getLocation().getWorld())).getRegion(WorldGuard.regionIDGenerator(customBossEntity.getCustomBossesConfigFields().getFilename(), customBossEntity.getSpawnLocation()));
        if (protectedRegion == null) {
            return;
        }
        com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((World)customBossEntity.getLocation().getWorld())).removeRegion(WorldGuard.regionIDGenerator(customBossEntity.getCustomBossesConfigFields().getFilename(), customBossEntity.getSpawnLocation()));
    }

    public static boolean checkArea(Location location, Player player) {
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt((Location)location);
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(wgLocation);
        if (set.testState(null, new StateFlag[]{BETTERSTRUCTURES_PROTECTED})) {
            player.sendMessage(DefaultConfig.getRegionProtectedMessage());
            return true;
        }
        return false;
    }

    private static String regionIDGenerator(String bossFilename, Location spawnLocation) {
        return "betterstructures_autoprotected_" + bossFilename.replace(".yml", "") + "_" + spawnLocation.getBlockX() + "_" + spawnLocation.getBlockY() + "_" + spawnLocation.getBlockZ();
    }

    @EventHandler
    public void onEliteDeath(EliteMobDeathEvent event) {
        if (event.getEliteEntity() instanceof RegionalBossEntity) {
            WorldGuard.Unprotect((CustomBossEntity)event.getEliteEntity());
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (WorldGuard.checkArea(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WorldGuard.checkArea(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

