/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;

public class Fuel
implements Setting {
    private String typeId;
    private double usage;

    @JsonIgnore
    public Optional<FuelType> getType() {
        return VehiclesPlusAPI.getFuelType(this.typeId);
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_FUEL_TYPE, (Map<String, String>)Map.of((Object)"%type%", (Object)this.typeId)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_FUEL_USAGE, (Map<String, String>)Map.of((Object)"%usage%", (Object)String.valueOf(this.usage))) + "\n";
    }

    @Generated
    public Fuel(String string, double d) {
        this.typeId = string;
        this.usage = d;
    }

    @Generated
    public String getTypeId() {
        return this.typeId;
    }

    @Generated
    public double getUsage() {
        return this.usage;
    }

    @Generated
    public void setTypeId(String string) {
        this.typeId = string;
    }

    @Generated
    public void setUsage(double d) {
        this.usage = d;
    }

    @Generated
    public Fuel() {
    }
}

