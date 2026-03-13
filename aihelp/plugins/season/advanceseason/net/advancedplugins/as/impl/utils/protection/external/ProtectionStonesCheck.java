/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.espi.protectionstones.PSRegion
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.protection.external;

import dev.espi.protectionstones.PSRegion;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProtectionStonesCheck
implements ProtectionType {
    @Override
    public String getName() {
        return "ProtectionStones";
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        PSRegion pSRegion = PSRegion.fromLocation((Location)location);
        if (pSRegion == null) {
            return true;
        }
        return pSRegion.isMember(player.getUniqueId()) || pSRegion.isOwner(player.getUniqueId());
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

