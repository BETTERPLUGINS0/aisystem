package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.nbt.tag.Tag;

public class SNBTUtil {
   public static String toSNBT(Tag<?> var0) {
      return (new SNBTSerializer()).toString(var0);
   }

   public static Tag<?> fromSNBT(String var0) {
      return (Tag)(new SNBTDeserializer()).fromString(var0);
   }

   public static Tag<?> fromSNBT(String var0, boolean var1) {
      return (new SNBTParser(var0)).parse(512, var1);
   }
}
