package com.bergerkiller.bukkit.tc.itemanimation;

import com.bergerkiller.bukkit.common.controller.DefaultEntityNetworkController;
import com.bergerkiller.bukkit.common.controller.EntityNetworkController;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonItem;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VirtualItem {
   public final CommonItem item;
   private final ItemStack itemStack;

   public VirtualItem(Location location, ItemStack itemstack) {
      this.item = (CommonItem)CommonEntity.create(EntityType.DROPPED_ITEM, location);
      this.item.last.set(this.item.loc.set(location));
      this.item.vel.y.add(0.1D);
      this.item.setItemStack(itemstack);
      this.itemStack = itemstack;
      this.refresh();
      this.item.setNetworkController(new DefaultEntityNetworkController());
   }

   public void update(Vector dir) {
      this.item.vel.setX(dir.getX() + Math.random() * 0.02D - 0.01D);
      this.item.vel.setY(MathUtil.useOld(this.item.vel.getY(), dir.getY(), 0.1D));
      this.item.vel.setZ(dir.getZ() + Math.random() * 0.02D - 0.01D);
      this.item.last.set(this.item.loc);
      this.item.loc.add(this.item.vel);
      this.refresh();
   }

   public void refresh() {
      this.item.setPositionChanged(true);
      this.item.setVelocityChanged(true);
   }

   public void die() {
      this.item.remove();
      this.item.setNetworkController((EntityNetworkController)null);
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public Location getLocation() {
      return this.item.getLocation();
   }
}
