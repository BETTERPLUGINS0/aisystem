/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class UnlockedVehicle
implements ConfigurationSerializable {
    private final AbstractVehicle vehicleType;
    private double health;
    private boolean inGarage;

    public UnlockedVehicle(AbstractVehicle abstractVehicle, double d, boolean bl) {
        this.vehicleType = abstractVehicle;
        this.health = d;
        this.inGarage = bl;
    }

    public UnlockedVehicle(Map<String, Object> map) {
        this.vehicleType = QualityArmoryVehicles.getVehicle((String)map.get("type"));
        this.inGarage = (Boolean)map.getOrDefault("inGarage", true);
        if (this.vehicleType == null) {
            return;
        }
        this.health = (Double)map.get("health");
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        UnlockedVehicle unlockedVehicle = (UnlockedVehicle)object;
        return Double.compare(unlockedVehicle.health, this.health) == 0 && this.inGarage == unlockedVehicle.inGarage && Objects.equals(this.vehicleType, unlockedVehicle.vehicleType);
    }

    public int hashCode() {
        return Objects.hash(this.vehicleType, this.health, this.inGarage);
    }

    public void setHealth(double d) {
        this.health = d;
    }

    public double getHealth() {
        return this.health;
    }

    public void setInGarage(boolean bl) {
        this.inGarage = bl;
    }

    public boolean isInGarage() {
        return this.inGarage;
    }

    public AbstractVehicle getVehicleType() {
        return this.vehicleType;
    }

    @NotNull
    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("type", this.vehicleType.getName());
        hashMap.put("health", this.health);
        hashMap.put("inGarage", this.inGarage);
        return hashMap;
    }
}

