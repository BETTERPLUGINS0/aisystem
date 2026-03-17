package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.Iterator;
import org.bukkit.entity.Entity;

public class StatementMob extends Statement {
   public boolean match(String text) {
      return text.equals("mob") || text.equals("mobs");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      Iterator var4 = ((CommonMinecart)member.getEntity()).getPassengers().iterator();

      Entity passenger;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         passenger = (Entity)var4.next();
      } while(!EntityUtil.isMob(passenger));

      return true;
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      int count = 0;
      Iterator var5 = group.iterator();

      while(var5.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var5.next();
         Iterator var7 = ((CommonMinecart)member.getEntity()).getPassengers().iterator();

         while(var7.hasNext()) {
            Entity passenger = (Entity)var7.next();
            if (EntityUtil.isMob(passenger)) {
               ++count;
            }
         }
      }

      return Util.evaluate((double)count, text);
   }

   public boolean matchArray(String text) {
      return text.equals("m");
   }

   public boolean hasMob(MinecartMember<?> member, String mob) {
      int idx = Util.getOperatorIndex(mob);
      if (idx == 0) {
         return false;
      } else {
         if (idx > 0) {
            mob = mob.substring(0, idx - 1);
         }

         Iterator var4 = ((CommonMinecart)member.getEntity()).getPassengers().iterator();

         while(var4.hasNext()) {
            Entity passenger = (Entity)var4.next();
            if (EntityUtil.isMob(passenger)) {
               String mobname = EntityUtil.getName(passenger);
               if (mobname.contains(mob)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean handleArray(MinecartMember<?> member, String[] mobs, SignActionEvent event) {
      if (mobs.length == 0) {
         return this.handle((MinecartMember)member, (String)null, event);
      } else {
         for(int i = 0; i < mobs.length; ++i) {
            mobs[i] = mobs[i].replace("_", "").replace(" ", "");
         }

         String[] var8 = mobs;
         int var5 = mobs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String mob = var8[var6];
            if (this.hasMob(member, mob)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean handleArray(MinecartGroup group, String[] mobs, SignActionEvent event) {
      if (mobs.length == 0) {
         return this.handle((MinecartGroup)group, (String)null, event);
      } else {
         for(int i = 0; i < mobs.length; ++i) {
            mobs[i] = mobs[i].replace("_", "").replace(" ", "");
         }

         String[] var11 = mobs;
         int var5 = mobs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String mob = var11[var6];
            int count = 0;
            Iterator var9 = group.iterator();

            while(var9.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var9.next();
               if (this.hasMob(member, mob)) {
                  ++count;
               }
            }

            if (Util.evaluate((double)count, mob)) {
               return true;
            }
         }

         return false;
      }
   }
}
