package com.volmit.iris.util.reflect;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

/** @deprecated */
@Deprecated(
   since = "3.7.1"
)
public class OldEnum {
   private static final Class<?> oldEnum;
   private static final MethodHandle name;

   public static boolean exists() {
      return oldEnum != null;
   }

   public static boolean isOldEnum(Class<?> c) {
      return oldEnum != null && oldEnum.isAssignableFrom(var0);
   }

   public static <T> T valueOf(Class<? extends T> c, String name) {
      return valueOf(var0, var1, var1.replace(".", "_"));
   }

   public static <T> T valueOf(Class<? extends T> c, String... names) {
      String[] var2 = var1;
      int var3 = var1.length;
      int var4 = 0;

      while(var4 < var3) {
         String var5 = var2[var4];

         try {
            return var0.getDeclaredField(var5).get((Object)null);
         } catch (Throwable var7) {
            ++var4;
         }
      }

      return null;
   }

   public static String name(Object o) {
      try {
         return name.invoke(var0);
      } catch (Throwable var2) {
         return null;
      }
   }

   public static String[] values(Class<?> clazz) {
      return !isOldEnum(var0) ? new String[0] : (String[])Arrays.stream(var0.getDeclaredFields()).filter((var0x) -> {
         return Modifier.isStatic(var0x.getModifiers());
      }).filter((var0x) -> {
         return Modifier.isFinal(var0x.getModifiers());
      }).map((var0x) -> {
         try {
            return name(var0x.get((Object)null));
         } catch (Throwable var2) {
            return null;
         }
      }).filter(Objects::nonNull).toArray((var0x) -> {
         return new String[var0x];
      });
   }

   public static <T> TypeAdapter<T> create(Class<? extends T> type) {
      return !isOldEnum(var0) ? null : new TypeAdapter<T>() {
         public void write(JsonWriter out, T value) {
            var1.value(OldEnum.name(var2));
         }

         public T read(JsonReader in) {
            return OldEnum.valueOf(var0, var1.nextString());
         }
      };
   }

   static {
      Class var0 = null;
      MethodHandle var1 = null;

      try {
         var0 = Class.forName("org.bukkit.util.OldEnum");
         var1 = MethodHandles.lookup().findVirtual(var0, "name", MethodType.methodType(String.class));
      } catch (Throwable var3) {
      }

      if (var0 != null && var1 != null) {
         oldEnum = var0;
         name = var1;
      } else {
         oldEnum = null;
         name = null;
      }

   }
}
