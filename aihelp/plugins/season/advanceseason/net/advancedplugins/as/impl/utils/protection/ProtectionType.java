/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionType {
    public String getName();

    public boolean canBreak(Player var1, Location var2);

    public boolean canAttack(Player var1, Player var2);

    public boolean isProtected(Location var1);
}

