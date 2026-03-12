package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

final class StyleSerializer extends TypeAdapter<Style> {
   private static final TextDecoration[] DECORATIONS;
   private static final String FALLBACK_URL_PROTOCOL = "https://";
   private final ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHover;
   private final BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement;
   private final boolean emitValueFieldHover;
   private final boolean emitCamelCaseHover;
   private final boolean emitSnakeCaseHover;
   private final boolean emitCamelCaseClick;
   private final boolean emitSnakeCaseClick;
   private final boolean strictEventValues;
   private final boolean emitShadowColor;
   private final boolean emitStringPage;
   private final boolean emitClickUrlHttps;
   private final Gson gson;

   static TypeAdapter<Style> create(@Nullable ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHover, @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement, OptionState features, Gson gson) {
      JSONOptions.HoverEventValueMode hoverMode = (JSONOptions.HoverEventValueMode)features.value(JSONOptions.EMIT_HOVER_EVENT_TYPE);
      JSONOptions.ClickEventValueMode clickMode = (JSONOptions.ClickEventValueMode)features.value(JSONOptions.EMIT_CLICK_EVENT_TYPE);
      return (new StyleSerializer(legacyHover, compatShowAchievement, hoverMode == JSONOptions.HoverEventValueMode.VALUE_FIELD || hoverMode == JSONOptions.HoverEventValueMode.ALL, hoverMode == JSONOptions.HoverEventValueMode.CAMEL_CASE || hoverMode == JSONOptions.HoverEventValueMode.ALL, hoverMode == JSONOptions.HoverEventValueMode.SNAKE_CASE || hoverMode == JSONOptions.HoverEventValueMode.ALL, clickMode == JSONOptions.ClickEventValueMode.CAMEL_CASE || clickMode == JSONOptions.ClickEventValueMode.BOTH, clickMode == JSONOptions.ClickEventValueMode.SNAKE_CASE || clickMode == JSONOptions.ClickEventValueMode.BOTH, (Boolean)features.value(JSONOptions.VALIDATE_STRICT_EVENTS), features.value(JSONOptions.SHADOW_COLOR_MODE) != JSONOptions.ShadowColorEmitMode.NONE, (Boolean)features.value(JSONOptions.EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING), (Boolean)features.value(JSONOptions.EMIT_CLICK_URL_HTTPS), gson)).nullSafe();
   }

   private StyleSerializer(@Nullable ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHover, @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement, boolean emitValueFieldHover, boolean emitCamelCaseHover, boolean emitSnakeCaseHover, boolean emitCamelCaseClick, boolean emitSnakeCaseClick, boolean strictEventValues, boolean emitShadowColor, boolean emitStringPage, boolean emitClickUrlHttps, Gson gson) {
      this.legacyHover = legacyHover;
      this.compatShowAchievement = compatShowAchievement;
      this.emitValueFieldHover = emitValueFieldHover;
      this.emitCamelCaseHover = emitCamelCaseHover;
      this.emitSnakeCaseHover = emitSnakeCaseHover;
      this.emitCamelCaseClick = emitCamelCaseClick;
      this.emitSnakeCaseClick = emitSnakeCaseClick;
      this.strictEventValues = strictEventValues;
      this.emitShadowColor = emitShadowColor;
      this.emitStringPage = emitStringPage;
      this.emitClickUrlHttps = emitClickUrlHttps;
      this.gson = gson;
   }

