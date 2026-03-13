/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.earth2me.essentials.Essentials;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.VanishHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EssentialsHook
extends PluginHookInstance
implements VanishHook,
Listener {
    public EssentialsHook() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)ASManager.getInstance());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.ESSENTIALS.getPluginName();
    }

    @Override
    public boolean isPlayerVanished(Player player) {
        return ((Essentials)this.getPluginInstance()).getUser(player).isVanished();
    }
}

