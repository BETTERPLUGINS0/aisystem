/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_8_R3.ScoreboardTeam
 *  net.minecraft.server.v1_8_R3.ScoreboardTeamBase$EnumNameTagVisibility
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_8_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerListImpl
extends ScoreboardTeam
implements VersionedTabGroup {
    private final SidebarLine prefix;
    public String prefixString = "";
    private final SidebarLine suffix;
    public String suffixString = "";
    private final WrappedSidebar sidebar;
    private final String id;
    private ScoreboardTeamBase.EnumNameTagVisibility nameTagVisibility;
    private Player papiSubject = null;
    private final Collection<PlaceholderProvider> placeholders;

    public PlayerListImpl(@NotNull WrappedSidebar sidebar, String identifier, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule, PlayerTab.NameTagVisibility nameTagVisibility, @Nullable Collection<PlaceholderProvider> placeholders) {
        super(null, identifier);
        this.suffix = suffix;
        this.prefix = prefix;
        this.sidebar = sidebar;
        this.setPushingRule(pushingRule);
        this.setNameTagVisibility(nameTagVisibility);
        this.id = identifier;
        this.placeholders = placeholders;
    }

    public void setPrefix(String var0) {
    }

    public String getFormattedName(String var0) {
        return this.getPrefix().concat(var0).concat(this.getSuffix());
    }

    public String getPrefix() {
        return this.prefixString;
    }

    public String getSuffix() {
        return this.suffixString;
    }

    public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility enumNameTagVisibility) {
        this.nameTagVisibility = enumNameTagVisibility;
    }

    public ScoreboardTeamBase.EnumNameTagVisibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    @Override
    public void add(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, Collections.singleton(player.getName()), 3);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendCreateToPlayer(Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, 0);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam);
    }

    @Override
    public void remove(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, Collections.singleton(player.getName()), 4);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUserCreateToReceivers(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, Collections.singleton(player.getName()), 3);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUpdateToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, 2);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendRemoveToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this, 1);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public boolean refreshContent() {
        String newPrefix = this.prefix.getTrimReplacePlaceholders(this.getSubject(), 16, this.placeholders);
        String newSuffix = this.suffix.getTrimReplacePlaceholders(this.getSubject(), 16, this.placeholders);
        if (newPrefix.equals(this.prefixString) && newSuffix.equals(this.suffixString)) {
            return false;
        }
        this.prefixString = newPrefix;
        this.suffixString = newSuffix;
        return true;
    }

    @Override
    public String getIdentifier() {
        return this.id;
    }

    @Override
    public void setSubject(@javax.annotation.Nullable Player papiSubject) {
        this.papiSubject = papiSubject;
    }

    @Override
    @Nullable
    public Player getSubject() {
        return this.papiSubject;
    }

    @Override
    public void setPushingRule(PlayerTab.PushingRule rule) {
    }

    @Override
    public void setNameTagVisibility(@NotNull PlayerTab.NameTagVisibility nameTagVisibility) {
        this.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.valueOf((String)nameTagVisibility.toString());
        if (null != this.id) {
            this.sendUpdateToReceivers();
        }
    }
}

