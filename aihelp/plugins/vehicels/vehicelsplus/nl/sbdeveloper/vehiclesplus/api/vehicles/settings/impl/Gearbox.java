/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Gearbox
implements Setting {
    private boolean realistic;
    private long cooldown = 10L;
    @JsonIgnore
    private transient boolean firstRun = true;
    @JsonIgnore
    private transient Gear currentGear = this.realistic ? Gear.NEUTRAL : Gear.FORWARD;
    @JsonIgnore
    private transient boolean inCooldown = false;

    public Gearbox() {
        this.realistic = false;
    }

    public Gearbox(boolean bl) {
        this.realistic = bl;
    }

    public Gearbox(boolean bl, long l) {
        this.realistic = bl;
        this.cooldown = l;
    }

    public int handleGearbox(MovementInput movementInput, float f) {
        if (this.realistic) {
            if (movementInput.isW()) {
                if (this.currentGear == Gear.REVERSE) {
                    if (f >= 0.0f) {
                        this.currentGear = Gear.NEUTRAL;
                        this.startCooldown();
                        return 0;
                    }
                    return 1;
                }
                if (this.currentGear == Gear.NEUTRAL) {
                    if (!this.inCooldown) {
                        if (this.firstRun) {
                            this.firstRun = false;
                        }
                        this.currentGear = Gear.FORWARD;
                        return 1;
                    }
                    return 0;
                }
                if (this.currentGear == Gear.FORWARD) {
                    return 1;
                }
            } else if (movementInput.isS()) {
                if (this.currentGear == Gear.FORWARD) {
                    if (f <= 0.0f) {
                        this.currentGear = Gear.NEUTRAL;
                        this.startCooldown();
                        return 0;
                    }
                    return -1;
                }
                if (this.currentGear == Gear.NEUTRAL) {
                    if (!this.inCooldown) {
                        if (this.firstRun) {
                            this.firstRun = false;
                        }
                        this.currentGear = Gear.REVERSE;
                        return -1;
                    }
                    return 0;
                }
                if (this.currentGear == Gear.REVERSE) {
                    return -1;
                }
            }
        } else {
            if (movementInput.isW()) {
                return 1;
            }
            if (movementInput.isS()) {
                return -1;
            }
        }
        throw new RuntimeException("Gearbox handler called with invalid input: " + String.valueOf(movementInput));
    }

    private void startCooldown() {
        this.inCooldown = true;
        Bukkit.getScheduler().runTaskLater((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), () -> {
            this.inCooldown = false;
        }, this.cooldown);
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_GEARBOX_REALISTIC, (Map<String, String>)Map.of((Object)"%realistic%", (Object)String.valueOf(this.realistic))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_GEARBOX_COOLDOWN, (Map<String, String>)Map.of((Object)"%cooldown%", (Object)String.valueOf(this.cooldown))) + "\n";
    }

    @Generated
    public boolean isRealistic() {
        return this.realistic;
    }

    @Generated
    public void setRealistic(boolean bl) {
        this.realistic = bl;
    }

    @Generated
    public long getCooldown() {
        return this.cooldown;
    }

    @Generated
    public void setCooldown(long l) {
        this.cooldown = l;
    }

    @JsonIgnore
    @Generated
    public boolean isFirstRun() {
        return this.firstRun;
    }

    @JsonIgnore
    @Generated
    public Gear getCurrentGear() {
        return this.currentGear;
    }

    @JsonIgnore
    @Generated
    public boolean isInCooldown() {
        return this.inCooldown;
    }

    public static enum Gear {
        FORWARD,
        NEUTRAL,
        REVERSE;

    }
}

