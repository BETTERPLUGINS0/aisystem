/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package me.zombie_striker.qav.hooks.worldguard.selection;

import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
import org.bukkit.Location;

public interface IPolygonalSelection
extends ISelection {
    public Set<Location> getPoints();

    public int getMinimumY();

    public int getMaximumY();
}

