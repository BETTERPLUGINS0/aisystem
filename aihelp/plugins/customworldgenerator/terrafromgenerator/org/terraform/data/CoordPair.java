package org.terraform.data;

public record CoordPair(int x, int z) {
   public CoordPair(int x, int z) {
      this.x = x;
      this.z = z;
   }

   public CoordPair chunkify() {
      return new CoordPair(this.x >> 4, this.z >> 4);
   }

   public int x() {
      return this.x;
   }

   public int z() {
      return this.z;
   }
}
