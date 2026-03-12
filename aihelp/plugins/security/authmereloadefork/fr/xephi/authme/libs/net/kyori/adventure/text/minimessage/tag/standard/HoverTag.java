package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.key.InvalidKeyException;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.key.Keyed;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValue;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class HoverTag {
   private static final String HOVER = "hover";
   static final TagResolver RESOLVER = SerializableResolver.claimingStyle("hover", HoverTag::create, StyleClaim.claim("hover", Style::hoverEvent, HoverTag::emit));

   private HoverTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String actionName = args.popOr("Hover event requires an action as its first argument").value();
      HoverEvent.Action<Object> action = (HoverEvent.Action)HoverEvent.Action.NAMES.value(actionName);
      HoverTag.ActionHandler<Object> value = actionHandler(action);
      if (value == null) {
         throw ctx.newException("Don't know how to turn '" + args + "' into a hover event", args);
      } else {
         return Tag.styling(HoverEvent.hoverEvent(action, value.parse(args, ctx)));
      }
   }

   static void emit(final HoverEvent<?> event, final TokenEmitter emitter) {
      HoverTag.ActionHandler<Object> handler = actionHandler(event.action());
      emitter.tag("hover").argument((String)HoverEvent.Action.NAMES.key(event.action()));
      handler.emit(event.value(), emitter);
   }

   @Nullable
   static <V> HoverTag.ActionHandler<V> actionHandler(final HoverEvent.Action<V> action) {
      HoverTag.ActionHandler<?> ret = null;
      if (action == HoverEvent.Action.SHOW_TEXT) {
         ret = HoverTag.ShowText.INSTANCE;
      } else if (action == HoverEvent.Action.SHOW_ITEM) {
         ret = HoverTag.ShowItem.INSTANCE;
      } else if (action == HoverEvent.Action.SHOW_ENTITY) {
         ret = HoverTag.ShowEntity.INSTANCE;
      }

      return (HoverTag.ActionHandler)ret;
   }

   @NotNull
   static String compactAsString(@NotNull final Key key) {
      return key.namespace().equals("minecraft") ? key.value() : key.asString();
   }

   static final class ShowEntity implements HoverTag.ActionHandler<HoverEvent.ShowEntity> {
      static final HoverTag.ShowEntity INSTANCE = new HoverTag.ShowEntity();

      private ShowEntity() {
      }

      @NotNull
      public HoverEvent.ShowEntity parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
         try {
            Key key = Key.key(args.popOr("Show entity needs a type argument").value());
            UUID id = UUID.fromString(args.popOr("Show entity needs an entity UUID").value());
            if (args.hasNext()) {
               Component name = ctx.deserialize(args.pop().value());
               return HoverEvent.ShowEntity.showEntity(key, id, name);
            } else {
               return HoverEvent.ShowEntity.showEntity(key, id);
            }
         } catch (InvalidKeyException | IllegalArgumentException var6) {
            throw ctx.newException("Exception parsing show_entity hover", var6, args);
         }
      }

      public void emit(final HoverEvent.ShowEntity event, final TokenEmitter emit) {
         emit.argument(HoverTag.compactAsString(event.type())).argument(event.id().toString());
         if (event.name() != null) {
            emit.argument(event.name());
         }

      }
   }

   static final class ShowItem implements HoverTag.ActionHandler<HoverEvent.ShowItem> {
      private static final HoverTag.ShowItem INSTANCE = new HoverTag.ShowItem();

      private ShowItem() {
      }

      @NotNull
      public HoverEvent.ShowItem parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
         try {
            Key key = Key.key(args.popOr("Show item hover needs at least an item ID").value());
            int count = args.hasNext() ? args.pop().asInt().orElseThrow(() -> {
               return ctx.newException("The count argument was not a valid integer");
            }) : 1;
            if (!args.hasNext()) {
               return HoverEvent.ShowItem.showItem(key, count);
            } else {
               String value = args.peek().value();
               if (value.startsWith("{")) {
                  args.pop();
                  return legacyShowItem(key, count, value);
               } else {
                  HashMap datas = new HashMap();

                  while(args.hasNext()) {
                     Key dataKey = Key.key(args.pop().value());
                     String dataVal = args.popOr("a value was expected for key " + dataKey).value();
                     datas.put(dataKey, BinaryTagHolder.binaryTagHolder(dataVal));
                  }

                  return HoverEvent.ShowItem.showItem((Keyed)key, count, (Map)datas);
               }
            }
         } catch (NumberFormatException | InvalidKeyException var9) {
            throw ctx.newException("Exception parsing show_item hover", var9, args);
         }
      }

      @NotNull
      private static HoverEvent.ShowItem legacyShowItem(final Key id, final int count, final String value) {
         return HoverEvent.ShowItem.showItem(id, count, BinaryTagHolder.binaryTagHolder(value));
      }

      public void emit(final HoverEvent.ShowItem event, final TokenEmitter emit) {
         emit.argument(HoverTag.compactAsString(event.item()));
         if (event.count() != 1 || hasLegacy(event) || !event.dataComponents().isEmpty()) {
            emit.argument(Integer.toString(event.count()));
            if (hasLegacy(event)) {
               emitLegacyHover(event, emit);
            } else {
               Iterator var3 = event.dataComponentsAs(DataComponentValue.TagSerializable.class).entrySet().iterator();

               while(var3.hasNext()) {
                  Entry<Key, DataComponentValue.TagSerializable> entry = (Entry)var3.next();
                  emit.argument(((Key)entry.getKey()).asMinimalString());
                  emit.argument(((DataComponentValue.TagSerializable)entry.getValue()).asBinaryTag().string());
               }
            }
         }

      }

      static boolean hasLegacy(final HoverEvent.ShowItem event) {
         return event.nbt() != null;
      }

      static void emitLegacyHover(final HoverEvent.ShowItem event, final TokenEmitter emit) {
         if (event.nbt() != null) {
            emit.argument(event.nbt().string());
         }

      }
   }

   static final class ShowText implements HoverTag.ActionHandler<Component> {
      private static final HoverTag.ShowText INSTANCE = new HoverTag.ShowText();

      private ShowText() {
      }

      @NotNull
      public Component parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
         return ctx.deserialize(args.popOr("show_text action requires a message").value());
      }

      public void emit(final Component event, final TokenEmitter emit) {
         emit.argument(event);
      }
   }

   interface ActionHandler<V> {
      @NotNull
      V parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException;

      void emit(final V event, final TokenEmitter emit);
   }
}
