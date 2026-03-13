package com.volmit.iris.util.reflect;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.volmit.iris.util.data.registry.RegistryTypeAdapter;
import com.volmit.iris.util.data.registry.RegistryUtil;
import org.bukkit.Keyed;

public class KeyedType {
   private static final boolean KEYED_ENABLED = Boolean.getBoolean("iris.keyed-types");
   private static final boolean KEYED_LENIENT = Boolean.getBoolean("iris.keyed-lenient");

   public static String[] values(Class<?> type) {
      if (!isKeyed(var0)) {
         return new String[0];
      } else {
         return !KEYED_ENABLED ? OldEnum.values(var0) : (String[])RegistryUtil.lookup(var0).map().keySet().stream().map(Object::toString).toArray((var0x) -> {
            return new String[var0x];
         });
      }
   }

   public static boolean isKeyed(Class<?> type) {
      if (KEYED_ENABLED) {
         if (KEYED_LENIENT) {
            return !RegistryUtil.lookup(var0).isEmpty();
         } else {
            return Keyed.class.isAssignableFrom(var0);
         }
      } else {
         return OldEnum.isOldEnum(var0);
      }
   }

   public static <T> TypeAdapter<T> createTypeAdapter(Gson gson, TypeToken<T> type) {
      if (!isKeyed(var1.getRawType())) {
         return null;
      } else {
         return (TypeAdapter)(KEYED_ENABLED ? RegistryTypeAdapter.of(var1.getRawType()) : OldEnum.create(var1.getRawType()));
      }
   }
}
