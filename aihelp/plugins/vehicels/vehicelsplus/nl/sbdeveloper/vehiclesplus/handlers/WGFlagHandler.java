/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.LocalPlayer
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.association.RegionAssociable
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.IntegerFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.flags.registry.FlagConflictException
 *  com.sk89q.worldguard.protection.flags.registry.FlagRegistry
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import java.lang.invoke.LambdaMetafactory;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.VehicleType;
import nl.sbdeveloper.vehiclesplus.utils.wg.MaxSpeedFlagType;
import nl.sbdeveloper.vehiclesplus.utils.wg.WorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class WGFlagHandler {
    private static StateFlag canSpawnFlag = new StateFlag("vehicles-spawn", true);
    private static StateFlag canDriveFlag = new StateFlag("vehicles-drive", true);
    private static MaxSpeedFlagType maxSpeedFlag = new MaxSpeedFlagType("vehicles-speedlimit");
    private static IntegerFlag maxSpawnFlag = new IntegerFlag("vehicles-limit");

    public static void load() {
        FlagRegistry flagRegistry = WorldGuardHelper.getInstance().getFlagRegistry();
        try {
            flagRegistry.register((Flag)canSpawnFlag);
            flagRegistry.register((Flag)canDriveFlag);
            flagRegistry.register((Flag)maxSpeedFlag);
            flagRegistry.register((Flag)maxSpawnFlag);
        } catch (FlagConflictException flagConflictException) {
            canSpawnFlag = (StateFlag)flagRegistry.get("vehicles-spawn");
            canDriveFlag = (StateFlag)flagRegistry.get("vehicles-drive");
            maxSpeedFlag = (MaxSpeedFlagType)flagRegistry.get("vehicles-speedlimit");
            maxSpawnFlag = (IntegerFlag)flagRegistry.get("vehicles-limit");
        } catch (IllegalStateException illegalStateException) {
            VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Could not load WorldGuard flags!", illegalStateException);
        }
    }

    public static boolean allowsVehicleSpawning(Player player, Location location) {
        ApplicableRegionSet applicableRegionSet = WorldGuardHelper.getInstance().getApplicableRegionSet(location);
        LocalPlayer localPlayer = WorldGuardHelper.getInstance().getWorldGuard().wrapPlayer(player);
        return applicableRegionSet.queryState((RegionAssociable)localPlayer, new StateFlag[]{canSpawnFlag}) == StateFlag.State.ALLOW;
    }

    public static boolean allowsVehicleDriving(Player player, Location location) {
        ApplicableRegionSet applicableRegionSet = WorldGuardHelper.getInstance().getApplicableRegionSet(location);
        LocalPlayer localPlayer = WorldGuardHelper.getInstance().getWorldGuard().wrapPlayer(player);
        return applicableRegionSet.queryState((RegionAssociable)localPlayer, new StateFlag[]{canDriveFlag}) == StateFlag.State.ALLOW;
    }

    @Nullable
    public static Integer getRegionMaxSpeed(Player player, Location location, VehicleType vehicleType) {
        ApplicableRegionSet applicableRegionSet = WorldGuardHelper.getInstance().getApplicableRegionSet(location);
        LocalPlayer localPlayer = WorldGuardHelper.getInstance().getWorldGuard().wrapPlayer(player);
        return (Integer)Optional.ofNullable((Map)applicableRegionSet.queryValue((RegionAssociable)localPlayer, (Flag)maxSpeedFlag)).orElseGet((Supplier<Map>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, of(), ()Ljava/util/Map;)()).get(vehicleType.getName());
    }

    public static boolean reachedVehicleLimit(Player player, Location location) {
        ApplicableRegionSet applicableRegionSet = WorldGuardHelper.getInstance().getApplicableRegionSet(location);
        LocalPlayer localPlayer = WorldGuardHelper.getInstance().getWorldGuard().wrapPlayer(player);
        applicableRegionSet.forEach(protectedRegion -> Bukkit.getLogger().info(protectedRegion.getId()));
        Integer n = (Integer)applicableRegionSet.queryValue((RegionAssociable)localPlayer, (Flag)maxSpawnFlag);
        int n2 = VehiclesPlusAPI.getSpawnedVehicles().stream().mapToInt(spawnedVehicle -> (int)StreamSupport.stream(WorldGuardHelper.getInstance().getApplicableRegionSet(spawnedVehicle.getHolder().getLocation()).spliterator(), false).filter(protectedRegion -> StreamSupport.stream(applicableRegionSet.spliterator(), false).anyMatch(protectedRegion2 -> protectedRegion2.getId().equals(protectedRegion.getId()))).count()).sum();
        return n != null && n2 >= n;
    }

    @Generated
    private WGFlagHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

