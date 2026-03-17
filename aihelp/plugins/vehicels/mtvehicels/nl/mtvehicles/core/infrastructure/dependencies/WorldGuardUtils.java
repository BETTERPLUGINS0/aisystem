/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.dependencies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.WorldGuardWrapper;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardUtils {
    public static WorldGuardWrapper instance = WorldGuardWrapper.getInstance();
    public static HashMap<WGFlag, IWrappedFlag<WrappedState>> flags = new HashMap();

    public WorldGuardUtils() {
        this.registerFlags();
    }

    public boolean isInsideGasStation(Player player, Location loc) {
        return this.isInRegionWithFlag(player, loc, WGFlag.GAS_STATION, WrappedState.ALLOW);
    }

    public Set<String> getRegionNames(Location loc) {
        HashSet<String> returns = new HashSet<String>();
        this.getRegions(loc).forEach(region -> returns.add(region.getId()));
        return returns;
    }

    public boolean isInsideRegion(Location loc, String regionName) {
        return this.getRegionNames(loc).contains(regionName);
    }

    private Set<IWrappedRegion> getRegions(Location loc) {
        return instance.getRegions(loc);
    }

    public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, WrappedState flagState) {
        Set<IWrappedRegion> regions = this.getRegions(loc);
        if (regions.size() == 0) {
            return false;
        }
        IWrappedFlag<WrappedState> flag = flags.get((Object)customFlag);
        boolean returns = false;
        for (IWrappedRegion region : regions) {
            WrappedState state;
            Optional<WrappedState> regionFlagState = region.getFlag(flag);
            if (!regionFlagState.isPresent() || !flagState.equals((Object)(state = (WrappedState)instance.queryFlag(player, loc, flag).orElse(null)))) continue;
            returns = true;
        }
        return returns;
    }

    public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, boolean flagState) {
        WrappedState state = flagState ? WrappedState.ALLOW : WrappedState.DENY;
        return this.isInRegionWithFlag(player, loc, customFlag, state);
    }

    private void registerFlags() {
        try {
            for (WGFlag flag : WGFlag.getFlagList()) {
                flags.put(flag, instance.registerFlag(flag.getKey(), WrappedState.class).orElse(null));
            }
        } catch (IllegalStateException e) {
            for (WGFlag flag : WGFlag.getFlagList()) {
                flags.put(flag, instance.getFlag(flag.getKey(), WrappedState.class).orElse(null));
                if (flags.get((Object)flag) != null) continue;
                Bukkit.getLogger().severe("[MTVehicles] Custom WorldGuard flags could not be registered for MTVehicles. Disabling soft-dependency... (If you've just reloaded the plugin with PlugMan, try restarting the server.)");
                DependencyModule.disableDependency(SoftDependency.WORLD_GUARD);
                return;
            }
        }
    }
}

