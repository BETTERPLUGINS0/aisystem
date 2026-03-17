/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.m56738.smoothcoasters.api;

import org.bukkit.entity.Player;

public interface NetworkInterface {
    public void sendMessage(Player var1, String var2, byte[] var3);
}

