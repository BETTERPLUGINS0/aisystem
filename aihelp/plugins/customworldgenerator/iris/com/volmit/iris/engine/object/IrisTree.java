package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.TreeType;

@Snippet("tree")
@Desc("Tree replace options for this object placer")
public class IrisTree {
   @Required
   @Desc("The types of trees overwritten by this object")
   @ArrayType(
      min = 1,
      type = TreeType.class
   )
   private KList<TreeType> treeTypes;
   @Desc("If enabled, overrides any TreeType")
   private boolean anyTree = false;
   @Required
   @Desc("The size of the square of saplings this applies to (2 means a 2 * 2 sapling area)")
   @ArrayType(
      min = 1,
      type = IrisTreeSize.class
   )
   private KList<IrisTreeSize> sizes = new KList();
   @Desc("If enabled, overrides trees of any size")
   private boolean anySize;

   public boolean matches(IrisTreeSize size, TreeType type) {
      return !this.matchesSize(var1) ? false : this.matchesType(var2);
   }

   private boolean matchesSize(IrisTreeSize size) {
      Iterator var2 = this.getSizes().iterator();

      IrisTreeSize var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (IrisTreeSize)var2.next();
      } while((var3.getDepth() != var1.getDepth() || var3.getWidth() != var1.getWidth()) && (var3.getDepth() != var1.getWidth() || var3.getWidth() != var1.getDepth()));

      return true;
   }

   private boolean matchesType(TreeType type) {
      return this.getTreeTypes().contains(var1);
   }

   @Generated
   public IrisTree(final KList<TreeType> treeTypes, final boolean anyTree, final KList<IrisTreeSize> sizes, final boolean anySize) {
      this.treeTypes = var1;
      this.anyTree = var2;
      this.sizes = var3;
      this.anySize = var4;
   }

   @Generated
   public IrisTree() {
   }

   @Generated
   public KList<TreeType> getTreeTypes() {
      return this.treeTypes;
   }

   @Generated
   public boolean isAnyTree() {
      return this.anyTree;
   }

   @Generated
   public KList<IrisTreeSize> getSizes() {
      return this.sizes;
   }

   @Generated
   public boolean isAnySize() {
      return this.anySize;
   }

   @Generated
   public IrisTree setTreeTypes(final KList<TreeType> treeTypes) {
      this.treeTypes = var1;
      return this;
   }

   @Generated
   public IrisTree setAnyTree(final boolean anyTree) {
      this.anyTree = var1;
      return this;
   }

   @Generated
   public IrisTree setSizes(final KList<IrisTreeSize> sizes) {
      this.sizes = var1;
      return this;
   }

   @Generated
   public IrisTree setAnySize(final boolean anySize) {
      this.anySize = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisTree)) {
         return false;
      } else {
         IrisTree var2 = (IrisTree)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isAnyTree() != var2.isAnyTree()) {
            return false;
         } else if (this.isAnySize() != var2.isAnySize()) {
            return false;
         } else {
            label40: {
               KList var3 = this.getTreeTypes();
               KList var4 = var2.getTreeTypes();
               if (var3 == null) {
                  if (var4 == null) {
                     break label40;
                  }
               } else if (var3.equals(var4)) {
                  break label40;
               }

               return false;
            }

            KList var5 = this.getSizes();
            KList var6 = var2.getSizes();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisTree;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isAnyTree() ? 79 : 97);
      var5 = var5 * 59 + (this.isAnySize() ? 79 : 97);
      KList var3 = this.getTreeTypes();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getSizes();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getTreeTypes());
      return "IrisTree(treeTypes=" + var10000 + ", anyTree=" + this.isAnyTree() + ", sizes=" + String.valueOf(this.getSizes()) + ", anySize=" + this.isAnySize() + ")";
   }
}
