package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

class Util {
   public static int readVarInt(ByteBuffer buffer) {
      int length = 0;
      int result = 0;

      byte b;
      do {
         b = buffer.get();
         result |= (b & 127) << length * 7;
         ++length;
         if (length > 5) {
            throw new IllegalArgumentException();
         }
      } while((b & 128) != 0);

      return result;
   }

   public static String readString(ByteBuffer buffer, int limit) {
      int length = readVarInt(buffer);
      if (length > buffer.remaining()) {
         throw new BufferUnderflowException();
      } else if (length > limit) {
         throw new IllegalArgumentException("String too long: " + length + " > " + limit);
      } else {
         byte[] data = new byte[length];
         buffer.get(data);
         return new String(data);
      }
   }
}
