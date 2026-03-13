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
import net.advancedplugins.as.impl.utils.hooks.plugins.LandsHook;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandsCheck
implements ProtectionType {
    @Override
    public String getName() {
        return "Lands";
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        LandsHook landsHook = (LandsHook)HooksHandler.getHook(HookPlugin.LANDS);
        if (landsHook == null) {
            return true;
        }
        return landsHook.canBuild(player, location);
    }

    @Override
    public boolean canAttack(Player player, Player player2) {
        LandsHook landsHook = (LandsHook)HooksHandler.getHook(HookPlugin.LANDS);
        if (landsHook == null) {
            return true;
        }
        return landsHook.canAttack(player, player2);
    }

    @Override
    public boolean isProtected(Location location) {
        LandsHook landsHook = (LandsHook)HooksHandler.getHook(HookPlugin.LANDS);
        if (landsHook == null) {
            return false;
        }
        return landsHook.isProtected(location);
    }
}

