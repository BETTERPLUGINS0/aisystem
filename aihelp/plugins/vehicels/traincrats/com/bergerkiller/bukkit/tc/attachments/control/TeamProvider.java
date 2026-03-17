package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeamHandle;
import com.bergerkiller.mountiplex.reflection.util.UniqueHash;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamProvider implements LibraryComponent {
   private final TrainCarts plugin;
   private final UniqueHash teamIdHash = new UniqueHash();
   private final Map<Player, TeamProvider.ViewerState> viewerStates = new HashMap();
   private final Set<TeamProvider.ViewerState.ViewedTeam> pendingTeamUpdates = new HashSet();
   private final Task updateTask;
   private final TeamProvider.Team disabledTeam = new TeamProvider.Team() {
      public void join(Player viewer, Iterable<UUID> entityUUIDs) {
         TeamProvider.this.reset(viewer, entityUUIDs);
      }

      public void join(AttachmentViewer viewer, Iterable<UUID> entityUUIDs) {
         TeamProvider.this.reset(viewer, entityUUIDs);
      }

      public void join(Player viewer, UUID entityUUID) {
         TeamProvider.this.reset(viewer, entityUUID);
      }

      public void join(AttachmentViewer viewer, UUID entityUUID) {
         TeamProvider.this.reset(viewer, entityUUID);
      }
   };
   private final TeamProvider.Team noCollisionTeam = this.buildTeam().visibility(false).collision(false).rememberEntities(false).build();
   private final GlowColorTeamProvider glowColors;

   public TeamProvider(TrainCarts plugin) {
      this.plugin = plugin;
      this.updateTask = new Task(plugin) {
         public void run() {
            synchronized(TeamProvider.this) {
               Iterator iter = TeamProvider.this.pendingTeamUpdates.iterator();

               while(iter.hasNext()) {
                  if (!((TeamProvider.ViewerState.ViewedTeam)iter.next()).update()) {
                     iter.remove();
                  }
               }

               TeamProvider.this.pendingTeamUpdates.forEach(TeamProvider.ViewerState.ViewedTeam::assignEntities);
               TeamProvider.this.pendingTeamUpdates.clear();
            }
         }
      };
      this.glowColors = new GlowColorTeamProvider(this);
   }

   public GlowColorTeamProvider glowColors() {
      return this.glowColors;
   }

   public TeamProvider.Team disabledTeam() {
      return this.disabledTeam;
   }

   public TeamProvider.Team noCollisionTeam() {
      return this.noCollisionTeam;
   }

   public synchronized void enable() {
   }

   public synchronized void disable() {
      if (!this.pendingTeamUpdates.isEmpty()) {
         this.pendingTeamUpdates.clear();
         this.updateTask.stop();
      }

      Iterator var1 = this.viewerStates.values().iterator();

      while(var1.hasNext()) {
         TeamProvider.ViewerState state = (TeamProvider.ViewerState)var1.next();
         state.teams.forEach(TeamProvider.ViewerState.ViewedTeam::reset);
      }

      this.viewerStates.clear();
   }

   private void schedule(TeamProvider.ViewerState.ViewedTeam viewedTeam) {
      if (this.pendingTeamUpdates.isEmpty() && this.updateTask.getPlugin().isEnabled()) {
         this.updateTask.start();
      }

      this.pendingTeamUpdates.add(viewedTeam);
   }

   public TeamProvider.TeamBuilder buildTeam() {
      return new TeamProvider.TeamBuilder("ZZTCTeam" + this.teamIdHash.nextHex());
   }

   public void reset(AttachmentViewer viewer, Iterable<UUID> entityUUIDs) {
      this.reset(viewer.getPlayer(), entityUUIDs);
   }

   public synchronized void reset(Player viewer, Iterable<UUID> entityUUIDs) {
      TeamProvider.ViewerState state = (TeamProvider.ViewerState)this.viewerStates.get(viewer);
      if (state != null) {
         Iterator var4 = state.teams.iterator();

         while(var4.hasNext()) {
            TeamProvider.ViewerState.ViewedTeam team = (TeamProvider.ViewerState.ViewedTeam)var4.next();
            Iterator var6 = entityUUIDs.iterator();

            while(var6.hasNext()) {
               UUID entityUUID = (UUID)var6.next();
               team.removeEntity(entityUUID);
            }
         }
      }

   }

   public void reset(AttachmentViewer viewer, UUID entityUUID) {
      this.reset(viewer.getPlayer(), entityUUID);
   }

   public synchronized void reset(Player viewer, UUID entityUUID) {
      TeamProvider.ViewerState state = (TeamProvider.ViewerState)this.viewerStates.get(viewer);
      if (state != null) {
         Iterator var4 = state.teams.iterator();

         while(var4.hasNext()) {
            TeamProvider.ViewerState.ViewedTeam team = (TeamProvider.ViewerState.ViewedTeam)var4.next();
            if (team.removeEntity(entityUUID)) {
               break;
            }
         }
      }

   }

   public synchronized void reset(Player viewer) {
      TeamProvider.ViewerState state = (TeamProvider.ViewerState)this.viewerStates.remove(viewer);
      if (state != null) {
         state.teams.forEach(TeamProvider.ViewerState.ViewedTeam::reset);
      }

   }

   private final class ViewerState {
      private final AttachmentViewer viewer;
      private final ArrayList<TeamProvider.ViewerState.ViewedTeam> teams;

      public ViewerState(Player viewer) {
         this((AttachmentViewer)TeamProvider.this.plugin.getAttachmentViewer(viewer));
      }

      public ViewerState(AttachmentViewer viewer) {
         this.viewer = viewer;
         this.teams = new ArrayList();
      }

      public void assignTeamEntities(TeamProvider.Team team, Iterable<UUID> entityUUIDs) {
         TeamProvider.ViewerState.ViewedTeam foundViewedTeam = null;
         Iterator iterx = this.teams.iterator();

         while(true) {
            while(iterx.hasNext()) {
               TeamProvider.ViewerState.ViewedTeam viewedTeam = (TeamProvider.ViewerState.ViewedTeam)iterx.next();
               Iterator iter;
               UUID eid;
               if (viewedTeam.team == team) {
                  iter = entityUUIDs.iterator();
                  if (!iter.hasNext()) {
                     return;
                  }

                  boolean hasPendingRemove = !viewedTeam.pendingRemove.isEmpty();

                  while(viewedTeam.entities.contains(eid = (UUID)iter.next())) {
                     if (hasPendingRemove) {
                        viewedTeam.pendingRemove.remove(eid.toString());
                     }

                     if (!iter.hasNext()) {
                        return;
                     }
                  }

                  foundViewedTeam = viewedTeam;
               } else {
                  iter = entityUUIDs.iterator();

                  while(iter.hasNext()) {
                     eid = (UUID)iter.next();
                     viewedTeam.removeEntity(eid);
                  }
               }
            }

            iterx = entityUUIDs.iterator();
            if (!iterx.hasNext()) {
               return;
            }

            if (foundViewedTeam == null) {
               foundViewedTeam = new TeamProvider.ViewerState.ViewedTeam(team);
               this.teams.add(foundViewedTeam);
            }

            do {
               foundViewedTeam.addEntity((UUID)iterx.next());
            } while(iterx.hasNext());

            return;
         }
      }

      public void assignTeamEntity(TeamProvider.Team team, UUID entityUUID) {
         TeamProvider.ViewerState.ViewedTeam foundViewedTeam = null;
         Iterator var4 = this.teams.iterator();

         while(var4.hasNext()) {
            TeamProvider.ViewerState.ViewedTeam viewedTeam = (TeamProvider.ViewerState.ViewedTeam)var4.next();
            if (viewedTeam.team == team) {
               if (viewedTeam.entities.contains(entityUUID)) {
                  if (!viewedTeam.pendingRemove.isEmpty()) {
                     viewedTeam.pendingRemove.remove(entityUUID.toString());
                  }

                  return;
               }

               foundViewedTeam = viewedTeam;
            } else {
               viewedTeam.removeEntity(entityUUID);
            }
         }

         if (foundViewedTeam == null) {
            foundViewedTeam = new TeamProvider.ViewerState.ViewedTeam(team);
            this.teams.add(foundViewedTeam);
         }

         foundViewedTeam.addEntity(entityUUID);
      }

      public final class ViewedTeam {
         public final TeamProvider.Team team;
         public final Set<UUID> entities = new HashSet();
         private Set<String> pendingAdd = Collections.emptySet();
         private Set<String> pendingRemove = Collections.emptySet();
         private boolean teamCreated;

         public ViewedTeam(TeamProvider.Team team) {
            this.team = team;
            this.teamCreated = false;
         }

         public boolean addEntity(UUID entityUUID) {
            if (this.entities.add(entityUUID)) {
               String entityUUIDStr = entityUUID.toString();
               if (!this.pendingRemove.isEmpty() && this.pendingRemove.remove(entityUUIDStr)) {
                  return true;
               } else {
                  if (this.pendingAdd.isEmpty()) {
                     this.pendingAdd = new HashSet();
                  }

                  this.pendingAdd.add(entityUUIDStr);
                  TeamProvider.this.schedule(this);
                  return true;
               }
            } else {
               return false;
            }
         }

         public boolean removeEntity(UUID entityUUID) {
            if (this.entities.remove(entityUUID)) {
               String entityUUIDStr = entityUUID.toString();
               if (!this.pendingAdd.isEmpty() && this.pendingAdd.remove(entityUUIDStr)) {
                  return true;
               } else {
                  if (this.pendingRemove.isEmpty()) {
                     this.pendingRemove = new HashSet();
                  }

                  this.pendingRemove.add(entityUUIDStr);
                  TeamProvider.this.schedule(this);
                  return true;
               }
            } else {
               return false;
            }
         }

         public void reset() {
            if (this.teamCreated) {
               this.teamCreated = false;
               this.pendingRemove = Collections.emptySet();
               this.pendingAdd = Collections.emptySet();
               this.entities.clear();
               ViewerState.this.viewer.send((PacketHandle)this.team.createPacket(1));
            }

         }

         public boolean update() {
            if (!this.team.rememberEntities) {
               this.entities.clear();
            }

            if (this.team.rememberEntities && this.entities.isEmpty()) {
               this.reset();
            } else if (!this.pendingRemove.isEmpty()) {
               PacketPlayOutScoreboardTeamHandle packet = this.team.createPacket(4);
               packet.setPlayers(this.pendingRemove);
               this.pendingRemove = Collections.emptySet();
               ViewerState.this.viewer.send((PacketHandle)packet);
            }

            if (this.pendingAdd.isEmpty()) {
               this.pendingAdd = Collections.emptySet();
               return false;
            } else {
               return true;
            }
         }

         public void assignEntities() {
            PacketPlayOutScoreboardTeamHandle packet;
            if (!this.teamCreated) {
               this.teamCreated = true;
               packet = this.team.createPacket(0);
               packet.setPlayers(this.pendingAdd);
               this.pendingAdd = Collections.emptySet();
               ViewerState.this.viewer.send((PacketHandle)packet);
            } else {
               packet = this.team.createPacket(3);
               packet.setPlayers(this.pendingAdd);
               this.pendingAdd = Collections.emptySet();
               ViewerState.this.viewer.send((PacketHandle)packet);
            }

         }
      }
   }

   public class Team {
      private final String name;
      private final ChatText displayName;
      private final ChatText prefix;
      private final ChatText suffix;
      private final ChatColor color;
      private final String visibility;
      private final String collision;
      private final boolean rememberEntities;

      private Team() {
         this.name = null;
         this.displayName = null;
         this.prefix = null;
         this.suffix = null;
         this.color = null;
         this.visibility = null;
         this.collision = null;
         this.rememberEntities = false;
      }

      private Team(TeamProvider.TeamBuilder opts) {
         this.name = opts.name;
         this.displayName = ChatText.fromMessage(opts.name);
         this.prefix = opts.prefix;
         this.suffix = opts.suffix;
         this.color = opts.color;
         this.visibility = opts.visibility;
         this.collision = opts.collision;
         this.rememberEntities = opts.rememberEntities;
      }

      public void join(Player viewer, Iterable<UUID> entityUUIDs) {
         synchronized(TeamProvider.this) {
            TeamProvider.ViewerState state = (TeamProvider.ViewerState)TeamProvider.this.viewerStates.computeIfAbsent(viewer.getPlayer(), (x$0) -> {
               return TeamProvider.this.new ViewerState(x$0);
            });
            state.assignTeamEntities(this, entityUUIDs);
         }
      }

      public void join(AttachmentViewer viewer, Iterable<UUID> entityUUIDs) {
         synchronized(TeamProvider.this) {
            TeamProvider.ViewerState state = (TeamProvider.ViewerState)TeamProvider.this.viewerStates.computeIfAbsent(viewer.getPlayer(), (p) -> {
               return TeamProvider.this.new ViewerState(viewer);
            });
            state.assignTeamEntities(this, entityUUIDs);
         }
      }

      public void join(Player viewer, UUID entityUUID) {
         synchronized(TeamProvider.this) {
            TeamProvider.ViewerState state = (TeamProvider.ViewerState)TeamProvider.this.viewerStates.computeIfAbsent(viewer.getPlayer(), (x$0) -> {
               return TeamProvider.this.new ViewerState(x$0);
            });
            state.assignTeamEntity(this, entityUUID);
         }
      }

      public void join(AttachmentViewer viewer, UUID entityUUID) {
         synchronized(TeamProvider.this) {
            TeamProvider.ViewerState state = (TeamProvider.ViewerState)TeamProvider.this.viewerStates.computeIfAbsent(viewer.getPlayer(), (p) -> {
               return TeamProvider.this.new ViewerState(viewer);
            });
            state.assignTeamEntity(this, entityUUID);
         }
      }

      private PacketPlayOutScoreboardTeamHandle createPacket(int method) {
         PacketPlayOutScoreboardTeamHandle packet = PacketPlayOutScoreboardTeamHandle.createNew();
         packet.setName(this.name);
         packet.setDisplayName(this.displayName);
         packet.setColor(this.color);
         packet.setPrefix(this.prefix);
         packet.setSuffix(this.suffix);
         packet.setMethod(method);
         packet.setVisibility(this.visibility);
         packet.setCollisionRule(this.collision);
         if (method == 0) {
            packet.setTeamOptionFlags(3);
         }

         return packet;
      }

      // $FF: synthetic method
      Team(Object x1) {
         this();
      }

      // $FF: synthetic method
      Team(TeamProvider.TeamBuilder x1, Object x2) {
         this((TeamProvider.TeamBuilder)x1);
      }
   }

   public class TeamBuilder {
      private final String name;
      private ChatText prefix;
      private ChatText suffix;
      private ChatColor color;
      private String visibility;
      private String collision;
      private boolean rememberEntities;

      private TeamBuilder(String name) {
         this.prefix = ChatText.empty();
         this.suffix = ChatText.empty();
         this.color = ChatColor.BLACK;
         this.visibility = "always";
         this.collision = "always";
         this.rememberEntities = true;
         this.name = name;
      }

      public TeamProvider.TeamBuilder prefix(ChatText prefix) {
         this.prefix = prefix;
         return this;
      }

      public TeamProvider.TeamBuilder suffix(ChatText suffix) {
         this.suffix = suffix;
         return this;
      }

      public TeamProvider.TeamBuilder color(ChatColor color) {
         this.color = color;
         return this;
      }

      public TeamProvider.TeamBuilder visibility(boolean visible) {
         this.visibility = visible ? "always" : "never";
         return this;
      }

      public TeamProvider.TeamBuilder collision(boolean enabled) {
         this.collision = enabled ? "always" : "never";
         return this;
      }

      public TeamProvider.TeamBuilder rememberEntities(boolean remember) {
         this.rememberEntities = remember;
         return this;
      }

      public TeamProvider.Team build() {
         return TeamProvider.this.new Team(this);
      }

      // $FF: synthetic method
      TeamBuilder(String x1, Object x2) {
         this(x1);
      }
   }
}
