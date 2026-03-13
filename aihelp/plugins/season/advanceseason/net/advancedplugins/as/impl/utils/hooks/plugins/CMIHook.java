/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.Zrips.CMI.CMI
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.Zrips.CMI.CMI;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.VanishHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class CMIHook
extends PluginHookInstance
implements VanishHook,
Listener {
    public CMIHook() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)ASManager.getInstance());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.CMI.getPluginName();
    }

    @Override
    public boolean isPlayerVanished(Player player) {
        return CMI.getInstance().getPlayerManager().getUser(player).isVanished();
    }
}

