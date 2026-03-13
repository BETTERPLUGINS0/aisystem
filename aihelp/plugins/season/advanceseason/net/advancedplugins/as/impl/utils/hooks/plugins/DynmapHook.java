/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.dynmap.DynmapAPI
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Bukkit;
import org.dynmap.DynmapAPI;

public class DynmapHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.DYNMAP.getPluginName();
    }

    public void setDynmapGeneration(boolean bl) {
        DynmapAPI dynmapAPI = (DynmapAPI)Bukkit.getPluginManager().getPlugin(HookPlugin.DYNMAP.getPluginName());
        assert (dynmapAPI != null);
        dynmapAPI.setPauseFullRadiusRenders(!bl);
    }
}

