package com.bergerkiller.bukkit.tc.itemanimation;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.utils.GroundItemsInventory;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemAnimation {
   private static final ArrayList<ItemAnimation> runningAnimations = new ArrayList();
   private static Task task;
   private final Object from;
   private final Object to;
   private final VirtualItem item;
   public int ticksToFinish = 10;

   private ItemAnimation(Object from, Object to, ItemStack data) {
      this.from = fixObject(from);
      this.to = fixObject(to);
      Location f = this.getFrom();
      Location t = this.getTo();
      if (f.getWorld() != t.getWorld()) {
         throw new IllegalArgumentException("Locations are on different worlds!");
      } else {
         this.item = new VirtualItem(f, data);
      }
   }

   public static void start(Object from, Object to, ItemStack data) {
      start(from, to, CommonItemStack.of(data));
   }

   public static void start(Object from, Object to, CommonItemStack data) {
      if (from != null && to != null && !data.isEmpty()) {
         data = data.clone();
         Location l1 = getLocation(fixObject(from));
         Iterator var4 = runningAnimations.iterator();

         while(var4.hasNext()) {
            ItemAnimation anim = (ItemAnimation)var4.next();
            Location l2 = getLocation(fixObject(anim.item));
            if (l2 != null && l1.getWorld() == l2.getWorld() && l1.distanceSquared(l2) < 4.0D) {
               CommonItemStack thisdata = CommonItemStack.of(anim.item.getItemStack());
               if (!thisdata.isEmpty()) {
                  data.transferTo(thisdata, -1);
                  if (data.isEmpty()) {
                     return;
                  }
               }
            }
         }

         runningAnimations.add(new ItemAnimation(from, to, data.toBukkit()));
         if (task == null) {
            task = (new Task(TrainCarts.plugin) {
               public void run() {
                  Iterator iter = ItemAnimation.runningAnimations.iterator();

                  while(iter.hasNext()) {
                     ItemAnimation anim = (ItemAnimation)iter.next();
                     if (anim.update()) {
                        anim.item.die();
                        iter.remove();
                     }
                  }

                  if (ItemAnimation.runningAnimations.isEmpty()) {
                     Task.stop(ItemAnimation.task);
                     ItemAnimation.task = null;
                  }

               }
            }).start(1L, 1L);
         }

      }
   }

   public static void deinit() {
      Iterator var0 = runningAnimations.iterator();

      while(var0.hasNext()) {
         ItemAnimation anim = (ItemAnimation)var0.next();
         anim.item.die();
      }

      runningAnimations.clear();
      Task.stop(task);
      task = null;
   }

   private static Object fixObject(Object object) {
      if (object instanceof GroundItemsInventory) {
         return ((GroundItemsInventory)object).getLocation();
      } else {
         if (object instanceof BlockState) {
            object = ((BlockState)object).getBlock();
         }

         if (object instanceof DoubleChest) {
            return ((DoubleChest)object).getLocation();
         } else if (object instanceof Block) {
            return ((Block)object).getLocation().add(0.5D, 0.5D, 0.5D);
         } else if (object instanceof MinecartMember) {
            return ((CommonMinecart)((MinecartMember)object).getEntity()).getEntity();
         } else {
            if (object instanceof VirtualItem) {
               object = ((VirtualItem)object).item.getEntity();
            }

            return object;
         }
      }
   }

   private static Location getLocation(Object object) {
      if (object instanceof Entity) {
         return ((Entity)object).getLocation();
      } else if (object instanceof Location) {
         return (Location)object;
      } else {
         throw new IllegalArgumentException("Unable to find the location of " + object.getClass().getName());
      }
   }

   public Location getTo() {
      return getLocation(this.to);
   }

   public Location getFrom() {
      return getLocation(this.from);
   }

   public boolean update() {
      if (--this.ticksToFinish > 0) {
         Vector dir = this.item.item.loc.offsetTo(this.getTo());
         double distancePerTick = dir.length();
         distancePerTick /= (double)this.ticksToFinish;
         dir.normalize().multiply(distancePerTick);
         this.item.update(dir);
         return false;
      } else {
         return true;
      }
   }
}
