package com.volmit.iris.util.mantle.flag;

import org.jetbrains.annotations.NotNull;

record CustomFlag(String name, int ordinal) implements MantleFlag {
   CustomFlag(String name, int ordinal) {
      this.name = var1;
      this.ordinal = var2;
   }

   @NotNull
   public String toString() {
      return this.name;
   }

   public boolean isCustom() {
      return false;
   }

   public boolean equals(Object object) {
      if (var1 instanceof CustomFlag) {
         CustomFlag var2 = (CustomFlag)var1;
         return this.ordinal == var2.ordinal;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.ordinal;
   }

   public String name() {
      return this.name;
   }

   public int ordinal() {
      return this.ordinal;
   }
}
