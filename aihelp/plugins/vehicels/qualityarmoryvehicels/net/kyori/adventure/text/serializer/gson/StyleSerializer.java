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
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import net.kyori.adventure.text.serializer.gson.GsonHacks;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;
import net.kyori.adventure.text.serializer.gson.TextColorWrapper;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import net.kyori.adventure.util.Codec;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

final class StyleSerializer
extends TypeAdapter<Style> {
    private static final TextDecoration[] DECORATIONS = new TextDecoration[]{TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED};
    private final LegacyHoverEventSerializer legacyHover;
    private final boolean emitLegacyHover;
    private final boolean emitModernHover;
    private final boolean strictEventValues;
    private final Gson gson;

    static TypeAdapter<Style> create(@Nullable LegacyHoverEventSerializer legacyHoverEventSerializer, OptionState optionState, Gson gson) {
        JSONOptions.HoverEventValueMode hoverEventValueMode = optionState.value(JSONOptions.EMIT_HOVER_EVENT_TYPE);
        return new StyleSerializer(legacyHoverEventSerializer, hoverEventValueMode == JSONOptions.HoverEventValueMode.LEGACY_ONLY || hoverEventValueMode == JSONOptions.HoverEventValueMode.BOTH, hoverEventValueMode == JSONOptions.HoverEventValueMode.MODERN_ONLY || hoverEventValueMode == JSONOptions.HoverEventValueMode.BOTH, optionState.value(JSONOptions.VALIDATE_STRICT_EVENTS), gson).nullSafe();
    }

    private StyleSerializer(@Nullable LegacyHoverEventSerializer legacyHoverEventSerializer, boolean bl, boolean bl2, boolean bl3, Gson gson) {
        this.legacyHover = legacyHoverEventSerializer;
        this.emitLegacyHover = bl;
        this.emitModernHover = bl2;
        this.strictEventValues = bl3;
        this.gson = gson;
    }

    @Override
    public Style read(JsonReader jsonReader) {
        jsonReader.beginObject();
        Style.Builder builder = Style.style();
        while (jsonReader.hasNext()) {
            Object object;
            Object object2;
            Object object3;
            String string = jsonReader.nextName();
            if (string.equals("font")) {
                builder.font((Key)this.gson.fromJson(jsonReader, SerializerFactory.KEY_TYPE));
                continue;
            }
            if (string.equals("color")) {
                object3 = (TextColorWrapper)this.gson.fromJson(jsonReader, SerializerFactory.COLOR_WRAPPER_TYPE);
                if (((TextColorWrapper)object3).color != null) {
                    builder.color(((TextColorWrapper)object3).color);
                    continue;
                }
                if (((TextColorWrapper)object3).decoration == null) continue;
                builder.decoration(((TextColorWrapper)object3).decoration, TextDecoration.State.TRUE);
                continue;
            }
            if (TextDecoration.NAMES.keys().contains(string)) {
                builder.decoration(TextDecoration.NAMES.value(string), GsonHacks.readBoolean(jsonReader));
                continue;
            }
            if (string.equals("insertion")) {
                builder.insertion(jsonReader.nextString());
                continue;
            }
            if (string.equals("clickEvent")) {
                jsonReader.beginObject();
                object3 = null;
                object2 = null;
                while (jsonReader.hasNext()) {
                    object = jsonReader.nextName();
                    if (((String)object).equals("action")) {
                        object3 = (ClickEvent.Action)((Object)this.gson.fromJson(jsonReader, SerializerFactory.CLICK_ACTION_TYPE));
                        continue;
                    }
                    if (((String)object).equals("value")) {
                        if (jsonReader.peek() == JsonToken.NULL && this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize("value");
                        }
                        object2 = jsonReader.peek() == JsonToken.NULL ? null : jsonReader.nextString();
                        continue;
                    }
                    jsonReader.skipValue();
                }
                if (object3 != null && ((ClickEvent.Action)((Object)object3)).readable() && object2 != null) {
                    builder.clickEvent(ClickEvent.clickEvent((ClickEvent.Action)((Object)object3), (String)object2));
                }
                jsonReader.endObject();
                continue;
            }
            if (string.equals("hoverEvent")) {
                Object object4;
                JsonElement jsonElement;
                object3 = (JsonObject)this.gson.fromJson(jsonReader, (Type)((Object)JsonObject.class));
                if (object3 == null || (object2 = ((JsonObject)object3).getAsJsonPrimitive("action")) == null || !((HoverEvent.Action)(object = this.gson.fromJson((JsonElement)object2, SerializerFactory.HOVER_ACTION_TYPE))).readable()) continue;
                Class clazz = ((HoverEvent.Action)object).type();
                if (((JsonObject)object3).has("contents")) {
                    jsonElement = ((JsonObject)object3).get("contents");
                    if (GsonHacks.isNullOrEmpty(jsonElement)) {
                        if (this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize(jsonElement);
                        }
                        object4 = null;
                    } else {
                        object4 = SerializerFactory.COMPONENT_TYPE.isAssignableFrom(clazz) ? this.gson.fromJson(jsonElement, SerializerFactory.COMPONENT_TYPE) : (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(clazz) ? this.gson.fromJson(jsonElement, SerializerFactory.SHOW_ITEM_TYPE) : (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(clazz) ? this.gson.fromJson(jsonElement, SerializerFactory.SHOW_ENTITY_TYPE) : null));
                    }
                } else if (((JsonObject)object3).has("value")) {
                    jsonElement = ((JsonObject)object3).get("value");
                    if (GsonHacks.isNullOrEmpty(jsonElement)) {
                        if (this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize(jsonElement);
                        }
                        object4 = null;
                    } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(clazz)) {
                        Component component = this.gson.fromJson(jsonElement, SerializerFactory.COMPONENT_TYPE);
                        object4 = this.legacyHoverEventContents((HoverEvent.Action<?>)object, component);
                    } else {
                        object4 = SerializerFactory.STRING_TYPE.isAssignableFrom(clazz) ? this.gson.fromJson(jsonElement, SerializerFactory.STRING_TYPE) : null;
                    }
                } else {
                    if (this.strictEventValues) {
                        throw ComponentSerializerImpl.notSureHowToDeserialize(object3);
                    }
                    object4 = null;
                }
                if (object4 == null) continue;
                builder.hoverEvent(HoverEvent.hoverEvent(object, object4));
                continue;
            }
            jsonReader.skipValue();
        }
        jsonReader.endObject();
        return builder.build();
    }

    private Object legacyHoverEventContents(HoverEvent.Action<?> action, Component component) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return component;
        }
        if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(component, this.decoder());
                }
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    return this.legacyHover.deserializeShowItem(component);
                }
            } catch (IOException iOException) {
                throw new JsonParseException(iOException);
            }
        }
        throw new UnsupportedOperationException();
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return string -> this.gson.fromJson((String)string, SerializerFactory.COMPONENT_TYPE);
    }

    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
    }

    @Override
    public void write(JsonWriter jsonWriter, Style style) {
        ClickEvent clickEvent;
        String string;
        HoverEvent.Action action;
        Object object;
        jsonWriter.beginObject();
        for (TextDecoration styleBuilderApplicable2 : DECORATIONS) {
            object = style.decoration(styleBuilderApplicable2);
            if (object == TextDecoration.State.NOT_SET) continue;
            action = TextDecoration.NAMES.key(styleBuilderApplicable2);
            assert (action != null);
            jsonWriter.name((String)((Object)action));
            jsonWriter.value(object == TextDecoration.State.TRUE);
        }
        @Nullable TextColor textColor = style.color();
        if (textColor != null) {
            jsonWriter.name("color");
            this.gson.toJson((Object)textColor, SerializerFactory.COLOR_TYPE, jsonWriter);
        }
        if ((string = style.insertion()) != null) {
            jsonWriter.name("insertion");
            jsonWriter.value(string);
        }
        if ((clickEvent = style.clickEvent()) != null) {
            jsonWriter.name("clickEvent");
            jsonWriter.beginObject();
            jsonWriter.name("action");
            this.gson.toJson((Object)clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, jsonWriter);
            jsonWriter.name("value");
            jsonWriter.value(clickEvent.value());
            jsonWriter.endObject();
        }
        if ((object = style.hoverEvent()) != null && (this.emitModernHover && ((HoverEvent)object).action() != HoverEvent.Action.SHOW_ACHIEVEMENT || this.emitLegacyHover)) {
            jsonWriter.name("hoverEvent");
            jsonWriter.beginObject();
            jsonWriter.name("action");
            action = ((HoverEvent)object).action();
            this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, jsonWriter);
            if (this.emitModernHover && action != HoverEvent.Action.SHOW_ACHIEVEMENT) {
                jsonWriter.name("contents");
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    this.gson.toJson(((HoverEvent)object).value(), SerializerFactory.SHOW_ITEM_TYPE, jsonWriter);
                } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                    this.gson.toJson(((HoverEvent)object).value(), SerializerFactory.SHOW_ENTITY_TYPE, jsonWriter);
                } else if (action == HoverEvent.Action.SHOW_TEXT) {
                    this.gson.toJson(((HoverEvent)object).value(), SerializerFactory.COMPONENT_TYPE, jsonWriter);
                } else {
                    throw new JsonParseException("Don't know how to serialize " + ((HoverEvent)object).value());
                }
            }
            if (this.emitLegacyHover) {
                jsonWriter.name("value");
                this.serializeLegacyHoverEvent((HoverEvent<?>)object, jsonWriter);
            }
            jsonWriter.endObject();
        }
        if ((action = style.font()) != null) {
            jsonWriter.name("font");
            this.gson.toJson((Object)action, SerializerFactory.KEY_TYPE, jsonWriter);
        }
        jsonWriter.endObject();
    }

    private void serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonWriter jsonWriter) {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, jsonWriter);
        } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
            this.gson.toJson(hoverEvent.value(), (Type)((Object)String.class), jsonWriter);
        } else if (this.legacyHover != null) {
            Component component = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    component = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder());
                } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    component = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
                }
            } catch (IOException iOException) {
                throw new JsonSyntaxException(iOException);
            }
            if (component != null) {
                this.gson.toJson((Object)component, SerializerFactory.COMPONENT_TYPE, jsonWriter);
            } else {
                jsonWriter.nullValue();
            }
        } else {
            jsonWriter.nullValue();
        }
    }

    static {
        EnumSet<TextDecoration> enumSet = EnumSet.allOf(TextDecoration.class);
        for (TextDecoration textDecoration : DECORATIONS) {
            enumSet.remove(textDecoration);
        }
        if (!enumSet.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + enumSet);
        }
    }
}

