/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;

public class HeightLimit
implements Setting {
    private double minHeight;
    private double maxHeight;

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HEIGHTLIMIT_MINHEIGHT, (Map<String, String>)Map.of((Object)"%min%", (Object)String.valueOf(this.minHeight))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HEIGHTLIMIT_MAXHEIGHT, (Map<String, String>)Map.of((Object)"%max%", (Object)String.valueOf(this.maxHeight))) + "\n";
    }

    @Generated
    public HeightLimit(double d, double d2) {
        this.minHeight = d;
        this.maxHeight = d2;
    }

    @Generated
    public double getMinHeight() {
        return this.minHeight;
    }

    @Generated
    public double getMaxHeight() {
        return this.maxHeight;
    }

    @Generated
    public void setMinHeight(double d) {
        this.minHeight = d;
    }

    @Generated
    public void setMaxHeight(double d) {
        this.maxHeight = d;
    }

    @Generated
    public HeightLimit() {
    }
}

