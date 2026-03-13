/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks;

import org.bukkit.entity.Player;

public interface PermissionHook {
    public boolean removePerm(Player var1, String var2);

    public boolean addPerm(Player var1, String var2);

    public boolean isPermEnabled();
}

