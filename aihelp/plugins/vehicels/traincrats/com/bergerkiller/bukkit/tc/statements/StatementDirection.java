package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.direction.RailEnterDirection;
import org.bukkit.block.BlockFace;

public class StatementDirection extends Statement {
   public boolean match(String text) {
      return false;
   }

   public boolean matchArray(String text) {
      return text.equals("ed");
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return false;
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      return false;
   }

   public boolean handleArray(MinecartGroup group, String[] directionNames, SignActionEvent event) {
      return event.getGroup() == group ? this.handleArray(event.getMember(), directionNames, event) : this.handleArray(group.head(), directionNames, event);
   }

   public boolean handleArray(MinecartMember<?> member, String[] directionNames, SignActionEvent event) {
      RailState enterState = null;
      if (member == null || event.getMember() == member) {
         enterState = event.getCartEnterState();
      }

      if (member != null && enterState == null) {
         enterState = member.getRailTracker().getState();
         if (enterState != null && enterState.railPiece().isNone()) {
            enterState = null;
         }
      }

      if (enterState == null) {
         return false;
      } else {
         BlockFace forwardDirection = event.getFacing().getOppositeFace();
         String[] var6 = directionNames;
         int var7 = directionNames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String directionName = var6[var8];
            RailEnterDirection[] var10 = RailEnterDirection.parseAll(event.getRailPiece(), forwardDirection, directionName);
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               RailEnterDirection dir = var10[var12];
               if (dir.match(enterState)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean hasRequiredContext(MinecartMember<?> member, MinecartGroup group, SignActionEvent event) {
      if (event == null) {
         return false;
      } else {
         return member != null || group != null || event.getCartEnterState() != null;
      }
   }

   public boolean requiredEvent() {
      return true;
   }

   public boolean isConstant() {
      return true;
   }
}
