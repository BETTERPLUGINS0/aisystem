/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.bekvon.bukkit.residence.Residence
 *  com.bekvon.bukkit.residence.containers.Flags
 *  com.bekvon.bukkit.residence.protection.ClaimedResidence
 *  com.bekvon.bukkit.residence.protection.ResidencePermissions
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResidenceHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.RESIDENCE.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        ClaimedResidence claimedResidence = Residence.getInstance().getResidenceManager().getByLoc(location);
        if (claimedResidence == null) {
            return true;
        }
        ResidencePermissions residencePermissions = claimedResidence.getPermissions();
        return residencePermissions.playerHas(player, Flags.build, true);
    }

    public boolean isProtected(Location location) {
        ClaimedResidence claimedResidence = Residence.getInstance().getResidenceManager().getByLoc(location);
        return claimedResidence != null;
    }
}

