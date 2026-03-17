package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementBoolean extends Statement {
   public static final StatementBoolean INSTANCE = new StatementBoolean();
   public static final StatementBoolean EMPTY = new StatementBoolean();

   public boolean match(String text) {
      return text.equals("true") || text.equals("false");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return text.equalsIgnoreCase("true");
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return text.equalsIgnoreCase("true");
   }

   public boolean requiresTrain() {
      return false;
   }

   public boolean matchArray(String text) {
      return false;
   }

   public boolean isConstant() {
      return true;
   }

   public int priority() {
      return 1;
   }
}
