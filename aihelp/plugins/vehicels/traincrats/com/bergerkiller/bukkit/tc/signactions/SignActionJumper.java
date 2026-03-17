package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.util.Vector;

public class SignActionJumper extends TrainCartsSignAction {
   public static void jump(MinecartMember<?> member, Vector offset) {
      ((CommonMinecart)member.getEntity()).vel.set(offset);
   }

   public SignActionJumper() {
      super("jump");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered() && info.hasMember()) {
         boolean isCart = info.isCartSign() && info.isAction(SignActionType.MEMBER_ENTER);
         boolean isTrain = info.isTrainSign() && info.isAction(SignActionType.GROUP_ENTER);
         if (isCart || isTrain) {
            Vector offset = Util.parseVector(info.getLine(2), new Vector(0.0D, 0.0D, 0.0D));
            if (offset.lengthSquared() != 0.0D) {
               float yaw = (float)FaceUtil.faceToYaw(info.getFacing().getOppositeFace());
               offset = MathUtil.rotate(yaw, 0.0F, offset);
               if (isCart) {
                  jump(info.getMember(), offset);
               } else {
                  Iterator var6 = info.getGroup().iterator();

                  while(var6.hasNext()) {
                     MinecartMember<?> member = (MinecartMember)var6.next();
                     jump(member, offset.clone());
                  }
               }

            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_JUMPER).setName(event.isCartSign() ? "cart jumper" : "train jumper");
      if (event.isTrainSign()) {
         opt.setDescription("cause a minecart to jump towards a certain direction");
      } else {
         opt.setDescription("cause an entire train to jump towards a certain direction");
      }

      return opt.handle(event);
   }
}
