/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package co.aikar.commands;

import co.aikar.commands.MessageFormatter;
import org.bukkit.ChatColor;

public class BukkitMessageFormatter
extends MessageFormatter<ChatColor> {
    public BukkitMessageFormatter(ChatColor ... chatColorArray) {
        super(chatColorArray);
    }

    @Override
    String format(ChatColor chatColor, String string) {
        return chatColor + string;
    }
}

