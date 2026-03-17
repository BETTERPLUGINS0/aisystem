/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldguard.WorldGuard
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.flags.registry.FlagRegistry
 *  com.sk89q.worldguard.protection.managers.RegionManager
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.utils.wg;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.utils.nms.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class WorldGuardHelper {
    private static WorldGuardHelper instance = null;
    private final int worldGuardVersion;

    private WorldGuardHelper() {
        int n;
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            n = 7;
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
                n = 6;
            } catch (ClassNotFoundException classNotFoundException2) {
                throw new IllegalStateException("Could not detect WorldGuard version. Both v6 and v7 not found!");
            }
        }
        this.worldGuardVersion = n;
    }

    public static WorldGuardHelper getInstance() {
        if (instance == null) {
            instance = new WorldGuardHelper();
        }
        return instance;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    public FlagRegistry getFlagRegistry() {
        if (this.worldGuardVersion == 7) {
            return WorldGuard.getInstance().getFlagRegistry();
        }
        Object object = ReflectionUtil.callDeclaredMethod(this.getWorldGuard(), "getFlagRegistry", new Object[0]);
        if (object == null) {
            throw new UnsupportedOperationException("Detected WorldGuard v6.x, but could not find a required method.");
        }
        return (FlagRegistry)object;
    }

    public RegionManager getRegionManager(World world) {
        if (this.worldGuardVersion == 7) {
            return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((World)world));
        }
        Object object = ReflectionUtil.callDeclaredMethod(this.getWorldGuard(), "getRegionManager", world);
        if (object == null) {
            throw new UnsupportedOperationException("Detected WorldGuard v6.x, but could not find a required method.");
        }
        return (RegionManager)object;
    }

    public ApplicableRegionSet getApplicableRegionSet(Location location) {
        if (this.worldGuardVersion == 7) {
            return this.getRegionManager(location.getWorld()).getApplicableRegions(BlockVector3.at((double)location.getX(), (double)location.getY(), (double)location.getZ()));
        }
        Class<?> clazz = ReflectionUtil.getClass("com.sk89q.worldedit.Vector");
        if (clazz == null) {
            throw new UnsupportedOperationException("Detected WorldGuard v6.x, but could not find a required method.");
        }
        Object object = ReflectionUtil.callDeclaredConstructor(clazz, location.getX(), location.getY(), location.getZ());
        if (object == null) {
            throw new UnsupportedOperationException("Detected WorldGuard v6.x, but could not find a required method.");
        }
        Object object2 = ReflectionUtil.callDeclaredMethod(this.getRegionManager(location.getWorld()), "getApplicableRegions", clazz, object);
        if (object2 == null) {
            throw new UnsupportedOperationException("Detected WorldGuard v6.x, but could not find a required method.");
        }
        return (ApplicableRegionSet)object2;
    }

    public static boolean hasWorldGuard() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }

    @Generated
    public int getWorldGuardVersion() {
        return this.worldGuardVersion;
    }
}

