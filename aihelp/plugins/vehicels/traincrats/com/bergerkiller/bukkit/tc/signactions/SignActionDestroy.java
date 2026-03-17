package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;

public class SignActionDestroy extends TrainCartsSignAction {
   public SignActionDestroy() {
      super("destroy");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER) && info.hasGroup()) {
            if (TCConfig.playHissWhenDestroyedBySign) {
               info.getGroup().playLinkEffect();
            }

            info.getGroup().destroy();
         } else if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER) && info.hasMember()) {
            info.getMember().onDie(true);
         } else {
            MinecartGroup group;
            if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
               for(Iterator var2 = info.getRCTrainGroups().iterator(); var2.hasNext(); group.destroy()) {
                  group = (MinecartGroup)var2.next();
                  if (TCConfig.playHissWhenDestroyedBySign) {
                     group.playLinkEffect();
                  }
               }
            }
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_DESTRUCTOR).setName(event.isCartSign() ? "cart destructor" : "train destructor").setTraincartsWIKIHelp("TrainCarts/Signs/Destroyer");
      if (event.isTrainSign()) {
         opt.setDescription("destroy an entire train");
      } else if (event.isCartSign()) {
         opt.setDescription("destroy minecarts");
      } else if (event.isRCSign()) {
         opt.setDescription("destroy an entire train remotely");
      }

      return opt.handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }
}
