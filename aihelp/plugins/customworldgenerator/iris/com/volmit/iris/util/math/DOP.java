package com.volmit.iris.util.math;

import org.bukkit.util.Vector;

public abstract class DOP {
   private final String type;

   public DOP(String type) {
      this.type = var1;
   }

   public abstract Vector op(Vector v);

   public String getType() {
      return this.type;
   }
}
