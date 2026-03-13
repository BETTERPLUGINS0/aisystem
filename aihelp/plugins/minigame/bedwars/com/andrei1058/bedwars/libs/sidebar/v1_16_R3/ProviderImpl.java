/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_16_R3.ChatComponentText
 *  net.minecraft.server.v1_16_R3.IScoreboardCriteria
 *  net.minecraft.server.v1_16_R3.Packet
 *  net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore
 *  net.minecraft.server.v1_16_R3.PlayerConnection
 *  net.minecraft.server.v1_16_R3.ScoreboardObjective
 *  net.minecraft.server.v1_16_R3.ScoreboardServer$Action
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_16_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.SidebarProvider;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import com.andrei1058.bedwars.libs.sidebar.v1_16_R3.PlayerListImpl;
import com.andrei1058.bedwars.libs.sidebar.v1_16_R3.SidebarImpl;
import java.util.Collection;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.IScoreboardCriteria;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.ScoreboardObjective;
import net.minecraft.server.v1_16_R3.ScoreboardServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProviderImpl
extends SidebarProvider {
    private static SidebarProvider instance;

    @Override
    public Sidebar createSidebar(SidebarLine title, Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProviders) {
        return new SidebarImpl(title, lines, placeholderProviders);
    }

    @Override
    public SidebarObjective createObjective(@NotNull WrappedSidebar sidebar, String name, boolean health, SidebarLine title, int type) {
        return ((SidebarImpl)sidebar).createObjective(name, health ? IScoreboardCriteria.HEALTH : IScoreboardCriteria.DUMMY, title, type);
    }

    @Override
    public ScoreLine createScoreLine(WrappedSidebar sidebar, SidebarLine line, int score, String color) {
        return ((SidebarImpl)sidebar).createScore(line, score, color);
    }

    @Override
    public void sendScore(@NotNull WrappedSidebar sidebar, String playerName, int score) {
        if (sidebar.getHealthObjective() == null) {
            return;
        }
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, ((ScoreboardObjective)sidebar.getHealthObjective()).getName(), playerName, score);
        for (Player player : sidebar.getReceivers()) {
            PlayerConnection playerConnection = ((CraftPlayer)player).getHandle().playerConnection;
            playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore);
        }
    }

    @Override
    public VersionedTabGroup createPlayerTab(WrappedSidebar sidebar, String identifier, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule, PlayerTab.NameTagVisibility nameTagVisibility, @Nullable Collection<PlaceholderProvider> placeholders) {
        return new PlayerListImpl(sidebar, identifier, prefix, suffix, pushingRule, nameTagVisibility, placeholders);
    }

    @Override
    public void sendHeaderFooter(Player player, String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.header = new ChatComponentText(header);
        packet.footer = new ChatComponentText(footer);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    public static SidebarProvider getInstance() {
        return null == instance ? (instance = new ProviderImpl()) : instance;
    }
}

