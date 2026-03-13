package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.util.BlockVector;

@Snippet("object-translator")
@Desc("Translate objects")
public class IrisObjectTranslate {
   @MinNumber(-128.0D)
   @MaxNumber(128.0D)
   @Desc("The x shift in blocks")
   private int x = 0;
   @Required
   @MinNumber(-128.0D)
   @MaxNumber(128.0D)
   @Desc("The y shift in blocks")
   private int y = 0;
   @MinNumber(-128.0D)
   @MaxNumber(128.0D)
   @Desc("Adds an additional amount of height randomly (translateY + rand(0 - yRandom))")
   private int yRandom = 0;
   @MinNumber(-128.0D)
   @MaxNumber(128.0D)
   @Desc("The z shift in blocks")
   private int z = 0;

   public boolean canTranslate() {
      return this.x != 0 || this.y != 0 || this.z != 0;
   }

   public BlockVector translate(BlockVector i) {
      return this.canTranslate() ? (BlockVector)var1.clone().add(new BlockVector(this.x, this.y, this.z)) : var1;
   }

   public BlockVector translate(BlockVector clone, IrisObjectRotation rotation, int sx, int sy, int sz) {
      return this.canTranslate() ? (BlockVector)var1.clone().add(var2.rotate(new BlockVector(this.x, this.y, this.z), var3, var4, var5)) : var1;
   }

   @Generated
   public IrisObjectTranslate() {
   }

   @Generated
   public IrisObjectTranslate(final int x, final int y, final int yRandom, final int z) {
      this.x = var1;
      this.y = var2;
      this.yRandom = var3;
      this.z = var4;
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
   public int getYRandom() {
      return this.yRandom;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public IrisObjectTranslate setX(final int x) {
      this.x = var1;
      return this;
   }

   @Generated
   public IrisObjectTranslate setY(final int y) {
      this.y = var1;
      return this;
   }

   @Generated
   public IrisObjectTranslate setYRandom(final int yRandom) {
      this.yRandom = var1;
      return this;
   }

   @Generated
   public IrisObjectTranslate setZ(final int z) {
      this.z = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectTranslate)) {
         return false;
      } else {
         IrisObjectTranslate var2 = (IrisObjectTranslate)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else if (this.getY() != var2.getY()) {
            return false;
         } else if (this.getYRandom() != var2.getYRandom()) {
            return false;
         } else {
            return this.getZ() == var2.getZ();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectTranslate;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getX();
      var3 = var3 * 59 + this.getY();
      var3 = var3 * 59 + this.getYRandom();
      var3 = var3 * 59 + this.getZ();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getX();
      return "IrisObjectTranslate(x=" + var10000 + ", y=" + this.getY() + ", yRandom=" + this.getYRandom() + ", z=" + this.getZ() + ")";
   }
}
