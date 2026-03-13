/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.IScoreboardCriteria
 *  net.minecraft.server.v1_8_R3.IScoreboardCriteria$EnumScoreboardHealthDisplay
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_8_R3.PlayerConnection
 *  net.minecraft.server.v1_8_R3.ScoreboardObjective
 *  net.minecraft.server.v1_8_R3.ScoreboardScore
 *  net.minecraft.server.v1_8_R3.ScoreboardTeam
 *  net.minecraft.server.v1_8_R3.ScoreboardTeamBase$EnumNameTagVisibility
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar.v1_8_R3;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.WrappedSidebar;
import java.util.Collection;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        private String prefix;
        private String suffix;
        private final TeamLine team;
        private SidebarLine content;

        public BucharestScoreLine(SidebarLine text, @NotNull int score, String color) {
            super(null, (ScoreboardObjective)SidebarImpl.this.getSidebarObjective(), color);
            this.prefix = " ";
            this.suffix = "";
            this.score = score;
            this.content = text;
            this.team = new TeamLine(color);
        }

        @Override
        public SidebarLine getLine() {
            return this.content;
        }

        @Override
        public void setLine(SidebarLine line) {
            this.content = line;
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
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore((ScoreboardScore)this);
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public void sendCreate(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 0);
            conn.sendPacket((Packet)packetPlayOutScoreboardTeam);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore((ScoreboardScore)this);
            conn.sendPacket((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        public void sendRemove(Player player) {
            PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 1);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getPlayerName(), (ScoreboardObjective)SidebarImpl.this.getSidebarObjective());
            conn.sendPacket((Packet)packetPlayOutScoreboardTeam);
            conn.sendPacket((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        public void sendRemoveToAllReceivers() {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 1);
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardTeam));
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getPlayerName(), (ScoreboardObjective)SidebarImpl.this.getSidebarObjective());
            SidebarImpl.this.getReceivers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        @Override
        public void sendUpdate(Player player) {
            PacketPlayOutScoreboardTeam packetTeamUpdate = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 2);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(this.getPlayerName(), (ScoreboardObjective)SidebarImpl.this.getSidebarObjective());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetTeamUpdate);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore);
        }

        @Override
        @Contract(pure=true)
        public boolean setContent(@NotNull SidebarLine line) {
            String content = line.getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().isEmpty() ? null : SidebarImpl.this.getReceivers().getFirst(), null, SidebarImpl.this.getPlaceholders());
            String oldPrefix = this.prefix;
            String oldSuffix = this.suffix;
            if (content.length() > 16) {
                this.prefix = content.substring(0, 16);
                if (this.prefix.charAt(15) == '\u00a7') {
                    this.prefix = content.substring(0, 15);
                    this.setSuffix(content.substring(15));
                } else {
                    this.setSuffix(content.substring(16));
                }
            } else {
                this.prefix = content;
                this.suffix = "";
            }
            return !oldPrefix.equals(this.prefix) || !oldSuffix.equals(this.suffix);
        }

        public void setSuffix(@NotNull String secondPart) {
            if (secondPart.isEmpty()) {
                this.suffix = "";
                return;
            }
            secondPart = ChatColor.getLastColors((String)this.prefix) + secondPart;
            this.suffix = secondPart.length() > 16 ? secondPart.substring(0, 16) : secondPart;
        }

        @Override
        public void sendUpdateToAllReceivers() {
            PacketPlayOutScoreboardTeam packetTeamUpdate = new PacketPlayOutScoreboardTeam((ScoreboardTeam)this.team, 2);
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore((ScoreboardScore)this);
            SidebarImpl.this.getReceivers().forEach(r -> {
                ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetTeamUpdate);
                ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore);
            });
        }

        @Override
        public int compareTo(@NotNull ScoreLine o) {
            return Integer.compare(this.score, o.getScoreAmount());
        }

        public void setScore(int score) {
            this.score = score;
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore((ScoreboardScore)this);
            SidebarImpl.this.getReceivers().forEach(r -> ((CraftPlayer)r).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardScore));
        }

        public int getScore() {
            return this.score;
        }

        public void addScore(int i) {
        }

        public void incrementScore() {
        }

        @Override
        public String getColor() {
            return this.team.getName().charAt(0) == '\u00a7' ? this.team.getName() : '\u00a7' + this.team.getName();
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

            public String getPrefix() {
                return BucharestScoreLine.this.prefix;
            }

            public void setPrefix(@Nullable String var0) {
            }

            public void setSuffix(@Nullable String var0) {
            }

            public String getSuffix() {
                return BucharestScoreLine.this.suffix;
            }

            public void setAllowFriendlyFire(boolean var0) {
            }

            public void setCanSeeFriendlyInvisibles(boolean var0) {
            }

            public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility var0) {
            }

            @NotNull
            public String getFormattedName(String var0) {
                return this.getPrefix().concat(var0).concat(this.getSuffix());
            }
        }
    }

    protected class NarniaSidebarObjective
    extends ScoreboardObjective
    implements SidebarObjective {
        private SidebarLine displayName;
        private String displayNameString;
        private final int type;

        public NarniaSidebarObjective(String name, IScoreboardCriteria criteria, SidebarLine displayName, int type) {
            super(null, name, criteria);
            this.displayNameString = "";
            this.displayName = displayName;
            this.type = type;
        }

        public IScoreboardCriteria.EnumScoreboardHealthDisplay e() {
            return IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER;
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
            String newTitleString = this.getTitle().getTrimReplacePlaceholders(SidebarImpl.this.getReceivers().size() == 1 ? SidebarImpl.this.getReceivers().getFirst() : null, 16, SidebarImpl.this.getPlaceholders());
            if (this.getDisplayName().equals(newTitleString)) {
                return false;
            }
            this.displayNameString = newTitleString;
            return true;
        }

        public String getDisplayName() {
            return this.displayNameString;
        }

        public void setDisplayName(String var0) {
        }

        @Override
        public void sendUpdate() {
            PacketPlayOutScoreboardObjective packetPlayOutScoreboardObjective = new PacketPlayOutScoreboardObjective((ScoreboardObjective)this, 2);
            SidebarImpl.this.getReceivers().forEach(player -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packetPlayOutScoreboardObjective));
        }
    }
}

