package com.bergerkiller.bukkit.tc.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BoundingRange {
   private final double min;
   private final double max;
   private final boolean exclusive;

   private BoundingRange(double min, double max, boolean exclusive) {
      this.min = min;
      this.max = max;
      this.exclusive = exclusive;
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public boolean isInside(double value) {
      return (value >= this.min && value <= this.max) != this.exclusive;
   }

   public double distance(double value) {
      if (value <= this.min) {
         return this.exclusive ? 0.0D : this.min - value;
      } else if (value >= this.max) {
         return this.exclusive ? 0.0D : value - this.max;
      } else {
         return this.exclusive ? Math.min(value - this.min, this.max - value) : 0.0D;
      }
   }

   public boolean isZeroLength() {
      return this.min == this.max;
   }

   public boolean isInclusive() {
      return !this.exclusive;
   }

   public boolean isExclusive() {
      return this.exclusive;
   }

   public BoundingRange invert() {
      return new BoundingRange(this.min, this.max, !this.exclusive);
   }

   public BoundingRange squared() {
      double new_min = this.min * this.min;
      double new_max = this.max * this.max;
      if (this.min < 0.0D) {
         new_min = -new_min;
      }

      if (this.max < 0.0D) {
         new_max = -new_max;
      }

      return new BoundingRange(new_min, new_max, this.exclusive);
   }

   public BoundingRange add(BoundingRange amount) {
      double new_min;
      double new_max;
      if (amount.isZeroLength()) {
         if (amount.min > 0.0D) {
            new_min = this.min;
            new_max = this.max + amount.min;
         } else {
            new_min = this.min + amount.min;
            new_max = this.max;
         }
      } else {
         new_min = this.min + amount.min;
         new_max = this.max + amount.max;
      }

      if (new_min > new_max) {
         if (Math.abs(new_max) < Math.abs(new_min)) {
            new_min = new_max;
         } else {
            new_max = new_min;
         }
      }

      return new BoundingRange(new_min, new_max, this.exclusive != amount.exclusive);
   }

   public static BoundingRange create(double min, double max) {
      return new BoundingRange(min, max, false);
   }

   public static final class Axis {
      public World world;
      public BoundingRange x;
      public BoundingRange y;
      public BoundingRange z;

      private Axis() {
      }

      public boolean isEmpty() {
         return this.world == null;
      }

      public boolean isInside(Vector position) {
         return this.x.isInside(position.getX()) && this.y.isInside(position.getY()) && this.z.isInside(position.getZ());
      }

      public double distanceSquared(Vector position) {
         double dx = this.x.distance(position.getX());
         double dy = this.y.distance(position.getY());
         double dz = this.z.distance(position.getZ());
         return dx * dx + dy * dy + dz * dz;
      }

      public double distanceSquared(Location location) {
         double dx = this.x.distance(location.getX());
         double dy = this.y.distance(location.getY());
         double dz = this.z.distance(location.getZ());
         return dx * dx + dy * dy + dz * dz;
      }

      public static BoundingRange.Axis empty() {
         return new BoundingRange.Axis();
      }

      public static BoundingRange.Axis forSender(CommandSender sender) {
         if (sender instanceof BlockCommandSender) {
            return forBlock(((BlockCommandSender)sender).getBlock());
         } else {
            return sender instanceof Entity ? forPoint(((Entity)sender).getLocation()) : empty();
         }
      }

      public static BoundingRange.Axis forPoint(Location location) {
         if (location == null) {
            return empty();
         } else {
            BoundingRange.Axis axis = new BoundingRange.Axis();
            axis.world = location.getWorld();
            axis.x = BoundingRange.create(location.getX(), location.getX());
            axis.y = BoundingRange.create(location.getY(), location.getY());
            axis.z = BoundingRange.create(location.getZ(), location.getZ());
            return axis;
         }
      }

      public static BoundingRange.Axis forBlock(Block block) {
         if (block == null) {
            return empty();
         } else {
            double x = (double)block.getX();
            double y = (double)block.getY();
            double z = (double)block.getZ();
            BoundingRange.Axis axis = new BoundingRange.Axis();
            axis.world = block.getWorld();
            axis.x = BoundingRange.create(x, x + 1.0D);
            axis.y = BoundingRange.create(y, y + 1.0D);
            axis.z = BoundingRange.create(z, z + 1.0D);
            return axis;
         }
      }
   }
}
