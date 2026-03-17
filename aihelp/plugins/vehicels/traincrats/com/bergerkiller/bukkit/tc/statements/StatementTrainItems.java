package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class StatementTrainItems extends StatementItems {
   public boolean match(String text) {
      return text.startsWith("items");
   }

   public boolean matchArray(String text) {
      return text.equals("i");
   }

   public Inventory getInventory(MinecartMember<?> member) {
      Entity entity = ((CommonMinecart)member.getEntity()).getEntity();
      return entity instanceof InventoryHolder ? ((InventoryHolder)entity).getInventory() : null;
   }

   public Inventory getInventory(MinecartGroup group) {
      return group.getInventory();
   }
}
