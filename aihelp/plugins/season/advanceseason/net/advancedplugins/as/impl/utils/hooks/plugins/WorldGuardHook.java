/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldguard.WorldGuard
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.Flags
 *  com.sk89q.worldguard.protection.flags.StateFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "WorldGuardHook";
    }

    public boolean canBuild(Player player, Location location) {
        if (player.isOp()) {
            return true;
        }
        try {
            return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(BukkitAdapter.adapt((Location)location), WorldGuardPlugin.inst().wrapPlayer(player), new StateFlag[]{Flags.BUILD}) || WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(BukkitAdapter.adapt((Location)location), WorldGuardPlugin.inst().wrapPlayer(player), new StateFlag[]{Flags.BLOCK_BREAK});
        } catch (Exception exception) {
            exception.printStackTrace();
            ASManager.getInstance().getLogger().warning("Error with WorldGuard v(" + Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion() + ") - Version unsupported");
            return false;
        }
    }

    public boolean isProtected(Location location) {
        try {
            return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((World)location.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector((Location)location)).size() > 0;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean canMobsSpawn(Location location) {
        for (ProtectedRegion protectedRegion : WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt((World)location.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector((Location)location))) {
            if (protectedRegion.getFlag((Flag)Flags.MOB_SPAWNING) != StateFlag.State.DENY) continue;
            return false;
        }
        return true;
    }
}

