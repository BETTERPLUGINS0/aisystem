package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ByteBitCache extends DataBitCache<Integer> {
   public ByteBitCache(int width, int height) {
      super(var1, var2);
   }

   public Integer readNodeData(DataInputStream din) {
      return Integer.valueOf(var1.readByte());
   }

   public void writeNodeData(DataOutputStream dos, Integer integer) {
      var1.writeByte(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, var2);
   }
}
