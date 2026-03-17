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

public class Hitbox
implements Setting {
    private double length;
    private double width;
    private double height;

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HITBOX_LENGTH, (Map<String, String>)Map.of((Object)"%length%", (Object)String.valueOf(this.length))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HITBOX_WIDTH, (Map<String, String>)Map.of((Object)"%width%", (Object)String.valueOf(this.width))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HITBOX_HEIGHT, (Map<String, String>)Map.of((Object)"%height%", (Object)String.valueOf(this.height))) + "\n";
    }

    @Generated
    public Hitbox(double d, double d2, double d3) {
        this.length = d;
        this.width = d2;
        this.height = d3;
    }

    @Generated
    public double getLength() {
        return this.length;
    }

    @Generated
    public double getWidth() {
        return this.width;
    }

    @Generated
    public double getHeight() {
        return this.height;
    }

    @Generated
    public void setLength(double d) {
        this.length = d;
    }

    @Generated
    public void setWidth(double d) {
        this.width = d;
    }

    @Generated
    public void setHeight(double d) {
        this.height = d;
    }

    @Generated
    public Hitbox() {
    }
}

