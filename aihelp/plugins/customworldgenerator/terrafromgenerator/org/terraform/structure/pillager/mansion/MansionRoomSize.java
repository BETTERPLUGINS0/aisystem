package org.terraform.structure.pillager.mansion;

public class MansionRoomSize {
   private final int widthX;
   private final int widthZ;

   public MansionRoomSize(int widthX, int widthZ) {
      this.widthX = widthX;
      this.widthZ = widthZ;
   }

   public int getWidthX() {
      return this.widthX;
   }

   public int getWidthZ() {
      return this.widthZ;
   }

   public int hashCode() {
      return this.widthX + 74077 * this.widthZ;
   }

   public boolean equals(Object other) {
      if (!(other instanceof MansionRoomSize)) {
         return false;
      } else {
         return ((MansionRoomSize)other).widthX == this.widthX && ((MansionRoomSize)other).widthZ == this.widthZ;
      }
   }
}
