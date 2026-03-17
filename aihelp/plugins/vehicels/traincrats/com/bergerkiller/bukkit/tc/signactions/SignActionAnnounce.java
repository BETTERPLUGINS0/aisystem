package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;

public class SignActionAnnounce extends TrainCartsSignAction {
   public SignActionAnnounce() {
      super("announce");
   }

   public static void sendMessage(SignActionEvent info, MinecartGroup group, String message) {
      Iterator var3 = group.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var3.next();
         sendMessage(info, member, message);
      }

   }

   public static void sendMessage(SignActionEvent info, MinecartMember<?> member, String message) {
      ((CommonMinecart)member.getEntity()).getPlayerPassengers().forEach((player) -> {
         TrainCarts.sendMessage(player, message);
      });
   }

   public static String getMessage(SignActionEvent info) {
      StringBuilder message = new StringBuilder(32);
      message.append(info.getLine(2));
      message.append(info.getLine(3));
      String[] var2 = info.getExtraLinesBelow();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String line = var2[var4];
         message.append(line);
      }

      return TrainCarts.getMessage(message.toString());
   }

   public void execute(SignActionEvent info) {
      String message = getMessage(info);
      if (info.isTrainSign() && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) {
         if (!info.hasRailedMember() || !info.isPowered()) {
            return;
         }

         sendMessage(info, info.getGroup(), message);
      } else if (info.isCartSign() && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)) {
         if (!info.hasRailedMember() || !info.isPowered()) {
            return;
         }

         sendMessage(info, info.getMember(), message);
      } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
         Iterator var3 = info.getRCTrainGroups().iterator();

         while(var3.hasNext()) {
            MinecartGroup group = (MinecartGroup)var3.next();
            sendMessage(info, group, message);
         }
      }

   }

   public boolean canSupportRC() {
      return true;
   }

   public boolean build(SignChangeActionEvent event) {
      return !event.isType(new String[]{"announce"}) ? false : SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_ANNOUNCER).setName("announcer").setDescription(event.isRCSign() ? "remotely send a message to all the players in the train" : "send a message to players in a train").setTraincartsWIKIHelp("TrainCarts/Signs/Announce").handle(event);
   }
}
