package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.resources.ResourceKey;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SignActionSound extends TrainCartsSignAction {
   public SignActionSound() {
      super("sound", "msound");
   }

   public ResourceKey<SoundEffect> getSound(SignActionEvent info) {
      try {
         return SoundEffect.fromName(info.getLine(2) + info.getLine(3));
      } catch (Throwable var3) {
         return null;
      }
   }

   public void execute(SignActionEvent info) {
      boolean move = info.isType("msound");
      if (info.isPowered()) {
         SignActionSound.SoundArgs args = new SignActionSound.SoundArgs();
         args.sound = this.getSound(info);
         if (args.sound != null) {
            String[] str_args = StringUtil.getAfter(info.getLine(1), " ").trim().split(" ", -1);

            for(int i = 0; i < str_args.length; ++i) {
               if (str_args[i].equalsIgnoreCase("in")) {
                  args.onlyInside = true;
                  str_args = StringUtil.remove(str_args, i);
                  --i;
               }
            }

            try {
               if (str_args.length >= 1) {
                  args.pitch = (float)ParseUtil.parseDouble(str_args[0], 1.0D);
               }

               if (str_args.length == 2) {
                  args.volume = (float)ParseUtil.parseDouble(str_args[1], 1.0D);
               }
            } catch (NumberFormatException var8) {
            }

            Iterator var10;
            MinecartMember member;
            if (info.isAction(SignActionType.MEMBER_MOVE)) {
               if (move) {
                  if (info.isTrainSign()) {
                     var10 = info.getGroup().iterator();

                     while(var10.hasNext()) {
                        member = (MinecartMember)var10.next();
                        args.play(member);
                     }
                  } else if (info.isCartSign()) {
                     args.play(info.getMember());
                  }
               }

            } else {
               if (info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER) && info.hasGroup()) {
                  var10 = info.getGroup().iterator();

                  while(var10.hasNext()) {
                     member = (MinecartMember)var10.next();
                     args.play(member);
                  }
               } else if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER) && info.hasMember()) {
                  args.play(info.getMember());
               } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
                  var10 = info.getRCTrainGroups().iterator();

                  while(var10.hasNext()) {
                     MinecartGroup group = (MinecartGroup)var10.next();
                     Iterator var6 = group.iterator();

                     while(var6.hasNext()) {
                        MinecartMember<?> member = (MinecartMember)var6.next();
                        args.play(member);
                     }
                  }
               } else if (info.isAction(SignActionType.REDSTONE_ON)) {
                  Location location;
                  if (info.hasRails()) {
                     location = info.getCenterLocation();
                  } else {
                     location = info.getLocation().add(0.0D, 2.0D, 0.0D);
                  }

                  args.play(location);
               }

            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      if (this.getSound(event) == null) {
         event.getPlayer().sendMessage(ChatColor.RED + "Sound name '" + event.getLine(2) + event.getLine(3) + "' is invalid!");
         return false;
      } else {
         String app = event.isType(new String[]{"msound"}) ? " while moving" : "";
         SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_SOUND).setName(event.isCartSign() ? "cart sound player" : "train sound player").setTraincartsWIKIHelp("TrainCarts/Signs/Sound");
         if (event.isCartSign()) {
            opt.setDescription("play a sound in the minecart" + app);
         } else if (event.isTrainSign()) {
            opt.setDescription("play a sound in all minecarts of the train" + app);
         } else if (event.isRCSign()) {
            opt.setDescription("play a sound in all minecarts of the train" + app);
         }

         return opt.handle(event);
      }
   }

   public boolean isMemberMoveHandled(SignActionEvent info) {
      return info.isType("msound");
   }

   public boolean canSupportRC() {
      return true;
   }

   private static class SoundArgs {
      public float volume;
      public float pitch;
      public boolean onlyInside;
      public ResourceKey<SoundEffect> sound;

      private SoundArgs() {
         this.volume = 1.0F;
         this.pitch = 1.0F;
         this.onlyInside = false;
      }

      public void play(MinecartMember<?> member) {
         if (this.onlyInside) {
            Iterator var2 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

            while(var2.hasNext()) {
               Player passenger = (Player)var2.next();
               PlayerUtil.playSound(passenger, passenger.getEyeLocation(), this.sound, this.volume, this.pitch);
            }
         } else {
            this.play(((CommonMinecart)member.getEntity()).getLocation());
         }

      }

      public void play(Location location) {
         WorldUtil.playSound(location, this.sound, this.volume, this.pitch);
      }

      // $FF: synthetic method
      SoundArgs(Object x0) {
         this();
      }
   }
}
