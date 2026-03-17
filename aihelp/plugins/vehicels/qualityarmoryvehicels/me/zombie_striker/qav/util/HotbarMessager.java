/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.util;

import me.zombie_striker.qav.util.xseries.messages.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HotbarMessager {
    public static void sendHotBarMessage(Player player, String string) {
        ActionBar.sendActionBar(player, ChatColor.translateAlternateColorCodes((char)'&', (String)string));
    }
}

