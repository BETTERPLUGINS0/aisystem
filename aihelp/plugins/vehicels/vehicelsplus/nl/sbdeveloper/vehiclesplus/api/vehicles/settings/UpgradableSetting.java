/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.settings;

import java.util.Map;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;

public class UpgradableSetting
implements Setting {
    private Number base;
    private boolean upgradable;
    private Number max;
    private Number step;
    private double stepCost;
    private String unit;

    public UpgradableSetting(Number number, Number number2, Number number3, double d, String string) {
        this(number, true, number2, number3, d, string);
    }

    public String toString() {
        return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_BASE, (Map<String, String>)Map.of((Object)"%base%", (Object)String.valueOf(this.base), (Object)"%unit%", (Object)this.unit)) + "\n" + (this.upgradable ? Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_MAX, (Map<String, String>)Map.of((Object)"%max%", (Object)String.valueOf(this.max), (Object)"%unit%", (Object)this.unit)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_STEP, (Map<String, String>)Map.of((Object)"%step%", (Object)String.valueOf(this.step), (Object)"%unit%", (Object)this.unit)) + "\n" + Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_STEPCOST, (Map<String, String>)Map.of((Object)"%stepcost%", (Object)MainUtil.___(this.stepCost))) + "\n" : Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_NOTUPGRADABLE) + "\n");
    }

    @Generated
    public UpgradableSetting(Number number, boolean bl, Number number2, Number number3, double d, String string) {
        this.base = number;
        this.upgradable = bl;
        this.max = number2;
        this.step = number3;
        this.stepCost = d;
        this.unit = string;
    }

    @Generated
    public Number getBase() {
        return this.base;
    }

    @Generated
    public boolean isUpgradable() {
        return this.upgradable;
    }

    @Generated
    public Number getMax() {
        return this.max;
    }

    @Generated
    public Number getStep() {
        return this.step;
    }

    @Generated
    public double getStepCost() {
        return this.stepCost;
    }

    @Generated
    public String getUnit() {
        return this.unit;
    }

    @Generated
    public void setBase(Number number) {
        this.base = number;
    }

    @Generated
    public void setUpgradable(boolean bl) {
        this.upgradable = bl;
    }

    @Generated
    public void setMax(Number number) {
        this.max = number;
    }

    @Generated
    public void setStep(Number number) {
        this.step = number;
    }

    @Generated
    public void setStepCost(double d) {
        this.stepCost = d;
    }

    @Generated
    public void setUnit(String string) {
        this.unit = string;
    }

    @Generated
    public UpgradableSetting() {
    }
}

