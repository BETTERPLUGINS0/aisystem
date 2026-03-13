/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.libs.sidebar;

import org.bukkit.entity.Player;

public interface PAPISupport {
    public String replacePlaceholders(Player var1, String var2);

    public boolean hasPlaceholders(String var1);
}

