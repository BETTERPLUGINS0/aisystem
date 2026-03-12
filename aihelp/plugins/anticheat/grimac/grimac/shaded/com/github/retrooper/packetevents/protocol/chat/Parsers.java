package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Parsers {
   private static final VersionedRegistry<Parsers.Parser> REGISTRY = new VersionedRegistry("command_argument_type");
   public static final Parsers.Parser BRIGADIER_BOOL = define("brigadier:bool", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser BRIGADIER_FLOAT = define("brigadier:float", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      float min = (flags & 1) != 0 ? packetWrapper.readFloat() : -3.4028235E38F;
      float max = (flags & 2) != 0 ? packetWrapper.readFloat() : Float.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeFloat((Float)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeFloat((Float)properties.get(2));
      }

   });
   public static final Parsers.Parser BRIGADIER_DOUBLE = define("brigadier:double", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      double min = (flags & 1) != 0 ? packetWrapper.readDouble() : -1.7976931348623157E308D;
      double max = (flags & 2) != 0 ? packetWrapper.readDouble() : Double.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeDouble((Double)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeDouble((Double)properties.get(2));
      }

   });
   public static final Parsers.Parser BRIGADIER_INTEGER = define("brigadier:integer", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      int min = (flags & 1) != 0 ? packetWrapper.readInt() : Integer.MIN_VALUE;
      int max = (flags & 2) != 0 ? packetWrapper.readInt() : Integer.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeInt((Integer)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeInt((Integer)properties.get(2));
      }

   });
   public static final Parsers.Parser BRIGADIER_LONG = define("brigadier:long", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      long min = (flags & 1) != 0 ? packetWrapper.readLong() : Long.MIN_VALUE;
      long max = (flags & 2) != 0 ? packetWrapper.readLong() : Long.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeLong((Long)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeLong((Long)properties.get(2));
      }

   });
   public static final Parsers.Parser BRIGADIER_STRING = define("brigadier:string", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readVarInt());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeVarInt((Integer)properties.get(0));
   });
   public static final Parsers.Parser ENTITY = define("entity", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readByte());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeByte(((Byte)properties.get(0)).intValue());
   });
   public static final Parsers.Parser GAME_PROFILE = define("game_profile", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser BLOCK_POS = define("block_pos", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser COLUMN_POS = define("column_pos", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser VEC3 = define("vec3", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser VEC2 = define("vec2", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser BLOCK_STATE = define("block_state", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser BLOCK_PREDICATE = define("block_predicate", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ITEM_STACK = define("item_stack", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ITEM_PREDICATE = define("item_predicate", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser COLOR = define("color", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser COMPONENT = define("component", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser STYLE = define("style", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser MESSAGE = define("message", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser NBT_COMPOUND_TAG = define("nbt_compound_tag", (Parsers.Reader)null, (Parsers.Writer)null);
   @ApiStatus.Obsolete
   public static final Parsers.Parser NBT = define("nbt", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser NBT_TAG = define("nbt_tag", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser NBT_PATH = define("nbt_path", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser OBJECTIVE = define("objective", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser OBJECTIVE_CRITERIA = define("objective_criteria", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser OPERATION = define("operation", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser PARTICLE = define("particle", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ANGLE = define("angle", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ROTATION = define("rotation", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser SCOREBOARD_SLOT = define("scoreboard_slot", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser SCORE_HOLDER = define("score_holder", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readByte());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeByte(((Byte)properties.get(0)).intValue());
   });
   public static final Parsers.Parser SWIZZLE = define("swizzle", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser TEAM = define("team", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ITEM_SLOT = define("item_slot", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ITEM_SLOTS = define("item_slots", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser RESOURCE_LOCATION = define("resource_location", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser MOB_EFFECT = define("mob_effect", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser FUNCTION = define("function", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ENTITY_ANCHOR = define("entity_anchor", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser INT_RANGE = define("int_range", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser FLOAT_RANGE = define("float_range", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ITEM_ENCHANTMENT = define("item_enchantment", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser ENTITY_SUMMON = define("entity_summon", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser DIMENSION = define("dimension", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser GAMEMODE = define("gamemode", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser TIME = define("time", (wrapper) -> {
      return Collections.singletonList(wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) ? wrapper.readInt() : 0);
   }, (wrapper, properties) -> {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
         wrapper.writeInt((Integer)properties.get(0));
      }

   });
   public static final Parsers.Parser RESOURCE_OR_TAG = define("resource_or_tag", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   });
   public static final Parsers.Parser RESOURCE_OR_TAG_KEY = define("resource_or_tag_key", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   });
   public static final Parsers.Parser RESOURCE = define("resource", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   });
   public static final Parsers.Parser RESOURCE_KEY = define("resource_key", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   });
   public static final Parsers.Parser TEMPLATE_MIRROR = define("template_mirror", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser TEMPLATE_ROTATION = define("template_rotation", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser HEIGHTMAP = define("heightmap", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser LOOT_TABLE = define("loot_table", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser LOOT_PREDICATE = define("loot_predicate", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser LOOT_MODIFIER = define("loot_modifier", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser UUID = define("uuid", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser RESOURCE_SELECTOR = define("resource_selector", (wrapper) -> {
      return Collections.singletonList(wrapper.readIdentifier());
   }, (wrapper, value) -> {
      wrapper.writeIdentifier((ResourceLocation)value.get(0));
   });
   public static final Parsers.Parser HEX_COLOR = define("hex_color", (Parsers.Reader)null, (Parsers.Writer)null);
   public static final Parsers.Parser DIALOG = define("dialog", (Parsers.Reader)null, (Parsers.Writer)null);

   private Parsers() {
   }

   @ApiStatus.Internal
   public static Parsers.Parser define(String key) {
      return define(key, (Parsers.Reader)null, (Parsers.Writer)null);
   }

   @ApiStatus.Internal
   public static Parsers.Parser define(String key, @Nullable Parsers.Reader reader, @Nullable Parsers.Writer writer) {
      return (Parsers.Parser)REGISTRY.define(key, (data) -> {
         return new Parsers.Parser(data, reader, writer);
      });
   }

   public static Parsers.Parser getByName(String name) {
      return (Parsers.Parser)REGISTRY.getByName(name);
   }

   public static Parsers.Parser getById(ClientVersion version, int id) {
      return (Parsers.Parser)REGISTRY.getById(version, id);
   }

   public static List<Parsers.Parser> getParsers() {
      return new ArrayList(REGISTRY.getEntries());
   }

   public static VersionedRegistry<Parsers.Parser> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }

   @FunctionalInterface
   public interface Reader extends Function<PacketWrapper<?>, List<Object>> {
   }

   @FunctionalInterface
   public interface Writer extends BiConsumer<PacketWrapper<?>, List<Object>> {
   }

   public static final class Parser extends AbstractMappedEntity {
      private final Parsers.Reader reader;
      private final Parsers.Writer writer;

      /** @deprecated */
      @Deprecated
      public Parser(String name, @Nullable Function<PacketWrapper<?>, List<Object>> read, @Nullable BiConsumer<PacketWrapper<?>, List<Object>> write) {
         TypesBuilderData var10001 = new TypesBuilderData(new ResourceLocation(name), new int[0]);
         Parsers.Reader var10002;
         if (read == null) {
            var10002 = null;
         } else {
            Objects.requireNonNull(read);
            var10002 = read::apply;
         }

         Parsers.Writer var10003;
         if (write == null) {
            var10003 = null;
         } else {
            Objects.requireNonNull(write);
            var10003 = write::accept;
         }

         this(var10001, var10002, var10003);
      }

      @ApiStatus.Internal
      public Parser(@Nullable TypesBuilderData data, @Nullable Parsers.Reader reader, @Nullable Parsers.Writer writer) {
         super(data);
         this.reader = reader;
         this.writer = writer;
      }

      public Optional<List<Object>> readProperties(PacketWrapper<?> wrapper) {
         return this.reader != null ? Optional.of((List)this.reader.apply(wrapper)) : Optional.empty();
      }

      public void writeProperties(PacketWrapper<?> wrapper, List<Object> properties) {
         if (this.writer != null) {
            this.writer.accept(wrapper, properties);
         }

      }
   }
}
