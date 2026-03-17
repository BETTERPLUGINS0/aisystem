package com.bergerkiller.bukkit.tc.controller.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartChest;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.persistence.MinecartInventoryPersistentCartAttribute;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import java.util.Iterator;
import java.util.ListIterator;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartMemberChest extends MinecartMember<CommonMinecartChest> {
   public MinecartMemberChest(TrainCarts plugin) {
      super(plugin);
      this.addPersistentCartAttribute(new MinecartInventoryPersistentCartAttribute());
   }

   public void onAttached() {
      super.onAttached();
   }

   public boolean hasItem(ItemParser item) {
      if (item == null) {
         return false;
      } else {
         return item.hasData() ? this.hasItem(item.getType(), item.getData()) : this.hasItem(item.getType());
      }
   }

   public boolean hasItem(Material type, int data) {
      ListIterator var3 = ((CommonMinecartChest)this.entity).getInventory().iterator();

      ItemStack stack;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         stack = (ItemStack)var3.next();
      } while(LogicUtil.nullOrEmpty(stack) || stack.getType() != type || stack.getDurability() != data);

      return true;
   }

   public boolean hasItem(Material type) {
      ListIterator var2 = ((CommonMinecartChest)this.entity).getInventory().iterator();

      ItemStack stack;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         stack = (ItemStack)var2.next();
      } while(LogicUtil.nullOrEmpty(stack) || stack.getType() != type);

      return true;
   }

   public boolean hasItems() {
      ListIterator var1 = ((CommonMinecartChest)this.entity).getInventory().iterator();

      ItemStack stack;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         stack = (ItemStack)var1.next();
      } while(stack == null);

      return true;
   }

   public void onPhysicsPostMove() throws MemberMissingException, GroupUnloadedException {
      super.onPhysicsPostMove();
      if (this.getProperties().canPickup()) {
         Inventory inv = ((CommonMinecartChest)this.entity).getInventory();
         Iterator var4 = ((CommonMinecartChest)this.entity).getNearbyEntities(TCConfig.itemPickupRadius).iterator();

         while(var4.hasNext()) {
            Entity e = (Entity)var4.next();
            if (e instanceof Item && !EntityUtil.isIgnored(e)) {
               CommonItemStack stack = CommonItemStack.of(((Item)e).getItemStack());
               double distance = ((CommonMinecartChest)this.entity).loc.distanceSquared(e);
               if (stack.testTransferTo(inv) == stack.getAmount()) {
                  if (distance < 0.7D) {
                     stack.transferAllTo(inv);
                     ((CommonMinecartChest)this.entity).getWorld().playEffect(((CommonMinecartChest)this.entity).getLocation(), Effect.CLICK1, 0);
                     if (stack.getAmount() == 0) {
                        e.remove();
                     }
                  } else {
                     double factor;
                     if (distance > 1.0D) {
                        factor = 0.8D;
                     } else if (distance > 0.75D) {
                        factor = 0.5D;
                     } else {
                        factor = 0.1D;
                     }

                     this.push(e, -factor / distance);
                  }
               }
            }
         }
      }

   }

   public void onItemSet(int index, ItemStack item) {
      super.onItemSet(index, item);
      this.onPropertiesChanged();
   }
}