   public Style read(JsonReader in) throws IOException {
      in.beginObject();
      Style.Builder style = Style.style();

      while(true) {
         while(in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("font")) {
               style.font((Key)this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
            } else if (fieldName.equals("color")) {
               TextColorWrapper color = (TextColorWrapper)this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
               if (color.color != null) {
                  style.color(color.color);
               } else if (color.decoration != null) {
                  style.decoration(color.decoration, TextDecoration.State.TRUE);
               }
            } else if (BackwardCompatUtil.IS_4_18_0_OR_NEWER && fieldName.equals("shadow_color")) {
               style.shadowColor((ARGBLike)this.gson.fromJson(in, SerializerFactory.SHADOW_COLOR_TYPE));
            } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
               style.decoration((TextDecoration)TextDecoration.NAMES.value(fieldName), GsonHacks.readBoolean(in));
            } else if (fieldName.equals("insertion")) {
               style.insertion(in.nextString());
            } else if (!fieldName.equals("click_event") && !fieldName.equals("clickEvent")) {
               if (!fieldName.equals("hover_event") && !fieldName.equals("hoverEvent")) {
                  in.skipValue();
               } else {
                  JsonObject hoverEventObject = (JsonObject)this.gson.fromJson(in, JsonObject.class);
                  if (hoverEventObject != null) {
                     JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
                     if (serializedAction != null) {
                        String actionString = (String)this.gson.fromJson(serializedAction, SerializerFactory.STRING_TYPE);
                        boolean isShowAchievement = false;
                        HoverEvent.Action action;
                        if (actionString.equals("show_achievement")) {
                           try {
                              action = HoverEvent.Action.SHOW_ACHIEVEMENT;
                           } catch (NoSuchFieldError var13) {
                              action = HoverEvent.Action.SHOW_TEXT;
                           }

                           isShowAchievement = true;
                        } else {
                           action = (HoverEvent.Action)this.gson.fromJson(serializedAction, SerializerFactory.HOVER_ACTION_TYPE);
                        }

                        if (action.readable()) {
                           Class<?> actionType = action.type();
                           Object value;
                           JsonElement rawValue;
                           if (hoverEventObject.has("contents")) {
                              rawValue = hoverEventObject.get("contents");
                              if (GsonHacks.isNullOrEmpty(rawValue)) {
                                 if (this.strictEventValues) {
                                    throw ComponentSerializerImpl.notSureHowToDeserialize(rawValue);
                                 }

                                 value = null;
                              } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                              } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
                              } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
                              } else {
                                 value = null;
                              }
                           } else if (hoverEventObject.has("value")) {
                              rawValue = hoverEventObject.get("value");
                              if (GsonHacks.isNullOrEmpty(rawValue)) {
                                 if (this.strictEventValues) {
                                    throw ComponentSerializerImpl.notSureHowToDeserialize(rawValue);
                                 }

                                 value = null;
                              } else if (Component.class.isAssignableFrom(actionType)) {
                                 if (isShowAchievement && this.compatShowAchievement != null) {
                                    String id = (String)this.gson.fromJson(rawValue, SerializerFactory.STRING_TYPE);
                                    value = this.compatShowAchievement.convert(id);
                                 } else {
                                    Component rawValue = (Component)this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                                    value = this.legacyHoverEventContents(action, rawValue);
                                 }
                              } else if (String.class.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.STRING_TYPE);
                              } else {
                                 value = null;
                              }
                           } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                              value = this.gson.fromJson(hoverEventObject, SerializerFactory.SHOW_ITEM_TYPE);
                           } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                              value = this.gson.fromJson(hoverEventObject, SerializerFactory.SHOW_ENTITY_TYPE);
                           } else {
                              if (this.strictEventValues) {
                                 throw ComponentSerializerImpl.notSureHowToDeserialize(hoverEventObject);
                              }

                              value = null;
                           }

                           if (value != null) {
                              style.hoverEvent(HoverEvent.hoverEvent(action, value));
                           }
                        }
                     }
                  }
               }
            } else {
               in.beginObject();
               ClickEvent.Action action = null;
               String value = null;
               Key key = null;
               Integer page = null;

               while(true) {
                  while(in.hasNext()) {
                     String clickEventField = in.nextName();
                     if (clickEventField.equals("action")) {
                        action = (ClickEvent.Action)this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE);
                     } else if (clickEventField.equals("page")) {
                        if (in.peek() == JsonToken.NUMBER) {
                           page = in.nextInt();
                        } else if (in.peek() == JsonToken.STRING) {
                           page = Integer.parseInt(in.nextString());
                        } else {
                           if (in.peek() == JsonToken.NULL) {
                              throw ComponentSerializerImpl.notSureHowToDeserialize(clickEventField);
                           }

                           in.skipValue();
                        }
                     } else if (!clickEventField.equals("value") && !clickEventField.equals("url") && !clickEventField.equals("path") && !clickEventField.equals("command") && !clickEventField.equals("payload")) {
                        if (clickEventField.equals("id")) {
                           key = Key.key(in.nextString());
                        } else {
                           in.skipValue();
                        }
                     } else if (in.peek() == JsonToken.NULL) {
                        if (this.strictEventValues) {
                           throw ComponentSerializerImpl.notSureHowToDeserialize(clickEventField);
                        }

                        in.nextNull();
                     } else {
                        value = in.nextString();
                     }
                  }

                  if (action != null && action.readable()) {
                     switch(action) {
                     case OPEN_URL:
                        if (value != null) {
                           style.clickEvent(ClickEvent.openUrl(value));
                        }
                        break;
                     case RUN_COMMAND:
                        if (value != null) {
                           style.clickEvent(ClickEvent.runCommand(value));
                        }
                        break;
                     case SUGGEST_COMMAND:
                        if (value != null) {
                           style.clickEvent(ClickEvent.suggestCommand(value));
                        }
                        break;
                     case CHANGE_PAGE:
                        if (page != null) {
                           style.clickEvent(ClickEvent.changePage(page));
                        }
                        break;
                     case COPY_TO_CLIPBOARD:
                        if (value != null) {
                           style.clickEvent(ClickEvent.copyToClipboard(value));
                        }
                        break;
                     case CUSTOM:
                        if (key != null && value != null) {
                           style.clickEvent(ClickEvent.custom(key, value));
                        }
                     case SHOW_DIALOG:
                     case OPEN_FILE:
                     }
                  }

                  in.endObject();
                  break;
               }
            }
         }

         in.endObject();
         return style.build();
      }
   }

   private Object legacyHoverEventContents(HoverEvent.Action<?> action, Component rawValue) {
      if (action == HoverEvent.Action.SHOW_TEXT) {
         return rawValue;
      } else {
         if (this.legacyHover != null) {
            try {
               if (action == HoverEvent.Action.SHOW_ENTITY) {
                  return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
               }

               if (action == HoverEvent.Action.SHOW_ITEM) {
                  return this.legacyHover.deserializeShowItem(rawValue);
               }
            } catch (IOException var4) {
               throw new JsonParseException(var4);
            }
         }

         throw new UnsupportedOperationException();
      }
   }

   private Codec.Decoder<Component, String, JsonParseException> decoder() {
      return (string) -> {
         return (Component)this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
      };
   }

   private Codec.Encoder<Component, String, JsonParseException> encoder() {
      return (component) -> {
         return this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
      };
   }

   public void write(JsonWriter out, Style value) throws IOException {
      out.beginObject();
      int i = 0;

      String payloadValue;
      for(int length = DECORATIONS.length; i < length; ++i) {
         TextDecoration decoration = DECORATIONS[i];
         TextDecoration.State state = value.decoration(decoration);
         if (state != TextDecoration.State.NOT_SET) {
            payloadValue = (String)TextDecoration.NAMES.key(decoration);

            assert payloadValue != null;

            out.name(payloadValue);
            out.value(state == TextDecoration.State.TRUE);
         }
      }

      TextColor color = value.color();
      if (color != null) {
         out.name("color");
         this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
      }

      if (BackwardCompatUtil.IS_4_18_0_OR_NEWER) {
         ShadowColor shadowColor = value.shadowColor();
         if (shadowColor != null && this.emitShadowColor) {
            out.name("shadow_color");
            this.gson.toJson(shadowColor, SerializerFactory.SHADOW_COLOR_TYPE, out);
         }
      }

      String insertion = value.insertion();
      if (insertion != null) {
         out.name("insertion");
         out.value(insertion);
      }

      ClickEvent clickEvent = value.clickEvent();
      if (clickEvent != null) {
         ClickEvent.Action action = clickEvent.action();
         if (this.emitSnakeCaseClick) {
            out.name("click_event");
            out.beginObject();
            out.name("action");
            this.gson.toJson(action, SerializerFactory.CLICK_ACTION_TYPE, out);
            if (action.readable()) {
               Object payload = BackwardCompatUtil.IS_4_22_0_OR_NEWER ? clickEvent.payload() : clickEvent.value();
               if (!(payload instanceof String) && !(payload instanceof ClickEvent.Payload.Text)) {
                  if (payload instanceof ClickEvent.Payload.Custom) {
                     ClickEvent.Payload.Custom customPayload = (ClickEvent.Payload.Custom)payload;
                     out.name("id");
                     this.gson.toJson(customPayload.key(), SerializerFactory.KEY_TYPE, out);
                     out.name("payload");
                     out.value(customPayload.data());
                  } else if (payload instanceof ClickEvent.Payload.Int) {
                     ClickEvent.Payload.Int intPayload = (ClickEvent.Payload.Int)payload;
                     out.name("page");
                     if (this.emitStringPage) {
                        out.value(String.valueOf(intPayload.integer()));
                     } else {
                        out.value((long)intPayload.integer());
                     }
                  }
               } else {
                  switch(action) {
                  case OPEN_URL:
                     out.name("url");
                     break;
                  case RUN_COMMAND:
                  case SUGGEST_COMMAND:
                     out.name("command");
                  case CHANGE_PAGE:
                  default:
                     break;
                  case COPY_TO_CLIPBOARD:
                     out.name("value");
                  }

                  String payloadValue = payload instanceof String ? (String)payload : ((ClickEvent.Payload.Text)payload).value();
                  if (action == ClickEvent.Action.OPEN_URL && this.emitClickUrlHttps && !isValidUrlScheme(payloadValue)) {
                     payloadValue = "https://" + payloadValue;
                  }

                  out.value(payloadValue);
               }
            }

            out.endObject();
         }

         if (this.emitCamelCaseClick && (!BackwardCompatUtil.IS_4_22_0_OR_NEWER || action.payloadType() == ClickEvent.Payload.Text.class)) {
            out.name("clickEvent");
            out.beginObject();
            out.name("action");
            this.gson.toJson(action, SerializerFactory.CLICK_ACTION_TYPE, out);
            out.name("value");
            payloadValue = clickEvent.value();
            if (action == ClickEvent.Action.OPEN_URL && this.emitClickUrlHttps && !isValidUrlScheme(payloadValue)) {
               payloadValue = "https://" + payloadValue;
            }

            out.value(payloadValue);
            out.endObject();
         }
      }

      HoverEvent<?> hoverEvent = value.hoverEvent();
      if (hoverEvent != null && ((this.emitSnakeCaseHover || this.emitCamelCaseHover) && !hoverEvent.action().toString().equals("show_achievement") || this.emitValueFieldHover)) {
         HoverEvent.Action<?> action = hoverEvent.action();
         if (this.emitSnakeCaseHover && !action.toString().equals("show_achievement")) {
            out.name("hover_event");
            out.beginObject();
            out.name("action");
            this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
            Entry entry;
            Iterator var21;
            if (action == HoverEvent.Action.SHOW_ITEM) {
               var21 = this.gson.toJsonTree(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE).getAsJsonObject().entrySet().iterator();

               while(var21.hasNext()) {
                  entry = (Entry)var21.next();
                  out.name((String)entry.getKey());
                  this.gson.toJson((JsonElement)entry.getValue(), out);
               }
            } else if (action == HoverEvent.Action.SHOW_ENTITY) {
               var21 = this.gson.toJsonTree(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE).getAsJsonObject().entrySet().iterator();

               while(var21.hasNext()) {
                  entry = (Entry)var21.next();
                  out.name((String)entry.getKey());
                  this.gson.toJson((JsonElement)entry.getValue(), out);
               }
            } else {
               if (action != HoverEvent.Action.SHOW_TEXT) {
                  throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
               }

               out.name("value");
               this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
            }

            out.endObject();
         }

         if (this.emitCamelCaseHover || this.emitValueFieldHover) {
            out.name("hoverEvent");
            out.beginObject();
            out.name("action");
            this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
            if (this.emitCamelCaseHover && !action.toString().equals("show_achievement")) {
               out.name("contents");
               if (action == HoverEvent.Action.SHOW_ITEM) {
                  this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
               } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                  this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
               } else {
                  if (action != HoverEvent.Action.SHOW_TEXT) {
                     throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                  }

                  this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
               }
            }

            if (this.emitValueFieldHover) {
               out.name("value");
               this.serializeLegacyHoverEvent(hoverEvent, out);
            }

            out.endObject();
         }
      }

      Key font = value.font();
      if (font != null) {
         out.name("font");
         this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
      }

      out.endObject();
   }

   private void serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonWriter out) throws IOException {
      if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
         this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
      } else if (hoverEvent.action().toString().equals("show_achievement")) {
         this.gson.toJson(hoverEvent.value(), SerializerFactory.STRING_TYPE, out);
      } else if (this.legacyHover != null) {
         Component serialized = null;

         try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
               serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder());
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
               serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
            }
         } catch (IOException var5) {
            throw new JsonSyntaxException(var5);
         }

         if (serialized != null) {
            this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
         } else {
            out.nullValue();
         }
      } else {
         out.nullValue();
      }

   }

   private static boolean isValidUrlScheme(String url) {
      return url.startsWith("http://") || url.startsWith("https://");
   }

   static {
      DECORATIONS = new TextDecoration[]{TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED};
      Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
      TextDecoration[] var1 = DECORATIONS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TextDecoration decoration = var1[var3];
         knownDecorations.remove(decoration);
      }

      if (!knownDecorations.isEmpty()) {
         throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
      }
   }
}
