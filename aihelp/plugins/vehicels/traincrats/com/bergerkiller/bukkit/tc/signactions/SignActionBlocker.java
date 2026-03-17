package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitState;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathPredictEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import org.bukkit.block.BlockFace;

public class SignActionBlocker extends TrainCartsSignAction {
   public SignActionBlocker() {
      super("blocker");
   }

   public void execute(SignActionEvent info) {
      if (info.getMode() != SignActionMode.NONE && info.hasRailedMember()) {
         if (!info.isAction(SignActionType.GROUP_LEAVE) && !info.isAction(SignActionType.REDSTONE_OFF)) {
            if (info.isPowered() && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON, SignActionType.MEMBER_MOVE)) {
               if (!info.isAction(SignActionType.MEMBER_MOVE)) {
                  Direction direction = Direction.parse(info.getLine(3));
                  if (direction != Direction.NONE) {
                     long delay = ParseUtil.parseTime(info.getLine(2));
                     BlockFace trainDirection = direction.getDirectionLegacy(info.getFacing(), info.getCartEnterFace());
                     info.getGroup().getActions().clear();
                     info.getGroup().getActions().addActionWaitState();
                     if (delay > 0L) {
                        info.getGroup().getActions().addActionWait(delay);
                     }

                     info.getMember().getActions().addActionLaunch(trainDirection, 2.0D, info.getGroup().getAverageForce());
                  }
               }

               info.getGroup().stop(info.isAction(SignActionType.MEMBER_MOVE));
            }
         } else {
            GroupActionWaitState action = (GroupActionWaitState)CommonUtil.tryCast(info.getGroup().getActions().getCurrentAction(), GroupActionWaitState.class);
            if (action != null) {
               action.stop();
            }
         }
      }

   }

   public boolean isMemberMoveHandled(SignActionEvent info) {
      return true;
   }

   public boolean isPathFindingBlocked(SignActionEvent info, RailState state) {
      return info.getHeader().isAlwaysOn() && info.isEnterActivated(state);
   }

   public void predictPathFinding(SignActionEvent info, PathPredictEvent prediction) {
      if (info.isEnterActivated() && info.isPowered()) {
         prediction.addSpeedLimit(0.0D);
      }

   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_BLOCKER).setName("train blocker").setDescription("block trains coming from a certain direction").setTraincartsWIKIHelp("TrainCarts/Signs/Blocker").handle(event);
   }
}
