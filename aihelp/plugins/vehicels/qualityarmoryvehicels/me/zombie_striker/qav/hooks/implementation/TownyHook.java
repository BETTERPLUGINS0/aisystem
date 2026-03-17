/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.palmergames.bukkit.towny.object.TownyPermission$ActionType
 *  com.palmergames.bukkit.towny.utils.PlayerCacheUtil
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks.implementation;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import me.zombie_striker.qav.hooks.ProtectionHook;
import me.zombie_striker.qav.util.BlockCollisionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TownyHook
implements ProtectionHook {
    @Override
    public boolean canMove(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission((Player)player, (Location)location, (Material)BlockCollisionUtil.getMaterial(location), (TownyPermission.ActionType)TownyPermission.ActionType.ITEM_USE);
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission((Player)player, (Location)location, (Material)BlockCollisionUtil.getMaterial(location), (TownyPermission.ActionType)TownyPermission.ActionType.BUILD);
    }

    @Override
    public boolean canRemove(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission((Player)player, (Location)location, (Material)BlockCollisionUtil.getMaterial(location), (TownyPermission.ActionType)TownyPermission.ActionType.DESTROY);
    }
}

