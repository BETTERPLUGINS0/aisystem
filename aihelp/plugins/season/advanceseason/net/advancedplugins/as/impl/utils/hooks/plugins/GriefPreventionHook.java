/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.ryanhamshire.GriefPrevention.Claim
 *  me.ryanhamshire.GriefPrevention.GriefPrevention
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefPreventionHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.GRIEFPREVENTION.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
        if (claim == null) {
            return true;
        }
        return claim.allowAccess(player) == null;
    }
}

