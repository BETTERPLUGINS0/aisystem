package org.terraform.data;

import org.jetbrains.annotations.NotNull;

public record TWSimpleLocation(TerraformWorld tw, int x, int y, int z) {
   public TWSimpleLocation(TerraformWorld tw, @NotNull SimpleLocation loc) {
      this(tw, loc.getX(), loc.getY(), loc.getZ());
   }

   public TWSimpleLocation(TerraformWorld tw, int x, int y, int z) {
      this.tw = tw;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public TerraformWorld tw() {
      return this.tw;
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public int z() {
      return this.z;
   }
}
