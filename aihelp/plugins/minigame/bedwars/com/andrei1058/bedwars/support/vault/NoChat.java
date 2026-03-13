/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.vault;

import com.andrei1058.bedwars.support.vault.Chat;
import org.bukkit.entity.Player;

public class NoChat
implements Chat {
    @Override
    public String getPrefix(Player p) {
        return "";
    }

    @Override
    public String getSuffix(Player p) {
        return "";
    }
}

