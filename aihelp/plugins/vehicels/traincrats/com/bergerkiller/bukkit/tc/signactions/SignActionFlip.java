package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import java.util.Locale;

public class SignActionFlip extends TrainCartsSignAction {
   public SignActionFlip() {
      super("flip");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         boolean perCart = info.getLine(1).toLowerCase(Locale.ENGLISH).contains("percart");
         Iterator var3;
         if (info.isTrainSign() && info.hasGroup() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
            if (perCart) {
               var3 = info.getGroup().iterator();

               while(var3.hasNext()) {
                  MinecartMember<?> member = (MinecartMember)var3.next();
                  member.flipOrientation();
               }
            } else {
               info.getGroup().flipOrientation();
            }
         } else if (info.isCartSign() && info.hasMember() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER)) {
            info.getMember().flipOrientation();
         } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
            var3 = info.getRCTrainGroups().iterator();

            while(true) {
               while(var3.hasNext()) {
                  MinecartGroup group = (MinecartGroup)var3.next();
                  if (perCart) {
                     Iterator var5 = group.iterator();

                     while(var5.hasNext()) {
                        MinecartMember<?> member = (MinecartMember)var5.next();
                        member.flipOrientation();
                     }
                  } else {
                     group.flipOrientation();
                  }
               }

               return;
            }
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_FLIPPER).setName(event.isCartSign() ? "cart flipper" : "train cart flipper");
      if (event.isTrainSign()) {
         opt.setDescription("flip the orientation of all Minecarts in a train");
      } else if (event.isCartSign()) {
         opt.setDescription("flip the orientation of a Minecart");
      } else if (event.isRCSign()) {
         opt.setDescription("flip the orientation of all Minecarts in a train remotely");
      }

      return opt.handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }
}
