package com.volmit.iris.util.nbt.io;

import com.volmit.iris.util.nbt.tag.Tag;

public class SNBTUtil {
   public static String toSNBT(Tag<?> tag) {
      return (new SNBTSerializer()).toString(var0);
   }

   public static Tag<?> fromSNBT(String string) {
      return (Tag)(new SNBTDeserializer()).fromString(var0);
   }
}
