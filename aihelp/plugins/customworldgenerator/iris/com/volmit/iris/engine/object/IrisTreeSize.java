package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("tree-size")
@Desc("Sapling override object picking options")
public class IrisTreeSize {
   @Required
   @Desc("The width of the sapling area")
   int width = 1;
   @Required
   @Desc("The depth of the sapling area")
   int depth = 1;

   public boolean doesMatch(IrisTreeSize size) {
      return this.width == var1.getWidth() && this.depth == var1.getDepth() || this.depth == var1.getWidth() && this.width == var1.getDepth();
   }

   @Generated
   public IrisTreeSize() {
   }

   @Generated
   public IrisTreeSize(final int width, final int depth) {
      this.width = var1;
      this.depth = var2;
   }

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getDepth() {
      return this.depth;
   }

   @Generated
   public IrisTreeSize setWidth(final int width) {
      this.width = var1;
      return this;
   }

   @Generated
   public IrisTreeSize setDepth(final int depth) {
      this.depth = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisTreeSize)) {
         return false;
      } else {
         IrisTreeSize var2 = (IrisTreeSize)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getWidth() != var2.getWidth()) {
            return false;
         } else {
            return this.getDepth() == var2.getDepth();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisTreeSize;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getWidth();
      var3 = var3 * 59 + this.getDepth();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getWidth();
      return "IrisTreeSize(width=" + var10000 + ", depth=" + this.getDepth() + ")";
   }
}
