package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonParseException;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonToken;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.key.Keyed;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValue;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import fr.xephi.authme.libs.net.kyori.option.OptionState;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem> {
   private static final String LEGACY_SHOW_ITEM_TAG = "tag";
   private final Gson gson;
   private final boolean emitDefaultQuantity;
   private final JSONOptions.ShowItemHoverDataMode itemDataMode;

   static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson, final OptionState opt) {
      return (new ShowItemSerializer(gson, (Boolean)opt.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY), (JSONOptions.ShowItemHoverDataMode)opt.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE))).nullSafe();
   }

   private ShowItemSerializer(final Gson gson, final boolean emitDefaultQuantity, final JSONOptions.ShowItemHoverDataMode itemDataMode) {
      this.gson = gson;
      this.emitDefaultQuantity = emitDefaultQuantity;
      this.itemDataMode = itemDataMode;
   }

   public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
      in.beginObject();
      Key key = null;
      int count = 1;
      BinaryTagHolder nbt = null;
      HashMap dataComponents = null;

      while(true) {
         while(in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("id")) {
               key = (Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE);
            } else if (fieldName.equals("count")) {
               count = in.nextInt();
            } else if (fieldName.equals("tag")) {
               JsonToken token = in.peek();
               if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
                  if (token == JsonToken.BOOLEAN) {
                     nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                  } else {
                     if (token != JsonToken.NULL) {
                        throw new JsonParseException("Expected tag to be a string");
                     }

                     in.nextNull();
                  }
               } else {
                  nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
               }
            } else if (!fieldName.equals("components")) {
               in.skipValue();
            } else {
               in.beginObject();

               Key id;
               JsonElement tree;
               for(; in.peek() != JsonToken.END_OBJECT; dataComponents.put(id, GsonDataComponentValue.gsonDataComponentValue(tree))) {
                  id = Key.key(in.nextName());
                  tree = (JsonElement)this.gson.fromJson((JsonReader)in, (Type)JsonElement.class);
                  if (dataComponents == null) {
                     dataComponents = new HashMap();
                  }
               }

               in.endObject();
            }
         }

         if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
         }

         in.endObject();
         if (dataComponents != null) {
            return HoverEvent.ShowItem.showItem((Keyed)key, count, (Map)dataComponents);
         }

         return HoverEvent.ShowItem.showItem(key, count, nbt);
      }
   }

   public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
      out.beginObject();
      out.name("id");
      this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, (JsonWriter)out);
      int count = value.count();
      if (count != 1 || this.emitDefaultQuantity) {
         out.name("count");
         out.value((long)count);
      }

      Map<Key, DataComponentValue> dataComponents = value.dataComponents();
      if (!dataComponents.isEmpty() && this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) {
         out.name("components");
         out.beginObject();
         Iterator var5 = value.dataComponentsAs(GsonDataComponentValue.class).entrySet().iterator();

         while(var5.hasNext()) {
            Entry<Key, GsonDataComponentValue> entry = (Entry)var5.next();
            out.name(((Key)entry.getKey()).asString());
            this.gson.toJson(((GsonDataComponentValue)entry.getValue()).element(), out);
         }

         out.endObject();
      } else if (this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
         maybeWriteLegacy(out, value);
      }

      out.endObject();
   }

   private static void maybeWriteLegacy(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
      BinaryTagHolder nbt = value.nbt();
      if (nbt != null) {
         out.name("tag");
         out.value(nbt.string());
      }

   }
}
