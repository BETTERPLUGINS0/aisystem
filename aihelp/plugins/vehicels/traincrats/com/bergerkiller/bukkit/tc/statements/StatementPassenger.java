package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;

public class StatementPassenger extends Statement {
   public boolean match(String text) {
      return text.startsWith("passenger") || text.startsWith("player");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return text.toLowerCase().startsWith("player") ? ((CommonMinecart)member.getEntity()).hasPlayerPassenger() : ((CommonMinecart)member.getEntity()).hasPassenger();
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      int count = 0;
      boolean playermode = text.toLowerCase().startsWith("player");
      Iterator var6 = group.iterator();

      while(var6.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var6.next();
         if (playermode) {
            count += ((CommonMinecart)member.getEntity()).getPlayerPassengers().size();
         } else {
            count += ((CommonMinecart)member.getEntity()).getPassengers().size();
         }
      }

      return Util.evaluate((double)count, text);
   }

   public boolean matchArray(String text) {
      return text.equals("p");
   }

   public boolean handleArray(MinecartMember<?> member, String[] names, SignActionEvent event) {
      List<Player> playerPassengers = ((CommonMinecart)member.getEntity()).getPlayerPassengers();
      if (!playerPassengers.isEmpty()) {
         Iterator var5 = playerPassengers.iterator();

         while(var5.hasNext()) {
            Player player = (Player)var5.next();
            String pname = player.getName();
            String[] var8 = names;
            int var9 = names.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String name = var8[var10];
               if (Util.matchText(pname, name)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public int priority() {
      return -1;
   }
}
