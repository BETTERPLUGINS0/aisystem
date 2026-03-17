package com.bergerkiller.bukkit.tc.controller.status;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.debug.DebugToolUtil;
import com.bergerkiller.bukkit.tc.events.MutexZoneConflictEvent;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZone;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneSlot;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneSlotType;
import com.bergerkiller.bukkit.tc.signactions.mutex.railslot.MutexRailSlot;
import com.bergerkiller.bukkit.tc.utils.LauncherConfig;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;

public interface TrainStatus {
   String getMessage();

   default ChatText getChatMessage() {
      return ChatText.fromMessage(this.getMessage());
   }

   public static final class EnteredMutexZone implements TrainStatus {
      private final MutexZoneSlot slot;
      private final List<MutexZone> zones;
      private final MutexZoneSlot.LoadedEnteredGroup group;

      public EnteredMutexZone(MutexZoneSlot slot, List<MutexZone> zones, MutexZoneSlot.LoadedEnteredGroup group) {
         this.slot = slot;
         this.zones = zones;
         this.group = group;
      }

      private boolean isSmart() {
         Iterator var1 = this.zones.iterator();

         MutexZone zone;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            zone = (MutexZone)var1.next();
         } while(zone.type != MutexZoneSlotType.SMART);

         return true;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.GREEN);
         if (this.group != null && !this.group.hardEnter) {
            str.append("Approaching");
         } else {
            str.append("Entered");
         }

         if (this.isSmart()) {
            str.append(" smart");
         }

         str.append(" mutex zone");
         if (!this.slot.isAnonymous()) {
            str.append(" ").append(ChatColor.WHITE).append(this.slot.getNameWithoutWorldUUID());
         }

         boolean first = true;
         str.append(ChatColor.GREEN).append(" at signs ");
         Iterator var3 = this.zones.iterator();

         while(var3.hasNext()) {
            MutexZone zone = (MutexZone)var3.next();
            if (first) {
               first = false;
            } else {
               str.append(ChatColor.GREEN).append(", ");
            }

            OfflineBlock pos = zone.signBlock;
            str.append(ChatColor.WHITE);
            str.append("[").append(pos.getX()).append("/").append(pos.getY()).append("/").append(pos.getZ()).append("]");
         }

