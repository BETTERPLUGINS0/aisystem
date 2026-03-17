/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionHook {
    public boolean canMove(Player var1, Location var2);

    public boolean canPlace(Player var1, Location var2);

    public boolean canRemove(Player var1, Location var2);
}

