/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.ChatComponentText
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatMutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$a
 *  net.minecraft.world.scores.ScoreboardTeam
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumTeamPush
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_17_R1;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerListImpl
extends ScoreboardTeam
implements VersionedTabGroup {
    private final SidebarLine prefix;
    private ChatComponentText prefixComponent = new ChatComponentText("");
    private final SidebarLine suffix;
    private ChatComponentText suffixComponent = new ChatComponentText("");
    private final WrappedSidebar sidebar;
    private final String id;
    private ScoreboardTeamBase.EnumNameTagVisibility nameTagVisibility;
    private Player papiSubject = null;
    private ScoreboardTeamBase.EnumTeamPush pushingRule;
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

    public void setPrefix(@javax.annotation.Nullable IChatBaseComponent var0) {
    }

    public ScoreboardTeamBase.EnumTeamPush getCollisionRule() {
        return this.pushingRule;
    }

    public IChatMutableComponent d() {
        return new ChatComponentText(this.id);
    }

    public IChatMutableComponent getFormattedName(IChatBaseComponent var0) {
        return new ChatComponentText(this.prefixComponent.h() + var0 + this.suffixComponent.h());
    }

    public IChatBaseComponent getPrefix() {
        return this.prefixComponent;
    }

    public IChatBaseComponent getSuffix() {
        return this.suffixComponent;
    }

    public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility enumNameTagVisibility) {
        this.nameTagVisibility = enumNameTagVisibility;
    }

    public ScoreboardTeamBase.EnumNameTagVisibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    @Override
    public void add(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.a);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendCreateToPlayer(Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (boolean)true);
        ((CraftPlayer)player).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam);
    }

    @Override
    public void remove(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.b);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUserCreateToReceivers(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.a);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUpdateToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (boolean)false);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendRemoveToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.sendPacket((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public boolean refreshContent() {
        String newPrefix = this.prefix.getTrimReplacePlaceholders(this.getSubject(), 16, this.placeholders);
        String newSuffix = this.suffix.getTrimReplacePlaceholders(this.getSubject(), 16, this.placeholders);
        if (newPrefix.equals(this.prefixComponent.h()) && newSuffix.equals(this.suffixComponent.h())) {
            return false;
        }
        this.prefixComponent = new ChatComponentText(newPrefix);
        this.suffixComponent = new ChatComponentText(newSuffix);
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
    public void setPushingRule(@NotNull PlayerTab.PushingRule rule) {
        switch (rule) {
            case NEVER: {
                this.pushingRule = ScoreboardTeamBase.EnumTeamPush.b;
                break;
            }
            case ALWAYS: {
                this.pushingRule = ScoreboardTeamBase.EnumTeamPush.a;
                break;
            }
            case PUSH_OTHER_TEAMS: {
                this.pushingRule = ScoreboardTeamBase.EnumTeamPush.c;
                break;
            }
            case PUSH_OWN_TEAM: {
                this.pushingRule = ScoreboardTeamBase.EnumTeamPush.d;
            }
        }
        if (null != this.id) {
            this.sendUpdateToReceivers();
        }
    }

    @Override
    public void setNameTagVisibility(@NotNull PlayerTab.NameTagVisibility nameTagVisibility) {
        switch (nameTagVisibility) {
            case NEVER: {
                this.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.b;
                break;
            }
            case ALWAYS: {
                this.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.a;
                break;
            }
            case HIDE_FOR_OTHER_TEAMS: {
                this.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.c;
                break;
            }
            case HIDE_FOR_OWN_TEAM: {
                this.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.d;
            }
        }
        if (null != this.id) {
            this.sendUpdateToReceivers();
        }
    }
}

