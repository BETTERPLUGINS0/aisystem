/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketModelEntity;
import org.bukkit.Location;

public interface PacketTextEntity
extends PacketModelEntity {
    public void setText(String var1);

    public void setTextVisible(boolean var1);

    public void initializeText(Location var1);
}

