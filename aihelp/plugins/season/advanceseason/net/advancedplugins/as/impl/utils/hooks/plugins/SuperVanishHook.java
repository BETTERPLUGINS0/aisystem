/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.myzelyam.api.vanish.VanishAPI
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import de.myzelyam.api.vanish.VanishAPI;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.VanishHook;
import org.bukkit.entity.Player;

public class SuperVanishHook
extends PluginHookInstance
implements VanishHook {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.SUPERVANISH.getPluginName();
    }

    @Override
    public boolean isPlayerVanished(Player player) {
        return VanishAPI.isInvisible((Player)player);
    }
}

