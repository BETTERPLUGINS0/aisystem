/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.zombie_striker.qav.hooks.worldguard;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.handler.IHandler;
import me.zombie_striker.qav.hooks.worldguard.implementation.IWorldGuardImplementation;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.WorldGuardImplementation;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.event.EventListener;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegionSet;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardWrapper
implements IWorldGuardImplementation {
    private static WorldGuardWrapper instance;
    private final IWorldGuardImplementation implementation;
    private final Listener listener;

    public static WorldGuardWrapper getInstance() {
        if (instance == null) {
            instance = new WorldGuardWrapper();
        }
        return instance;
    }

    private WorldGuardWrapper() {
        int n;
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            n = 7;
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
                n = 6;
            } catch (ClassNotFoundException classNotFoundException2) {
                n = -6;
            }
        }
        if (n == 6) {
            this.implementation = new me.zombie_striker.qav.hooks.worldguard.implementation.v6.WorldGuardImplementation();
            this.listener = new me.zombie_striker.qav.hooks.worldguard.implementation.v6.event.EventListener();
        } else if (n == -6) {
            this.implementation = new me.zombie_striker.qav.hooks.worldguard.implementation.legacy.WorldGuardImplementation();
            this.listener = new me.zombie_striker.qav.hooks.worldguard.implementation.legacy.event.EventListener();
        } else {
            this.implementation = new WorldGuardImplementation();
            this.listener = new EventListener();
        }
    }

    public void registerEvents(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this.listener, (Plugin)javaPlugin);
    }

    @Override
    public JavaPlugin getWorldGuardPlugin() {
        return this.implementation.getWorldGuardPlugin();
    }

    @Override
    public int getApiVersion() {
        return this.implementation.getApiVersion();
    }

    @Override
    public void registerHandler(Supplier<IHandler> supplier) {
        this.implementation.registerHandler(supplier);
    }

    @Override
    public <T> Optional<T> queryFlag(Player player, Location location, IWrappedFlag<T> iWrappedFlag) {
        return this.implementation.queryFlag(player, location, iWrappedFlag);
    }

    @Override
    public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player player, Location location) {
        return this.implementation.queryApplicableFlags(player, location);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String string, Class<T> clazz) {
        return this.implementation.getFlag(string, clazz);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(String string, Class<T> clazz, T t) {
        return this.implementation.registerFlag(string, clazz, t);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(String string, Class<T> clazz) {
        return this.implementation.registerFlag(string, clazz);
    }

    @Override
    public Optional<IWrappedRegion> getRegion(World world, String string) {
        return this.implementation.getRegion(world, string);
    }

    @Override
    public Map<String, IWrappedRegion> getRegions(World world) {
        return this.implementation.getRegions(world);
    }

    @Override
    public Set<IWrappedRegion> getRegions(Location location) {
        return this.implementation.getRegions(location);
    }

    @Override
    public Set<IWrappedRegion> getRegions(Location location, Location location2) {
        return this.implementation.getRegions(location, location2);
    }

    @Override
    public Optional<IWrappedRegionSet> getRegionSet(Location location) {
        return this.implementation.getRegionSet(location);
    }

    @Override
    public Optional<IWrappedRegion> addRegion(String string, List<Location> list, int n, int n2) {
        return this.implementation.addRegion(string, list, n, n2);
    }

    @Override
    public Optional<IWrappedRegion> addCuboidRegion(String string, Location location, Location location2) {
        return this.implementation.addCuboidRegion(string, location, location2);
    }

    @Override
    public Optional<IWrappedRegion> addRegion(String string, ISelection iSelection) {
        return this.implementation.addRegion(string, iSelection);
    }

    @Override
    public Optional<Set<IWrappedRegion>> removeRegion(World world, String string) {
        return this.implementation.removeRegion(world, string);
    }

    @Override
    public Optional<ISelection> getPlayerSelection(Player player) {
        return this.implementation.getPlayerSelection(player);
    }
}

