package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementVelocity extends Statement {
   public boolean match(String text) {
      return text.startsWith("vel") || text.startsWith("speed");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return Util.evaluate(member.getRealSpeed(), text);
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return Util.evaluate(group.getAverageForce(), text);
   }

   public boolean matchArray(String text) {
      return false;
   }
}
