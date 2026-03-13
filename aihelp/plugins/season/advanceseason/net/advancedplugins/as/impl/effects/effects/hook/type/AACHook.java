/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AACHook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("AAC") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("AAC");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering AAC Hook and enabling module...");
            Bukkit.getPluginManager().registerEvents((Listener)new AAC4ViolationEvent(), (Plugin)ASManager.getInstance());
            EffectsHandler.getAntiCheatHooks().put("AAC", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from AAC");
        return this.exempt(player);
    }

    @Override
    public boolean exempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return false;
        }
        return generalExemptions.add(player.getUniqueId());
    }

    @Override
    public void nonExempt(@NotNull Player player) {
        generalExemptions.remove(player.getUniqueId());
    }

    @Override
    public AntiCheatHook getHook() {
        return this;
    }

    private static class AAC4ViolationEvent
    implements Listener {
        private AAC4ViolationEvent() {
        }
    }
}

