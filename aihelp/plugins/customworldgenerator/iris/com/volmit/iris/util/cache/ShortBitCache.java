package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ShortBitCache extends DataBitCache<Short> {
   public ShortBitCache(int width, int height) {
      super(var1, var2);
   }

   public Short readNodeData(DataInputStream din) {
      return var1.readShort();
   }

   public void writeNodeData(DataOutputStream dos, Short integer) {
      var1.writeShort(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, (short)var2);
   }
}