         return str.toString();
      }

      public ChatText getChatMessage() {
         ChatText text = ChatText.fromMessage(this.getMessage() + ChatColor.GREEN + " - ");
         if (this.group != null) {
            StringBuilder str = new StringBuilder();
            str.append("Full Name: ").append(this.slot.getName()).append("\r\n");
            str.append("Active: ").append(this.group.active).append("\r\n");
            str.append("Entered Mutex: ").append(this.group.hardEnter).append("\r\n");
            str.append("Distance To Mutex: ").append(this.group.distanceToMutex).append("\r\n");
            str.append("Mutex Signs:\r\n");
            Iterator var3 = this.zones.iterator();

            while(var3.hasNext()) {
               MutexZone zone = (MutexZone)var3.next();
               OfflineBlock pos = zone.signBlock;
               str.append("  [").append(pos.getX()).append("/").append(pos.getY()).append("/").append(pos.getZ()).append("]\r\n");
            }

            if (this.group.active) {
               str.append("Locked Rail Blocks:\r\n");
               var3 = this.group.getLastPath().iterator();

               while(var3.hasNext()) {
                  MutexRailSlot slot = (MutexRailSlot)var3.next();
                  str.append("  ");
                  slot.debugPrint(str);
                  str.append("\r\n");
               }
            } else {
               List<MutexRailSlot> rails = this.group.getLastPath();
               if (rails.isEmpty()) {
                  str.append("Waiting for Rail: Unknown\r\n");
               } else {
                  str.append("Path taken through Mutex:\r\n");

                  for(int i = 0; i < rails.size() - 1; ++i) {
                     MutexRailSlot slot = (MutexRailSlot)rails.get(i);
                     str.append("  ");
                     slot.debugPrint(str);
                     str.append("\r\n");
                  }

                  str.append("Waiting for Rail: ");
                  ((MutexRailSlot)rails.get(rails.size() - 1)).debugPrint(str);
                  str.append("\r\n");
               }
            }

            MutexZoneConflictEvent conflict = this.group.getConflict();
            if (conflict != null) {
               IntVector3 rail = conflict.getRailPosition();
               str.append("Mutex Zone Conflict occurred:\r\n");
               str.append("  train: ").append(conflict.getGroupCrossed().getProperties().getTrainName()).append("\r\n");
               str.append("  rail: [").append(rail.x).append("/").append(rail.y).append("/").append(rail.z).append("]\r\n");
            }

            ChatText clickable = ChatText.fromClickableContent(ChatColor.WHITE.toString() + ChatColor.UNDERLINE + "Copy Details", str.toString());
            clickable.setHoverText("> Click to copy to your clipboard <");
            text.append(clickable);
         }

         return text;
      }
   }

   public static final class Derailed implements TrainStatus {
      public String getMessage() {
         return ChatColor.RED + "Is (partially) derailed";
      }
   }

   public static final class Moving implements TrainStatus {
      private final double speed;

      public Moving(double speed) {
         this.speed = speed;
      }

      public String getMessage() {
         return ChatColor.GREEN + "Is moving at " + ChatColor.WHITE + DebugToolUtil.formatNumber(this.speed) + "b/t";
      }
   }

   public static final class NotMoving implements TrainStatus {
      public String getMessage() {
         return ChatColor.RED + "Has zero velocity: is not moving";
      }
   }

   public static final class NotMovingSpeedLimited implements TrainStatus.Waiting {
      public String getMessage() {
         return ChatColor.RED + "Has zero velocity: not moving because something imposed a speed limit";
      }
   }

   public static final class KeepingChunksLoaded implements TrainStatus {
      public String getMessage() {
         return ChatColor.YELLOW + "Is keeping chunks " + ChatColor.GREEN + "loaded";
      }
   }

   public static final class ApproachingRailSpeedTrap implements TrainStatus {
      private final RailPiece rail;
      private final double distance;
      private final double speedLimit;

      public ApproachingRailSpeedTrap(RailPiece rail, double distance, double speedLimit) {
         this.rail = rail;
         this.distance = distance;
         this.speedLimit = speedLimit;
      }

      public String getMessage() {
         Block block = this.rail.block();
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW).append("Approaching speed trap at rail ");
         str.append(ChatColor.WHITE).append(block.getX()).append("/").append(block.getY()).append("/").append(block.getZ());
         str.append(ChatColor.YELLOW).append(" of speed ");
         str.append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.speedLimit));
         str.append(ChatColor.YELLOW).append(" which is ");
         str.append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.distance));
         str.append(ChatColor.YELLOW).append(" blocks away");
         return str.toString();
      }
   }

   public static final class WaitingAtRailBlock implements TrainStatus {
      private final RailPiece rail;

      public WaitingAtRailBlock(RailPiece rail) {
         this.rail = rail;
      }

      public String getMessage() {
         Block block = this.rail.block();
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW).append("Waiting at rail ");
         str.append(ChatColor.RED).append(block.getX()).append("/").append(block.getY()).append("/").append(block.getZ());
         return str.toString();
      }
   }

   public static final class ApproachingMutexZone implements TrainStatus {
      private final MutexZone zone;
      private final double distance;
      private final double speed;

      public ApproachingMutexZone(MutexZone zone, double distance, double speed) {
         this.zone = zone;
         this.distance = distance;
         this.speed = speed;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW).append("Approaching mutex zone ");
         OfflineBlock pos = this.zone.signBlock;
         if (!this.zone.slot.isAnonymous()) {
            str.append(ChatColor.RED).append(this.zone.slot.getName());
            str.append(ChatColor.YELLOW).append(" at ");
         }

         str.append(ChatColor.WHITE);
         str.append(pos.getX()).append("/").append(pos.getY()).append("/").append(pos.getZ());
         str.append(ChatColor.YELLOW).append(", ").append(ChatColor.WHITE);
         str.append(DebugToolUtil.formatNumber(this.distance)).append(ChatColor.YELLOW).append(" blocks ahead");
         List<MinecartGroup> groups = this.zone.slot.getCurrentGroups();
         if (!groups.isEmpty()) {
            str.append(", currently occupied by ");
            str.append(ChatColor.RED);

            for(int i = 0; i < groups.size(); ++i) {
               if (i > 0) {
                  str.append(", ");
               }

               str.append(((MinecartGroup)groups.get(i)).getProperties().getTrainName());
            }

            str.append(ChatColor.YELLOW);
         }

         str.append(", slowed down to a speed of ").append(ChatColor.WHITE);
         str.append(DebugToolUtil.formatNumber(this.speed)).append("b/t");
         return str.toString();
      }
   }

   public static final class FollowingTrain implements TrainStatus {
      private final MinecartMember<?> member;
      private final double distance;
      private final double speed;

      public FollowingTrain(MinecartMember<?> member, double distance, double speed) {
         this.member = member;
         this.distance = distance;
         this.speed = speed;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW);
         if (this.member.getForce() > 1.0E-4D) {
            str.append("Following train ");
         } else {
            str.append("Approaching train ");
         }

         str.append(ChatColor.WHITE).append(this.member.getGroup().getProperties().getTrainName());
         str.append(ChatColor.YELLOW).append(" at a speed of ");
         str.append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.speed)).append("b/t");
         str.append(ChatColor.YELLOW + " which is ");
         str.append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.distance));
         str.append(ChatColor.YELLOW).append(" blocks up ahead");
         return str.toString();
      }
   }

   public static final class WaitingForMutexZone implements TrainStatus.Waiting {
      private final MutexZone zone;

      public WaitingForMutexZone(MutexZone zone) {
         this.zone = zone;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW).append("Waiting for mutex zone");
         OfflineBlock pos = this.zone.signBlock;
         if (!this.zone.slot.isAnonymous()) {
            str.append(" ").append(ChatColor.RED).append(this.zone.slot.getNameWithoutWorldUUID());
         }

         str.append(ChatColor.YELLOW).append(" at sign ");
         str.append(ChatColor.RED);
         str.append(pos.getX()).append("/").append(pos.getY()).append("/").append(pos.getZ());
         List<MinecartGroup> groups = this.zone.slot.getCurrentGroups();
         if (!groups.isEmpty()) {
            str.append(ChatColor.YELLOW).append(" currently occupied by ");
            str.append(ChatColor.RED);

            for(int i = 0; i < groups.size(); ++i) {
               if (i > 0) {
                  str.append(", ");
               }

               str.append(((MinecartGroup)groups.get(i)).getProperties().getTrainName());
            }
         }

         return str.toString();
      }
   }

   public static final class WaitingForTrain implements TrainStatus.Waiting {
      private final MinecartMember<?> member;
      private final double distance;

      public WaitingForTrain(MinecartMember<?> member, double distance) {
         this.member = member;
         this.distance = distance;
      }

      public MinecartMember<?> getMember() {
         return this.member;
      }

      public double getDistance() {
         return this.distance;
      }

      public String getMessage() {
         return ChatColor.YELLOW + "Waiting for train " + ChatColor.RED + this.member.getGroup().getProperties().getTrainName() + ChatColor.YELLOW + " which is " + ChatColor.WHITE + DebugToolUtil.formatNumber(this.distance) + ChatColor.YELLOW + " blocks up ahead";
      }
   }

   public static final class WaitingForDelay implements TrainStatus.Waiting {
      private final double durationSeconds;

      public WaitingForDelay(double durationSeconds) {
         this.durationSeconds = durationSeconds;
      }

      public String getMessage() {
         return ChatColor.YELLOW + "Waiting for configured delay, " + DebugToolUtil.formatNumber(this.durationSeconds) + " seconds remaining";
      }
   }

   public static final class WaitingZeroSpeedLimit implements TrainStatus.Waiting {
      public String getMessage() {
         return ChatColor.YELLOW + "Waiting because the speed limit is set to " + ChatColor.RED + "zero";
      }
   }

   public static enum WaitingForRouting implements TrainStatus.Waiting {
      CALCULATING("Waiting for path finding router to finish"),
      NO_ROUTE("Waiting infinitely because no route to destination could be found, and there is no fallback direction"),
      NO_DESTINATION("Waiting infinitely because no destination is set, and there is no fallback direction"),
      AT_DESTINATION("Waiting infinitely because current destination is this station");

      private final String message;

      private WaitingForRouting(String message) {
         this.message = message;
      }

      public String getMessage() {
         return ChatColor.RED + this.message;
      }

      // $FF: synthetic method
      private static TrainStatus.WaitingForRouting[] $values() {
         return new TrainStatus.WaitingForRouting[]{CALCULATING, NO_ROUTE, NO_DESTINATION, AT_DESTINATION};
      }
   }

   public static final class WaitingForDuration implements TrainStatus.Waiting {
      private final long durationMillis;

      public WaitingForDuration(long durationMillis) {
         this.durationMillis = durationMillis;
      }

      public long getDuration() {
         return this.durationMillis;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.RED).append("Waiting for a time of ").append(ChatColor.WHITE);
         double timeSeconds = (double)this.durationMillis / 1000.0D;
         int timeMinutes = (int)timeSeconds / 60;
         if (timeMinutes > 0) {
            timeSeconds -= (double)(timeMinutes * 60);
            str.append(timeMinutes).append(" minutes");
            str.append(ChatColor.RED).append(" and ").append(ChatColor.WHITE);
            str.append((int)timeSeconds).append(" seconds");
         } else {
            str.append(timeSeconds).append(" seconds");
         }

         return str.toString();
      }
   }

   public static final class WaitingForever implements TrainStatus.Waiting {
      public String getMessage() {
         return ChatColor.RED + "Waiting forever for an external trigger";
      }
   }

   public interface Waiting extends TrainStatus {
   }

   public static final class Launching implements TrainStatus {
      private final double targetSpeed;
      private final double targetSpeedLimit;
      private final LauncherConfig config;

      public Launching(double targetSpeed, double targetSpeedLimit, LauncherConfig config) {
         this.targetSpeed = targetSpeed;
         this.targetSpeedLimit = targetSpeedLimit;
         this.config = config;
      }

      public double getTargetSpeed() {
         return this.targetSpeed;
      }

      public double getTargetSpeedLimit() {
         return this.targetSpeedLimit;
      }

      public LauncherConfig getConfig() {
         return this.config;
      }

      public String getMessage() {
         StringBuilder str = new StringBuilder();
         str.append(ChatColor.YELLOW).append("Launching to a speed of ").append(ChatColor.WHITE);
         if (!Double.isNaN(this.targetSpeedLimit) && !(this.targetSpeed <= this.targetSpeedLimit)) {
            str.append(DebugToolUtil.formatNumber(this.targetSpeedLimit)).append("b/t");
            str.append(ChatColor.YELLOW).append(" (").append(ChatColor.WHITE).append('+');
            str.append(DebugToolUtil.formatNumber(this.targetSpeed - this.targetSpeedLimit));
            str.append(ChatColor.YELLOW).append(" energy)");
         } else {
            str.append(DebugToolUtil.formatNumber(this.targetSpeed)).append("b/t");
         }

         str.append(ChatColor.YELLOW);
         if (this.config.hasDuration()) {
            str.append(" for ").append(ChatColor.WHITE).append(DebugToolUtil.formatNumber((double)this.config.getDuration())).append(ChatColor.YELLOW).append(" ticks");
         } else if (this.config.hasDistance()) {
            str.append(" over a distance of ").append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.config.getDistance())).append(ChatColor.YELLOW).append(" blocks");
         } else if (this.config.hasAcceleration()) {
            str.append(" at ").append(ChatColor.WHITE).append(DebugToolUtil.formatNumber(this.config.getAcceleration())).append(ChatColor.YELLOW).append("b/t/t");
         }

         return str.toString();
      }
   }
}
