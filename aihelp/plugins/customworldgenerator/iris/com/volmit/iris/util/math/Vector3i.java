package com.volmit.iris.util.math;

import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Vector3i extends BlockVector {
   public Vector3i(int x, int y, int z) {
      super(var1, var2, var3);
   }

   public Vector3i(Vector vec) {
      super(var1);
   }

   @NotNull
   public Vector3i clone() {
      return (Vector3i)super.clone();
   }

   public int hashCode() {
      return (int)this.x ^ (int)this.z << 12 ^ (int)this.y << 24;
   }
}
