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
import net.advancedplugins.as.impl.utils.hooks.factions.FactionsPluginHook;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsCheck
implements ProtectionType {
    @Override
    public String getName() {
        return "Factions";
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        FactionsPluginHook factionsPluginHook = (FactionsPluginHook)HooksHandler.getHook(HookPlugin.FACTIONS);
        if (!factionsPluginHook.isEnabled()) {
            return true;
        }
        String string = factionsPluginHook.getRelationOfLand(player);
        return string.equalsIgnoreCase("Wilderness") || string.equalsIgnoreCase("null") || string.equalsIgnoreCase("neutral") || string.equalsIgnoreCase("member");
    }

    @Override
    public boolean isProtected(Location location) {
        return false;
    }

    @Override
    public boolean canAttack(Player player, Player player2) {
        return true;
    }
}

