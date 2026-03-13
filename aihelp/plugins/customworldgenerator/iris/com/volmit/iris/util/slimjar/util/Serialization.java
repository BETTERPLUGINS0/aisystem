package com.volmit.iris.util.slimjar.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class Serialization {
   private Serialization() {
   }

   public static void writeURL(@NotNull URL var0, @NotNull DataOutput var1) {
      var1.writeUTF(var0.toExternalForm());
   }

   public static <T> void writeList(@NotNull Collection<T> var0, @NotNull DataOutput var1, @NotNull Serialization.Serializer<T> var2) {
      int var3 = var0.size();
      writeSignedVarInt(var3, var1);
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var2.invoke(var5, var1);
      }

   }

   public static void writeSignedVarInt(int var0, @NotNull DataOutput var1) {
      for(var0 = var0 << 1 ^ var0 >> 31; (long)(var0 & -128) != 0L; var0 >>>= 7) {
         var1.writeByte(var0 & 127 | 128);
      }

      var1.writeByte(var0 & 127);
   }

   public static URL readURL(@NotNull DataInput var0) {
      return URI.create(var0.readUTF()).toURL();
   }

   public static <T> List<T> readList(@NotNull DataInput var0, @NotNull Serialization.Deserializer<T> var1) {
      int var2 = readSignedVarInt(var0);
      ArrayList var3 = new ArrayList(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(var1.invoke(var0));
      }

      return var3;
   }

   public static int readSignedVarInt(DataInput var0) {
      int var1 = 0;
      int var2 = 0;

      do {
         byte var3;
         if (((var3 = var0.readByte()) & 128) == 0) {
            int var4 = var1 | var3 << var2;
            int var5 = (var4 << 31 >> 31 ^ var4) >> 1;
            return var5 ^ var4 & Integer.MIN_VALUE;
         }

         var1 |= (var3 & 127) << var2;
         var2 += 7;
      } while(var2 <= 35);

      throw new IllegalArgumentException("Variable length quantity is too long");
   }

   @FunctionalInterface
   public interface Serializer<T> {
      void invoke(@NotNull T var1, @NotNull DataOutput var2) throws IOException;
   }

   @FunctionalInterface
   public interface Deserializer<T> {
      T invoke(@NotNull DataInput var1) throws IOException;
   }
}
