package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementEmpty extends Statement {
   public boolean match(String text) {
      return text.equals("empty");
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return !group.hasItems() && !group.hasPassenger();
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      if (member instanceof MinecartMemberChest) {
         return !((MinecartMemberChest)member).hasItems();
      } else {
         return !((CommonMinecart)member.getEntity()).hasPassenger();
      }
   }

   public boolean matchArray(String text) {
      return false;
   }
}
