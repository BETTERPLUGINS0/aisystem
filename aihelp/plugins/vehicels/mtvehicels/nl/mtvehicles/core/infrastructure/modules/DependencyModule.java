/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 */
package nl.mtvehicles.core.infrastructure.modules;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import nl.mtvehicles.core.infrastructure.dependencies.PlaceholderUtils;
import nl.mtvehicles.core.infrastructure.dependencies.SkriptUtils;
import nl.mtvehicles.core.infrastructure.dependencies.VaultUtils;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import org.bukkit.Bukkit;

public class DependencyModule {
    private static DependencyModule instance;
    public static List<SoftDependency> loadedDependencies;
    public static WorldGuardUtils worldGuard;
    public static VaultUtils vault;
    public static PlaceholderUtils placeholderAPI;
    public static SkriptUtils skript;

    public DependencyModule() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                worldGuard = new WorldGuardUtils();
                loadedDependencies.add(SoftDependency.WORLD_GUARD);
            } catch (NoClassDefFoundError e) {
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading WorldGuard as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            try {
                vault = new VaultUtils();
                loadedDependencies.add(SoftDependency.VAULT);
            } catch (NoClassDefFoundError e) {
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Vault as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                placeholderAPI = new PlaceholderUtils();
                placeholderAPI.register();
                loadedDependencies.add(SoftDependency.PLACEHOLDER_API);
            } catch (NoClassDefFoundError e) {
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading PlaceholderAPI as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("Skript") != null) {
            try {
                skript = new SkriptUtils();
                loadedDependencies.add(SoftDependency.SKRIPT);
            } catch (NoClassDefFoundError e) {
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Skript as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
    }

    public static boolean isDependencyEnabled(SoftDependency dependency) {
        return loadedDependencies.contains((Object)dependency);
    }

    public static void disableDependency(SoftDependency dependency) {
        if (DependencyModule.isDependencyEnabled(dependency)) {
            loadedDependencies.remove((Object)dependency);
        }
    }

    @Generated
    public static DependencyModule getInstance() {
        return instance;
    }

    @Generated
    public static void setInstance(DependencyModule instance) {
        DependencyModule.instance = instance;
    }

    static {
        loadedDependencies = new ArrayList<SoftDependency>();
    }
}

