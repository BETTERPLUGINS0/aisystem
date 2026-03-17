/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.m56738.smoothcoasters.api;

import me.m56738.smoothcoasters.api.NetworkInterface;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DefaultNetworkInterface
implements NetworkInterface {
    private final Plugin plugin;

    public DefaultNetworkInterface(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(Player player, String string, byte[] byArray) {
        player.sendPluginMessage(this.plugin, string, byArray);
    }
}

