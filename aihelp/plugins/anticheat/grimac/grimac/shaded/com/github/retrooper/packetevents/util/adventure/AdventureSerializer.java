package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stats.Statistics;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import com.google.gson.JsonElement;
import java.util.EnumMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdventureSerializer implements NbtEncoder<Component>, NbtDecoder<Component> {
   private static final Map<ClientVersion, AdventureSerializer> SERIALIZERS = new EnumMap(ClientVersion.class);
   private final ClientVersion version;
   @Nullable
   private GsonComponentSerializer gson;
   @Nullable
   private LegacyComponentSerializer legacy;
   @Nullable
   private AdventureNBTSerializer nbt;

   private AdventureSerializer(ClientVersion version) {
      this.version = version;
   }

   public static AdventureSerializer serializer(PacketWrapper<?> wrapper) {
      return serializer(wrapper.getServerVersion().toClientVersion());
   }

   public static AdventureSerializer serializer(ClientVersion version) {
      AdventureSerializer holder = (AdventureSerializer)SERIALIZERS.get(version);
      if (holder != null) {
         return holder;
      } else {
         synchronized(SERIALIZERS) {
            return (AdventureSerializer)SERIALIZERS.computeIfAbsent(version, AdventureSerializer::new);
         }
      }
   }

   public static AdventureSerializer serializer() {
      return serializer(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static GsonComponentSerializer getGsonSerializer() {
      return serializer().gson();
   }

   /** @deprecated */
   @Deprecated
   public static LegacyComponentSerializer getLegacyGsonSerializer() {
      return serializer().legacy();
   }

   /** @deprecated */
   @Deprecated
   public static LegacyComponentSerializer getLegacySerializer() {
      return serializer().legacy();
   }

   /** @deprecated */
   @Deprecated
   public static AdventureNBTSerializer getNBTSerializer() {
      return serializer().nbt();
   }

   /** @deprecated */
   @Deprecated
   public static String asVanilla(Component component) {
      return serializer().asLegacy(component);
   }

   /** @deprecated */
   @Deprecated
   public static Component fromLegacyFormat(String legacyMessage) {
      return serializer().fromLegacy(legacyMessage);
   }

   /** @deprecated */
   @Deprecated
   public static String toLegacyFormat(Component component) {
      return serializer().asLegacy(component);
   }

   /** @deprecated */
   @Deprecated
   public static Component parseComponent(String json) {
      return serializer().fromJson(json);
   }

   /** @deprecated */
   @Deprecated
   public static Component parseJsonTree(JsonElement json) {
      return serializer().fromJsonTree(json);
   }

   /** @deprecated */
   @Deprecated
   public static String toJson(Component component) {
      return serializer().asJson(component);
   }

   /** @deprecated */
   @Deprecated
   public static JsonElement toJsonTree(Component component) {
      return serializer().asJsonTree(component);
   }

   /** @deprecated */
   @Deprecated
   public static Component fromNbt(NBT tag) {
      return serializer().fromNbtTag(tag);
   }

   /** @deprecated */
   @Deprecated
   public static NBT toNbt(Component component) {
      return serializer().asNbtTag(component);
   }

   public Component fromLegacy(String legacy) {
      return this.legacy().deserializeOrNull(legacy);
   }

   public String asLegacy(Component component) {
      return (String)this.legacy().serializeOrNull(component);
   }

   public Component fromJson(String json) {
      return this.gson().deserializeOrNull(json);
   }

   public String asJson(Component component) {
      return (String)this.gson().serializeOrNull(component);
   }

   @Contract("!null -> !null")
   @Nullable
   public Component fromJsonTree(@Nullable JsonElement json) {
      return json != null ? this.gson().deserializeFromTree(json) : null;
   }

   @Contract("!null -> !null")
   @Nullable
   public JsonElement asJsonTree(@Nullable Component component) {
      return component != null ? this.gson().serializeToTree(component) : null;
   }

   /** @deprecated */
   @Deprecated
   public Component fromNbtTag(NBT tag) {
      return this.fromNbtTag(tag, PacketWrapper.createDummyWrapper(this.version));
   }

   public Component fromNbtTag(NBT tag, PacketWrapper<?> wrapper) {
      return this.nbt().deserializeOrNull(tag, wrapper);
   }

   /** @deprecated */
   @Deprecated
   public NBT asNbtTag(Component component) {
      return this.asNbtTag(component, PacketWrapper.createDummyWrapper(this.version));
   }

   public NBT asNbtTag(Component component, PacketWrapper<?> wrapper) {
      return this.nbt().serializeOrNull(component, wrapper);
   }

   public GsonComponentSerializer gson() {
      if (this.gson == null) {
         this.gson = GsonComponentSerializer.builder().editOptions((builder) -> {
            builder.values((OptionState)JSONOptions.byDataVersion().at(0));
            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
               builder.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.CAMEL_CASE);
               if (!PacketEvents.getAPI().getSettings().shouldDownsampleColors()) {
                  builder.value(JSONOptions.EMIT_RGB, true);
               }
            }

            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_3)) {
               builder.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, true);
               builder.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true);
               builder.value(JSONOptions.VALIDATE_STRICT_EVENTS, true);
            }

            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
               builder.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY, true);
               builder.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, JSONOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS);
            }

            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
               builder.value(JSONOptions.SHADOW_COLOR_MODE, JSONOptions.ShadowColorEmitMode.EMIT_INTEGER);
            }

            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
               builder.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.SNAKE_CASE);
               builder.value(JSONOptions.EMIT_CLICK_EVENT_TYPE, JSONOptions.ClickEventValueMode.SNAKE_CASE);
               builder.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID, false);
               builder.value(JSONOptions.EMIT_CLICK_URL_HTTPS, true);
            }

            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_21_6)) {
               builder.value(JSONOptions.EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING, false);
            }

         }).legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get()).showAchievementToComponent((input) -> {
            return Statistics.getById(input).display();
         }).build();
      }

      return this.gson;
   }

   public LegacyComponentSerializer legacy() {
      if (this.legacy == null) {
         LegacyComponentSerializer.Builder builder = LegacyComponentSerializer.builder();
         if (this.version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
            builder.hexColors();
         }

         this.legacy = builder.build();
      }

      return this.legacy;
   }

   public AdventureNBTSerializer nbt() {
      if (this.nbt == null) {
         boolean downsample = this.version.isOlderThan(ClientVersion.V_1_16) || PacketEvents.getAPI().getSettings().shouldDownsampleColors();
         this.nbt = new AdventureNBTSerializer(this.version, downsample);
      }

      return this.nbt;
   }

   public Component decode(NBT nbt, PacketWrapper<?> wrapper) {
      return this.nbt().deserialize(nbt, wrapper);
   }

   public NBT encode(PacketWrapper<?> wrapper, Component value) {
      return this.nbt().serialize(value, wrapper);
   }
}
