/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundBundlePacket
 *  net.minecraft.server.level.EntityPlayer
 *  org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketBundle
implements AbstractPacketBundle {
    private final List<PacketBundleEntry> entries = new ArrayList<PacketBundleEntry>();
    private static final int MAX_PACKETS_PER_BUNDLE = 3000;

    @Override
    public void addPacket(Object packet, List<Player> viewers) {
        this.entries.add(new PacketBundleEntry((Packet)packet, viewers));
    }

    @Override
    public void send() {
        HashMap<Player, List> playerPackets = new HashMap<Player, List>();
        for (PacketBundleEntry entry : this.entries) {
            if (entry.viewers().isEmpty() || !this.isClientGamePacket(entry.packet())) continue;
            Packet<?> clientPacket = entry.packet();
            for (Player viewer : entry.viewers()) {
                playerPackets.computeIfAbsent(viewer, k -> new ArrayList()).add(clientPacket);
            }
        }
        playerPackets.forEach((player, packets) -> {
            if (packets.isEmpty() || player == null || !player.isOnline()) {
                return;
            }
            for (int i = 0; i < packets.size(); i += 3000) {
                int end = Math.min(i + 3000, packets.size());
                List chunk = packets.subList(i, end);
                ClientboundBundlePacket bundle = new ClientboundBundlePacket(new HashSet(chunk));
                this.sendPacketBundle((Player)player, (Packet<?>)bundle);
            }
        });
        int bundleCount = playerPackets.values().stream().mapToInt(packets -> (packets.size() + 3000 - 1) / 3000).sum();
    }

    private boolean isClientGamePacket(Packet<?> packet) {
        return packet != null;
    }

    private void sendPacketBundle(Player player, Packet<?> nmsPacket) {
        if (nmsPacket == null) {
            return;
        }
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        nmsPlayer.c.b(nmsPacket);
    }

    private record PacketBundleEntry(Packet<?> packet, List<Player> viewers) {
    }
}

