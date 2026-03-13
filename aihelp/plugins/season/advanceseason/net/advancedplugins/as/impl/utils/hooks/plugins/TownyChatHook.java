/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.palmergames.bukkit.TownyChat.Chat
 *  com.palmergames.bukkit.TownyChat.channels.Channel
 *  com.palmergames.bukkit.TownyChat.channels.channelTypes
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;
import com.palmergames.bukkit.TownyChat.channels.channelTypes;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.entity.Player;

public class TownyChatHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.TOWNYCHAT.getPluginName();
    }

    public boolean isInTownyChannel(Player player) {
        Channel channel = Chat.getTownyChat().getPlayerChannel(player);
        return channel != null && channel.getType() != channelTypes.GLOBAL;
    }
}

