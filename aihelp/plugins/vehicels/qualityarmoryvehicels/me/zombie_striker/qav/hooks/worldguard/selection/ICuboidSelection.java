/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package me.zombie_striker.qav.hooks.worldguard.selection;

import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
import org.bukkit.Location;

public interface ICuboidSelection
extends ISelection {
    public Location getMinimumPoint();

    public Location getMaximumPoint();
}

