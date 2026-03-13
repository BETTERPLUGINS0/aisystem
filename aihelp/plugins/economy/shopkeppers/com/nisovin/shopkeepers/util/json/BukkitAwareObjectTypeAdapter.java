package com.nisovin.shopkeepers.util.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitAwareObjectTypeAdapter extends YamlLikeObjectTypeAdapter {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      @Nullable
      public <T> TypeAdapter<T> create(@Nullable Gson gson, @Nullable TypeToken<T> type) {
         assert gson != null && type != null;

         Class<?> rawType = type.getRawType();

         assert rawType != null;

         return rawType != Object.class && !ConfigurationSerializable.class.isAssignableFrom(rawType) ? null : BukkitAwareObjectTypeAdapter.create(gson);
      }
   };

   public static TypeAdapter<Object> create(Gson gson) {
      return new BukkitAwareObjectTypeAdapter(gson);
   }

   @Nullable
   public static <T> T fromJson(Gson gson, @Nullable String json) throws IllegalArgumentException {
      Validate.notNull(gson, (String)"gson is null");
      if (json != null && !json.isEmpty()) {
         TypeAdapter<Object> bukkitAwareObjectTypeAdapter = getBukkitAwareObjectTypeAdapter(gson);
         JsonReader jsonReader = gson.newJsonReader(new StringReader(json));

         assert jsonReader != null;

         try {
            T value = bukkitAwareObjectTypeAdapter.read(jsonReader);
            assertEmptyReader(jsonReader);
            return value;
         } catch (Exception var5) {
            throw new IllegalArgumentException("Could not deserialize object from Json!", var5);
         }
      } else {
         return null;
      }
   }

   private static BukkitAwareObjectTypeAdapter getBukkitAwareObjectTypeAdapter(Gson gson) {
      TypeAdapter<?> typeAdapter = gson.getAdapter(ConfigurationSerializable.class);
      if (!(typeAdapter instanceof BukkitAwareObjectTypeAdapter)) {
         throw new IllegalArgumentException("Could not retrieve the BukkitAwareObjectTypeAdapter from the given Gson instance!");
      } else {
         return (BukkitAwareObjectTypeAdapter)typeAdapter;
      }
   }

   private static void assertEmptyReader(JsonReader reader) throws IllegalArgumentException {
      try {
         if (reader.peek() != JsonToken.END_DOCUMENT) {
            throw new IllegalArgumentException("Json document was not fully consumed!");
         }
      } catch (IOException var2) {
         throw new IllegalArgumentException(var2);
      }
   }

   protected BukkitAwareObjectTypeAdapter(Gson gson) {
      super(gson);
   }

   @Nullable
   public Object read(@Nullable JsonReader in) throws IOException {
      assert in != null;

      Object value = super.read(in);
      if (value instanceof Map) {
         Map<String, Object> map = (Map)value;
         if (map.containsKey("==")) {
            return ConfigUtils.deserialize(map);
         }
      }

      return value;
   }

   public void write(@Nullable JsonWriter out, @Nullable Object value) throws IOException {
      assert out != null;

      Object prepared = value;
      if (value instanceof ConfigurationSerializable) {
         prepared = ConfigUtils.serialize((ConfigurationSerializable)value);
      }

      super.write(out, prepared);
   }
}
