/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.angeschossen.lands.api.LandsIntegration
 *  me.angeschossen.lands.api.flags.Flags
 *  me.angeschossen.lands.api.flags.type.NaturalFlag
 *  me.angeschossen.lands.api.land.Area
 *  me.angeschossen.lands.api.land.LandWorld
 *  me.angeschossen.lands.api.player.LandPlayer
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.Flags;
import me.angeschossen.lands.api.flags.type.NaturalFlag;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LandsHook
extends PluginHookInstance {
    LandsIntegration landsIntegration = LandsIntegration.of((Plugin)ASManager.getInstance());

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.LANDS.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        LandWorld landWorld = this.landsIntegration.getWorld(player.getWorld());
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(player.getUniqueId());
        if (landWorld == null) {
            return true;
        }
        if (landPlayer == null) {
            return true;
        }
        return landWorld.hasFlag(player, location, player.getInventory().getItemInMainHand().getType(), Flags.BLOCK_BREAK, false);
    }

    public boolean canAttack(Player player, Player player2) {
        return this.landsIntegration.canPvP(player, player2, player2.getLocation(), false, false);
    }

    public boolean canMobsSpawn(Location location) {
        try {
            LandWorld landWorld = this.landsIntegration.getWorld(location.getWorld());
            if (landWorld == null) {
                return true;
            }
            Area area = landWorld.getArea(location);
            if (area == null) {
                return true;
            }
            return area.hasNaturalFlag((NaturalFlag)Flags.MONSTER_SPAWN);
        } catch (Exception exception) {
            exception.printStackTrace();
            return true;
        }
    }

    public boolean isProtected(Location location) {
        return this.landsIntegration.getArea(location) != null;
    }
}

