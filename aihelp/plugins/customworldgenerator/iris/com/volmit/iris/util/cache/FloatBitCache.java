package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FloatBitCache extends DataBitCache<Float> {
   public FloatBitCache(int width, int height) {
      super(var1, var2);
   }

   public Float readNodeData(DataInputStream din) {
      return var1.readFloat();
   }

   public void writeNodeData(DataOutputStream dos, Float integer) {
      var1.writeFloat(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, (float)var2);
   }
}
