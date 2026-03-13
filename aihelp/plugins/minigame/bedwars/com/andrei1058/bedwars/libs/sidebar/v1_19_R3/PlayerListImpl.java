/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatMutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$a
 *  net.minecraft.world.scores.ScoreboardTeam
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumTeamPush
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_19_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerListImpl
extends ScoreboardTeam
implements VersionedTabGroup {
    private ScoreboardTeamBase.EnumTeamPush pushingRule;
    private final SidebarLine prefix;
    private IChatMutableComponent prefixComp = IChatBaseComponent.b((String)"");
    private final SidebarLine suffix;
    private IChatMutableComponent suffixComp = IChatBaseComponent.b((String)"");
    private final WrappedSidebar sidebar;
    private final String id;
    private ScoreboardTeamBase.EnumNameTagVisibility nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.a;
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

    public void b(@javax.annotation.Nullable IChatBaseComponent var0) {
    }

    public ScoreboardTeamBase.EnumTeamPush l() {
        return this.pushingRule;
    }

    public IChatMutableComponent d() {
        return IChatBaseComponent.b((String)this.id);
    }

    public IChatMutableComponent d(IChatBaseComponent var0) {
        return IChatBaseComponent.b((String)(this.prefixComp.getString() + var0 + this.suffixComp.getString()));
    }

    public String b() {
        return this.getIdentifier();
    }

    public IChatBaseComponent e() {
        return this.prefixComp;
    }

    public void c(@javax.annotation.Nullable IChatBaseComponent var0) {
    }

    public IChatBaseComponent f() {
        return this.suffixComp;
    }

    public void a(boolean b2) {
    }

    public void b(boolean b2) {
    }

    public void a(ScoreboardTeamBase.EnumNameTagVisibility enumNameTagVisibility) {
        this.nameTagVisibility = enumNameTagVisibility;
    }

    public ScoreboardTeamBase.EnumNameTagVisibility j() {
        return this.nameTagVisibility;
    }

    @Override
    public void add(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.a);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.a((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendCreateToPlayer(Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (boolean)true);
        ((CraftPlayer)player).getHandle().b.a((Packet)packetPlayOutScoreboardTeam);
    }

    @Override
    public void remove(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.b);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.a((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUserCreateToReceivers(@NotNull Player player) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (String)player.getName(), (PacketPlayOutScoreboardTeam.a)PacketPlayOutScoreboardTeam.a.a);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.a((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendUpdateToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this, (boolean)false);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.a((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public void sendRemoveToReceivers() {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this);
        this.sidebar.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().b.a((Packet)packetPlayOutScoreboardTeam));
    }

    @Override
    public boolean refreshContent() {
        String newPrefix = this.prefix.getTrimReplacePlaceholders(this.getSubject(), 32, this.placeholders);
        String newSuffix = this.suffix.getTrimReplacePlaceholders(this.getSubject(), 32, this.placeholders);
        if (newPrefix.equals(this.prefixComp.getString()) && newSuffix.equals(this.suffixComp.getString())) {
            return false;
        }
        this.prefixComp = IChatBaseComponent.b((String)newPrefix);
        this.suffixComp = IChatBaseComponent.b((String)newSuffix);
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

