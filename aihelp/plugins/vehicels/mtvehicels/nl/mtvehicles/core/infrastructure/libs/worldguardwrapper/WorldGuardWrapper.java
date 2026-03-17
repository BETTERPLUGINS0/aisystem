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
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.IWorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.event.EventListener;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.WorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
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
        int targetVersion;
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            targetVersion = 7;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
                targetVersion = 6;
            } catch (ClassNotFoundException e1) {
                targetVersion = -6;
            }
        }
        if (targetVersion == 6) {
            this.implementation = new WorldGuardImplementation();
            this.listener = new nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.event.EventListener();
        } else if (targetVersion == -6) {
            this.implementation = new nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.WorldGuardImplementation();
            this.listener = new EventListener();
        } else {
            this.implementation = new nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.WorldGuardImplementation();
            this.listener = new nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.event.EventListener();
        }
    }

    public void registerEvents(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this.listener, (Plugin)plugin);
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
    public void registerHandler(Supplier<IHandler> arg0) {
        this.implementation.registerHandler(arg0);
    }

    @Override
    public <T> Optional<T> queryFlag(Player arg0, Location arg1, IWrappedFlag<T> arg2) {
        return this.implementation.queryFlag(arg0, arg1, arg2);
    }

    @Override
    public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player arg0, Location arg1) {
        return this.implementation.queryApplicableFlags(arg0, arg1);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String arg0, Class<T> arg1) {
        return this.implementation.getFlag(arg0, arg1);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(String arg0, Class<T> arg1, T arg2) {
        return this.implementation.registerFlag(arg0, arg1, arg2);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(String name, Class<T> type) {
        return this.implementation.registerFlag(name, type);
    }

    @Override
    public Optional<IWrappedRegion> getRegion(World arg0, String arg1) {
        return this.implementation.getRegion(arg0, arg1);
    }

    @Override
    public Map<String, IWrappedRegion> getRegions(World arg0) {
        return this.implementation.getRegions(arg0);
    }

    @Override
    public Set<IWrappedRegion> getRegions(Location arg0) {
        return this.implementation.getRegions(arg0);
    }

    @Override
    public Set<IWrappedRegion> getRegions(Location arg0, Location arg1) {
        return this.implementation.getRegions(arg0, arg1);
    }

    @Override
    public Optional<IWrappedRegionSet> getRegionSet(Location arg0) {
        return this.implementation.getRegionSet(arg0);
    }

    @Override
    public Optional<IWrappedRegion> addRegion(String arg0, List<Location> arg1, int arg2, int arg3) {
        return this.implementation.addRegion(arg0, arg1, arg2, arg3);
    }

    @Override
    public Optional<IWrappedRegion> addCuboidRegion(String id, Location point1, Location point2) {
        return this.implementation.addCuboidRegion(id, point1, point2);
    }

    @Override
    public Optional<IWrappedRegion> addRegion(String id, ISelection selection) {
        return this.implementation.addRegion(id, selection);
    }

    @Override
    public Optional<Set<IWrappedRegion>> removeRegion(World arg0, String arg1) {
        return this.implementation.removeRegion(arg0, arg1);
    }

    @Override
    public Optional<ISelection> getPlayerSelection(Player arg0) {
        return this.implementation.getPlayerSelection(arg0);
    }
}

