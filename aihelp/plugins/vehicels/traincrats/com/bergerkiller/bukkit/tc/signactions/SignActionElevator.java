package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.utils.BlockTimeoutMap;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class SignActionElevator extends TrainCartsSignAction {
   public static final SignActionElevator INSTANCE = new SignActionElevator();
   public final BlockTimeoutMap ignoreTimes = new BlockTimeoutMap();

   public SignActionElevator() {
      super("elevator");
   }

   public SignActionElevator.ElevatorRail findNextElevator(RailPiece from, BlockFace direction, int elevatorCount) {
      label25:
      while(true) {
         if ((from = Util.findNextRailPiece(from.block(), direction)) != null) {
            RailLookup.TrackedSign[] var4 = from.signs();
            int var5 = var4.length;
            int var6 = 0;

            while(true) {
               if (var6 >= var5) {
                  continue label25;
               }

               RailLookup.TrackedSign sign = var4[var6];
               if (sign.getAction() == this) {
                  --elevatorCount;
                  if (elevatorCount > 0) {
                     continue label25;
                  }

                  return new SignActionElevator.ElevatorRail(from, sign);
               }

               ++var6;
            }
         }

         return null;
      }
   }

   private static double getTrackDistance(RailState state) {
      TrackWalkingPoint p = new TrackWalkingPoint(state);
      p.setLoopFilter(true);
      p.skipFirst();
      p.move(16.0D);
      return p.movedTotal;
   }

   public void execute(SignActionEvent info) {
      if (info.getMode() != SignActionMode.NONE && info.hasRailedMember() && info.isPowered()) {
         if (info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_CHANGE)) {
            if (!this.ignoreTimes.isMarked(info.getRails(), 1000L)) {
               boolean forced = false;
               BlockFace mode = BlockFace.UP;
               if (info.isLine(2, "down")) {
                  mode = BlockFace.DOWN;
                  forced = true;
               } else if (info.isLine(2, "up")) {
                  forced = true;
               }

               int elevatorCount = ParseUtil.parseInt(info.getLine(2), 1);
               SignActionElevator.ElevatorRail nextElevator = this.findNextElevator(info.getRailPiece(), mode, elevatorCount);
               if (!forced && nextElevator == null) {
                  nextElevator = this.findNextElevator(info.getRailPiece(), mode.getOppositeFace(), elevatorCount);
               }

               if (nextElevator != null) {
                  this.ignoreTimes.mark(nextElevator.rail.block());
                  RailState spawnState = nextElevator.findSpawnState(info);
                  info.getGroup().teleportAndGo(spawnState.railBlock(), spawnState.motionVector());
               }
            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_ENTER).setName("train elevator").setDescription("teleport trains vertically").setTraincartsWIKIHelp("TrainCarts/Signs/Elevator").handle(event);
   }

   public static class ElevatorRail {
      public final RailPiece rail;
      public final RailLookup.TrackedSign sign;

      public ElevatorRail(RailPiece rail, RailLookup.TrackedSign sign) {
         this.rail = rail;
         this.sign = sign;
      }

      public RailState findSpawnState(SignActionEvent info) {
         RailState spawnState = RailState.getSpawnState(this.rail);
         Direction launchDirection = Direction.parse(info.getLine(3));
         if (launchDirection != Direction.NONE) {
            if (spawnState.position().motDot(launchDirection.getDirection(info.getFacing(), info.getCartEnterFace())) < 0.0D) {
               spawnState.position().invertMotion();
            }

            return spawnState;
         } else {
            Vector signForward = FaceUtil.faceToVector(this.sign.getFacing());
            double dot = signForward.dot(spawnState.motionVector());
            if (Math.abs(dot) > 0.707106781D) {
               if (dot < 0.0D) {
                  spawnState.position().invertMotion();
               }

               return spawnState;
            } else {
               RailState spawnStateReverse = spawnState.cloneAndInvertMotion();
               return SignActionElevator.getTrackDistance(spawnStateReverse) > SignActionElevator.getTrackDistance(spawnState) ? spawnStateReverse : spawnState;
            }
         }
      }
   }
}
