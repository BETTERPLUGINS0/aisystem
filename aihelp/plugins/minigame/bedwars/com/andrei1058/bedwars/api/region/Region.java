/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.andrei1058.bedwars.api.region;

import org.bukkit.Location;

public interface Region {
    public boolean isInRegion(Location var1);

    public boolean isProtected();
}

