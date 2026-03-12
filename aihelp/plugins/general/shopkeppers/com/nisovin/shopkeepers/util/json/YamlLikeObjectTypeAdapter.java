package com.nisovin.shopkeepers.util.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.yaml.YamlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public class YamlLikeObjectTypeAdapter extends TypeAdapter<Object> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      @Nullable
      public <T> TypeAdapter<T> create(@Nullable Gson gson, @Nullable TypeToken<T> type) {
         assert gson != null && type != null;

         return type.getRawType() == Object.class ? (TypeAdapter)Unsafe.cast(YamlLikeObjectTypeAdapter.create(gson)) : null;
      }
   };
   private final Gson gson;

   public static TypeAdapter<Object> create(Gson gson) {
      return new YamlLikeObjectTypeAdapter(gson);
   }

   protected YamlLikeObjectTypeAdapter(Gson gson) {
      Validate.notNull(gson, (String)"gson is null");
      this.gson = gson;
   }

   public Object read(JsonReader in) throws IOException {
      assert in != null;

      JsonToken token = in.peek();
      switch(token) {
      case BEGIN_ARRAY:
         List<Object> list = new ArrayList();
         in.beginArray();

         while(in.hasNext()) {
            list.add(this.read(in));
         }

         in.endArray();
         return list;
      case BEGIN_OBJECT:
         Map<String, Object> map = new LinkedHashMap();
         in.beginObject();

         while(in.hasNext()) {
            map.put((String)Unsafe.assertNonNull(in.nextName()), this.read(in));
         }

         in.endObject();
         return map;
      case STRING:
         String string = in.nextString();

         assert string != null;

         if (in.isLenient()) {
            try {
               double number = Double.parseDouble(string);
               if (!Double.isFinite(number)) {
                  return number;
               }
            } catch (NumberFormatException var8) {
            }
         }

         return string;
      case NUMBER:
         String numberString = in.nextString();

         assert numberString != null;

         Object number = YamlUtils.fromYaml(numberString);
         if (!(number instanceof Number)) {
            throw new IllegalStateException("Could not parse number: " + numberString);
         }

         return number;
      case BOOLEAN:
         return in.nextBoolean();
      case NULL:
         in.nextNull();
         return null;
      default:
         throw new IllegalStateException();
      }
   }

   public void write(@Nullable JsonWriter out, @Nullable Object value) throws IOException {
      assert out != null;

      if (value == null) {
         out.nullValue();
      } else {
         Class<?> clazz = value.getClass();
         TypeAdapter<Object> typeAdapter = this.gson.getAdapter(clazz);
         if (typeAdapter instanceof YamlLikeObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
         } else {
            typeAdapter.write(out, value);
         }
      }
   }
}
