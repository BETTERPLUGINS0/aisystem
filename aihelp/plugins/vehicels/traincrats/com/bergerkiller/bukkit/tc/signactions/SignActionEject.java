package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SignActionEject extends TrainCartsSignAction {
   public SignActionEject() {
      super("eject");
   }

   public boolean click(SignActionEvent info, Player player) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(player.getVehicle());
      if (member == null) {
         return false;
      } else {
         info.setMember(member);
         this.eject(info);
         return true;
      }
   }

   public void eject(SignActionEvent info) {
      boolean hasSettings = !info.getLine(2).isEmpty() || !info.getLine(3).isEmpty();
      new Vector();
      float yaw = 0.0F;
      float pitch = 0.0F;
      if (hasSettings) {
         boolean isAbsolute = info.getLine(1).toLowerCase(Locale.ENGLISH).contains(" at");
         Vector offset = Util.parseVector(info.getLine(2), (Vector)null);
         if (offset == null) {
            isAbsolute = false;
            offset = new Vector();
         } else if (!isAbsolute && offset.length() > TCConfig.maxEjectDistance) {
            offset.normalize().multiply(TCConfig.maxEjectDistance);
         }

         boolean retainEntityRotation = false;
         if (!info.getLine(3).isEmpty()) {
            String[] angletext = Util.splitBySeparator(info.getLine(3));
            if (angletext.length == 2) {
               yaw = ParseUtil.parseFloat(angletext[0], 0.0F);
               pitch = ParseUtil.parseFloat(angletext[1], 0.0F);
            } else if (angletext.length == 1) {
               yaw = ParseUtil.parseFloat(angletext[0], 0.0F);
            }
         } else {
            retainEntityRotation = true;
         }

         if (!isAbsolute) {
            float signyawoffset = (float)FaceUtil.faceToYaw(info.getFacing().getOppositeFace());
            offset = MathUtil.rotate(signyawoffset, 0.0F, offset);
            yaw += signyawoffset + 90.0F;
         }

         if (isAbsolute) {
            Location at = new Location(info.getWorld(), offset.getX(), offset.getY(), offset.getZ(), yaw, pitch);
            Iterator var9 = info.getMembers().iterator();

            while(var9.hasNext()) {
               MinecartMember<?> mm = (MinecartMember)var9.next();
               mm.eject(at, retainEntityRotation);
            }
         } else {
            Iterator var15;
            MinecartMember mm;
            if (retainEntityRotation) {
               var15 = info.getMembers().iterator();

               while(var15.hasNext()) {
                  mm = (MinecartMember)var15.next();
                  mm.eject(offset);
               }
            } else {
               var15 = info.getMembers().iterator();

               while(var15.hasNext()) {
                  mm = (MinecartMember)var15.next();
                  mm.eject(offset, yaw, pitch);
               }
            }
         }
      } else {
         Iterator var11 = info.getMembers().iterator();

         while(var11.hasNext()) {
            MinecartMember<?> mm = (MinecartMember)var11.next();
            mm.eject();
         }
      }

   }

   public void execute(SignActionEvent info) {
      boolean isRemote = false;
      if ((!info.isCartSign() || !info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)) && (!info.isTrainSign() || !info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON))) {
         if (!info.isRCSign() || !info.isAction(SignActionType.REDSTONE_ON)) {
            return;
         }

         isRemote = true;
      }

      if (isRemote || info.hasMember() && info.isPowered()) {
         this.eject(info);
      }

   }

   public boolean build(SignChangeActionEvent event) {
      return event.getLine(1).toLowerCase(Locale.ENGLISH).contains(" at") && !Permission.BUILD_EJECTOR_ABSOLUTE.handleMsg(event.getPlayer(), ChatColor.RED + "You do not have permission to build eject signs that teleport to world coordinates") ? false : SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_EJECTOR).setName("train ejector").setDescription("eject the passengers of a " + (event.isRCSign() ? "remote train" : "train")).setTraincartsWIKIHelp("TrainCarts/Signs/Ejector").handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }
}
