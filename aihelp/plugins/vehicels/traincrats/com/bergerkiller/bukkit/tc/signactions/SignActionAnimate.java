package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class SignActionAnimate extends TrainCartsSignAction {
   public SignActionAnimate() {
      super("animate");
   }

   public void animate(SignActionEvent info) {
      AnimationOptions options = new AnimationOptions();
      options.loadFromSign(info);
      Iterator var3 = info.getMembers().iterator();

      while(var3.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var3.next();
         mm.playNamedAnimation(options);
      }

   }

   public boolean click(SignActionEvent info, Player player) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(player.getVehicle());
      if (member == null) {
         return false;
      } else {
         info.setMember(member);
         this.animate(info);
         return true;
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
         this.animate(info);
      }

   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_ANIMATOR).setName("animator").setDescription(event.isRCSign() ? "play train model animations remotely" : "play train model animations").setTraincartsWIKIHelp("TrainCarts/Signs/Animate").handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }
}
