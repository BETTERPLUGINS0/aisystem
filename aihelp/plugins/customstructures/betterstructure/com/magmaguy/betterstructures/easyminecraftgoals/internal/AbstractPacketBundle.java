/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import java.util.List;
import org.bukkit.entity.Player;

public interface AbstractPacketBundle {
    public void addPacket(Object var1, List<Player> var2);

    public void send();
}

