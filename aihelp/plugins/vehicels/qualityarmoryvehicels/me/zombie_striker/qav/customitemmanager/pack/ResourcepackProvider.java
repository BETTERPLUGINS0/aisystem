/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.customitemmanager.pack;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface ResourcepackProvider {
    public String getFor(@Nullable Player var1);

    public Object serialize();
}

