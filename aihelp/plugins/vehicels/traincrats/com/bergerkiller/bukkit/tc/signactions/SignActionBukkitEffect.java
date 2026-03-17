package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.Effect;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class SignActionBukkitEffect extends TrainCartsSignAction {
   public static Effect parse(SignActionEvent event) {
      Effect eff = new Effect();
      eff.parseEffect(event.getLine(2));
      eff.parseEffect(event.getLine(3));
      String[] args = StringUtil.getAfter(event.getLine(1), " ").trim().split(" ", -1);

      try {
         if (args.length >= 1) {
            eff.pitch = (float)ParseUtil.parseDouble(args[0], 1.0D);
         }

         if (args.length == 2) {
            eff.volume = (float)ParseUtil.parseDouble(args[1], 1.0D);
         }
      } catch (NumberFormatException var4) {
      }

      return eff;
   }

   public SignActionBukkitEffect() {
      super("beffect", "meffect", "peffect");
   }

   public void execute(SignActionEvent info) {
      boolean move = info.isType("meffect");
      boolean player = info.isType("peffect");
      if (info.isPowered()) {
         Effect eff = parse(info);
         Iterator var5;
         MinecartMember member;
         if (info.isAction(SignActionType.MEMBER_MOVE)) {
            if (move) {
               if (info.isTrainSign()) {
                  var5 = info.getGroup().iterator();

                  while(var5.hasNext()) {
                     member = (MinecartMember)var5.next();
                     eff.play(((CommonMinecart)member.getEntity()).getLocation());
                  }
               } else if (info.isCartSign()) {
                  eff.play(((CommonMinecart)info.getMember().getEntity()).getLocation());
               }
            }

         } else {
            Iterator var7;
            if (player) {
               if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
                  var5 = info.getGroup().iterator();

                  while(var5.hasNext()) {
                     member = (MinecartMember)var5.next();
                     var7 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

                     while(var7.hasNext()) {
                        Player p = (Player)var7.next();
                        eff.play(p);
                     }
                  }
               } else if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER)) {
                  var5 = ((CommonMinecart)info.getMember().getEntity()).getPlayerPassengers().iterator();

                  while(var5.hasNext()) {
                     Player p = (Player)var5.next();
                     eff.play(p);
                  }
               }

            } else {
               if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER) && info.hasGroup()) {
                  var5 = info.getGroup().iterator();

                  while(var5.hasNext()) {
                     member = (MinecartMember)var5.next();
                     eff.play(((CommonMinecart)member.getEntity()).getLocation());
                  }
               } else if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER) && info.hasMember()) {
                  eff.play(((CommonMinecart)info.getMember().getEntity()).getLocation());
               } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
                  var5 = info.getRCTrainGroups().iterator();

                  while(var5.hasNext()) {
                     MinecartGroup group = (MinecartGroup)var5.next();
                     var7 = group.iterator();

                     while(var7.hasNext()) {
                        MinecartMember<?> member = (MinecartMember)var7.next();
                        eff.play(((CommonMinecart)member.getEntity()).getLocation());
                     }
                  }
               } else if (info.isAction(SignActionType.REDSTONE_ON)) {
                  if (info.hasRails()) {
                     eff.play(info.getCenterLocation());
                  } else {
                     eff.play(info.getLocation().add(0.0D, 2.0D, 0.0D));
                  }
               }

            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      String app = event.isType(new String[]{"meffect"}) ? " while moving" : "";
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_EFFECT).setName(event.isCartSign() ? "cart Bukkit effect player" : "train Bukkit effect player").setTraincartsWIKIHelp("TrainCarts/Signs/Effect");
      if (event.isTrainSign()) {
         opt.setDescription("play a Bukkit effect in all minecarts of the train" + app);
      } else if (event.isCartSign()) {
         opt.setDescription("play a Bukkit effect in the minecart" + app);
      } else if (event.isRCSign()) {
         opt.setDescription("play a Bukkit effect in all minecarts of the train" + app);
      }

      return opt.handle(event);
   }

   public boolean isMemberMoveHandled(SignActionEvent info) {
      return info.isType("meffect");
   }

   public boolean canSupportRC() {
      return true;
   }
}
