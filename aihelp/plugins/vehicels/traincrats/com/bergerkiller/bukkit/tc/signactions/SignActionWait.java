package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.actions.Action;
import com.bergerkiller.bukkit.tc.actions.MemberActionWaitOccupied;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import org.bukkit.block.BlockFace;

public class SignActionWait extends TrainCartsSignAction {
   public SignActionWait() {
      super("wait");
   }

   public void execute(SignActionEvent info) {
      if (info.isAction(SignActionType.GROUP_ENTER) && info.isPowered()) {
         if (!info.hasRailedMember()) {
            return;
         }

         BlockFace launchDirection = null;
         String[] launchData = Util.splitBySeparator(info.getLine(3));
         Double launchVelocity = null;
         double launchDistance;
         if (launchData.length == 3) {
            launchDistance = ParseUtil.parseDouble(launchData[0], 2.0D);
            launchDirection = Direction.parse(launchData[1]).getDirectionLegacy(info.getFacing(), info.getCartEnterFace());
            launchVelocity = Util.parseVelocity(launchData[2], info.getGroup().getAverageForce());
         } else if (launchData.length == 1) {
            launchDistance = ParseUtil.parseDouble(launchData[0], 2.0D);
         } else {
            launchDistance = 2.0D;
         }

         String distanceData = info.getLine(1);
         if (distanceData.startsWith("waiter ")) {
            distanceData = distanceData.replaceFirst("waiter ", "");
         } else if (distanceData.startsWith("waiter")) {
            distanceData = distanceData.replaceFirst("waiter", "");
         } else if (distanceData.startsWith("wait ")) {
            distanceData = distanceData.replaceFirst("wait ", "");
         } else if (distanceData.startsWith("wait")) {
            distanceData = distanceData.replaceFirst("wait", "");
         }

         double distance = Double.NaN;
         if (!distanceData.matches("[a-zA-Z]+")) {
            distance = ParseUtil.parseDouble(info.getLine(1), 100.0D);
         } else {
            RailState state = info.getGroup().head().discoverRail();
            if (launchDirection != null) {
               state.setMotionVector(FaceUtil.faceToVector(launchDirection));
            }

            TrackWalkingPoint walkingPoint = new TrackWalkingPoint(state);

            label78:
            while(walkingPoint.movedTotal < (double)TCConfig.maxDetectorLength && walkingPoint.moveFull()) {
               RailLookup.TrackedSign[] var12 = walkingPoint.state.railSigns();
               int var13 = var12.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                  RailLookup.TrackedSign sign = var12[var14];
                  if (!sign.getRail().block().equals(info.getRails())) {
                     SignActionEvent found = new SignActionEvent(sign, info.getGroup());
                     if (found.isType(distanceData)) {
                        distance = walkingPoint.movedTotal;
                        break label78;
                     }
                  }
               }
            }

            if (Double.isNaN(distance)) {
               Localization.WAITER_TARGET_NOT_FOUND.broadcast(info.getGroup(), distanceData);
            } else {
               info.setLine(1, "waiter" + String.valueOf(MathUtil.round(distance, 3)));
            }
         }

         long delay = ParseUtil.parseTime(info.getLine(2));
         if (info.getGroup().isObstacleAhead(distance, true, false)) {
            Action currentAction = info.getGroup().getActions().getCurrentAction();
            if (currentAction instanceof MemberActionWaitOccupied) {
               MemberActionWaitOccupied waitOccupied = (MemberActionWaitOccupied)currentAction;
               waitOccupied.adjustDistance(distance);
            } else {
               info.getGroup().getActions().launchReset();
               info.getMember().getActions().addActionWaitOccupied(distance, delay, launchDistance, launchDirection, launchVelocity).setToggleOutputOf(info.getTrackedSign());
            }
         }
      } else if (info.isAction(SignActionType.REDSTONE_OFF)) {
         info.setLevers(false);
         if (info.hasRailedMember()) {
            info.getGroup().getActions().clear();
         }
      }

   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_WAIT).setName("train waiter sign").setDescription("waits the train until the tracks ahead are clear").setTraincartsWIKIHelp("TrainCarts/Signs/Waiter").handle(event);
   }
}
