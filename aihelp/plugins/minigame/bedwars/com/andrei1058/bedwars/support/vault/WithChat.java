/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.chat.Chat
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.vault;

import com.andrei1058.bedwars.support.vault.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WithChat
implements Chat {
    private static net.milkbowl.vault.chat.Chat chat;

    @Override
    public String getPrefix(Player p) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)chat.getPlayerPrefix(p));
    }

    @Override
    public String getSuffix(Player p) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)chat.getPlayerSuffix(p));
    }

    public static void setChat(net.milkbowl.vault.chat.Chat chat) {
        WithChat.chat = chat;
    }
}

