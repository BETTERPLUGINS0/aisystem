/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.enums;

import java.util.Locale;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum VehicleType {
    CAR,
    HOVER,
    TANK,
    HELICOPTER,
    AIRPLANE,
    BOAT;


    public String getName() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase(Locale.ROOT);
    }

    public boolean isCar() {
        return this.equals((Object)CAR);
    }

    public boolean isHover() {
        return this.equals((Object)HOVER);
    }

    public boolean isTank() {
        return this.equals((Object)TANK);
    }

    public boolean isHelicopter() {
        return this.equals((Object)HELICOPTER);
    }

    public boolean isAirplane() {
        return this.equals((Object)AIRPLANE);
    }

    public boolean isBoat() {
        return this.equals((Object)BOAT);
    }

    public boolean canFly() {
        return this.equals((Object)AIRPLANE) || this.equals((Object)HELICOPTER);
    }

    public boolean isUsageDisabled(Player player, Location loc) {
        if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
            return false;
        }
        return DependencyModule.worldGuard.isInRegionWithFlag(player, loc, WGFlag.valueOf("USE_" + this.toString()), false);
    }
}

