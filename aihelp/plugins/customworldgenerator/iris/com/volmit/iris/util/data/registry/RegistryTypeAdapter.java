package com.volmit.iris.util.data.registry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

public class RegistryTypeAdapter<T> extends TypeAdapter<T> {
   private final KeyedRegistry<T> registry;

   private RegistryTypeAdapter(KeyedRegistry<T> type) {
      this.registry = var1;
   }

   @Nullable
   public static <T> RegistryTypeAdapter<T> of(@NonNull Class<T> type) {
      if (var0 == null) {
         throw new NullPointerException("type is marked non-null but is null");
      } else {
         KeyedRegistry var1 = RegistryUtil.lookup(var0);
         return var1.isEmpty() ? null : new RegistryTypeAdapter(var1);
      }
   }

   public void write(JsonWriter out, T value) {
      if (var2 == null) {
         var1.nullValue();
      } else {
         NamespacedKey var3 = this.registry.keyOf(var2);
         if (var3 == null) {
            var1.nullValue();
         } else {
            var1.value(var3.toString());
         }

      }
   }

   public T read(JsonReader in) {
      NamespacedKey var2 = NamespacedKey.fromString(var1.nextString());
      return var2 == null ? null : this.registry.get(var2);
   }
}
