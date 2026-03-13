/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.ChatComponentText
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore
 *  net.minecraft.server.ScoreboardServer$Action
 *  net.minecraft.server.network.PlayerConnection
 *  net.minecraft.world.scores.ScoreboardObjective
 *  net.minecraft.world.scores.criteria.IScoreboardCriteria
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_17_R1;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.SidebarProvider;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import com.andrei1058.bedwars.libs.sidebar.v1_17_R1.PlayerListImpl;
import com.andrei1058.bedwars.libs.sidebar.v1_17_R1.SidebarImpl;
import java.util.Collection;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
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
        return ((SidebarImpl)sidebar).createObjective(name, health ? IScoreboardCriteria.f : IScoreboardCriteria.a, title, type);
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
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.a, ((ScoreboardObjective)sidebar.getHealthObjective()).getName(), playerName, score);
        for (Player player : sidebar.getReceivers()) {
            PlayerConnection playerConnection = ((CraftPlayer)player).getHandle().b;
            playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore);
        }
    }

    @Override
    public VersionedTabGroup createPlayerTab(WrappedSidebar sidebar, String identifier, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule, PlayerTab.NameTagVisibility nameTagVisibility, @Nullable Collection<PlaceholderProvider> placeholders) {
        return new PlayerListImpl(sidebar, identifier, prefix, suffix, pushingRule, nameTagVisibility, placeholders);
    }

    @Override
    public void sendHeaderFooter(Player player, String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter((IChatBaseComponent)new ChatComponentText(header), (IChatBaseComponent)new ChatComponentText(footer));
        ((CraftPlayer)player).getHandle().b.sendPacket((Packet)packet);
    }

    public static SidebarProvider getInstance() {
        return null == instance ? (instance = new ProviderImpl()) : instance;
    }
}

