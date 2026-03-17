/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.serializer.gson.GsonHacks;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.util.Buildable;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

final class ComponentSerializerImpl
extends TypeAdapter<Component> {
    static final Type COMPONENT_LIST_TYPE = new TypeToken<List<Component>>(){}.getType();
    static final Type TRANSLATABLE_ARGUMENT_LIST_TYPE = new TypeToken<List<TranslationArgument>>(){}.getType();
    private final boolean emitCompactTextComponent;
    private final Gson gson;

    static TypeAdapter<Component> create(OptionState optionState, Gson gson) {
        return new ComponentSerializerImpl(optionState.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT), gson).nullSafe();
    }

    private ComponentSerializerImpl(boolean bl, Gson gson) {
        this.emitCompactTextComponent = bl;
        this.gson = gson;
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public BuildableComponent<?, ?> read(JsonReader jsonReader) {
        Object object;
        JsonToken jsonToken = jsonReader.peek();
        if (jsonToken == JsonToken.STRING || jsonToken == JsonToken.NUMBER || jsonToken == JsonToken.BOOLEAN) {
            return Component.text(GsonHacks.readString(jsonReader));
        }
        if (jsonToken == JsonToken.BEGIN_ARRAY) {
            void var3_4;
            Object var3_3 = null;
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                Object object2 = this.read(jsonReader);
                if (var3_4 == null) {
                    Buildable.Builder builder = object2.toBuilder();
                    continue;
                }
                var3_4.append((Component)object2);
            }
            if (var3_4 == null) {
                throw ComponentSerializerImpl.notSureHowToDeserialize(jsonReader.getPath());
            }
            jsonReader.endArray();
            return var3_4.build();
        }
        if (jsonToken != JsonToken.BEGIN_OBJECT) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(jsonReader.getPath());
        }
        JsonObject jsonObject = new JsonObject();
        List list = Collections.emptyList();
        String string = null;
        String string2 = null;
        String string3 = null;
        List list2 = null;
        String string4 = null;
        String string5 = null;
        String string6 = null;
        String string7 = null;
        String string8 = null;
        String string9 = null;
        boolean bl = false;
        BlockNBTComponent.Pos pos = null;
        String string10 = null;
        Key key = null;
        Object object3 = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            object = jsonReader.nextName();
            if (((String)object).equals("text")) {
                string = GsonHacks.readString(jsonReader);
                continue;
            }
            if (((String)object).equals("translate")) {
                string2 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("fallback")) {
                string3 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("with")) {
                list2 = (List)this.gson.fromJson(jsonReader, TRANSLATABLE_ARGUMENT_LIST_TYPE);
                continue;
            }
            if (((String)object).equals("score")) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String string11 = jsonReader.nextName();
                    if (string11.equals("name")) {
                        string4 = jsonReader.nextString();
                        continue;
                    }
                    if (string11.equals("objective")) {
                        string5 = jsonReader.nextString();
                        continue;
                    }
                    if (string11.equals("value")) {
                        string6 = jsonReader.nextString();
                        continue;
                    }
                    jsonReader.skipValue();
                }
                if (string4 == null || string5 == null) {
                    throw new JsonParseException("A score component requires a name and objective");
                }
                jsonReader.endObject();
                continue;
            }
            if (((String)object).equals("selector")) {
                string7 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("keybind")) {
                string8 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("nbt")) {
                string9 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("interpret")) {
                bl = jsonReader.nextBoolean();
                continue;
            }
            if (((String)object).equals("block")) {
                pos = (BlockNBTComponent.Pos)this.gson.fromJson(jsonReader, SerializerFactory.BLOCK_NBT_POS_TYPE);
                continue;
            }
            if (((String)object).equals("entity")) {
                string10 = jsonReader.nextString();
                continue;
            }
            if (((String)object).equals("storage")) {
                key = (Key)this.gson.fromJson(jsonReader, SerializerFactory.KEY_TYPE);
                continue;
            }
            if (((String)object).equals("extra")) {
                list = (List)this.gson.fromJson(jsonReader, COMPONENT_LIST_TYPE);
                continue;
            }
            if (((String)object).equals("separator")) {
                object3 = this.read(jsonReader);
                continue;
            }
            jsonObject.add((String)object, (JsonElement)this.gson.fromJson(jsonReader, (Type)((Object)JsonElement.class)));
        }
        if (string != null) {
            object = Component.text().content(string);
        } else if (string2 != null) {
            object = list2 != null ? Component.translatable().key(string2).fallback(string3).arguments(list2) : Component.translatable().key(string2).fallback(string3);
        } else if (string4 != null && string5 != null) {
            object = string6 == null ? Component.score().name(string4).objective(string5) : Component.score().name(string4).objective(string5).value(string6);
        } else if (string7 != null) {
            object = Component.selector().pattern(string7).separator((ComponentLike)object3);
        } else if (string8 != null) {
            object = Component.keybind().keybind(string8);
        } else {
            if (string9 == null) throw ComponentSerializerImpl.notSureHowToDeserialize(jsonReader.getPath());
            if (pos != null) {
                object = ComponentSerializerImpl.nbt(Component.blockNBT(), string9, bl, object3).pos(pos);
            } else if (string10 != null) {
                object = ComponentSerializerImpl.nbt(Component.entityNBT(), string9, bl, object3).selector(string10);
            } else {
                if (key == null) throw ComponentSerializerImpl.notSureHowToDeserialize(jsonReader.getPath());
                object = ComponentSerializerImpl.nbt(Component.storageNBT(), string9, bl, object3).storage(key);
            }
        }
        object.style(this.gson.fromJson((JsonElement)jsonObject, SerializerFactory.STYLE_TYPE)).append(list);
        jsonReader.endObject();
        return object.build();
    }

    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(B b, String string, boolean bl, @Nullable Component component) {
        return b.nbtPath(string).interpret(bl).separator(component);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void write(JsonWriter jsonWriter, Component component) {
        Object object;
        if (component instanceof TextComponent && component.children().isEmpty() && !component.hasStyling() && this.emitCompactTextComponent) {
            jsonWriter.value(((TextComponent)component).content());
            return;
        }
        jsonWriter.beginObject();
        if (component.hasStyling() && ((JsonElement)(object = this.gson.toJsonTree(component.style(), SerializerFactory.STYLE_TYPE))).isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : ((JsonElement)object).getAsJsonObject().entrySet()) {
                jsonWriter.name(entry.getKey());
                this.gson.toJson(entry.getValue(), jsonWriter);
            }
        }
        if (!component.children().isEmpty()) {
            jsonWriter.name("extra");
            this.gson.toJson(component.children(), COMPONENT_LIST_TYPE, jsonWriter);
        }
        if (component instanceof TextComponent) {
            jsonWriter.name("text");
            jsonWriter.value(((TextComponent)component).content());
        } else if (component instanceof TranslatableComponent) {
            object = (TranslatableComponent)component;
            jsonWriter.name("translate");
            jsonWriter.value(object.key());
            String string = object.fallback();
            if (string != null) {
                jsonWriter.name("fallback");
                jsonWriter.value(string);
            }
            if (!object.arguments().isEmpty()) {
                jsonWriter.name("with");
                this.gson.toJson(object.arguments(), TRANSLATABLE_ARGUMENT_LIST_TYPE, jsonWriter);
            }
        } else if (component instanceof ScoreComponent) {
            object = (ScoreComponent)component;
            jsonWriter.name("score");
            jsonWriter.beginObject();
            jsonWriter.name("name");
            jsonWriter.value(object.name());
            jsonWriter.name("objective");
            jsonWriter.value(object.objective());
            if (object.value() != null) {
                jsonWriter.name("value");
                jsonWriter.value(object.value());
            }
            jsonWriter.endObject();
        } else if (component instanceof SelectorComponent) {
            object = (SelectorComponent)component;
            jsonWriter.name("selector");
            jsonWriter.value(object.pattern());
            this.serializeSeparator(jsonWriter, object.separator());
        } else if (component instanceof KeybindComponent) {
            jsonWriter.name("keybind");
            jsonWriter.value(((KeybindComponent)component).keybind());
        } else {
            if (!(component instanceof NBTComponent)) throw ComponentSerializerImpl.notSureHowToSerialize(component);
            object = (NBTComponent)component;
            jsonWriter.name("nbt");
            jsonWriter.value(object.nbtPath());
            jsonWriter.name("interpret");
            jsonWriter.value(object.interpret());
            this.serializeSeparator(jsonWriter, object.separator());
            if (component instanceof BlockNBTComponent) {
                jsonWriter.name("block");
                this.gson.toJson((Object)((BlockNBTComponent)component).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, jsonWriter);
            } else if (component instanceof EntityNBTComponent) {
                jsonWriter.name("entity");
                jsonWriter.value(((EntityNBTComponent)component).selector());
            } else {
                if (!(component instanceof StorageNBTComponent)) throw ComponentSerializerImpl.notSureHowToSerialize(component);
                jsonWriter.name("storage");
                this.gson.toJson((Object)((StorageNBTComponent)component).storage(), SerializerFactory.KEY_TYPE, jsonWriter);
            }
        }
        jsonWriter.endObject();
    }

    private void serializeSeparator(JsonWriter jsonWriter, @Nullable Component component) {
        if (component != null) {
            jsonWriter.name("separator");
            this.write(jsonWriter, component);
        }
    }

    static JsonParseException notSureHowToDeserialize(Object object) {
        return new JsonParseException("Don't know how to turn " + object + " into a Component");
    }

    private static IllegalArgumentException notSureHowToSerialize(Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }
}

