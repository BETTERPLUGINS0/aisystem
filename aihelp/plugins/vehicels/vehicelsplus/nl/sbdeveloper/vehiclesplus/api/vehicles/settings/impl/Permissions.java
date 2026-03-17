/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(builder=PermissionsBuilder.class)
public class Permissions
implements Setting {
    private String buy;
    private String adjust;
    private String spawn;
    private String ride;
    private boolean sitWithoutRidePermission;

    public Permissions(String string) {
        this.buy = "vp.buy." + string;
        this.adjust = "vp.adjust." + string;
        this.spawn = "vp.spawn." + string;
        this.ride = "vp.ride." + string;
        this.sitWithoutRidePermission = true;
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_PERMISSIONS_BUY, (Map<String, String>)Map.of((Object)"%buy%", (Object)this.buy)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_PERMISSIONS_ADJUST, (Map<String, String>)Map.of((Object)"%adjust%", (Object)this.adjust)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_PERMISSIONS_SPAWN, (Map<String, String>)Map.of((Object)"%spawn%", (Object)this.spawn)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_PERMISSIONS_RIDE, (Map<String, String>)Map.of((Object)"%ride%", (Object)this.ride)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_PERMISSIONS_CANSITWITHOUTRIDE, (Map<String, String>)Map.of((Object)"%sit%", (Object)String.valueOf(this.sitWithoutRidePermission))) + "\n";
    }

    @Generated
    public static PermissionsBuilder builder() {
        return new PermissionsBuilder();
    }

    @Generated
    public String getBuy() {
        return this.buy;
    }

    @Generated
    public String getAdjust() {
        return this.adjust;
    }

    @Generated
    public String getSpawn() {
        return this.spawn;
    }

    @Generated
    public String getRide() {
        return this.ride;
    }

    @Generated
    public boolean isSitWithoutRidePermission() {
        return this.sitWithoutRidePermission;
    }

    @Generated
    public void setBuy(String string) {
        this.buy = string;
    }

    @Generated
    public void setAdjust(String string) {
        this.adjust = string;
    }

    @Generated
    public void setSpawn(String string) {
        this.spawn = string;
    }

    @Generated
    public void setRide(String string) {
        this.ride = string;
    }

    @Generated
    public void setSitWithoutRidePermission(boolean bl) {
        this.sitWithoutRidePermission = bl;
    }

    @Generated
    public Permissions(String string, String string2, String string3, String string4, boolean bl) {
        this.buy = string;
        this.adjust = string2;
        this.spawn = string3;
        this.ride = string4;
        this.sitWithoutRidePermission = bl;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonPOJOBuilder(withPrefix="", buildMethodName="build")
    @Generated
    public static class PermissionsBuilder {
        @Generated
        private String buy;
        @Generated
        private String adjust;
        @Generated
        private String spawn;
        @Generated
        private String ride;
        @Generated
        private boolean sitWithoutRidePermission;

        @Generated
        PermissionsBuilder() {
        }

        @Generated
        public PermissionsBuilder buy(String string) {
            this.buy = string;
            return this;
        }

        @Generated
        public PermissionsBuilder adjust(String string) {
            this.adjust = string;
            return this;
        }

        @Generated
        public PermissionsBuilder spawn(String string) {
            this.spawn = string;
            return this;
        }

        @Generated
        public PermissionsBuilder ride(String string) {
            this.ride = string;
            return this;
        }

        @Generated
        public PermissionsBuilder sitWithoutRidePermission(boolean bl) {
            this.sitWithoutRidePermission = bl;
            return this;
        }

        @Generated
        public Permissions build() {
            return new Permissions(this.buy, this.adjust, this.spawn, this.ride, this.sitWithoutRidePermission);
        }

        @Generated
        public String toString() {
            return "Permissions.PermissionsBuilder(buy=" + this.buy + ", adjust=" + this.adjust + ", spawn=" + this.spawn + ", ride=" + this.ride + ", sitWithoutRidePermission=" + this.sitWithoutRidePermission + ")";
        }
    }
}

