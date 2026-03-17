/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.TimingUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(builder=SoundsBuilder.class)
public class Sounds
implements Setting {
    private Sound idle;
    private Sound start;
    private Sound accelerate;
    private Sound driving;
    private Sound slowingDown;

    void stopAllCarSounds(Player player) {
        player.stopSound(this.getIdle().getSound());
        player.stopSound(this.getStart().getSound());
        player.stopSound(this.getAccelerate().getSound());
        player.stopSound(this.getDriving().getSound());
        player.stopSound(this.getSlowingDown().getSound());
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_IDLE, (Map<String, String>)Map.of((Object)"%sound%", (Object)("\n" + this.idle.toString()))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_START, (Map<String, String>)Map.of((Object)"%sound%", (Object)("\n" + this.start.toString()))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_ACCELERATION, (Map<String, String>)Map.of((Object)"%sound%", (Object)("\n" + this.accelerate.toString()))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_DRIVING, (Map<String, String>)Map.of((Object)"%sound%", (Object)("\n" + this.driving.toString()))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_SLOWINGDOWN, (Map<String, String>)Map.of((Object)"%sound%", (Object)("\n" + this.slowingDown.toString()))) + "\n";
    }

    @Generated
    Sounds(Sound sound, Sound sound2, Sound sound3, Sound sound4, Sound sound5) {
        this.idle = sound;
        this.start = sound2;
        this.accelerate = sound3;
        this.driving = sound4;
        this.slowingDown = sound5;
    }

    @Generated
    public static SoundsBuilder builder() {
        return new SoundsBuilder();
    }

    @Generated
    public Sound getIdle() {
        return this.idle;
    }

    @Generated
    public Sound getStart() {
        return this.start;
    }

    @Generated
    public Sound getAccelerate() {
        return this.accelerate;
    }

    @Generated
    public Sound getDriving() {
        return this.driving;
    }

    @Generated
    public Sound getSlowingDown() {
        return this.slowingDown;
    }

    @Generated
    public void setIdle(Sound sound) {
        this.idle = sound;
    }

    @Generated
    public void setStart(Sound sound) {
        this.start = sound;
    }

    @Generated
    public void setAccelerate(Sound sound) {
        this.accelerate = sound;
    }

    @Generated
    public void setDriving(Sound sound) {
        this.driving = sound;
    }

    @Generated
    public void setSlowingDown(Sound sound) {
        this.slowingDown = sound;
    }

    public static class Sound {
        private String sound;
        private float volume = 1.0f;
        private float pitch = 1.0f;
        private int duration;

        public Sound(String string, int n) {
            this.sound = string;
            this.duration = n;
        }

        public void playSound(DrivableVehicle drivableVehicle, Player player) {
            Sounds sounds = drivableVehicle.getVehicleModel().getSounds();
            if (TimingUtil.isInTimer("alreadyListening", this.duration, player.getUniqueId())) {
                return;
            }
            sounds.stopAllCarSounds(player);
            this.playSound(drivableVehicle.getHolder().getLocation());
            sounds.stopAllCarSounds(player);
            this.playSound(drivableVehicle.getHolder().getLocation());
        }

        public void playSound(Location location) {
            location.getWorld().playSound(location, this.sound, this.volume, this.pitch);
        }

        public String toString() {
            return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_SOUND_SOUND, (Map<String, String>)Map.of((Object)"%sound%", (Object)this.sound)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_SOUND_DURATION, (Map<String, String>)Map.of((Object)"%duration%", (Object)String.valueOf(this.duration))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_SOUND_VOLUME, (Map<String, String>)Map.of((Object)"%volume%", (Object)String.valueOf(this.volume))) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SOUNDS_SOUND_PITCH, (Map<String, String>)Map.of((Object)"%pitch%", (Object)String.valueOf(this.pitch))) + "\n";
        }

        @Generated
        public String getSound() {
            return this.sound;
        }

        @Generated
        public float getVolume() {
            return this.volume;
        }

        @Generated
        public float getPitch() {
            return this.pitch;
        }

        @Generated
        public int getDuration() {
            return this.duration;
        }

        @Generated
        public void setSound(String string) {
            this.sound = string;
        }

        @Generated
        public void setVolume(float f) {
            this.volume = f;
        }

        @Generated
        public void setPitch(float f) {
            this.pitch = f;
        }

        @Generated
        public void setDuration(int n) {
            this.duration = n;
        }

        @Generated
        public Sound(String string, float f, float f2, int n) {
            this.sound = string;
            this.volume = f;
            this.pitch = f2;
            this.duration = n;
        }

        @Generated
        public Sound() {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonPOJOBuilder(withPrefix="", buildMethodName="build")
    @Generated
    public static class SoundsBuilder {
        @Generated
        private Sound idle;
        @Generated
        private Sound start;
        @Generated
        private Sound accelerate;
        @Generated
        private Sound driving;
        @Generated
        private Sound slowingDown;

        @Generated
        SoundsBuilder() {
        }

        @Generated
        public SoundsBuilder idle(Sound sound) {
            this.idle = sound;
            return this;
        }

        @Generated
        public SoundsBuilder start(Sound sound) {
            this.start = sound;
            return this;
        }

        @Generated
        public SoundsBuilder accelerate(Sound sound) {
            this.accelerate = sound;
            return this;
        }

        @Generated
        public SoundsBuilder driving(Sound sound) {
            this.driving = sound;
            return this;
        }

        @Generated
        public SoundsBuilder slowingDown(Sound sound) {
            this.slowingDown = sound;
            return this;
        }

        @Generated
        public Sounds build() {
            return new Sounds(this.idle, this.start, this.accelerate, this.driving, this.slowingDown);
        }

        @Generated
        public String toString() {
            return "Sounds.SoundsBuilder(idle=" + String.valueOf(this.idle) + ", start=" + String.valueOf(this.start) + ", accelerate=" + String.valueOf(this.accelerate) + ", driving=" + String.valueOf(this.driving) + ", slowingDown=" + String.valueOf(this.slowingDown) + ")";
        }
    }
}

