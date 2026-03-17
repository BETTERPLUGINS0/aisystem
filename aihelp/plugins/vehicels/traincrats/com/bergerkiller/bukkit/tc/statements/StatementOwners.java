package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementOwners extends Statement {
   public boolean match(String text) {
      return text.equals("owner") || text.equals("owners");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return Util.evaluate((double)member.getProperties().getOwners().size(), text);
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return Util.evaluate((double)group.getProperties().getOwners().size(), text);
   }

   public boolean matchArray(String text) {
      return text.equals("o");
   }

   public boolean handleArray(MinecartMember<?> member, String[] owners, SignActionEvent event) {
      String[] var4 = owners;
      int var5 = owners.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String owner = var4[var6];
         if (member.getProperties().isOwner(owner.toLowerCase())) {
            return true;
         }
      }

      return false;
   }
}
