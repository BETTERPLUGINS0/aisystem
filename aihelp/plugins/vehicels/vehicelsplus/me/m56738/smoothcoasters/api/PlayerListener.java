/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRegisterChannelEvent
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package me.m56738.smoothcoasters.api;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.logging.Level;
import me.m56738.smoothcoasters.api.PlayerEntry;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import me.m56738.smoothcoasters.api.Util;
import me.m56738.smoothcoasters.api.event.PlayerSmoothCoastersHandshakeEvent;
import me.m56738.smoothcoasters.api.implementation.Implementation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PlayerListener
implements Listener,
PluginMessageListener {
    private static final String CHANNEL = "smoothcoasters:hs";
    private final SmoothCoastersAPI api;

    public PlayerListener(SmoothCoastersAPI smoothCoastersAPI) {
        this.api = smoothCoastersAPI;
        Bukkit.getPluginManager().registerEvents((Listener)this, smoothCoastersAPI.getPlugin());
        Bukkit.getMessenger().registerIncomingPluginChannel(smoothCoastersAPI.getPlugin(), CHANNEL, (PluginMessageListener)this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(smoothCoastersAPI.getPlugin(), CHANNEL);
    }

    @EventHandler
    public void onPlayerRegisterChannel(PlayerRegisterChannelEvent playerRegisterChannelEvent) {
        if (!playerRegisterChannelEvent.getChannel().equals(CHANNEL)) {
            return;
        }
        Map<Byte, Implementation> map = this.api.getImplementations();
        byte[] byArray = new byte[map.size() + 1];
        byArray[0] = (byte)map.size();
        int n = 1;
        for (Byte by : map.keySet()) {
            byArray[n++] = by;
        }
        playerRegisterChannelEvent.getPlayer().sendPluginMessage(this.api.getPlugin(), CHANNEL, byArray);
    }

    public void onPluginMessageReceived(String string, Player player, byte[] byArray) {
        String string2;
        Implementation implementation;
        if (!string.equals(CHANNEL) || byArray.length < 1) {
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray);
        try {
            implementation = this.api.getImplementations().get(byteBuffer.get());
            string2 = byteBuffer.hasRemaining() ? Util.readString(byteBuffer, 32) : null;
        } catch (Exception exception) {
            this.api.getPlugin().getLogger().log(Level.SEVERE, "[SmoothCoastersAPI] Received invalid handshake from " + player.getName(), exception);
            return;
        }
        PlayerEntry playerEntry = this.api.getOrCreateEntry(player);
        playerEntry.setImplementation(implementation);
        playerEntry.setVersion(string2);
        if (implementation != null) {
            Bukkit.getPluginManager().callEvent((Event)new PlayerSmoothCoastersHandshakeEvent(player, implementation, string2));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        this.api.removeEntry(playerQuitEvent.getPlayer());
    }

    public void unregister() {
        HandlerList.unregisterAll((Listener)this);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this.api.getPlugin(), CHANNEL, (PluginMessageListener)this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.api.getPlugin(), CHANNEL);
    }
}

