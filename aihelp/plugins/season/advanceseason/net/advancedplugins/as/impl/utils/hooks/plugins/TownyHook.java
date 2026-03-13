/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.palmergames.bukkit.towny.TownyAPI
 *  com.palmergames.bukkit.towny.TownySettings
 *  com.palmergames.bukkit.towny.object.Resident
 *  com.palmergames.bukkit.towny.object.Town
 *  com.palmergames.bukkit.towny.object.TownBlock
 *  com.palmergames.bukkit.towny.object.TownBlockType
 *  com.palmergames.bukkit.towny.object.TownyPermission$ActionType
 *  com.palmergames.bukkit.towny.utils.PlayerCacheUtil
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TownyHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.TOWNY.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission((Player)player, (Location)location, (Material)location.getBlock().getType(), (TownyPermission.ActionType)TownyPermission.ActionType.BUILD);
    }

    public boolean canBreak(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission((Player)player, (Location)location, (Material)location.getBlock().getType(), (TownyPermission.ActionType)TownyPermission.ActionType.DESTROY);
    }

    public boolean hasKeepInventory(Player player) {
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null) {
            return false;
        }
        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(player.getLocation());
        return this.getKeepInventoryValue(resident, townBlock);
    }

    private boolean getKeepInventoryValue(Resident resident, TownBlock townBlock) {
        boolean bl;
        boolean bl2 = bl = TownySettings.getKeepInventoryInTowns() && townBlock != null;
        if (townBlock == null) {
            return bl;
        }
        if (resident.hasTown() && !bl) {
            Town town = resident.getTownOrNull();
            Town town2 = townBlock.getTownOrNull();
            if (TownySettings.getKeepInventoryInOwnTown() && town2.equals((Object)town)) {
                bl = true;
            }
            if (TownySettings.getKeepInventoryInAlliedTowns() && !bl && town2.isAlliedWith(town)) {
                bl = true;
            }
        }
        if (TownySettings.getKeepInventoryInArenas() && !bl && townBlock.getType() == TownBlockType.ARENA) {
            bl = true;
        }
        return bl;
    }
}

