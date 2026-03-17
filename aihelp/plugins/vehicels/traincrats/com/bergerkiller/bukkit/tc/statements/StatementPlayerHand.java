package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.inventory.InventoryBaseImpl;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StatementPlayerHand extends StatementItems {
   public boolean match(String text) {
      return text.startsWith("playerhand");
   }

   public boolean matchArray(String text) {
      return text.equals("ph");
   }

   private void addItems(MinecartMember<?> member, ArrayList<ItemStack> itemsList) {
      Iterator var3 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         ItemStack item1 = HumanHand.getItemInMainHand(player);
         ItemStack item2 = HumanHand.getItemInOffHand(player);
         if (!LogicUtil.nullOrEmpty(item1)) {
            itemsList.add(item1);
         }

         if (!LogicUtil.nullOrEmpty(item2)) {
            itemsList.add(item2);
         }
      }

   }

   public Inventory getInventory(MinecartMember<?> member) {
      ArrayList<ItemStack> items = new ArrayList();
      this.addItems(member, items);
      return new InventoryBaseImpl(items, false);
   }

   public Inventory getInventory(MinecartGroup group) {
      ArrayList<ItemStack> items = new ArrayList();
      Iterator var3 = group.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var3.next();
         this.addItems(member, items);
      }

      return new InventoryBaseImpl(items, false);
   }
}
