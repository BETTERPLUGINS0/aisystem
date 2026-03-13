/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.EnumChatFormat
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatMutableComponent
 *  net.minecraft.network.chat.numbers.FixedFormat
 *  net.minecraft.network.chat.numbers.NumberFormat
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundResetScorePacket
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore
 *  net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.network.PlayerConnection
 *  net.minecraft.world.scores.DisplaySlot
 *  net.minecraft.world.scores.ScoreboardObjective
 *  net.minecraft.world.scores.ScoreboardScore
 *  net.minecraft.world.scores.ScoreboardTeam
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.world.scores.ScoreboardTeamBase$EnumTeamPush
 *  net.minecraft.world.scores.criteria.IScoreboardCriteria
 *  net.minecraft.world.scores.criteria.IScoreboardCriteria$EnumScoreboardHealthDisplay
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_20_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.chat.numbers.FixedFormat;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardScore;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidebarImpl
extends WrappedSidebar {
    public SidebarImpl(@NotNull SidebarLine title, @NotNull Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProvider) {
        super(title, lines, placeholderProvider);
    }

    public ScoreLine createScore(SidebarLine line, int score, String color) {
        return new NarniaScoreLine(line, score, color);
    }

    public SidebarObjective createObjective(String name, IScoreboardCriteria iScoreboardCriteria, SidebarLine title, int type) {
        return new NarniaSidebarObjective(name, iScoreboardCriteria, title, type);
    }

    public class NarniaScoreLine
    extends ScoreboardScore
    implements ScoreLine,
    Comparable<ScoreLine> {
        private int score;
        private IChatMutableComponent prefix = IChatBaseComponent.b((String)" ");
        private IChatMutableComponent suffix = IChatBaseComponent.b((String)" ");
        private final TeamLine team;
        private SidebarLine text;
        private final String color;

        public NarniaScoreLine(SidebarLine text, @NotNull int score, String color) {
            this.score = score;
            this.text = text;
            this.team = new TeamLine(color);
            this.color = color;
        }

        public void a(int score) {
            this.score = score;
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getColor(), SidebarImpl.this.getSidebarObjective().getName(), score, null, (NumberFormat)new FixedFormat((IChatBaseComponent)IChatBaseComponent.b((String)this.text.getTrimReplacePlaceholdersScore(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders()))));
            SidebarImpl.this.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().c.b((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public SidebarLine getLine() {
            return this.text;
        }

        @Override
        public void setLine(SidebarLine line) {
            this.text = line;
        }

        @Override
        public int getScoreAmount() {
            return this.score;
        }

        @Override
        public void setScoreAmount(int score) {
            this.a(score);
        }

        @Override
        public void sendCreateToAllReceivers() {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team, (boolean)true);
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().c.b((Packet)packetPlayOutScoreboardTeam));
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getColor(), SidebarImpl.this.getSidebarObjective().getName(), this.getScoreAmount(), null, (NumberFormat)new FixedFormat((IChatBaseComponent)IChatBaseComponent.b((String)this.text.getTrimReplacePlaceholdersScore(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders()))));
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().c.b((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public void sendCreate(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().c;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team, (boolean)true);
            conn.b((Packet)packetPlayOutScoreboardTeam);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getColor(), SidebarImpl.this.getSidebarObjective().getName(), this.getScoreAmount(), null, (NumberFormat)new FixedFormat((IChatBaseComponent)IChatBaseComponent.b((String)this.text.getTrimReplacePlaceholdersScore(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders()))));
            conn.b((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        public void sendRemove(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().c;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team);
            ClientboundResetScorePacket resetScore = new ClientboundResetScorePacket(this.team.b(), SidebarImpl.this.getSidebarObjective().getName());
            conn.b((Packet)resetScore);
            conn.b((Packet)packetPlayOutScoreboardTeam);
        }

        @Override
        public void sendRemoveToAllReceivers() {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team);
            ClientboundResetScorePacket resetScore = new ClientboundResetScorePacket(this.team.b(), SidebarImpl.this.getSidebarObjective().getName());
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().c.b((Packet)resetScore));
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().c.b((Packet)packetPlayOutScoreboardTeam));
        }

        @Override
        public void sendUpdate(Player player) {
            PacketPlayOutScoreboardTeam packetTeamUpdate = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team, (boolean)false);
            ((CraftPlayer)player).getHandle().c.b((Packet)packetTeamUpdate);
        }

        @Override
        @Contract(pure=true)
        public boolean setContent(@NotNull SidebarLine line) {
            IChatMutableComponent oldPrefix = this.prefix;
            IChatMutableComponent oldSuffix = this.suffix;
            String content = line.getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders());
            if (content.length() > 256) {
                this.prefix = IChatBaseComponent.b((String)content.substring(0, 256));
                if (this.prefix.getString().charAt(255) == '\u00a7') {
                    this.prefix = IChatBaseComponent.b((String)content.substring(0, 255));
                    this.setSuffix(content.substring(255));
                } else {
                    this.setSuffix(content.substring(256));
                }
            } else {
                this.prefix = IChatBaseComponent.b((String)content);
                this.suffix = IChatBaseComponent.b((String)"");
            }
            return !oldPrefix.equals((Object)this.prefix) || !oldSuffix.equals((Object)this.suffix);
        }

        public void setSuffix(@NotNull String secondPart) {
            if (((String)secondPart).isEmpty()) {
                this.suffix = IChatBaseComponent.b((String)"");
                return;
            }
            secondPart = ChatColor.getLastColors((String)this.prefix.getString()) + (String)secondPart;
            this.suffix = IChatBaseComponent.b((String)(((String)secondPart).length() > 256 ? ((String)secondPart).substring(0, 256) : secondPart));
        }

        @Override
        public void sendUpdateToAllReceivers() {
            PacketPlayOutScoreboardTeam packetTeamUpdate = PacketPlayOutScoreboardTeam.a((ScoreboardTeam)this.team, (boolean)false);
            SidebarImpl.this.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().c.b((Packet)packetTeamUpdate));
        }

        @Override
        public int compareTo(@NotNull ScoreLine o) {
            return Integer.compare(this.score, o.getScoreAmount());
        }

        public int a() {
            return this.score;
        }

        @Override
        public String getColor() {
            return this.color.charAt(0) == '\u00a7' ? this.color : "\u00a7" + this.color;
        }

        @Override
        public boolean refreshContent() {
            return this.setContent(this.getLine());
        }

        private class TeamLine
        extends ScoreboardTeam {
            public TeamLine(String color) {
                super(null, color);
                this.g().add(color);
            }

            @Contract(value=" -> new", pure=true)
            @NotNull
            public IChatBaseComponent e() {
                return NarniaScoreLine.this.prefix;
            }

            public void b(@Nullable IChatBaseComponent var0) {
            }

            public void c(@Nullable IChatBaseComponent var0) {
            }

            @Contract(value=" -> new", pure=true)
            @NotNull
            public IChatBaseComponent f() {
                return NarniaScoreLine.this.suffix;
            }

            public void a(boolean var0) {
            }

            public void b(boolean var0) {
            }

            public void a(ScoreboardTeamBase.EnumNameTagVisibility var0) {
            }

            public void a(ScoreboardTeamBase.EnumTeamPush var0) {
            }

            public void a(EnumChatFormat var0) {
            }

            @Contract(value="_ -> new", pure=true)
            @NotNull
            public IChatMutableComponent d(IChatBaseComponent var0) {
                return IChatBaseComponent.b((String)(NarniaScoreLine.this.prefix.getString() + var0.getString() + NarniaScoreLine.this.suffix.getString()));
            }
        }
    }

    protected class NarniaSidebarObjective
    extends ScoreboardObjective
    implements SidebarObjective {
        private SidebarLine displayName;
        private IChatMutableComponent displayNameComp;
        private final DisplaySlot type;

        public NarniaSidebarObjective(String name, IScoreboardCriteria criteria, SidebarLine displayName, int type) {
            super(null, name, criteria, (IChatBaseComponent)IChatBaseComponent.b((String)name), IScoreboardCriteria.EnumScoreboardHealthDisplay.a, false, null);
            this.displayNameComp = IChatBaseComponent.b((String)" ");
            this.displayName = displayName;
            this.type = DisplaySlot.values()[type];
        }

        @Override
        public void setTitle(SidebarLine title) {
            this.displayName = title;
        }

        @Override
        public SidebarLine getTitle() {
            return this.displayName;
        }

        @Override
        public void sendCreate(Player player) {
            this.sendCreate(((CraftPlayer)player).getHandle().c);
        }

        @Override
        public void sendRemove(Player player) {
            this.sendRemove(((CraftPlayer)player).getHandle().c);
        }

        @Override
        public String getName() {
            return this.b();
        }

        @Override
        public boolean refreshTitle() {
            String newTitle = this.displayName.getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), 256, SidebarImpl.this.getPlaceholders());
            if (newTitle.equals(this.displayNameComp.getString())) {
                return false;
            }
            this.displayNameComp = IChatBaseComponent.b((String)newTitle);
            return true;
        }

        public IChatBaseComponent d() {
            return this.displayNameComp;
        }

        public void a(IChatBaseComponent var0) {
        }

        public IChatBaseComponent g() {
            return IChatBaseComponent.b((String)this.d().toString());
        }

        public void a(IScoreboardCriteria.EnumScoreboardHealthDisplay var0) {
        }

        private void sendCreate(@NotNull PlayerConnection playerConnection) {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 0);
            playerConnection.b((Packet)packetPlayOutScoreboardObjective);
            PacketPlayOutScoreboardDisplayObjective packetPlayOutScoreboardDisplayObjective = new PacketPlayOutScoreboardDisplayObjective(this.type, (ScoreboardObjective)this);
            playerConnection.b((Packet)packetPlayOutScoreboardDisplayObjective);
            if (this.b().equalsIgnoreCase("health")) {
                PacketPlayOutScoreboardDisplayObjective packetPlayOutScoreboardDisplayObjective2 = new PacketPlayOutScoreboardDisplayObjective(DisplaySlot.a, (ScoreboardObjective)this);
                playerConnection.b((Packet)packetPlayOutScoreboardDisplayObjective2);
            }
        }

        @Override
        public void sendUpdate() {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 2);
            SidebarImpl.this.getReceivers().forEach(player -> ((CraftPlayer)player).getHandle().c.b((Packet)packetPlayOutScoreboardObjective));
        }

        public void sendRemove(@NotNull PlayerConnection playerConnection) {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 1);
            playerConnection.b((Packet)packetPlayOutScoreboardObjective);
        }
    }
}

