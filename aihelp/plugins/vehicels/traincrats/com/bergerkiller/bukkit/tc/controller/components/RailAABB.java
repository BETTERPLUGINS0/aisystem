package com.bergerkiller.bukkit.tc.controller.components;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailAABB {
   public static RailAABB BLOCK = new RailAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   public final double x_min;
   public final double y_min;
   public final double z_min;
   public final double x_max;
   public final double y_max;
   public final double z_max;
   private final double x_min_err;
   private final double y_min_err;
   private final double z_min_err;
   private final double x_max_err;
   private final double y_max_err;
   private final double z_max_err;
   private final double offset_x_pos;
   private final double offset_x_neg;
   private final double offset_y_pos;
   private final double offset_y_neg;
   private final double offset_z_pos;
   private final double offset_z_neg;

   public RailAABB(double x_min, double y_min, double z_min, double x_max, double y_max, double z_max) {
      this.x_min = x_min;
      this.y_min = y_min;
      this.z_min = z_min;
      this.x_max = x_max;
      this.y_max = y_max;
      this.z_max = z_max;
      double CONST_BOX_ERROR = 1.0E-10D;
      this.x_min_err = this.x_min - 1.0E-10D;
      this.y_min_err = this.y_min - 1.0E-10D;
      this.z_min_err = this.z_min - 1.0E-10D;
      this.x_max_err = 1.0E-10D + this.x_max;
      this.y_max_err = 1.0E-10D + this.y_max;
      this.z_max_err = 1.0E-10D + this.z_max;
      this.offset_x_pos = computeAxisOffset(x_min, x_max, 1);
      this.offset_x_neg = computeAxisOffset(x_min, x_max, -1);
      this.offset_y_pos = computeAxisOffset(y_min, y_max, 1);
      this.offset_y_neg = computeAxisOffset(y_min, y_max, -1);
      this.offset_z_pos = computeAxisOffset(z_min, z_max, 1);
      this.offset_z_neg = computeAxisOffset(z_min, z_max, -1);
   }

   public BlockFace calculateEnterFace(Vector position, Vector direction) {
      Vector result_end = new Vector(Double.NaN, Double.NaN, Double.NaN);
      BlockFace result = BlockFace.DOWN;
      double dx = direction.getX();
      if (dx > 0.0D) {
         if (this.match(position, direction, (position.getX() - this.offset_x_pos) / dx, result_end)) {
            result = BlockFace.EAST;
         }
      } else if (dx < 0.0D && this.match(position, direction, (position.getX() - this.offset_x_neg) / dx, result_end)) {
         result = BlockFace.WEST;
      }

      double dy = direction.getY();
      if (dy > 0.0D) {
         if (this.match(position, direction, (position.getY() - this.offset_y_pos) / dy, result_end)) {
            result = BlockFace.UP;
         }
      } else if (dy < 0.0D && this.match(position, direction, (position.getY() - this.offset_y_neg) / dy, result_end)) {
         result = BlockFace.DOWN;
      }

      double dz = direction.getZ();
      if (dz > 0.0D) {
         if (this.match(position, direction, (position.getZ() - this.offset_z_pos) / dz, result_end)) {
            result = BlockFace.SOUTH;
         }
      } else if (dz < 0.0D && this.match(position, direction, (position.getZ() - this.offset_z_neg) / dz, result_end)) {
         result = BlockFace.NORTH;
      }

      return result;
   }

   public boolean match(Vector position, Vector direction, double factor, Vector prev_result) {
      double end_x = position.getX() - factor * direction.getX();
      double end_y = position.getY() - factor * direction.getY();
      double end_z = position.getZ() - factor * direction.getZ();
      if (!(end_x < this.x_min_err) && !(end_y < this.y_min_err) && !(end_z < this.z_min_err) && !(end_x > this.x_max_err) && !(end_y > this.y_max_err) && !(end_z > this.z_max_err)) {
         if (!Double.isNaN(prev_result.getX())) {
            double dot = (end_x - prev_result.getX()) * direction.getX() + (end_y - prev_result.getY()) * direction.getY() + (end_z - prev_result.getZ()) * direction.getZ();
            if (dot > 0.0D) {
               return false;
            }
         }

         prev_result.setX(end_x);
         prev_result.setY(end_y);
         prev_result.setZ(end_z);
         return true;
      } else {
         return false;
      }
   }

   private static double computeAxisOffset(double min, double max, int axis) {
      return min + (max - min) * 0.5D * (double)(1 - axis);
   }
}
