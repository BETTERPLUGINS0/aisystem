/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  thito.beaconplus.BeaconAPI
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import thito.beaconplus.BeaconAPI;

public class BeaconsPlus3Hook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.BEACONPLUS3.getPluginName();
    }

    public boolean isBeaconPlus(Location location) {
        return BeaconAPI.getAPI().getBeaconData(location) != null;
    }
}

