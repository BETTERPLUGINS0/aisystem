/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  org.bukkit.block.Biome
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.biomes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.util.Arrays;
import org.bukkit.block.Biome;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomeDrawer {
    public BiomeDrawer(JavaPlugin javaPlugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)javaPlugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.MAP_CHUNK}){

            public void onPacketSending(PacketEvent packetEvent) {
                PacketContainer packetContainer = packetEvent.getPacket();
                int[] nArray = (int[])packetContainer.getIntegerArrays().read(0);
                Arrays.fill(nArray, Biome.PLAINS.ordinal());
                packetContainer.getIntegerArrays().write(0, (Object)nArray);
            }
        });
    }
}

