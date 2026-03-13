package com.volmit.iris.util.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

public final class DUTF {
   private DUTF() {
   }

   public static void write(String s, DataOutputStream dos) {
      byte[] var2 = var0.getBytes(StandardCharsets.UTF_8);
      var1.writeShort(var2.length);
      var1.write(var2);
   }

   public static String read(DataInputStream din) {
      byte[] var1 = new byte[var0.readShort()];
      var0.read(var1);
      return new String(var1, StandardCharsets.UTF_8);
   }
}
