package com.volmit.iris.util.math;

import lombok.Generated;
import org.bukkit.util.Vector;

public class INode {
   private Vector position;
   private double tension;
   private double bias;
   private double continuity;

   public INode() {
      this(new Vector(0, 0, 0));
   }

   public INode(INode other) {
      this.position = var1.position;
      this.tension = var1.tension;
      this.bias = var1.bias;
      this.continuity = var1.continuity;
   }

   public INode(Vector position) {
      this.position = var1;
   }

   @Generated
   public Vector getPosition() {
      return this.position;
   }

   @Generated
   public double getTension() {
      return this.tension;
   }

   @Generated
   public double getBias() {
      return this.bias;
   }

   @Generated
   public double getContinuity() {
      return this.continuity;
   }

   @Generated
   public void setPosition(final Vector position) {
      this.position = var1;
   }

   @Generated
   public void setTension(final double tension) {
      this.tension = var1;
   }

   @Generated
   public void setBias(final double bias) {
      this.bias = var1;
   }

   @Generated
   public void setContinuity(final double continuity) {
      this.continuity = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof INode)) {
         return false;
      } else {
         INode var2 = (INode)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getTension(), var2.getTension()) != 0) {
            return false;
         } else if (Double.compare(this.getBias(), var2.getBias()) != 0) {
            return false;
         } else if (Double.compare(this.getContinuity(), var2.getContinuity()) != 0) {
            return false;
         } else {
            Vector var3 = this.getPosition();
            Vector var4 = var2.getPosition();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof INode;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getTension());
      int var10 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getBias());
      var10 = var10 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getContinuity());
      var10 = var10 * 59 + (int)(var7 >>> 32 ^ var7);
      Vector var9 = this.getPosition();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPosition());
      return "INode(position=" + var10000 + ", tension=" + this.getTension() + ", bias=" + this.getBias() + ", continuity=" + this.getContinuity() + ")";
   }
}
