/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.protection.external;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.plugins.WorldGuardHook;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardCheck
implements ProtectionType {
    @Override
    public String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        PluginHookInstance pluginHookInstance = HooksHandler.getHook(HookPlugin.WORLDGUARD);
        if (!pluginHookInstance.isEnabled()) {
            return true;
        }
        return ((WorldGuardHook)pluginHookInstance).canBuild(player, location);
    }

    @Override
    public boolean canAttack(Player player, Player player2) {
        return true;
    }

    @Override
    public boolean isProtected(Location location) {
        PluginHookInstance pluginHookInstance = HooksHandler.getHook(HookPlugin.WORLDGUARD);
        if (!pluginHookInstance.isEnabled()) {
            return false;
        }
        return ((WorldGuardHook)pluginHookInstance).isProtected(location);
    }
}

