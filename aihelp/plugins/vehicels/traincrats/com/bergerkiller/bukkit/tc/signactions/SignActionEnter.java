package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SignActionEnter extends TrainCartsSignAction {
   private static boolean canEnter(Entity entity, boolean enterPlayers, boolean enterMobs, boolean enterMisc) {
      if (entity instanceof Player) {
         return enterPlayers;
      } else if (EntityUtil.isMob(entity)) {
         return enterMobs;
      } else {
         return MinecartMemberStore.getFromEntity(entity) != null ? false : enterMisc;
      }
   }

   public SignActionEnter() {
      super("enter");
   }

   public void execute(SignActionEvent info) {
      if (!info.isAction(SignActionType.REDSTONE_ON)) {
         if (info.isCartSign()) {
            if (!info.isAction(SignActionType.MEMBER_ENTER)) {
               return;
            }
         } else {
            if (!info.isTrainSign()) {
               return;
            }

            if (!info.isAction(SignActionType.GROUP_ENTER)) {
               return;
            }
         }
      }

      if (info.isPowered()) {
         double radiusXZ = Double.min(TCConfig.maxEnterDistance, ParseUtil.parseDouble(info.getLine(1), 2.0D));
         double radiusY = 1.0D;
         if (info.getLine(1).toLowerCase(Locale.ENGLISH).endsWith("s")) {
            radiusY = radiusXZ;
         }

         boolean enterPlayers = false;
         boolean enterMobs = false;
         boolean enterMisc = false;
         if (!info.getLine(2).isEmpty()) {
            String mode = info.getLine(2).toLowerCase(Locale.ENGLISH);
            if (mode.contains("mob")) {
               enterMobs = true;
            }

            if (mode.contains("player")) {
               enterPlayers = true;
            }

            if (mode.contains("misc")) {
               enterMisc = true;
            }
         } else {
            enterPlayers = true;
         }

         boolean aroundSign = ParseUtil.parseBool(info.getLine(3));
         Collection<MinecartMember<?>> members = info.getMembers();
         if (aroundSign) {
            Location center = info.hasRails() ? info.getRailLocation() : info.getLocation();
            Iterator var12 = WorldUtil.getNearbyEntities(center, radiusXZ, radiusY, radiusXZ).iterator();

            while(true) {
               Entity entity;
               do {
                  do {
                     if (!var12.hasNext()) {
                        return;
                     }

                     entity = (Entity)var12.next();
                  } while(entity.getVehicle() != null);
               } while(!canEnter(entity, enterPlayers, enterMobs, enterMisc));

               Iterator var14 = members.iterator();

               while(var14.hasNext()) {
                  MinecartMember<?> member = (MinecartMember)var14.next();
                  if (member.getAvailableSeatCount(entity) > 0 && member.addPassengerForced(entity)) {
                     break;
                  }
               }
            }
         } else {
            Iterator var16 = members.iterator();

            while(var16.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var16.next();
               List nearby = ((CommonMinecart)member.getEntity()).getNearbyEntities(radiusXZ, radiusY, radiusXZ);

               while(!nearby.isEmpty()) {
                  double lastDistance = Double.MAX_VALUE;
                  Entity selectedEntity = null;
                  Iterator var19 = nearby.iterator();

                  while(var19.hasNext()) {
                     Entity entity = (Entity)var19.next();
                     if (entity.getVehicle() == null && canEnter(entity, enterPlayers, enterMobs, enterMisc) && member.getAvailableSeatCount(entity) != 0) {
                        double distance = ((CommonMinecart)member.getEntity()).loc.distanceSquared(entity);
                        if (distance < lastDistance) {
                           lastDistance = distance;
                           selectedEntity = entity;
                        }
                     }
                  }

                  if (selectedEntity == null) {
                     break;
                  }

                  nearby.remove(selectedEntity);
                  member.addPassengerForced(selectedEntity);
               }
            }

         }
      }
   }

   public boolean canSupportRC() {
      return true;
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_ENTER).setName("train enter sign").setDescription("cause nearby players/mobs to enter the train").setTraincartsWIKIHelp("TrainCarts/Signs/Enter").handle(event);
   }
}
