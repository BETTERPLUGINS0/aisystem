package com.volmit.iris.util.math;

import java.util.Objects;
import lombok.Generated;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockPosition {
   private static final int m1 = 1 + MathHelper.f(MathHelper.c(30000000));
   private static final int m2;
   private static final long m3;
   private static final long m5;
   private static final long m4;
   private static final long m6;
   private int x;
   private int y;
   private int z;

   public BlockPosition(int x, int y, int z) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public static long toLong(int x, int y, int z) {
      long var3 = 0L;
      var3 |= ((long)var0 & m4) << (int)m3;
      var3 |= (long)var1 & m5;
      var3 |= ((long)var2 & m6) << m2;
      return var3;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y, this.z});
   }

   public boolean equals(Object o) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof BlockPosition)) {
         return false;
      } else {
         BlockPosition var2 = (BlockPosition)var1;
         return var2.x == this.x && var2.y == this.y && var2.z == this.z;
      }
   }

   public int getChunkX() {
      return this.x >> 4;
   }

   public int getChunkZ() {
      return this.z >> 4;
   }

   public boolean is(int x, int z) {
      return this.x == var1 && this.z == var2;
   }

   public boolean is(int x, int y, int z) {
      return this.x == var1 && this.y == var2 && this.z == var3;
   }

   public long asLong() {
      return toLong(this.getX(), this.getY(), this.getZ());
   }

   public Block toBlock(World world) {
      return var1.getBlockAt(this.x, this.y, this.z);
   }

   public BlockPosition add(int x, int y, int z) {
      return new BlockPosition(this.x + var1, this.y + var2, this.z + var3);
   }

   public void min(BlockPosition i) {
      this.setX(Math.min(var1.getX(), this.getX()));
      this.setY(Math.min(var1.getY(), this.getY()));
      this.setZ(Math.min(var1.getZ(), this.getZ()));
   }

   public void max(BlockPosition i) {
      this.setX(Math.max(var1.getX(), this.getX()));
      this.setY(Math.max(var1.getY(), this.getY()));
      this.setZ(Math.max(var1.getZ(), this.getZ()));
   }

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getY() {
      return this.y;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public void setX(final int x) {
      this.x = var1;
   }

   @Generated
   public void setY(final int y) {
      this.y = var1;
   }

   @Generated
   public void setZ(final int z) {
      this.z = var1;
   }

   @Generated
   public String toString() {
      int var10000 = this.getX();
      return "BlockPosition(x=" + var10000 + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
   }

   static {
      m2 = 64 - m1 * 2;
      m3 = (long)(m1 + m2);
      m5 = (1L << m2) - 1L;
      m4 = (1L << m1) - 1L;
      m6 = (1L << m1) - 1L;
   }
}
