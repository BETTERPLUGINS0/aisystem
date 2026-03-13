/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  us.lynuxcraft.deadsilenceiv.advancedchests.AdvancedChestsPlugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import us.lynuxcraft.deadsilenceiv.advancedchests.AdvancedChestsPlugin;

public class AdvancedChestsHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.ADVANCEDCHESTS.getPluginName();
    }

    public boolean isAdvancedChest(Location location) {
        return AdvancedChestsPlugin.getInstance().getChestsManager().getAdvancedChest(location) != null;
    }
}

