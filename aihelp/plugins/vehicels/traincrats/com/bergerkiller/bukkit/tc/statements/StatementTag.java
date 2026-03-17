package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementTag extends Statement {
   public int priority() {
      return -99999;
   }

   public boolean match(String text) {
      return true;
   }

   public boolean matchArray(String text) {
      return true;
   }

   public boolean handle(MinecartMember<?> member, String tag, SignActionEvent event) {
      return this.handleArray(member, parseArray(tag), event);
   }

   public boolean handleArray(MinecartMember<?> member, String[] tags, SignActionEvent event) {
      String[] var4 = tags;
      int var5 = tags.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String tag = var4[var6];
         if (member.getProperties().matchTag(tag)) {
            return true;
         }
      }

      return false;
   }
}
