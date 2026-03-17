package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.inventory.Inventory;

public class StatementPlayerItems extends StatementItems {
   public boolean match(String text) {
      return text.startsWith("playeritems");
   }

   public boolean matchArray(String text) {
      return text.equals("pi");
   }

   public Inventory getInventory(MinecartMember<?> member) {
      return member.getPlayerInventory();
   }

   public Inventory getInventory(MinecartGroup group) {
      return group.getPlayerInventory();
   }
}
