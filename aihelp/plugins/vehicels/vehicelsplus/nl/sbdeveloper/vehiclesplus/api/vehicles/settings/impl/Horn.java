/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Sounds;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Horn
implements Setting {
    private boolean enabled;
    private Sounds.Sound sound;
    @JsonIgnore
    private int cooldown = 0;

    public Horn() {
        this.enabled = false;
        this.sound = null;
    }

    public Horn(Sounds.Sound sound) {
        this.enabled = true;
        this.sound = sound;
    }

    public Horn(boolean bl, Sounds.Sound sound) {
        this.enabled = bl;
        this.sound = sound;
    }

    public boolean horn(Location location) {
        if (!this.enabled) {
            return false;
        }
        Bukkit.getScheduler().runTask((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), () -> {
            if (this.cooldown == 0) {
                this.sound.playSound(location);
            }
            this.cooldown = 5;
            Bukkit.getScheduler().runTaskLater((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), () -> {
                this.cooldown = 0;
            }, 100L);
        });
        return this.cooldown != 0;
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HORN_ENABLED, (Map<String, String>)Map.of((Object)"%enabled%", (Object)String.valueOf(this.enabled))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HORN_SOUND, (Map<String, String>)Map.of((Object)"%sound%", (Object)(this.sound == null ? "\nnull" : "\n" + String.valueOf(this.sound)))) + "\n";
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public Sounds.Sound getSound() {
        return this.sound;
    }

    @Generated
    public int getCooldown() {
        return this.cooldown;
    }

    @Generated
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Generated
    public void setSound(Sounds.Sound sound) {
        this.sound = sound;
    }
}

