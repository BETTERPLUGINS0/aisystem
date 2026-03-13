package org.terraform.data;

public record TWCoordPair(TerraformWorld tw, int x, int z) {
   public TWCoordPair(TerraformWorld tw, int x, int z) {
      this.tw = tw;
      this.x = x;
      this.z = z;
   }

   public TerraformWorld tw() {
      return this.tw;
   }

   public int x() {
      return this.x;
   }

   public int z() {
      return this.z;
   }
}
