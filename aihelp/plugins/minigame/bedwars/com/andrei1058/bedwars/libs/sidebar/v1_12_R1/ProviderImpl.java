/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.ChatComponentText
 *  net.minecraft.server.v1_12_R1.IScoreboardCriteria
 *  net.minecraft.server.v1_12_R1.Packet
 *  net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore
 *  net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore$EnumScoreboardAction
 *  net.minecraft.server.v1_12_R1.PlayerConnection
 *  net.minecraft.server.v1_12_R1.ScoreboardBaseCriteria
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_12_R1;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.SidebarProvider;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import com.andrei1058.bedwars.libs.sidebar.v1_12_R1.PlayerListImpl;
import com.andrei1058.bedwars.libs.sidebar.v1_12_R1.SidebarImpl;
import java.lang.reflect.Field;
import java.util.Collection;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.IScoreboardCriteria;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.ScoreboardBaseCriteria;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProviderImpl
extends SidebarProvider {
    private static SidebarProvider instance;
    private Field headerM;
    private Field footerM;
    private Field scorePlayerName;
    private Field scoreObjectiveName;
    private Field scoreScore;
    private Field scoreAction;

    @Override
    public Sidebar createSidebar(SidebarLine title, Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProviders) {
        return new SidebarImpl(title, lines, placeholderProviders);
    }

    @Override
    public SidebarObjective createObjective(@NotNull WrappedSidebar sidebar, String name, boolean health, SidebarLine title, int type) {
        return ((SidebarImpl)sidebar).createObjective(name, (IScoreboardCriteria)(health ? new ScoreboardBaseCriteria("health") : IScoreboardCriteria.b), title, type);
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
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore();
        if (null == this.scorePlayerName) {
            try {
                this.scorePlayerName = PacketPlayOutScoreboardScore.class.getDeclaredField("a");
                this.scorePlayerName.setAccessible(true);
                this.scoreObjectiveName = PacketPlayOutScoreboardScore.class.getDeclaredField("b");
                this.scoreObjectiveName.setAccessible(true);
                this.scoreScore = PacketPlayOutScoreboardScore.class.getDeclaredField("c");
                this.scoreScore.setAccessible(true);
                this.scoreAction = PacketPlayOutScoreboardScore.class.getDeclaredField("d");
                this.scoreAction.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        try {
            this.scorePlayerName.set(packetPlayOutScoreboardScore, playerName);
            this.scoreObjectiveName.set(packetPlayOutScoreboardScore, sidebar.getHealthObjective().getName());
            this.scoreScore.setInt(packetPlayOutScoreboardScore, score);
            this.scoreAction.set(packetPlayOutScoreboardScore, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        try {
            if (null == this.headerM) {
                this.headerM = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("a");
                this.headerM.setAccessible(true);
            }
            if (null == this.footerM) {
                this.footerM = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
                this.footerM.setAccessible(true);
            }
            this.headerM.set(packet, new ChatComponentText(header));
            this.footerM.set(packet, new ChatComponentText(footer));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    public static SidebarProvider getInstance() {
        return null == instance ? (instance = new ProviderImpl()) : instance;
    }
}

