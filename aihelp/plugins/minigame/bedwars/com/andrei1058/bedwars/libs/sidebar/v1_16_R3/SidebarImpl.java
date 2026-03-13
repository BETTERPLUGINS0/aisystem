/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_16_R3.ChatComponentText
 *  net.minecraft.server.v1_16_R3.EnumChatFormat
 *  net.minecraft.server.v1_16_R3.IChatBaseComponent
 *  net.minecraft.server.v1_16_R3.IChatMutableComponent
 *  net.minecraft.server.v1_16_R3.IScoreboardCriteria
 *  net.minecraft.server.v1_16_R3.IScoreboardCriteria$EnumScoreboardHealthDisplay
 *  net.minecraft.server.v1_16_R3.Packet
 *  net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardDisplayObjective
 *  net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective
 *  net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore
 *  net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_16_R3.PlayerConnection
 *  net.minecraft.server.v1_16_R3.ScoreboardObjective
 *  net.minecraft.server.v1_16_R3.ScoreboardScore
 *  net.minecraft.server.v1_16_R3.ScoreboardServer$Action
 *  net.minecraft.server.v1_16_R3.ScoreboardTeam
 *  net.minecraft.server.v1_16_R3.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.server.v1_16_R3.ScoreboardTeamBase$EnumTeamPush
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_16_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatMutableComponent;
import net.minecraft.server.v1_16_R3.IScoreboardCriteria;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.ScoreboardObjective;
import net.minecraft.server.v1_16_R3.ScoreboardScore;
import net.minecraft.server.v1_16_R3.ScoreboardServer;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
        return new BucharestScoreLine(line, score, color);
    }

    public SidebarObjective createObjective(String name, IScoreboardCriteria iScoreboardCriteria, SidebarLine title, int type) {
        return new NarniaSidebarObjective(name, iScoreboardCriteria, title, type);
    }

    public class BucharestScoreLine
    extends ScoreboardScore
    implements ScoreLine,
    Comparable<ScoreLine> {
        private int score;
        private ChatComponentText prefixComponent;
        private ChatComponentText suffixComponent;
        private final TeamLine team;
        private SidebarLine text;

        public BucharestScoreLine(SidebarLine text, @NotNull int score, String color) {
            super(null, (ScoreboardObjective)SidebarImpl.this.getSidebarObjective(), color);
            this.prefixComponent = new ChatComponentText("");
            this.suffixComponent = new ChatComponentText("");
            this.score = score;
            this.text = text;
            this.team = new TeamLine(color);
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
            this.setScore(score);
        }

        @Override
        public void sendCreateToAllReceivers() {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 0);
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, ((ScoreboardObjective)SidebarImpl.this.getSidebarObjective()).getName(), this.getPlayerName(), this.getScoreAmount());
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public void sendCreate(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 0);
            conn.sendPacket((Packet)packetPlayOutScoreboardTeam);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, ((ScoreboardObjective)SidebarImpl.this.getSidebarObjective()).getName(), this.getPlayerName(), this.getScoreAmount());
            conn.sendPacket((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        public void sendRemove(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 1);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, ((ScoreboardObjective)SidebarImpl.this.getSidebarObjective()).getName(), this.getPlayerName(), this.getScoreAmount());
            conn.sendPacket((Packet)packetPlayOutScoreboardTeam);
            conn.sendPacket((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        public void sendRemoveToAllReceivers() {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 1);
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, ((ScoreboardObjective)SidebarImpl.this.getSidebarObjective()).getName(), this.getPlayerName(), this.getScoreAmount());
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public void sendUpdate(Player player) {
            PacketPlayOutScoreboardTeam packetTeamUpdate = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetTeamUpdate);
        }

        @Override
        @Contract(pure=true)
        public boolean setContent(@NotNull SidebarLine line) {
            String content = line.getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().size() == 1 ? SidebarImpl.this.getReceivers().getFirst() : null, null, SidebarImpl.this.getPlaceholders());
            String oldPrefix = this.prefixComponent.h();
            String oldSuffix = this.suffixComponent.h();
            if (content.length() > 32) {
                this.prefixComponent = new ChatComponentText(content.substring(0, 32));
                if (this.prefixComponent.h().charAt(31) == '\u00a7') {
                    this.prefixComponent = new ChatComponentText(content.substring(0, 31));
                    this.setSuffix(content.substring(31));
                } else {
                    this.setSuffix(content.substring(32));
                }
            } else {
                this.prefixComponent = new ChatComponentText(content);
                this.suffixComponent = new ChatComponentText("");
            }
            return !oldPrefix.equals(this.prefixComponent.h()) || !oldSuffix.equals(this.suffixComponent.h());
        }

        public void setSuffix(@NotNull String secondPart) {
            if (((String)secondPart).isEmpty()) {
                this.suffixComponent = new ChatComponentText("");
                return;
            }
            secondPart = ChatColor.getLastColors((String)this.prefixComponent.h()) + (String)secondPart;
            this.suffixComponent = new ChatComponentText((String)(((String)secondPart).length() > 32 ? ((String)secondPart).substring(0, 32) : secondPart));
        }

        @Override
        public void sendUpdateToAllReceivers() {
            PacketPlayOutScoreboardTeam packetTeamUpdate = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 2);
            SidebarImpl.this.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetTeamUpdate));
        }

        @Override
        public int compareTo(@NotNull ScoreLine o) {
            return Integer.compare(this.score, o.getScoreAmount());
        }

        public void setScore(int score) {
            this.score = score;
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, ((ScoreboardObjective)SidebarImpl.this.getSidebarObjective()).getName(), this.getPlayerName(), score);
            SidebarImpl.this.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        public int getScore() {
            return this.score;
        }

        public void c() {
        }

        public void addScore(int i) {
        }

        public void incrementScore() {
        }

        @Override
        public String getColor() {
            return this.team.getName().charAt(0) == '\u00a7' ? this.team.getName() : "\u00a7" + this.team.getName();
        }

        @Override
        public boolean refreshContent() {
            return this.setContent(this.getLine());
        }

        private class TeamLine
        extends ScoreboardTeam {
            public TeamLine(String color) {
                super(null, color);
                this.getPlayerNameSet().add(color);
            }

            @Contract(value=" -> new", pure=true)
            @NotNull
            public IChatBaseComponent getPrefix() {
                return BucharestScoreLine.this.prefixComponent;
            }

            public void setPrefix(@Nullable IChatBaseComponent var0) {
            }

            public void setSuffix(@Nullable IChatBaseComponent var0) {
            }

            @Contract(value=" -> new", pure=true)
            @NotNull
            public IChatBaseComponent getSuffix() {
                return BucharestScoreLine.this.suffixComponent;
            }

            public void setAllowFriendlyFire(boolean var0) {
            }

            public void setCanSeeFriendlyInvisibles(boolean var0) {
            }

            public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility var0) {
            }

            public void setCollisionRule(ScoreboardTeamBase.EnumTeamPush var0) {
            }

            public void setColor(EnumChatFormat var0) {
            }

            @Contract(value="_ -> new", pure=true)
            @NotNull
            public IChatMutableComponent getFormattedName(IChatBaseComponent var0) {
                return new ChatComponentText(BucharestScoreLine.this.prefixComponent.h() + var0 + BucharestScoreLine.this.suffixComponent.h());
            }
        }
    }

    protected class NarniaSidebarObjective
    extends ScoreboardObjective
    implements SidebarObjective {
        private SidebarLine displayName;
        private ChatComponentText displayNameString;
        private final int type;

        public NarniaSidebarObjective(String name, IScoreboardCriteria criteria, SidebarLine displayName, int type) {
            super(null, name, criteria, (IChatBaseComponent)new ChatComponentText(name), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
            this.displayNameString = new ChatComponentText("");
            this.displayName = displayName;
            this.type = type;
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
            PlayerConnection playerConnection = ((CraftPlayer)player).getHandle().playerConnection;
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 0);
            playerConnection.sendPacket((Packet)packetPlayOutScoreboardObjective);
            PacketPlayOutScoreboardDisplayObjective packetPlayOutScoreboardDisplayObjective = new PacketPlayOutScoreboardDisplayObjective(this.type, (ScoreboardObjective)this);
            playerConnection.sendPacket((Packet)packetPlayOutScoreboardDisplayObjective);
            if (this.getName().equalsIgnoreCase("health")) {
                PacketPlayOutScoreboardDisplayObjective packetPlayOutScoreboardDisplayObjective2 = new PacketPlayOutScoreboardDisplayObjective(0, (ScoreboardObjective)this);
                playerConnection.sendPacket((Packet)packetPlayOutScoreboardDisplayObjective2);
            }
        }

        @Override
        public void sendRemove(Player player) {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 1);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardObjective);
        }

        @Override
        public boolean refreshTitle() {
            String newTitle = this.displayName.getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders());
            if (this.displayNameString.h().equals(newTitle)) {
                return false;
            }
            this.displayNameString = new ChatComponentText(newTitle);
            return true;
        }

        public IChatBaseComponent getDisplayName() {
            return this.displayNameString;
        }

        public void setDisplayName(IChatBaseComponent var0) {
        }

        public void setRenderType(IScoreboardCriteria.EnumScoreboardHealthDisplay var0) {
        }

        @Override
        public void sendUpdate() {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 2);
            SidebarImpl.this.getReceivers().forEach(player -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardObjective));
        }
    }
}

