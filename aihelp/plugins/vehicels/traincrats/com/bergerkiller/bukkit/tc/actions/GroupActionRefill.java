package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import java.util.Iterator;

public class GroupActionRefill extends GroupAction {
   public void start() {
      Iterator var1 = this.getGroup().iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         if (member instanceof MinecartMemberFurnace) {
            ((CommonMinecartFurnace)((MinecartMemberFurnace)member).getEntity()).setFuelTicks(3600);
         }
      }

   }
}
