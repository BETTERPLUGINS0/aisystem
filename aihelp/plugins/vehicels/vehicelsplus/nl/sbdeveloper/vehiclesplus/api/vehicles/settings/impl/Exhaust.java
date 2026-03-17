/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.Particle
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.Location;
import org.bukkit.Particle;

public class Exhaust {
    private boolean enabled;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private Particle particle;

    public void spawnParticle(Location location) {
        if (!this.enabled) {
            return;
        }
        Location location2 = location.clone().add(location.getDirection().setY(0).normalize().multiply(this.xOffset));
        double d = location2.getZ() + this.zOffset * Math.sin(Math.toRadians(location2.getYaw()));
        double d2 = location2.getX() + this.zOffset * Math.sin(Math.toRadians(location2.getYaw()));
        Location location3 = new Location(location.getWorld(), d2, location.getY() + this.yOffset, d, location2.getYaw(), location2.getPitch());
        location3.getWorld().spawnParticle(this.particle, location3, 1, 0.0, 0.0, 0.0, 0.0);
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_EXHAUST_ENABLED, (Map<String, String>)Map.of((Object)"%enabled%", (Object)String.valueOf(this.enabled))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_EXHAUST_XOFFSET, (Map<String, String>)Map.of((Object)"%xoffset%", (Object)String.valueOf(this.xOffset))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_EXHAUST_YOFFSET, (Map<String, String>)Map.of((Object)"%yoffset%", (Object)String.valueOf(this.yOffset))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_EXHAUST_ZOFFSET, (Map<String, String>)Map.of((Object)"%zoffset%", (Object)String.valueOf(this.zOffset))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_EXHAUST_PARTICLE, (Map<String, String>)Map.of((Object)"%particle%", (Object)this.particle.name())) + "\n";
    }

    @Generated
    public Exhaust() {
    }

    @Generated
    public Exhaust(boolean bl, double d, double d2, double d3, Particle particle) {
        this.enabled = bl;
        this.xOffset = d;
        this.yOffset = d2;
        this.zOffset = d3;
        this.particle = particle;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public double getXOffset() {
        return this.xOffset;
    }

    @Generated
    public double getYOffset() {
        return this.yOffset;
    }

    @Generated
    public double getZOffset() {
        return this.zOffset;
    }

    @Generated
    public Particle getParticle() {
        return this.particle;
    }

    @Generated
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Generated
    public void setXOffset(double d) {
        this.xOffset = d;
    }

    @Generated
    public void setYOffset(double d) {
        this.yOffset = d;
    }

    @Generated
    public void setZOffset(double d) {
        this.zOffset = d;
    }

    @Generated
    public void setParticle(Particle particle) {
        this.particle = particle;
    }
}

