package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.UniqueIdUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.ComponentSerializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson.BackwardCompatUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.key.Keyed;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.EntityNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.KeybindComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.ObjectComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.ScoreComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.SelectorComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.StorageNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.SpriteObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdventureNBTSerializer implements ComponentSerializer<Component, Component, NBT> {
   private final ClientVersion version;
   private final boolean downsampleColor;

   public AdventureNBTSerializer(ClientVersion version, boolean downsampleColor) {
      this.version = version;
      this.downsampleColor = downsampleColor;
   }

   /** @deprecated */
   @Deprecated
   public AdventureNBTSerializer(boolean downsampleColor) {
      this(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), downsampleColor);
   }

   /** @deprecated */
   @Deprecated
   @Contract("!null -> !null")
   @Nullable
   public Component deserializeOrNull(@Nullable NBT input) {
      return this.deserializeOrNull(input, PacketWrapper.createDummyWrapper(this.version));
   }

   @Contract("!null, _ -> !null")
   @Nullable
   public Component deserializeOrNull(@Nullable NBT input, PacketWrapper<?> wrapper) {
      return input != null ? this.deserialize(input, wrapper) : null;
   }

   /** @deprecated */
   @Deprecated
   @Contract("_, !null -> !null")
   @Nullable
   public Component deserializeOr(@Nullable NBT input, @Nullable Component fallback) {
      return this.deserializeOr(input, fallback, PacketWrapper.createDummyWrapper(this.version));
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public Component deserializeOr(@Nullable NBT input, @Nullable Component fallback, PacketWrapper<?> wrapper) {
      return input != null ? this.deserialize(input, wrapper) : fallback;
   }

   /** @deprecated */
   @Deprecated
   @Contract("!null -> !null")
   @Nullable
   public NBT serializeOrNull(@Nullable Component component) {
      return this.serializeOrNull(component, PacketWrapper.createDummyWrapper(this.version));
   }

   @Contract("!null, _ -> !null")
   @Nullable
   public NBT serializeOrNull(@Nullable Component component, PacketWrapper<?> wrapper) {
      return component != null ? this.serialize(component, wrapper) : null;
   }

   /** @deprecated */
   @Deprecated
   @Contract("_, !null -> !null")
   @Nullable
   public NBT serializeOr(@Nullable Component component, @Nullable NBT fallback) {
      return this.serializeOr(component, fallback, PacketWrapper.createDummyWrapper(this.version));
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public NBT serializeOr(@Nullable Component component, @Nullable NBT fallback, PacketWrapper<?> wrapper) {
      return component != null ? this.serialize(component, wrapper) : fallback;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Component deserialize(@NotNull NBT input) {
      return this.deserialize(input, PacketWrapper.createDummyWrapper(this.version));
   }

   @NotNull
   public Component deserialize(@NotNull NBT input, PacketWrapper<?> wrapper) {
      if (input.getType() == NBTType.STRING) {
         return Component.text(((NBTString)input).getValue());
      } else if (input.getType() == NBTType.BYTE && ((NBTByte)input).getAsByte() < 2) {
         return Component.text(((NBTByte)input).getAsByte() == 1);
      } else if (input instanceof NBTNumber) {
         return Component.text(((NBTNumber)input).getAsInt());
      } else {
         NBTCompound compound = (NBTCompound)requireType(input, NBTType.COMPOUND);
         AdventureNBTSerializer.NBTReader reader = new AdventureNBTSerializer.NBTReader(wrapper, compound);
         Function<NBT, String> textFunction = (nbtx) -> {
            if (nbtx.getType() == NBTType.STRING) {
               return ((NBTString)nbtx).getValue();
            } else if (nbtx.getType() == NBTType.BYTE && ((NBTByte)nbtx).getAsByte() < 2) {
               return String.valueOf(((NBTByte)nbtx).getAsByte() == 1);
            } else if (nbtx instanceof NBTNumber) {
               return String.valueOf(((NBTNumber)nbtx).getAsInt());
            } else {
               throw new IllegalStateException("Don't know how to deserialize " + nbtx.getType() + " to text");
            }
         };
         String text = (String)reader.read("text", textFunction);
         if (text == null) {
            text = (String)reader.read("", textFunction);
         }

         String translate = (String)reader.readUTF("translate", Function.identity());
         String translateFallback = (String)reader.readUTF("fallback", Function.identity());
         List translateWith;
         if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
            NBTType<?> type = reader.type("with");
            if (type == NBTType.INT_ARRAY) {
               translateWith = (List)reader.readIntArray("with", (params) -> {
                  List<TranslationArgument> args = new ArrayList(params.length);
                  int[] var2 = params;
                  int var3 = params.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     int param = var2[var4];
                     args.add(TranslationArgument.numeric(param));
                  }

                  return args;
               });
            } else if (type == NBTType.BYTE_ARRAY) {
               translateWith = (List)reader.readByteArray("with", (params) -> {
                  List<TranslationArgument> args = new ArrayList(params.length);
                  byte[] var2 = params;
                  int var3 = params.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     byte param = var2[var4];
                     args.add(TranslationArgument.bool(param != 0));
                  }

                  return args;
               });
            } else if (type == NBTType.LONG_ARRAY) {
               translateWith = (List)reader.readLongArray("with", (params) -> {
                  List<TranslationArgument> args = new ArrayList(params.length);
                  long[] var2 = params;
                  int var3 = params.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     long param = var2[var4];
                     args.add(TranslationArgument.numeric(param));
                  }

                  return args;
               });
            } else {
               translateWith = (List)reader.readList("with", (tag) -> {
                  return this.deserializeTranslationArgumentList(tag, wrapper);
               });
            }
         } else {
            translateWith = (List)reader.readList("with", (tag) -> {
               return this.deserializeComponentList(tag, wrapper);
            });
         }

         AdventureNBTSerializer.NBTReader score = reader.child("score");
         String selector = (String)reader.readUTF("selector", Function.identity());
         String keybind = (String)reader.readUTF("keybind", Function.identity());
         String nbt = (String)reader.readUTF("nbt", Function.identity());
         boolean nbtInterpret = (Boolean)Optional.ofNullable((Boolean)reader.readBoolean("interpret", Function.identity())).orElse(false);
         BlockNBTComponent.Pos nbtBlock = (BlockNBTComponent.Pos)reader.readUTF("block", BlockNBTComponent.Pos::fromString);
         String nbtEntity = (String)reader.readUTF("entity", Function.identity());
         Key nbtStorage = (Key)reader.readUTF("storage", Key::key);
         List<Component> extra = (List)reader.readList("extra", (tag) -> {
            return this.deserializeComponentList(tag, wrapper);
         });
         Component separator = (Component)reader.read("separator", (tag) -> {
            return this.deserialize(tag, wrapper);
         });
         NBT player = (NBT)reader.read("player", Function.identity());
         String sprite = (String)reader.readUTF("sprite", Function.identity());
         Style style = this.deserializeStyle(compound, wrapper);
         Object builder;
         if (text != null) {
            builder = Component.text().content(text);
         } else if (translate != null) {
            TranslatableComponent.Builder i18nBuilder;
            builder = i18nBuilder = Component.translatable().key(translate);
            if (translateWith != null) {
               if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
                  i18nBuilder.arguments(translateWith);
               } else {
                  i18nBuilder.args(translateWith);
               }
            }

            if (BackwardCompatUtil.IS_4_13_0_OR_NEWER) {
               i18nBuilder.fallback(translateFallback);
            }
         } else if (score != null) {
            builder = Component.score().name((String)score.readUTF("name", Function.identity())).objective((String)score.readUTF("objective", Function.identity()));
         } else if (selector != null) {
            builder = Component.selector().pattern(selector).separator(separator);
         } else if (keybind != null) {
            builder = Component.keybind().keybind(keybind);
         } else if (nbt != null) {
            if (nbtBlock != null) {
               builder = ((BlockNBTComponent.Builder)((BlockNBTComponent.Builder)((BlockNBTComponent.Builder)Component.blockNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).pos(nbtBlock);
            } else if (nbtEntity != null) {
               builder = ((EntityNBTComponent.Builder)((EntityNBTComponent.Builder)((EntityNBTComponent.Builder)Component.entityNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).selector(nbtEntity);
            } else {
               if (nbtStorage == null) {
                  throw new IllegalStateException("Illegal nbt component, block/entity/storage is missing");
               }

               builder = ((StorageNBTComponent.Builder)((StorageNBTComponent.Builder)((StorageNBTComponent.Builder)Component.storageNBT().nbtPath(nbt)).interpret(nbtInterpret)).separator(separator)).storage(nbtStorage);
            }
         } else if (player != null) {
            if (BackwardCompatUtil.IS_4_25_0_OR_NEWER) {
               ItemProfile profile = ItemProfile.decode(player, wrapper);
               PlayerHeadObjectContents playerHead = ObjectContents.playerHead().id(profile.getId()).name(profile.getName()).profileProperties(profile.getAdventureProperties()).hat((Boolean)Optional.ofNullable((Boolean)reader.readBoolean("hat", Function.identity())).orElse(true)).build();
               builder = Component.object().contents(playerHead);
            } else {
               builder = Component.text();
            }
         } else {
            if (sprite == null) {
               throw new IllegalStateException("Illegal nbt component, component type could not be determined");
            }

            if (BackwardCompatUtil.IS_4_25_0_OR_NEWER) {
               Key spriteKey = Key.key(sprite);
               Key atlasKey = (Key)reader.readUTF("atlas", (atlas) -> {
                  return Key.key(atlas);
               });
               builder = Component.object().contents(atlasKey != null ? ObjectContents.sprite(atlasKey, spriteKey) : ObjectContents.sprite(spriteKey));
            } else {
               builder = Component.text();
            }
         }

         ((ComponentBuilder)builder).style(style);
         if (extra != null) {
            ((ComponentBuilder)builder).append((Iterable)extra);
         }

         return ((ComponentBuilder)builder).build();
      }
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public NBT serialize(@NotNull Component component) {
      return this.serialize(component, PacketWrapper.createDummyWrapper(this.version));
   }

   @NotNull
   public NBT serialize(@NotNull Component component, PacketWrapper<?> wrapper) {
      return (NBT)(component instanceof TextComponent && !component.hasStyling() && component.children().isEmpty() ? new NBTString(((TextComponent)component).content()) : this.serializeComponent(component, wrapper));
   }

   @NotNull
   private NBTCompound serializeComponent(Component component, PacketWrapper<?> wrapper) {
      AdventureNBTSerializer.NBTWriter writer = new AdventureNBTSerializer.NBTWriter(new NBTCompound());
      List args;
      if (component instanceof TextComponent) {
         writer.writeUTF("text", ((TextComponent)component).content());
      } else {
         String nbtPath;
         if (component instanceof TranslatableComponent) {
            writer.writeUTF("translate", ((TranslatableComponent)component).key());
            if (BackwardCompatUtil.IS_4_13_0_OR_NEWER) {
               nbtPath = ((TranslatableComponent)component).fallback();
               if (nbtPath != null) {
                  writer.writeUTF("fallback", nbtPath);
               }
            }

            args = ((TranslatableComponent)component).args();
            if (!args.isEmpty()) {
               if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
                  writer.writeList("with", NBTType.COMPOUND, this.serializeTranslationArgumentList(((TranslatableComponent)component).arguments(), wrapper));
               } else {
                  writer.writeList("with", NBTType.COMPOUND, this.serializeComponentList(args, wrapper));
               }
            }
         } else if (component instanceof ScoreComponent) {
            AdventureNBTSerializer.NBTWriter score = writer.child("score");
            String scoreName = ((ScoreComponent)component).name();
            score.writeUTF("name", scoreName);
            String scoreObjective = ((ScoreComponent)component).objective();
            score.writeUTF("objective", scoreObjective);
         } else if (component instanceof SelectorComponent) {
            writer.writeUTF("selector", ((SelectorComponent)component).pattern());
            Component separator = ((SelectorComponent)component).separator();
            if (separator != null) {
               writer.write("separator", this.serialize(separator, wrapper));
            }
         } else if (component instanceof KeybindComponent) {
            writer.writeUTF("keybind", ((KeybindComponent)component).keybind());
         } else if (component instanceof NBTComponent) {
            nbtPath = ((NBTComponent)component).nbtPath();
            writer.writeUTF("nbt", nbtPath);
            boolean interpret = ((NBTComponent)component).interpret();
            if (interpret) {
               writer.writeBoolean("interpret", true);
            }

            Component separator = ((NBTComponent)component).separator();
            if (separator != null) {
               writer.write("separator", this.serialize(separator, wrapper));
            }

            if (component instanceof BlockNBTComponent) {
               BlockNBTComponent.Pos pos = ((BlockNBTComponent)component).pos();
               writer.writeUTF("block", pos.asString());
            } else if (component instanceof EntityNBTComponent) {
               String selector = ((EntityNBTComponent)component).selector();
               writer.writeUTF("entity", selector);
            } else if (component instanceof StorageNBTComponent) {
               Key storage = ((StorageNBTComponent)component).storage();
               writer.writeUTF("storage", storage.asString());
            }
         } else if (component instanceof ObjectComponent) {
            if (BackwardCompatUtil.IS_4_25_0_OR_NEWER && this.version.isNewerThanOrEquals(ClientVersion.V_1_21_9)) {
               ObjectContents objectContents = ((ObjectComponent)component).contents();
               if (objectContents instanceof PlayerHeadObjectContents) {
                  PlayerHeadObjectContents playerHead = (PlayerHeadObjectContents)objectContents;
                  ItemProfile profile = ItemProfile.fromAdventure(playerHead);
                  writer.write("player", ItemProfile.encode(wrapper, profile));
                  if (!playerHead.hat()) {
                     writer.writeBoolean("hat", playerHead.hat());
                  }
               } else if (objectContents instanceof SpriteObjectContents) {
                  SpriteObjectContents spriteObjectContents = (SpriteObjectContents)objectContents;
                  if (!spriteObjectContents.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
                     writer.writeUTF("atlas", spriteObjectContents.atlas().toString());
                  }

                  writer.writeUTF("sprite", spriteObjectContents.sprite().toString());
               }
            } else {
               writer.writeUTF("text", "");
            }
         }
      }

      if (component.hasStyling()) {
         Map var10000 = this.serializeStyle(component.style(), wrapper).getTags();
         Objects.requireNonNull(writer);
         var10000.forEach(writer::write);
      }

      args = component.children();
      if (!args.isEmpty()) {
         writer.writeList("extra", NBTType.COMPOUND, this.serializeComponentList(args, wrapper));
      }

      return writer.compound;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Style deserializeStyle(NBTCompound input) {
      return this.deserializeStyle(input, PacketWrapper.createDummyWrapper(this.version));
   }

   @NotNull
   public Style deserializeStyle(NBTCompound input, PacketWrapper<?> wrapper) {
      if (input.isEmpty()) {
         return Style.empty();
      } else {
         Style.Builder style = Style.style();
         AdventureNBTSerializer.NBTReader reader = new AdventureNBTSerializer.NBTReader(wrapper, input);
         reader.useUTF("font", (valuex) -> {
            style.font(Key.key(valuex));
         });
         reader.useUTF("color", (valuex) -> {
            TextColor color = this.deserializeColor(valuex);
            if (color != null) {
               style.color(color);
            }

         });
         if (BackwardCompatUtil.IS_4_18_0_OR_NEWER) {
            reader.useNumber("shadow_color", (num) -> {
               style.shadowColor(ShadowColor.shadowColor(num.intValue()));
            });
         }

         Iterator var5 = TextDecoration.NAMES.keys().iterator();

         while(var5.hasNext()) {
            String decorationKey = (String)var5.next();
            reader.useBoolean(decorationKey, (valuex) -> {
               style.decoration((TextDecoration)AdventureIndexUtil.indexValueOrThrow(TextDecoration.NAMES, decorationKey), TextDecoration.State.byBoolean(valuex));
            });
         }

         Objects.requireNonNull(style);
         reader.useUTF("insertion", style::insertion);
         boolean modernEvents = this.version.isNewerThanOrEquals(ClientVersion.V_1_21_5);
         AdventureNBTSerializer.NBTReader clickEvent = reader.child(modernEvents ? "click_event" : "clickEvent");
         Index var10002;
         if (clickEvent != null) {
            var10002 = ClickEvent.Action.NAMES;
            Objects.requireNonNull(var10002);
            ClickEvent.Action action = (ClickEvent.Action)clickEvent.readUTF("action", var10002::value);
            ClickEvent value;
            if (!modernEvents) {
               value = ClickEvent.clickEvent(action, (String)clickEvent.readUTF("value", Function.identity()));
            } else {
               switch(action) {
               case OPEN_URL:
                  value = ClickEvent.openUrl((String)clickEvent.readUTF("url", Function.identity()));
                  break;
               case OPEN_FILE:
                  value = ClickEvent.openFile((String)clickEvent.readUTF("path", Function.identity()));
                  break;
               case RUN_COMMAND:
                  value = ClickEvent.runCommand((String)clickEvent.readUTF("command", Function.identity()));
                  break;
               case SUGGEST_COMMAND:
                  value = ClickEvent.suggestCommand((String)clickEvent.readUTF("command", Function.identity()));
                  break;
               case CHANGE_PAGE:
                  value = ClickEvent.changePage((Integer)clickEvent.readNumber("page", Number::intValue));
                  break;
               case COPY_TO_CLIPBOARD:
                  value = ClickEvent.copyToClipboard((String)clickEvent.readUTF("value", Function.identity()));
                  break;
               case SHOW_DIALOG:
                  NBT dialogTag = (NBT)clickEvent.read("dialog", Function.identity());
                  value = ClickEvent.showDialog(Dialog.decode(dialogTag, wrapper));
                  break;
               case CUSTOM:
                  Key key = (Key)clickEvent.readUTF("id", Key::key);
                  NBT payload = (NBT)clickEvent.read("payload", Function.identity());
                  value = ClickEvent.custom(key, (BinaryTagHolder)(new NbtTagHolder((NBT)(payload != null ? payload : NBTEnd.INSTANCE))));
                  break;
               default:
                  throw new UnsupportedOperationException("Unsupported clickevent: " + action);
               }
            }

            style.clickEvent(value);
         }

         AdventureNBTSerializer.NBTReader hoverEvent = reader.child(modernEvents ? "hover_event" : "hoverEvent");
         if (hoverEvent != null) {
            var10002 = HoverEvent.Action.NAMES;
            Objects.requireNonNull(var10002);
            HoverEvent.Action action = (HoverEvent.Action)hoverEvent.readUTF("action", var10002::value);
            String var20 = action.toString();
            byte var21 = -1;
            switch(var20.hashCode()) {
            case -1903644907:
               if (var20.equals("show_item")) {
                  var21 = 1;
               }
               break;
            case -1903331025:
               if (var20.equals("show_text")) {
                  var21 = 0;
               }
               break;
            case 133701477:
               if (var20.equals("show_entity")) {
                  var21 = 2;
               }
            }

            switch(var21) {
            case 0:
               style.hoverEvent(HoverEvent.showText((Component)hoverEvent.read(modernEvents ? "value" : "contents", (tagx) -> {
                  return this.deserialize(tagx, wrapper);
               })));
               break;
            case 1:
               if (!modernEvents && hoverEvent.type("contents") == NBTType.STRING) {
                  style.hoverEvent(HoverEvent.showItem((Key)((Key)hoverEvent.readUTF("contents", Key::key)), 1));
               } else {
                  AdventureNBTSerializer.NBTReader item = modernEvents ? hoverEvent : hoverEvent.child("contents");
                  if (item != null) {
                     Key itemId = (Key)item.readUTF("id", Key::key);
                     Integer count = (Integer)item.readNumber("count", Number::intValue);
                     int nonNullCount = count == null ? 1 : count;
                     BinaryTagHolder tag = (BinaryTagHolder)item.readUTF("tag", BinaryTagHolder::binaryTagHolder);
                     if (tag == null && BackwardCompatUtil.IS_4_17_0_OR_NEWER) {
                        Map<Key, DataComponentValue> components = (Map)item.readCompound("components", (nbt) -> {
                           Map<Key, DataComponentValue> map = new HashMap(nbt.size());
                           Iterator var2 = nbt.getTags().entrySet().iterator();

                           while(var2.hasNext()) {
                              Entry<String, NBT> entry = (Entry)var2.next();
                              Key key;
                              if (((String)entry.getKey()).startsWith("!")) {
                                 key = Key.key(((String)entry.getKey()).substring(1));
                                 map.put(key, DataComponentValue.removed());
                              } else {
                                 key = Key.key((String)entry.getKey());
                                 map.put(key, new NbtTagHolder((NBT)entry.getValue()));
                              }
                           }

                           return map;
                        });
                        style.hoverEvent(HoverEvent.showItem((Keyed)itemId, nonNullCount, (Map)(components == null ? Collections.emptyMap() : components)));
                     } else {
                        style.hoverEvent(HoverEvent.showItem(itemId, nonNullCount, tag));
                     }
                  }
               }
               break;
            case 2:
               AdventureNBTSerializer.NBTReader entity = modernEvents ? hoverEvent : hoverEvent.child("contents");
               if (entity != null) {
                  style.hoverEvent(HoverEvent.showEntity((Key)entity.readUTF(modernEvents ? "id" : "type", Key::key), (UUID)entity.read(modernEvents ? "uuid" : "id", (NbtDecoder)NbtCodecs.LENIENT_UUID), (Component)entity.read("name", (name) -> {
                     return this.deserialize(name, wrapper);
                  })));
               }
            }
         }

         return style.build();
      }
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public NBTCompound serializeStyle(Style style) {
      return this.serializeStyle(style, PacketWrapper.createDummyWrapper(this.version));
   }

   @NotNull
   public NBTCompound serializeStyle(Style style, PacketWrapper<?> wrapper) {
      if (style.isEmpty()) {
         return new NBTCompound();
      } else {
         AdventureNBTSerializer.NBTWriter writer = new AdventureNBTSerializer.NBTWriter(new NBTCompound());
         Key font = style.font();
         if (font != null) {
            writer.writeUTF("font", font.asString());
         }

         TextColor color = style.color();
         if (color != null) {
            writer.writeUTF("color", this.serializeColor(color));
         }

         if (BackwardCompatUtil.IS_4_18_0_OR_NEWER) {
            ShadowColor shadowColor = style.shadowColor();
            if (shadowColor != null) {
               writer.writeInt("shadow_color", shadowColor.value());
            }
         }

         Iterator var23 = TextDecoration.NAMES.values().iterator();

         while(var23.hasNext()) {
            TextDecoration decoration = (TextDecoration)var23.next();
            TextDecoration.State state = style.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
               writer.writeBoolean(decoration.toString(), state == TextDecoration.State.TRUE);
            }
         }

         String insertion = style.insertion();
         if (insertion != null) {
            writer.writeUTF("insertion", insertion);
         }

         ClickEvent clickEvent = style.clickEvent();
         if (clickEvent != null) {
            boolean modern = this.version.isNewerThanOrEquals(ClientVersion.V_1_21_5);
            AdventureNBTSerializer.NBTWriter child = writer.child(modern ? "click_event" : "clickEvent");
            child.writeUTF("action", clickEvent.action().toString());
            if (!modern) {
               child.writeUTF("value", clickEvent.value());
            } else {
               switch(clickEvent.action()) {
               case OPEN_URL:
                  child.writeUTF("url", clickEvent.value());
                  break;
               case OPEN_FILE:
                  child.writeUTF("path", clickEvent.value());
                  break;
               case RUN_COMMAND:
               case SUGGEST_COMMAND:
                  child.writeUTF("command", clickEvent.value());
                  break;
               case CHANGE_PAGE:
                  if (BackwardCompatUtil.IS_4_22_0_OR_NEWER) {
                     child.writeInt("page", ((ClickEvent.Payload.Int)clickEvent.payload()).integer());
                  } else {
                     child.writeInt("page", Integer.parseInt(clickEvent.value()));
                  }
                  break;
               case COPY_TO_CLIPBOARD:
                  child.writeUTF("value", clickEvent.value());
                  break;
               case SHOW_DIALOG:
                  Dialog dialog = (Dialog)((ClickEvent.Payload.Dialog)clickEvent.payload()).dialog();
                  child.write("dialog", Dialog.encode(wrapper, dialog));
                  break;
               case CUSTOM:
                  ClickEvent.Payload.Custom customPayload = (ClickEvent.Payload.Custom)clickEvent.payload();
                  child.writeUTF("id", customPayload.key().asString());
                  BinaryTagHolder nbtHolder = customPayload.nbt();
                  if (nbtHolder instanceof NbtTagHolder) {
                     NBT payloadTag = ((NbtTagHolder)nbtHolder).getTag();
                     if (!(payloadTag instanceof NBTEnd)) {
                        child.write("payload", payloadTag);
                     }
                  } else {
                     String nbtString = nbtHolder.string();
                     if (!nbtString.isEmpty()) {
                        child.write("payload", AdventureNbtUtil.fromString(nbtString));
                     }
                  }
                  break;
               default:
                  throw new UnsupportedOperationException("Unsupported clickevent: " + clickEvent);
               }
            }
         }

         HoverEvent<?> hoverEvent = style.hoverEvent();
         if (hoverEvent != null) {
            boolean modern = this.version.isNewerThanOrEquals(ClientVersion.V_1_21_5);
            AdventureNBTSerializer.NBTWriter child = writer.child(modern ? "hover_event" : "hoverEvent");
            child.writeUTF("action", hoverEvent.action().toString());
            String var30 = hoverEvent.action().toString();
            byte var31 = -1;
            switch(var30.hashCode()) {
            case -1903644907:
               if (var30.equals("show_item")) {
                  var31 = 1;
               }
               break;
            case -1903331025:
               if (var30.equals("show_text")) {
                  var31 = 0;
               }
               break;
            case 133701477:
               if (var30.equals("show_entity")) {
                  var31 = 2;
               }
            }

            AdventureNBTSerializer.NBTWriter compsNbt;
            switch(var31) {
            case 0:
               child.write(modern ? "value" : "contents", this.serialize((Component)hoverEvent.value(), wrapper));
               break;
            case 1:
               HoverEvent.ShowItem item = (HoverEvent.ShowItem)hoverEvent.value();
               Key itemId = item.item();
               int count = item.count();
               BinaryTagHolder nbt = item.nbt();
               boolean emptyComps = !BackwardCompatUtil.IS_4_17_0_OR_NEWER || item.dataComponents().isEmpty();
               if (!modern && count == 1 && nbt == null && emptyComps) {
                  child.writeUTF("contents", itemId.asString());
               } else {
                  AdventureNBTSerializer.NBTWriter itemNBT = modern ? child : child.child("contents");
                  itemNBT.writeUTF("id", itemId.asString());
                  if (!modern || count != 1) {
                     itemNBT.writeInt("count", count);
                  }

                  if (nbt != null) {
                     itemNBT.writeUTF("tag", nbt.string());
                  }

                  if (!emptyComps) {
                     compsNbt = itemNBT.child("components");
                     Iterator var20 = item.dataComponents().entrySet().iterator();

                     while(var20.hasNext()) {
                        Entry<Key, DataComponentValue> entry = (Entry)var20.next();
                        if (entry.getValue() == DataComponentValue.removed()) {
                           compsNbt.writeCompound("!" + entry.getKey(), new NBTCompound());
                        } else if (entry.getValue() instanceof NbtTagHolder) {
                           NBT compNbt = ((NbtTagHolder)entry.getValue()).getTag();
                           compsNbt.write(((Key)entry.getKey()).toString(), compNbt);
                        }
                     }
                  }
               }
               break;
            case 2:
               HoverEvent.ShowEntity showEntity = (HoverEvent.ShowEntity)hoverEvent.value();
               compsNbt = modern ? child : child.child("contents");
               if (compsNbt != null) {
                  compsNbt.writeUTF(modern ? "id" : "type", showEntity.type().asString());
                  compsNbt.writeIntArray(modern ? "uuid" : "id", UniqueIdUtil.toIntArray(showEntity.id()));
                  if (showEntity.name() != null) {
                     compsNbt.write("name", this.serialize(showEntity.name(), wrapper));
                  }
               }
            }
         }

         return writer.compound;
      }
   }

   @Nullable
   private TextColor deserializeColor(@NotNull final String value) {
      TextColor color;
      if (value.startsWith("#")) {
         color = TextColor.fromHexString(value);
      } else {
         color = (TextColor)NamedTextColor.NAMES.value(value);
      }

      if (color == null) {
         return null;
      } else {
         return (TextColor)(this.downsampleColor ? NamedTextColor.nearestTo(color) : color);
      }
   }

   @NotNull
   private String serializeColor(@NotNull final TextColor value) {
      if (value instanceof NamedTextColor) {
         return (String)NamedTextColor.NAMES.key((NamedTextColor)value);
      } else {
         return this.downsampleColor ? (String)NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)) : String.format(Locale.ROOT, "%c%06X", '#', value.value());
      }
   }

   @NotNull
   private List<Component> deserializeComponentList(List<?> value, PacketWrapper<?> wrapper) {
      if (value.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<Component> components = new ArrayList(value.size());
         Iterator var4 = value.iterator();

         while(var4.hasNext()) {
            Object nbt = var4.next();
            components.add(this.deserialize((NBT)nbt, wrapper));
         }

         return components;
      }
   }

   private List<NBTCompound> serializeComponentList(List<Component> value, PacketWrapper<?> wrapper) {
      List<NBTCompound> components = new ArrayList(value.size());
      Iterator var4 = value.iterator();

      while(var4.hasNext()) {
         Component component = (Component)var4.next();
         components.add(this.serializeComponent(component, wrapper));
      }

      return components;
   }

   @NotNull
   private List<TranslationArgument> deserializeTranslationArgumentList(List<?> value, PacketWrapper<?> wrapper) {
      if (value.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<TranslationArgument> arguments = new ArrayList(value.size());
         Iterator var4 = value.iterator();

         while(var4.hasNext()) {
            Object nbt = var4.next();
            if (nbt instanceof NBTByte) {
               arguments.add(TranslationArgument.bool(((NBTByte)nbt).getAsByte() != 0));
            } else if (nbt instanceof NBTNumber) {
               arguments.add(TranslationArgument.numeric(((NBTNumber)nbt).getAsInt()));
            } else if (nbt instanceof NBTString) {
               arguments.add(TranslationArgument.component(Component.text(((NBTString)nbt).getValue())));
            } else {
               arguments.add(TranslationArgument.component(this.deserialize(requireType((NBT)nbt, NBTType.COMPOUND), wrapper)));
            }
         }

         return arguments;
      }
   }

   private List<NBTCompound> serializeTranslationArgumentList(List<TranslationArgument> value, PacketWrapper<?> wrapper) {
      List<NBTCompound> arguments = new ArrayList(value.size());
      Iterator var4 = value.iterator();

      while(var4.hasNext()) {
         TranslationArgument argument = (TranslationArgument)var4.next();
         arguments.add(this.serializeComponent(argument.asComponent(), wrapper));
      }

      return arguments;
   }

   private static <T extends NBT> T requireType(NBT nbt, NBTType<T> required) {
      if (nbt.getType() != required) {
         throw new IllegalArgumentException("Expected " + required + " but got " + nbt.getType());
      } else {
         return nbt;
      }
   }

   static class NBTReader {
      private final PacketWrapper<?> wrapper;
      private final NBTCompound compound;

      public NBTReader(PacketWrapper<?> wrapper, NBTCompound compound) {
         this.wrapper = wrapper;
         this.compound = compound;
      }

      public void useBoolean(String key, Consumer<Boolean> consumer) {
         this.useNumber(key, (num) -> {
            consumer.accept(num.byteValue() != 0);
         });
      }

      public <R> R readBoolean(String key, Function<Boolean, R> function) {
         return this.readNumber(key, (num) -> {
            return function.apply(num.byteValue() != 0);
         });
      }

      public void useNumber(String key, Consumer<Number> consumer) {
         this.useTag(key, (tag) -> {
            if (tag instanceof NBTNumber) {
               consumer.accept(((NBTNumber)tag).getAsNumber());
            } else {
               throw new IllegalArgumentException("Expected number but got " + tag.getType());
            }
         });
      }

      public <R> R readNumber(String key, Function<Number, R> function) {
         return this.withTag(key, (tag) -> {
            if (tag instanceof NBTNumber) {
               return function.apply(((NBTNumber)tag).getAsNumber());
            } else {
               throw new IllegalArgumentException("Expected number but got " + tag.getType());
            }
         });
      }

      public void useUTF(String key, Consumer<String> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept(((NBTString)AdventureNBTSerializer.requireType(tag, NBTType.STRING)).getValue());
         });
      }

      public <R> R readUTF(String key, Function<String, R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply(((NBTString)AdventureNBTSerializer.requireType(tag, NBTType.STRING)).getValue());
         });
      }

      public void useByteArray(String key, Consumer<byte[]> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept(((NBTByteArray)AdventureNBTSerializer.requireType(tag, NBTType.BYTE_ARRAY)).getValue());
         });
      }

      public <R> R readByteArray(String key, Function<byte[], R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply(((NBTByteArray)AdventureNBTSerializer.requireType(tag, NBTType.BYTE_ARRAY)).getValue());
         });
      }

      public void useIntArray(String key, Consumer<int[]> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept(((NBTIntArray)AdventureNBTSerializer.requireType(tag, NBTType.INT_ARRAY)).getValue());
         });
      }

      public <R> R readIntArray(String key, Function<int[], R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply(((NBTIntArray)AdventureNBTSerializer.requireType(tag, NBTType.INT_ARRAY)).getValue());
         });
      }

      public void useLongArray(String key, Consumer<long[]> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept(((NBTLongArray)AdventureNBTSerializer.requireType(tag, NBTType.LONG_ARRAY)).getValue());
         });
      }

      public <R> R readLongArray(String key, Function<long[], R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply(((NBTLongArray)AdventureNBTSerializer.requireType(tag, NBTType.LONG_ARRAY)).getValue());
         });
      }

      public void useCompound(String key, Consumer<NBTCompound> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept((NBTCompound)AdventureNBTSerializer.requireType(tag, NBTType.COMPOUND));
         });
      }

      public <R> R readCompound(String key, Function<NBTCompound, R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply((NBTCompound)AdventureNBTSerializer.requireType(tag, NBTType.COMPOUND));
         });
      }

      public void useList(String key, Consumer<List<?>> consumer) {
         this.useTag(key, (tag) -> {
            consumer.accept(((NBTList)AdventureNBTSerializer.requireType(tag, NBTType.LIST)).getTags());
         });
      }

      public <R> R readList(String key, Function<List<?>, R> function) {
         return this.withTag(key, (tag) -> {
            return function.apply(((NBTList)AdventureNBTSerializer.requireType(tag, NBTType.LIST)).getTags());
         });
      }

      public void use(String key, Consumer<NBT> consumer) {
         this.useTag(key, consumer);
      }

      public <R> R read(String key, NbtDecoder<R> decoder) {
         return this.withTag(key, (tag) -> {
            return decoder.decode(tag, this.wrapper);
         });
      }

      public <R> R read(String key, Function<NBT, R> function) {
         return this.withTag(key, function);
      }

      public AdventureNBTSerializer.NBTReader child(String key) {
         return (AdventureNBTSerializer.NBTReader)this.withTag(key, (tag) -> {
            NBTCompound compound = (NBTCompound)AdventureNBTSerializer.requireType(tag, NBTType.COMPOUND);
            return new AdventureNBTSerializer.NBTReader(this.wrapper, compound);
         });
      }

      public NBTType<?> type(String key) {
         return (NBTType)this.withTag(key, NBT::getType);
      }

      private void useTag(String key, Consumer<NBT> consumer) {
         NBT tag = this.compound.getTagOrNull(key);
         if (tag != null) {
            consumer.accept(tag);
         }

      }

      private <R> R withTag(String key, Function<NBT, R> function) {
         NBT tag = this.compound.getTagOrNull(key);
         return tag == null ? null : function.apply(tag);
      }
   }

   static class NBTWriter {
      private final NBTCompound compound;

      public NBTWriter(NBTCompound compound) {
         this.compound = compound;
      }

      public void writeBoolean(String key, boolean value) {
         this.compound.setTag(key, new NBTByte((byte)(value ? 1 : 0)));
      }

      public void writeByte(String key, byte value) {
         this.compound.setTag(key, new NBTByte(value));
      }

      public void writeShort(String key, short value) {
         this.compound.setTag(key, new NBTShort(value));
      }

      public void writeInt(String key, int value) {
         this.compound.setTag(key, new NBTInt(value));
      }

      public void writeLong(String key, long value) {
         this.compound.setTag(key, new NBTLong(value));
      }

      public void writeFloat(String key, float value) {
         this.compound.setTag(key, new NBTFloat(value));
      }

      public void writeDouble(String key, double value) {
         this.compound.setTag(key, new NBTDouble(value));
      }

      public void writeUTF(String key, String value) {
         this.compound.setTag(key, new NBTString(value));
      }

      public void writeByteArray(String key, byte[] value) {
         this.compound.setTag(key, new NBTByteArray(value));
      }

      public void writeIntArray(String key, int[] value) {
         this.compound.setTag(key, new NBTIntArray(value));
      }

      public void writeLongArray(String key, long[] value) {
         this.compound.setTag(key, new NBTLongArray(value));
      }

      public <T extends NBT> void writeList(String key, NBTType<T> innerType, List<T> value) {
         this.compound.setTag(key, new NBTList(innerType, value));
      }

      public void writeCompound(String key, NBTCompound value) {
         this.compound.setTag(key, value);
      }

      public void write(String key, NBT value) {
         this.compound.setTag(key, value);
      }

      public AdventureNBTSerializer.NBTWriter child(String key) {
         NBTCompound child = new NBTCompound();
         this.compound.setTag(key, child);
         return new AdventureNBTSerializer.NBTWriter(child);
      }
   }
}
