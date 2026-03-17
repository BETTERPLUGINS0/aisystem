package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class StatementDestination extends Statement {
   public boolean match(String text) {
      return text.equals("destination");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return member.getProperties().hasDestination();
   }

   public boolean matchArray(String text) {
      return text.equals("d");
   }

   public boolean handleArray(MinecartMember<?> member, String[] text, SignActionEvent event) {
      String dest = member.getProperties().getDestination();
      String[] var5 = text;
      int var6 = text.length;
      int var7 = 0;

      while(true) {
         if (var7 >= var6) {
            return false;
         }

         String elem = var5[var7];
         if (dest == null) {
            if (elem.length() == 0) {
               break;
            }
         } else if (elem.equals(dest)) {
            break;
         }

         ++var7;
      }

      return true;
   }
}
