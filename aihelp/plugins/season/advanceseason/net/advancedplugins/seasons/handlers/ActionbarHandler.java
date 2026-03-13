/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.WrappedChatComponent
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ActionbarHandler {
    private static HashMap<UUID, Long> actionbarCooldown = new HashMap();

    public ActionbarHandler(JavaPlugin javaPlugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)javaPlugin, ListenerPriority.MONITOR, new PacketType[]{PacketType.Play.Server.SET_ACTION_BAR_TEXT, PacketType.Play.Server.SYSTEM_CHAT}){

            public void onPacketSending(PacketEvent packetEvent) {
                try {
                    WrappedChatComponent wrappedChatComponent;
                    String string;
                    boolean bl;
                    Player player = packetEvent.getPlayer();
                    boolean bl2 = bl = packetEvent.getPacketType().equals((Object)PacketType.Play.Server.SYSTEM_CHAT) ? (Boolean)packetEvent.getPacket().getBooleans().read(0) : true;
                    if (bl && !(string = (wrappedChatComponent = (WrappedChatComponent)packetEvent.getPacket().getChatComponents().read(0)).getJson()).startsWith("{\"text\":\"\",\"extra\":[{\"text\":\" \",\"italic\":true,\"color\":\"yellow\"},{\"text\":\" \",\"color\":\"green\"},")) {
                        actionbarCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 3000L);
                    }
                } catch (Exception exception) {
                    // empty catch block
                }
            }
        });
    }

    public static boolean canSendActionbar(Player player) {
        Long l = actionbarCooldown.get(player.getUniqueId());
        if (l == null) {
            return true;
        }
        return l < System.currentTimeMillis();
    }
}

