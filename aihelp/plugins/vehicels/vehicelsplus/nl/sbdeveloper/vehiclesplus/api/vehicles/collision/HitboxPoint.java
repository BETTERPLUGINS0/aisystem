/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxSide;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class HitboxPoint {
    private final Vector offset;
    private final HitboxSide side;

    public Location toLocation(Location location) {
        return location.clone().add(this.offset);
    }

    @Generated
    public Vector getOffset() {
        return this.offset;
    }

    @Generated
    public HitboxSide getSide() {
        return this.side;
    }

    @Generated
    public HitboxPoint(Vector vector, HitboxSide hitboxSide) {
        this.offset = vector;
        this.side = hitboxSide;
    }
}

