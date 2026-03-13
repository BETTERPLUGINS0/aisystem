package com.volmit.iris.util.math;

import java.util.Collections;
import java.util.List;
import org.bukkit.util.Vector;

public class KochanekBartelsInterpolation implements PathInterpolation {
   private List<INode> nodes;
   private Vector[] coeffA;
   private Vector[] coeffB;
   private Vector[] coeffC;
   private Vector[] coeffD;
   private double scaling;

   public KochanekBartelsInterpolation() {
      this.setNodes(Collections.emptyList());
   }

   public void setNodes(List<INode> nodes) {
      this.nodes = var1;
      this.recalc();
   }

   private void recalc() {
      int var1 = this.nodes.size();
      this.coeffA = new Vector[var1];
      this.coeffB = new Vector[var1];
      this.coeffC = new Vector[var1];
      this.coeffD = new Vector[var1];
      if (var1 != 0) {
         INode var2 = (INode)this.nodes.get(0);
         double var3 = var2.getTension();
         double var5 = var2.getBias();
         double var7 = var2.getContinuity();

         for(int var9 = 0; var9 < var1; ++var9) {
            if (var9 + 1 < var1) {
               var2 = (INode)this.nodes.get(var9 + 1);
               var3 = var2.getTension();
               var5 = var2.getBias();
               var7 = var2.getContinuity();
            }

            double var16 = (1.0D - var3) * (1.0D + var5) * (1.0D + var7) / 2.0D;
            double var18 = (1.0D - var3) * (1.0D - var5) * (1.0D - var7) / 2.0D;
            double var20 = (1.0D - var3) * (1.0D + var5) * (1.0D - var7) / 2.0D;
            double var22 = (1.0D - var3) * (1.0D - var5) * (1.0D + var7) / 2.0D;
            this.coeffA[var9] = this.linearCombination(var9, -var16, var16 - var18 - var20 + 2.0D, var18 + var20 - var22 - 2.0D, var22);
            this.coeffB[var9] = this.linearCombination(var9, 2.0D * var16, -2.0D * var16 + 2.0D * var18 + var20 - 3.0D, -2.0D * var18 - var20 + var22 + 3.0D, -var22);
            this.coeffC[var9] = this.linearCombination(var9, -var16, var16 - var18, var18, 0.0D);
            this.coeffD[var9] = this.retrieve(var9);
         }

         this.scaling = (double)(this.nodes.size() - 1);
      }
   }

   private Vector linearCombination(int baseIndex, double f1, double f2, double f3, double f4) {
      Vector var10 = this.retrieve(var1 - 1).multiply(var2);
      Vector var11 = this.retrieve(var1).multiply(var4);
      Vector var12 = this.retrieve(var1 + 1).multiply(var6);
      Vector var13 = this.retrieve(var1 + 2).multiply(var8);
      return var10.add(var11).add(var12).add(var13);
   }

   private Vector retrieve(int index) {
      if (var1 < 0) {
         return this.fastRetrieve(0);
      } else {
         return var1 >= this.nodes.size() ? this.fastRetrieve(this.nodes.size() - 1) : this.fastRetrieve(var1);
      }
   }

   private Vector fastRetrieve(int index) {
      return ((INode)this.nodes.get(var1)).getPosition();
   }

   public Vector getPosition(double position) {
      if (this.coeffA == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (var1 > 1.0D) {
         return null;
      } else {
         var1 *= this.scaling;
         int var3 = (int)Math.floor(var1);
         double var4 = var1 - (double)var3;
         Vector var6 = this.coeffA[var3];
         Vector var7 = this.coeffB[var3];
         Vector var8 = this.coeffC[var3];
         Vector var9 = this.coeffD[var3];
         return var6.multiply(var4).add(var7).multiply(var4).add(var8).multiply(var4).add(var9);
      }
   }

   public Vector get1stDerivative(double position) {
      if (this.coeffA == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (var1 > 1.0D) {
         return null;
      } else {
         var1 *= this.scaling;
         int var3 = (int)Math.floor(var1);
         Vector var4 = this.coeffA[var3];
         Vector var5 = this.coeffB[var3];
         Vector var6 = this.coeffC[var3];
         return var4.multiply(1.5D * var1 - 3.0D * (double)var3).add(var5).multiply(2.0D * var1).add(var4.multiply(1.5D * (double)var3).subtract(var5).multiply(2.0D * (double)var3)).add(var6).multiply(this.scaling);
      }
   }

   public double arcLength(double positionA, double positionB) {
      if (this.coeffA == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (var1 > var3) {
         return this.arcLength(var3, var1);
      } else {
         var1 *= this.scaling;
         var3 *= this.scaling;
         int var5 = (int)Math.floor(var1);
         double var6 = var1 - (double)var5;
         int var8 = (int)Math.floor(var3);
         double var9 = var3 - (double)var8;
         return this.arcLengthRecursive(var5, var6, var8, var9);
      }
   }

   private double arcLengthRecursive(int indexLeft, double remainderLeft, int indexRight, double remainderRight) {
      switch(var4 - var1) {
      case 0:
         return this.arcLengthRecursive(var1, var2, var5);
      case 1:
         return this.arcLengthRecursive(var1, var2, 1.0D) + this.arcLengthRecursive(var4, 0.0D, var5);
      default:
         return this.arcLengthRecursive(var1, var2, var4 - 1, 1.0D) + this.arcLengthRecursive(var4, 0.0D, var5);
      }
   }

   private double arcLengthRecursive(int index, double remainderLeft, double remainderRight) {
      Vector var6 = this.coeffA[var1].multiply(3.0D);
      Vector var7 = this.coeffB[var1].multiply(2.0D);
      Vector var8 = this.coeffC[var1];
      boolean var9 = true;
      double var10 = var6.multiply(var2).add(var7).multiply(var2).add(var8).length() / 2.0D;

      for(int var12 = 1; var12 < 7; ++var12) {
         double var13 = (double)var12 / 8.0D;
         var13 = (var4 - var2) * var13 + var2;
         var10 += var6.multiply(var13).add(var7).multiply(var13).add(var8).length();
      }

      var10 += var6.multiply(var4).add(var7).multiply(var4).add(var8).length() / 2.0D;
      return var10 * (var4 - var2) / 8.0D;
   }

   public int getSegment(double position) {
      if (this.coeffA == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (var1 > 1.0D) {
         return Integer.MAX_VALUE;
      } else {
         var1 *= this.scaling;
         return (int)Math.floor(var1);
      }
   }
}
