/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.viaversion.viaversion.api.Via;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.entity.Player;

public class ViaVersionHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.VIAVERSION.getPluginName();
    }

    public String getPlayerVersion(Player player) {
        return "" + Via.getAPI().getPlayerVersion((Object)player);
    }
}

