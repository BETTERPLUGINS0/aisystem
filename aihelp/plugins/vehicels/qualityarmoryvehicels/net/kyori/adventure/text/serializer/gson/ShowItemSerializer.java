/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

final class ShowItemSerializer
extends TypeAdapter<HoverEvent.ShowItem> {
    private static final String LEGACY_SHOW_ITEM_TAG = "tag";
    private final Gson gson;
    private final boolean emitDefaultQuantity;
    private final JSONOptions.ShowItemHoverDataMode itemDataMode;

    static TypeAdapter<HoverEvent.ShowItem> create(Gson gson, OptionState optionState) {
        return new ShowItemSerializer(gson, optionState.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY), optionState.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE)).nullSafe();
    }

    private ShowItemSerializer(Gson gson, boolean bl, JSONOptions.ShowItemHoverDataMode showItemHoverDataMode) {
        this.gson = gson;
        this.emitDefaultQuantity = bl;
        this.itemDataMode = showItemHoverDataMode;
    }

    @Override
    public HoverEvent.ShowItem read(JsonReader jsonReader) {
        jsonReader.beginObject();
        Key key = null;
        int n = 1;
        BinaryTagHolder binaryTagHolder = null;
        HashMap<JsonToken, GsonDataComponentValue> hashMap = null;
        while (jsonReader.hasNext()) {
            Object object;
            String string = jsonReader.nextName();
            if (string.equals("id")) {
                key = (Key)this.gson.fromJson(jsonReader, SerializerFactory.KEY_TYPE);
                continue;
            }
            if (string.equals("count")) {
                n = jsonReader.nextInt();
                continue;
            }
            if (string.equals(LEGACY_SHOW_ITEM_TAG)) {
                object = jsonReader.peek();
                if (object == JsonToken.STRING || object == JsonToken.NUMBER) {
                    binaryTagHolder = BinaryTagHolder.binaryTagHolder(jsonReader.nextString());
                    continue;
                }
                if (object == JsonToken.BOOLEAN) {
                    binaryTagHolder = BinaryTagHolder.binaryTagHolder(String.valueOf(jsonReader.nextBoolean()));
                    continue;
                }
                if (object == JsonToken.NULL) {
                    jsonReader.nextNull();
                    continue;
                }
                throw new JsonParseException("Expected tag to be a string");
            }
            if (string.equals("components")) {
                jsonReader.beginObject();
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    object = Key.key(jsonReader.nextName());
                    JsonElement jsonElement = (JsonElement)this.gson.fromJson(jsonReader, (Type)((Object)JsonElement.class));
                    if (hashMap == null) {
                        hashMap = new HashMap<JsonToken, GsonDataComponentValue>();
                    }
                    hashMap.put((JsonToken)((Object)object), GsonDataComponentValue.gsonDataComponentValue(jsonElement));
                }
                jsonReader.endObject();
                continue;
            }
            jsonReader.skipValue();
        }
        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        jsonReader.endObject();
        if (hashMap != null) {
            return HoverEvent.ShowItem.showItem(key, n, hashMap);
        }
        return HoverEvent.ShowItem.showItem(key, n, binaryTagHolder);
    }

    @Override
    public void write(JsonWriter jsonWriter, HoverEvent.ShowItem showItem) {
        Map<Key, DataComponentValue> map;
        jsonWriter.beginObject();
        jsonWriter.name("id");
        this.gson.toJson((Object)showItem.item(), SerializerFactory.KEY_TYPE, jsonWriter);
        int n = showItem.count();
        if (n != 1 || this.emitDefaultQuantity) {
            jsonWriter.name("count");
            jsonWriter.value(n);
        }
        if (!(map = showItem.dataComponents()).isEmpty() && this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) {
            jsonWriter.name("components");
            jsonWriter.beginObject();
            for (Map.Entry<Key, GsonDataComponentValue> entry : showItem.dataComponentsAs(GsonDataComponentValue.class).entrySet()) {
                jsonWriter.name(entry.getKey().asString());
                this.gson.toJson(entry.getValue().element(), jsonWriter);
            }
            jsonWriter.endObject();
        } else if (this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
            ShowItemSerializer.maybeWriteLegacy(jsonWriter, showItem);
        }
        jsonWriter.endObject();
    }

    private static void maybeWriteLegacy(JsonWriter jsonWriter, HoverEvent.ShowItem showItem) {
        @Nullable BinaryTagHolder binaryTagHolder = showItem.nbt();
        if (binaryTagHolder != null) {
            jsonWriter.name(LEGACY_SHOW_ITEM_TAG);
            jsonWriter.value(binaryTagHolder.string());
        }
    }
}

