package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("object-limit")
@Desc("Translate objects")
public class IrisObjectLimit {
   @MinNumber(0.0D)
   @MaxNumber(1024.0D)
   @Desc("The minimum height for placement (bottom of object)")
   private int minimumHeight = -2048;
   @MinNumber(0.0D)
   @MaxNumber(1024.0D)
   @Desc("The maximum height for placement (top of object)")
   private int maximumHeight = 2048;

   public boolean canPlace(int h, int l) {
      return var1 <= this.maximumHeight && var2 >= this.minimumHeight;
   }

   @Generated
   public IrisObjectLimit() {
   }

   @Generated
   public IrisObjectLimit(final int minimumHeight, final int maximumHeight) {
      this.minimumHeight = var1;
      this.maximumHeight = var2;
   }

   @Generated
   public int getMinimumHeight() {
      return this.minimumHeight;
   }

   @Generated
   public int getMaximumHeight() {
      return this.maximumHeight;
   }

   @Generated
   public IrisObjectLimit setMinimumHeight(final int minimumHeight) {
      this.minimumHeight = var1;
      return this;
   }

   @Generated
   public IrisObjectLimit setMaximumHeight(final int maximumHeight) {
      this.maximumHeight = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectLimit)) {
         return false;
      } else {
         IrisObjectLimit var2 = (IrisObjectLimit)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinimumHeight() != var2.getMinimumHeight()) {
            return false;
         } else {
            return this.getMaximumHeight() == var2.getMaximumHeight();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectLimit;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getMinimumHeight();
      var3 = var3 * 59 + this.getMaximumHeight();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getMinimumHeight();
      return "IrisObjectLimit(minimumHeight=" + var10000 + ", maximumHeight=" + this.getMaximumHeight() + ")";
   }
}
