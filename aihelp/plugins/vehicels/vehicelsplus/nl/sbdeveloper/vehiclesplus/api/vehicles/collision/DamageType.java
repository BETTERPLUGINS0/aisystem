/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import lombok.Generated;

public enum DamageType {
    REALISTIC(1.7),
    NORMAL(1.5),
    REDUCED(1.4),
    MINIMAL(1.3),
    NONE(0.0);

    private final double damageMultiplier;

    private DamageType(double d) {
        this.damageMultiplier = d;
    }

    public static DamageType fromString(String string) {
        if (string == null) {
            return NONE;
        }
        String string2 = string.trim();
        for (DamageType damageType : DamageType.values()) {
            if (!damageType.name().equalsIgnoreCase(string2)) continue;
            return damageType;
        }
        return NONE;
    }

    @Generated
    public double getDamageMultiplier() {
        return this.damageMultiplier;
    }
}

