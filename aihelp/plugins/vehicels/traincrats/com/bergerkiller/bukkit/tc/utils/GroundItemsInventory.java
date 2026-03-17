package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.inventory.InventoryBase;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.generated.net.minecraft.util.RandomSourceHandle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GroundItemsInventory extends InventoryBase {
   private final List<Item> items;
   private final Location location;

   public GroundItemsInventory(Block block, double range) {
      this(block.getLocation().add(0.5D, 0.5D, 0.5D), range);
   }

   public GroundItemsInventory(Location location, double range) {
      this.items = new ArrayList();
      this.location = location;
      double rangeSquared = range * range;
      Iterator var6 = WorldUtil.getEntities(location.getWorld()).iterator();

      while(var6.hasNext()) {
         Entity e = (Entity)var6.next();
         if (e instanceof Item && e.getLocation().distanceSquared(location) <= rangeSquared) {
            this.items.add((Item)e);
         }
      }

   }

   public int getSize() {
      return this.items.size() + 1;
   }

   public Location getLocation() {
      return this.location;
   }

   public Item getEntity(int index) {
      return (Item)this.items.get(index);
   }

   public void setItem(int index, ItemStack stack) {
      if (index == this.items.size()) {
         if (!LogicUtil.nullOrEmpty(stack)) {
            RandomSourceHandle random = WorldUtil.getRandom(this.location.getWorld());
            Location spawnLoc = this.location.clone().add(-0.45D, -0.45D, -0.45D);
            spawnLoc = spawnLoc.add((double)(0.9F * random.nextFloat()), (double)(0.9F * random.nextFloat()), (double)(0.9F * random.nextFloat()));
            Item item = this.location.getWorld().dropItem(spawnLoc, stack);
            item.setVelocity(new Vector(0, 0, 0));
            this.items.add(item);
         }
      } else {
         Item item = (Item)this.items.get(index);
         EntityUtil.setDestroyed(item, LogicUtil.nullOrEmpty(stack));
         if (!item.isDead()) {
            item.setItemStack(stack);
            this.items.set(index, ItemUtil.respawnItem(item));
         }
      }

   }

   public ItemStack getItem(int index) {
      if (index == this.items.size()) {
         return null;
      } else {
         Item item = (Item)this.items.get(index);
         return item.isDead() ? null : item.getItemStack();
      }
   }

   public String getName() {
      return "Ground Items";
   }
}
