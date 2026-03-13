/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.griefdefender.api.GriefDefender
 *  com.griefdefender.api.claim.Claim
 *  com.griefdefender.api.permission.flag.Flag
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.permission.flag.Flag;
import java.util.HashSet;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefDefenderHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.GRIEFDEFENDER.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        Claim claim = GriefDefender.getCore().getClaimAt((Object)location);
        if (claim == null) {
            return true;
        }
        return true;
    }

    public boolean canIceGenerate(Location location) {
        try {
            Claim claim = GriefDefender.getCore().getClaimAt((Object)location);
            if (claim == null) {
                return true;
            }
            return claim.getFlagPermissionValue(Flag.builder().name("ice-growth").build(), new HashSet()).asBoolean();
        } catch (Exception exception) {
            return true;
        }
    }
}

