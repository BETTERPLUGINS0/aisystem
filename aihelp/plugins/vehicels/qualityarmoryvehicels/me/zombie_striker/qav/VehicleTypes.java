/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav;

import org.jetbrains.annotations.NotNull;

public enum VehicleTypes {
    CAR("Car"),
    PLANE("Plane"),
    BOAT("Boat"),
    HELI("Helicopter"),
    TRAIN("Train"),
    DRILL("Drill"),
    TRACTOR("Tractor"),
    INVALID("Invalid");

    private String name;

    private VehicleTypes(String string2) {
        this.name = string2;
    }

    public String getName() {
        return this.name;
    }

    @NotNull
    public static VehicleTypes getTypeByName(String string) {
        for (VehicleTypes vehicleTypes : VehicleTypes.values()) {
            if (!vehicleTypes.getName().equals(string)) continue;
            return vehicleTypes;
        }
        return INVALID;
    }
}

