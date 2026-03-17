package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.inventory.MergedInventory;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.ListIterator;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class StatementItems extends Statement {
   public abstract Inventory getInventory(MinecartMember<?> var1);

   public abstract Inventory getInventory(MinecartGroup var1);

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      Inventory inventory = this.getInventory(member);
      if (inventory == null) {
         inventory = new MergedInventory(new Inventory[0]);
      }

      int count = ItemUtil.getItemCount((Inventory)inventory, (Material)null, -1);
      return Util.evaluate((double)count, text);
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      Inventory inventory = this.getInventory(group);
      if (inventory == null) {
         inventory = new MergedInventory(new Inventory[0]);
      }

      int count = ItemUtil.getItemCount((Inventory)inventory, (Material)null, -1);
      return Util.evaluate((double)count, text);
   }

   public boolean handleInventory(Inventory inv, String[] items) {
      if (inv == null) {
         return false;
      } else {
         String[] var3 = items;
         int var4 = items.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String itemname = var3[var5];
            int count;
            if (inv.getSize() == 0) {
               count = 0;
            } else {
               int opidx = Util.getOperatorIndex(itemname);
               String itemnamefixed;
               if (opidx > 0) {
                  itemnamefixed = itemname.substring(0, opidx);
               } else {
                  itemnamefixed = itemname;
               }

               ItemParser[] var10 = Util.getParsers(itemnamefixed);
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  ItemParser parser = var10[var12];
                  count = ItemUtil.getItemCount(inv, parser.getType(), parser.getData());
                  if (opidx == -1) {
                     if (parser.hasAmount()) {
                        if (count >= parser.getAmount()) {
                           return true;
                        }
                     } else if (count > 0) {
                        return true;
                     }
                  } else if (Util.evaluate((double)count, itemname)) {
                     return true;
                  }
               }

               count = 0;
               ListIterator var14 = inv.iterator();

               while(var14.hasNext()) {
                  ItemStack item = (ItemStack)var14.next();
                  if (item != null && ItemUtil.hasDisplayName(item) && ItemUtil.getDisplayName(item).equals(itemnamefixed)) {
                     count += item.getAmount();
                  }
               }
            }

            if (Util.evaluate((double)count, itemname)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean handleArray(MinecartMember<?> member, String[] items, SignActionEvent event) {
      return this.handleInventory(this.getInventory(member), items);
   }

   public boolean handleArray(MinecartGroup group, String[] items, SignActionEvent event) {
      return this.handleInventory(this.getInventory(group), items);
   }
}
