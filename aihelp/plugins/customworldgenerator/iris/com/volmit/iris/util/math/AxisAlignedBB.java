package com.volmit.iris.util.math;

import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.data.Cuboid;
import org.bukkit.World;
import org.bukkit.util.BlockVector;

public class AxisAlignedBB {
   private final double xa;
   private final double xb;
   private final double ya;
   private final double yb;
   private final double za;
   private final double zb;

   public AxisAlignedBB(double xa, double xb, double ya, double yb, double za, double zb) {
      this.xa = var1;
      this.xb = var3;
      this.ya = var5;
      this.yb = var7;
      this.za = var9;
      this.zb = var11;
   }

   public AxisAlignedBB(AlignedPoint a, AlignedPoint b) {
      this(var1.getX(), var2.getX(), var1.getY(), var2.getY(), var1.getZ(), var2.getZ());
   }

   public AxisAlignedBB(IrisPosition a, IrisPosition b) {
      this((double)var1.getX(), (double)var2.getX(), (double)var1.getY(), (double)var2.getY(), (double)var1.getZ(), (double)var2.getZ());
   }

   public AxisAlignedBB shifted(IrisPosition p) {
      return this.shifted((double)var1.getX(), (double)var1.getY(), (double)var1.getZ());
   }

   public AxisAlignedBB shifted(double x, double y, double z) {
      return new AxisAlignedBB(this.min().add(new IrisPosition((int)var1, (int)var3, (int)var5)), this.max().add(new IrisPosition((int)var1, (int)var3, (int)var5)));
   }

   public boolean contains(AlignedPoint p) {
      return var1.getX() >= this.xa && var1.getX() <= this.xb && var1.getY() >= this.ya && var1.getZ() <= this.yb && var1.getZ() >= this.za && var1.getZ() <= this.zb;
   }

   public boolean contains(IrisPosition p) {
      return (double)var1.getX() >= this.xa && (double)var1.getX() <= this.xb && (double)var1.getY() >= this.ya && (double)var1.getZ() <= this.yb && (double)var1.getZ() >= this.za && (double)var1.getZ() <= this.zb;
   }

   public boolean intersects(AxisAlignedBB s) {
      return this.xb >= var1.xa && this.yb >= var1.ya && this.zb >= var1.za && var1.xb >= this.xa && var1.yb >= this.ya && var1.zb >= this.za;
   }

   public IrisPosition max() {
      return new IrisPosition((int)this.xb, (int)this.yb, (int)this.zb);
   }

   public BlockVector maxbv() {
      return new BlockVector((int)this.xb, (int)this.yb, (int)this.zb);
   }

   public IrisPosition min() {
      return new IrisPosition((int)this.xa, (int)this.ya, (int)this.za);
   }

   public BlockVector minbv() {
      return new BlockVector((int)this.xa, (int)this.ya, (int)this.za);
   }

   public Cuboid toCuboid(World world) {
      return new Cuboid(this.min().toLocation(var1), this.max().toLocation(var1));
   }
}
