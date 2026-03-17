package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class StatementPermission extends Statement {
   public boolean match(String text) {
      return false;
   }

   public boolean matchArray(String text) {
      return text.equals("pm") || text.equals("perm");
   }

   public boolean handleArray(MinecartGroup group, String[] text, SignActionEvent event) {
      Iterator var4 = group.iterator();

      MinecartMember member;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         member = (MinecartMember)var4.next();
      } while(this.handleArray(member, text, event));

      return false;
   }

   public boolean handleArray(MinecartMember<?> member, String[] text, SignActionEvent event) {
      if (((CommonMinecart)member.getEntity()).hasPlayerPassenger()) {
         Iterator var4 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

         while(var4.hasNext()) {
            Player player = (Player)var4.next();
            String[] var6 = text;
            int var7 = text.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String perm = var6[var8];
               if (!player.hasPermission(perm)) {
                  return false;
               }
            }
         }
      }

      return true;
   }
}
