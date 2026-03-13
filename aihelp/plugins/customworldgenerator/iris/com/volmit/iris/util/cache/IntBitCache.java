package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class IntBitCache extends DataBitCache<Integer> {
   public IntBitCache(int width, int height) {
      super(var1, var2);
   }

   public Integer readNodeData(DataInputStream din) {
      return var1.readInt();
   }

   public void writeNodeData(DataOutputStream dos, Integer integer) {
      var1.writeInt(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, var2);
   }
}
