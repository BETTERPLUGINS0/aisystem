/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import java.util.List;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxPoint;
import org.bukkit.util.Vector;

public class HitboxResult {
    private final List<HitboxPoint> hitboxPoints;
    private final List<Vector> hitboxCorners;

    @Generated
    public List<HitboxPoint> getHitboxPoints() {
        return this.hitboxPoints;
    }

    @Generated
    public List<Vector> getHitboxCorners() {
        return this.hitboxCorners;
    }

    @Generated
    public HitboxResult(List<HitboxPoint> list, List<Vector> list2) {
        this.hitboxPoints = list;
        this.hitboxCorners = list2;
    }
}

