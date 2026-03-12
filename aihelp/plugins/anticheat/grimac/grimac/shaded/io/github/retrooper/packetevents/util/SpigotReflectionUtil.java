package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.NestedClassUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import com.google.common.collect.BiMap;
import com.google.common.collect.MapMaker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@ApiStatus.Internal
public final class SpigotReflectionUtil {
   private static final String MODIFIED_PACKAGE_NAME;
   public static final String LEGACY_NMS_PACKAGE;
   public static final String OBC_PACKAGE;
   public static ServerVersion VERSION;
   public static boolean V_1_19_OR_HIGHER;
   public static boolean V_1_17_OR_HIGHER;
   public static boolean V_1_12_OR_HIGHER;
   public static Class<?> MINECRAFT_SERVER_CLASS;
   public static Class<?> NMS_PACKET_DATA_SERIALIZER_CLASS;
   public static Class<?> NMS_ITEM_STACK_CLASS;
   public static Class<?> NMS_IMATERIAL_CLASS;
   public static Class<?> NMS_ENTITY_CLASS;
   public static Class<?> ENTITY_PLAYER_CLASS;
   public static Class<?> BOUNDING_BOX_CLASS;
   public static Class<?> NMS_MINECRAFT_KEY_CLASS;
   public static Class<?> ENTITY_HUMAN_CLASS;
   public static Class<?> PLAYER_CONNECTION_CLASS;
   public static Class<?> TRANSFER_COOKIE_CONNECTION_CLASS;
   public static Class<?> SERVER_LOGIN_PACKET_LISTENER_IMPL_CLASS;
   public static Class<?> SERVER_COMMON_PACKETLISTENER_IMPL_CLASS;
   public static Class<?> SERVER_CONNECTION_CLASS;
   public static Class<?> NETWORK_MANAGER_CLASS;
   public static Class<?> NMS_ENUM_PARTICLE_CLASS;
   public static Class<?> MOB_EFFECT_LIST_CLASS;
   public static Class<?> NMS_ITEM_CLASS;
   public static Class<?> DEDICATED_SERVER_CLASS;
   public static Class<?> LEVEL_CLASS;
   public static Class<?> SERVER_LEVEL_CLASS;
   public static Class<?> ENUM_PROTOCOL_DIRECTION_CLASS;
   public static Class<?> GAME_PROFILE_CLASS;
   public static Class<?> CRAFT_WORLD_CLASS;
   public static Class<?> CRAFT_SERVER_CLASS;
   public static Class<?> CRAFT_PLAYER_CLASS;
   public static Class<?> CRAFT_ENTITY_CLASS;
   public static Class<?> CRAFT_ITEM_STACK_CLASS;
   public static Class<?> CRAFT_PARTICLE_CLASS;
   public static Class<?> LEVEL_ENTITY_GETTER_CLASS;
   public static Class<?> ENTITY_ACCESS_CLASS;
   public static Class<?> PERSISTENT_ENTITY_SECTION_MANAGER_CLASS;
   public static Class<?> PAPER_ENTITY_LOOKUP_CLASS;
   public static Class<?> CRAFT_MAGIC_NUMBERS_CLASS;
   public static Class<?> IBLOCK_DATA_CLASS;
   public static Class<?> BLOCK_CLASS;
   public static Class<?> CRAFT_BLOCK_DATA_CLASS;
   public static Class<?> PROPERTY_MAP_CLASS;
   public static Class<?> DIMENSION_MANAGER_CLASS;
   public static Class<?> MOJANG_CODEC_CLASS;
   public static Class<?> MOJANG_ENCODER_CLASS;
   public static Class<?> DATA_RESULT_CLASS;
   public static Class<?> DYNAMIC_OPS_NBT_CLASS;
   public static Class<?> NMS_NBT_COMPOUND_CLASS;
   public static Class<?> NMS_NBT_BASE_CLASS;
   public static Class<?> NBT_COMPRESSION_STREAM_TOOLS_CLASS;
   public static Class<?> STREAM_CODEC;
   public static Class<?> STREAM_DECODER;
   public static Class<?> STREAM_ENCODER;
   public static Class<?> REGISTRY_FRIENDLY_BYTE_BUF;
   public static Class<?> REGISTRY_ACCESS;
   public static Class<?> REGISTRY_ACCESS_FROZEN;
   public static Class<?> RESOURCE_KEY;
   public static Class<?> REGISTRY;
   public static Class<?> WRITABLE_REGISTRY;
   public static Class<?> NBT_ACCOUNTER;
   public static Class<?> CHUNK_PROVIDER_SERVER_CLASS;
   public static Class<?> ICHUNKPROVIDER_CLASS;
   public static Class<?> CHUNK_STATUS_CLASS;
   public static Class<?> BLOCK_POSITION_CLASS;
   public static Class<?> PLAYER_CHUNK_MAP_CLASS;
   public static Class<?> PLAYER_CHUNK_CLASS;
   public static Class<?> CHUNK_CLASS;
   public static Class<?> IBLOCKACCESS_CLASS;
   public static Class<?> ICHUNKACCESS_CLASS;
   public static Class<?> REMOTE_CHAT_SESSION_CLASS;
   public static Class<?> DATA_WATCHER_CLASS;
   public static Class<?> CLIENTBOUND_SET_ENTITY_DATA_PACKET_CLASS;
   public static Class<?> DATA_WATCHER_ITEM_CLASS;
   public static Class<?> DATA_WATCHER_VALUE_CLASS;
   public static Class<?> PAPER_COMMON_CONNECTION_CLASS;
   public static Class<?> CHANNEL_CLASS;
   public static Class<?> BYTE_BUF_CLASS;
   public static Class<?> BYTE_TO_MESSAGE_DECODER;
   public static Class<?> MESSAGE_TO_BYTE_ENCODER;
   public static Field ENTITY_PLAYER_PING_FIELD;
   public static Field ENTITY_BOUNDING_BOX_FIELD;
   public static Field BYTE_BUF_IN_PACKET_DATA_SERIALIZER;
   public static Field DIMENSION_CODEC_FIELD;
   public static Field DYNAMIC_OPS_NBT_INSTANCE_FIELD;
   public static Field CHUNK_PROVIDER_SERVER_FIELD;
   public static Field CRAFT_PARTICLE_PARTICLES_FIELD;
   public static Field NMS_MK_KEY_FIELD;
   public static Field LEGACY_NMS_PARTICLE_KEY_FIELD;
   public static Field LEGACY_NMS_KEY_TO_NMS_PARTICLE;
   public static Field REMOTE_CHAT_SESSION_FIELD;
   public static Field REGISTRY_KEY_LOCATION_FIELD;
   public static Field DATA_WATCHER_FIELD;
   public static Field PAPER_CONNECTION_HANDLE_FIELD;
   public static Field PACKETLISTENER_CONNECTION_FIELD;
   public static Field CONNECTION_CHANNEL_FIELD;
   public static Method IS_DEBUGGING;
   public static Method GET_CRAFT_PLAYER_HANDLE_METHOD;
   public static Method GET_CRAFT_ENTITY_HANDLE_METHOD;
   public static Method GET_CRAFT_WORLD_HANDLE_METHOD;
   public static Method GET_MOB_EFFECT_LIST_ID_METHOD;
   public static Method GET_MOB_EFFECT_LIST_BY_ID_METHOD;
   public static Method GET_ITEM_ID_METHOD;
   public static Method GET_ITEM_BY_ID_METHOD;
   public static Method GET_BUKKIT_ENTITY_METHOD;
   public static Method GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD;
   public static Method GET_ENTITY_BY_ID_LEVEL_ENTITY_GETTER_METHOD;
   public static Method GET_ENTITY_BY_ID_METHOD;
   public static Method CRAFT_ITEM_STACK_AS_BUKKIT_COPY;
   public static Method CRAFT_ITEM_STACK_AS_NMS_COPY;
   public static Method BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE;
   public static Method NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE;
   public static Method READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD;
   public static Method WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD;
   public static Method GET_COMBINED_ID;
   public static Method GET_BY_COMBINED_ID;
   public static Method GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA;
   public static Method PROPERTY_MAP_GET_METHOD;
   public static Method GET_DIMENSION_MANAGER;
   public static Method GET_DIMENSION_ID;
   public static Method GET_DIMENSION_KEY;
   public static Method CODEC_ENCODE_METHOD;
   public static Method DATA_RESULT_GET_METHOD;
   public static Method READ_NBT_FROM_STREAM_METHOD;
   public static Method WRITE_NBT_TO_STREAM_METHOD;
   public static Method STREAM_DECODER_DECODE;
   public static Method STREAM_ENCODER_ENCODE;
   public static Method CREATE_REGISTRY_RESOURCE_KEY;
   public static Method GET_REGISTRY_OR_THROW;
   public static Method GET_DIMENSION_TYPES;
   public static Method GET_REGISTRY_ID;
   public static Method NBT_ACCOUNTER_UNLIMITED_HEAP;
   public static Method CHUNK_CACHE_GET_IBLOCKACCESS;
   public static Method CHUNK_CACHE_GET_ICHUNKACCESS;
   public static Method IBLOCKACCESS_GET_BLOCK_DATA;
   public static Method CHUNK_GET_BLOCK_DATA;
   public static Method PLAYER_CHUNK_MAP_GET_PLAYER_CHUNK;
   public static Method PLAYER_CHUNK_GET_CHUNK;
   public static Method LEGACY_DATA_WATCHER_WRITE_METHOD;
   public static Method CLIENTBOUND_SET_ENTITY_DATA_PACKET_WRITE_DATA_WATCHER_METHOD;
   public static Method GET_DATA_VALUE_FROM_DATA_ITEM_METHOD;
   public static Method GET_TPS;
   private static Constructor<?> NMS_ITEM_STACK_CONSTRUCTOR;
   private static Constructor<?> NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR;
   private static Constructor<?> NMS_MINECRAFT_KEY_CONSTRUCTOR;
   private static Constructor<?> REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR;
   private static Constructor<?> BLOCK_POSITION_CONSTRUCTOR;
   private static Object MINECRAFT_SERVER_INSTANCE;
   private static Object MINECRAFT_SERVER_CONNECTION_INSTANCE;
   private static Object MINECRAFT_SERVER_REGISTRY_ACCESS;
   private static Object ITEM_STACK_OPTIONAL_STREAM_CODEC;
   private static Object DIMENSION_TYPE_REGISTRY_KEY;
   private static boolean PAPER_ENTITY_LOOKUP_EXISTS;
   private static boolean PAPER_ENTITY_LOOKUP_LEGACY;
   private static boolean IS_OBFUSCATED;
   public static Map<Integer, Entity> ENTITY_ID_CACHE;

   private static void initConstructors() {
      Class itemClass = NMS_IMATERIAL_CLASS != null ? NMS_IMATERIAL_CLASS : NMS_ITEM_CLASS;

      try {
         NMS_ITEM_STACK_CONSTRUCTOR = NMS_ITEM_STACK_CLASS.getConstructor(itemClass, Integer.TYPE);
         NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR = NMS_PACKET_DATA_SERIALIZER_CLASS.getConstructor(BYTE_BUF_CLASS);
         if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            NMS_MINECRAFT_KEY_CONSTRUCTOR = NMS_MINECRAFT_KEY_CLASS.getDeclaredConstructor(String.class, String.class);
            NMS_MINECRAFT_KEY_CONSTRUCTOR.setAccessible(true);
         }

         if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR = REGISTRY_FRIENDLY_BYTE_BUF.getConstructor(BYTE_BUF_CLASS, REGISTRY_ACCESS);
         }

         if (BLOCK_POSITION_CLASS != null) {
            BLOCK_POSITION_CONSTRUCTOR = BLOCK_POSITION_CLASS.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
         }
      } catch (NoSuchMethodException var2) {
         var2.printStackTrace();
      }

   }

   private static void initMethods() {
      IS_DEBUGGING = Reflection.getMethod(MINECRAFT_SERVER_CLASS, (String)"isDebugging", 0);
      GET_BUKKIT_ENTITY_METHOD = Reflection.getMethod(NMS_ENTITY_CLASS, (Class)CRAFT_ENTITY_CLASS, 0);
      GET_CRAFT_PLAYER_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, (String)"getHandle", 0);
      GET_CRAFT_ENTITY_HANDLE_METHOD = Reflection.getMethod(CRAFT_ENTITY_CLASS, (String)"getHandle", 0);
      GET_CRAFT_WORLD_HANDLE_METHOD = Reflection.getMethod(CRAFT_WORLD_CLASS, (String)"getHandle", 0);
      GET_MOB_EFFECT_LIST_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, (String)(V_1_19_OR_HIGHER ? "g" : "getId"), 0);
      GET_MOB_EFFECT_LIST_BY_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, (String)(V_1_19_OR_HIGHER ? "a" : "fromId"), 0);
      GET_ITEM_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, (String)(V_1_19_OR_HIGHER ? "g" : "getId"), 0);
      GET_ITEM_BY_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, (Class)NMS_ITEM_CLASS, 0);
      if (V_1_17_OR_HIGHER) {
         GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD = Reflection.getMethod(LEVEL_ENTITY_GETTER_CLASS, (Class)Iterable.class, 0);
         GET_ENTITY_BY_ID_LEVEL_ENTITY_GETTER_METHOD = Reflection.getMethod(LEVEL_ENTITY_GETTER_CLASS, ENTITY_ACCESS_CLASS, 0, Integer.TYPE);
      }

      if (DIMENSION_MANAGER_CLASS != null) {
         GET_DIMENSION_KEY = Reflection.getMethod(LEVEL_CLASS, (String)"getTypeKey", 0);
         GET_DIMENSION_MANAGER = Reflection.getMethod(LEVEL_CLASS, (Class)DIMENSION_MANAGER_CLASS, 0);
         GET_DIMENSION_ID = Reflection.getMethod(DIMENSION_MANAGER_CLASS, (Class)Integer.TYPE, 0);
      }

      CODEC_ENCODE_METHOD = Reflection.getMethod(MOJANG_ENCODER_CLASS, (String)"encodeStart", 0);
      DATA_RESULT_GET_METHOD = Reflection.getMethod(DATA_RESULT_CLASS, (String)"result", 0);
      String entityIdMethodName = VERSION.isOlderThan(ServerVersion.V_1_9) ? "a" : (VERSION.isOlderThan(ServerVersion.V_1_17) ? "getEntity" : "b");
      GET_ENTITY_BY_ID_METHOD = Reflection.getMethodExact(SERVER_LEVEL_CLASS, entityIdMethodName, NMS_ENTITY_CLASS, Integer.TYPE);
      if (GET_ENTITY_BY_ID_METHOD == null) {
         GET_ENTITY_BY_ID_METHOD = Reflection.getMethodExact(SERVER_LEVEL_CLASS, "getEntity", NMS_ENTITY_CLASS, Integer.TYPE);
      }

      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE = Reflection.getMethod(CRAFT_PARTICLE_CLASS, "toNMS", NMS_ENUM_PARTICLE_CLASS);
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            Class<?> particleClass = Reflection.getClassByNameWithoutException("org.bukkit.Particle");
            NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE = Reflection.getMethod(CRAFT_PARTICLE_CLASS, "toBukkit", particleClass);
         }
      }

      CRAFT_ITEM_STACK_AS_BUKKIT_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, (String)"asBukkitCopy", 0);
      CRAFT_ITEM_STACK_AS_NMS_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", ItemStack.class);
      READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethodExact(NMS_PACKET_DATA_SERIALIZER_CLASS, "k", NMS_ITEM_STACK_CLASS);
      if (READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD == null) {
         READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, (Class)NMS_ITEM_STACK_CLASS, 0);
      }

      WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethodExact(NMS_PACKET_DATA_SERIALIZER_CLASS, "a", NMS_PACKET_DATA_SERIALIZER_CLASS, NMS_ITEM_STACK_CLASS);
      if (WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD == null) {
         WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, 0, NMS_ITEM_STACK_CLASS);
      }

      GET_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, Integer.TYPE, 0, IBLOCK_DATA_CLASS);
      GET_BY_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, IBLOCK_DATA_CLASS, 0, Integer.TYPE);
      if (CRAFT_BLOCK_DATA_CLASS != null) {
         GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA = Reflection.getMethodExact(CRAFT_BLOCK_DATA_CLASS, "fromData", CRAFT_BLOCK_DATA_CLASS, IBLOCK_DATA_CLASS);
      }

      READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInputStream.class);
      if (READ_NBT_FROM_STREAM_METHOD == null) {
         if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInput.class, NBT_ACCOUNTER);
         } else {
            READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInput.class);
         }
      }

      if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_2) && VERSION.isOlderThan(ServerVersion.V_1_20_5)) {
         WRITE_NBT_TO_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, "a", NMS_NBT_BASE_CLASS, DataOutput.class);
      } else {
         WRITE_NBT_TO_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_2) ? NMS_NBT_BASE_CLASS : NMS_NBT_COMPOUND_CLASS, DataOutput.class);
      }

      if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         NBT_ACCOUNTER_UNLIMITED_HEAP = Reflection.getMethod(NBT_ACCOUNTER, (Class)NBT_ACCOUNTER, 0);
      }

      if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         STREAM_DECODER_DECODE = STREAM_DECODER.getMethods()[0];
         STREAM_ENCODER_ENCODE = STREAM_ENCODER.getMethods()[0];
      }

      CREATE_REGISTRY_RESOURCE_KEY = Reflection.getMethod(RESOURCE_KEY, 0, NMS_MINECRAFT_KEY_CLASS);
      GET_REGISTRY_OR_THROW = Reflection.getMethod(REGISTRY_ACCESS, VERSION.isNewerThanOrEquals(ServerVersion.V_1_17) ? REGISTRY : WRITABLE_REGISTRY, 0, RESOURCE_KEY);
      GET_DIMENSION_TYPES = Reflection.getMethod(REGISTRY_ACCESS_FROZEN, (Class)REGISTRY, 0);
      GET_REGISTRY_ID = Reflection.getMethod(REGISTRY, Integer.TYPE, 0, Object.class);
      if (IBLOCKACCESS_CLASS != null) {
         CHUNK_CACHE_GET_IBLOCKACCESS = Reflection.getMethod(CHUNK_PROVIDER_SERVER_CLASS, IBLOCKACCESS_CLASS, 0, Integer.TYPE, Integer.TYPE);
         IBLOCKACCESS_GET_BLOCK_DATA = Reflection.getMethod(IBLOCKACCESS_CLASS, (Class)IBLOCK_DATA_CLASS, 0);
      }

      if (ICHUNKACCESS_CLASS != null) {
         CHUNK_CACHE_GET_ICHUNKACCESS = Reflection.getMethod(CHUNK_PROVIDER_SERVER_CLASS, ICHUNKACCESS_CLASS, 0, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
      }

      if (IBLOCK_DATA_CLASS != null) {
         CHUNK_GET_BLOCK_DATA = Reflection.getMethod(CHUNK_CLASS, IBLOCK_DATA_CLASS, 0, BLOCK_POSITION_CLASS);
      }

      if (PLAYER_CHUNK_CLASS != null) {
         PLAYER_CHUNK_MAP_GET_PLAYER_CHUNK = Reflection.getMethod(PLAYER_CHUNK_MAP_CLASS, PLAYER_CHUNK_CLASS, 0, Long.TYPE);
      }

      if (CHUNK_CLASS != null) {
         PLAYER_CHUNK_GET_CHUNK = Reflection.getMethod(PLAYER_CHUNK_CLASS, (Class)CHUNK_CLASS, 0);
      }

      LEGACY_DATA_WATCHER_WRITE_METHOD = Reflection.getMethod(DATA_WATCHER_CLASS, Void.TYPE, 0, NMS_PACKET_DATA_SERIALIZER_CLASS);
      CLIENTBOUND_SET_ENTITY_DATA_PACKET_WRITE_DATA_WATCHER_METHOD = Reflection.getMethod(CLIENTBOUND_SET_ENTITY_DATA_PACKET_CLASS, 0, List.class, REGISTRY_FRIENDLY_BYTE_BUF);
      GET_DATA_VALUE_FROM_DATA_ITEM_METHOD = Reflection.getMethod(DATA_WATCHER_ITEM_CLASS, (Class)DATA_WATCHER_VALUE_CLASS, 0);
      GET_TPS = Reflection.getMethod(Server.class, "getTPS");
   }

   private static void initFields() {
      ENTITY_BOUNDING_BOX_FIELD = Reflection.getField(NMS_ENTITY_CLASS, BOUNDING_BOX_CLASS, 0, true);
      ENTITY_PLAYER_PING_FIELD = Reflection.getField(ENTITY_PLAYER_CLASS, "ping");
      BYTE_BUF_IN_PACKET_DATA_SERIALIZER = Reflection.getField(NMS_PACKET_DATA_SERIALIZER_CLASS, BYTE_BUF_CLASS, 0, true);
      CRAFT_PARTICLE_PARTICLES_FIELD = Reflection.getField(CRAFT_PARTICLE_CLASS, "particles");
      NMS_MK_KEY_FIELD = Reflection.getField(NMS_MINECRAFT_KEY_CLASS, "key");
      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         LEGACY_NMS_PARTICLE_KEY_FIELD = Reflection.getField(NMS_ENUM_PARTICLE_CLASS, "X");
         LEGACY_NMS_KEY_TO_NMS_PARTICLE = Reflection.getField(NMS_ENUM_PARTICLE_CLASS, "ac");
      }

      DIMENSION_CODEC_FIELD = Reflection.getField(DIMENSION_MANAGER_CLASS, MOJANG_CODEC_CLASS, 0);
      DYNAMIC_OPS_NBT_INSTANCE_FIELD = Reflection.getField(DYNAMIC_OPS_NBT_CLASS, DYNAMIC_OPS_NBT_CLASS, 0);
      CHUNK_PROVIDER_SERVER_FIELD = Reflection.getField(SERVER_LEVEL_CLASS, CHUNK_PROVIDER_SERVER_CLASS, 0);
      if (CHUNK_PROVIDER_SERVER_FIELD == null) {
         CHUNK_PROVIDER_SERVER_FIELD = Reflection.getField(SERVER_LEVEL_CLASS, ICHUNKPROVIDER_CLASS, 0);
      }

      PAPER_ENTITY_LOOKUP_EXISTS = Reflection.getField(SERVER_LEVEL_CLASS, PAPER_ENTITY_LOOKUP_CLASS, 0) != null;
      if (PAPER_ENTITY_LOOKUP_EXISTS) {
         PAPER_ENTITY_LOOKUP_LEGACY = Reflection.getField(LEVEL_CLASS, PAPER_ENTITY_LOOKUP_CLASS, 0) == null;
      }

      REMOTE_CHAT_SESSION_FIELD = Reflection.getField(ENTITY_PLAYER_CLASS, REMOTE_CHAT_SESSION_CLASS, 0);
      REGISTRY_KEY_LOCATION_FIELD = Reflection.getField(RESOURCE_KEY, NMS_MINECRAFT_KEY_CLASS, 1);
      DATA_WATCHER_FIELD = Reflection.getField(NMS_ENTITY_CLASS, DATA_WATCHER_CLASS, 0, true);
      PAPER_CONNECTION_HANDLE_FIELD = Reflection.getField(PAPER_COMMON_CONNECTION_CLASS, SERVER_COMMON_PACKETLISTENER_IMPL_CLASS, 0);
      PACKETLISTENER_CONNECTION_FIELD = Reflection.getField(SERVER_COMMON_PACKETLISTENER_IMPL_CLASS, NETWORK_MANAGER_CLASS, 0);
      CONNECTION_CHANNEL_FIELD = Reflection.getField(NETWORK_MANAGER_CLASS, CHANNEL_CLASS, 0);
   }

   private static void initClasses() {
      IS_OBFUSCATED = Reflection.getClassByNameWithoutException("net.minecraft.server.network.PlayerConnection") != null;
      MINECRAFT_SERVER_CLASS = getServerClass("server.MinecraftServer", "MinecraftServer");
      NMS_PACKET_DATA_SERIALIZER_CLASS = getServerClass(IS_OBFUSCATED ? "network.PacketDataSerializer" : "network.FriendlyByteBuf", "PacketDataSerializer");
      NMS_ITEM_STACK_CLASS = getServerClass("world.item.ItemStack", "ItemStack");
      NMS_IMATERIAL_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.IMaterial" : "world.level.ItemLike", "IMaterial");
      NMS_ENTITY_CLASS = getServerClass("world.entity.Entity", "Entity");
      ENTITY_PLAYER_CLASS = getServerClass(IS_OBFUSCATED ? "server.level.EntityPlayer" : "server.level.ServerPlayer", "EntityPlayer");
      BOUNDING_BOX_CLASS = getServerClass(IS_OBFUSCATED ? "world.phys.AxisAlignedBB" : "world.phys.AABB", "AxisAlignedBB");
      String identifierCn = VERSION.isNewerThanOrEquals(ServerVersion.V_1_21_11) ? "resources.Identifier" : "resources.ResourceLocation";
      NMS_MINECRAFT_KEY_CLASS = getServerClass(IS_OBFUSCATED ? "resources.MinecraftKey" : identifierCn, "MinecraftKey");
      ENTITY_HUMAN_CLASS = getServerClass(IS_OBFUSCATED ? "world.entity.player.EntityHuman" : "world.entity.player.Player", "EntityHuman");
      PLAYER_CONNECTION_CLASS = getServerClass(IS_OBFUSCATED ? "server.network.PlayerConnection" : "server.network.ServerGamePacketListenerImpl", "PlayerConnection");
      SERVER_LOGIN_PACKET_LISTENER_IMPL_CLASS = getServerClass(IS_OBFUSCATED ? "server.network.LoginListener" : "server.network.ServerLoginPacketListenerImpl", "LoginListener");
      SERVER_COMMON_PACKETLISTENER_IMPL_CLASS = getServerClass("server.network.ServerCommonPacketListenerImpl", "ServerCommonPacketListenerImpl");
      TRANSFER_COOKIE_CONNECTION_CLASS = getOBCClass("entity.CraftPlayer$TransferCookieConnection");
      PAPER_COMMON_CONNECTION_CLASS = Reflection.getClassByNameWithoutException("io.papermc.paper.connection.PaperCommonConnection");
      SERVER_CONNECTION_CLASS = getServerClass(IS_OBFUSCATED ? "server.network.ServerConnection" : "server.network.ServerConnectionListener", "ServerConnection");
      NETWORK_MANAGER_CLASS = getServerClass(IS_OBFUSCATED ? "network.NetworkManager" : "network.Connection", "NetworkManager");
      MOB_EFFECT_LIST_CLASS = getServerClass(IS_OBFUSCATED ? "world.effect.MobEffectList" : "world.effect.MobEffect", "MobEffectList");
      NMS_ITEM_CLASS = getServerClass("world.item.Item", "Item");
      DEDICATED_SERVER_CLASS = getServerClass("server.dedicated.DedicatedServer", "DedicatedServer");
      LEVEL_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.World" : "world.level.Level", "World");
      SERVER_LEVEL_CLASS = getServerClass(IS_OBFUSCATED ? "server.level.WorldServer" : "server.level.ServerLevel", "WorldServer");
      ENUM_PROTOCOL_DIRECTION_CLASS = getServerClass(IS_OBFUSCATED ? "network.protocol.EnumProtocolDirection" : "network.protocol.PacketFlow", "EnumProtocolDirection");
      if (V_1_17_OR_HIGHER) {
         LEVEL_ENTITY_GETTER_CLASS = getServerClass("world.level.entity.LevelEntityGetter", "");
         PERSISTENT_ENTITY_SECTION_MANAGER_CLASS = getServerClass("world.level.entity.PersistentEntitySectionManager", "");
         PAPER_ENTITY_LOOKUP_CLASS = Reflection.getClassByNameWithoutException("ca.spottedleaf.moonrise.patches.chunk_system.level.entity.EntityLookup");
         if (PAPER_ENTITY_LOOKUP_CLASS == null) {
            PAPER_ENTITY_LOOKUP_CLASS = Reflection.getClassByNameWithoutException("io.papermc.paper.chunk.system.entity.EntityLookup");
         }

         ENTITY_ACCESS_CLASS = getServerClass("world.level.entity.EntityAccess", "");
      }

      DIMENSION_MANAGER_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.dimension.DimensionManager" : "world.level.dimension.DimensionType", "DimensionManager");
      MOJANG_CODEC_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.Codec");
      MOJANG_ENCODER_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.Encoder");
      DATA_RESULT_CLASS = Reflection.getClassByNameWithoutException("com.mojang.serialization.DataResult");
      DYNAMIC_OPS_NBT_CLASS = getServerClass(IS_OBFUSCATED ? "nbt.DynamicOpsNBT" : "nbt.NbtOps", "DynamicOpsNBT");
      if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         NMS_ENUM_PARTICLE_CLASS = getServerClass((String)null, "EnumParticle");
      }

      CRAFT_MAGIC_NUMBERS_CLASS = getOBCClass("util.CraftMagicNumbers");
      IBLOCK_DATA_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.block.state.IBlockData" : "world.level.block.state.BlockState", "IBlockData");
      BLOCK_CLASS = getServerClass("world.level.block.Block", "Block");
      CRAFT_BLOCK_DATA_CLASS = getOBCClass("block.data.CraftBlockData");
      GAME_PROFILE_CLASS = Reflection.getClassByNameWithoutException("com.mojang.authlib.GameProfile");
      CRAFT_WORLD_CLASS = getOBCClass("CraftWorld");
      CRAFT_PLAYER_CLASS = getOBCClass("entity.CraftPlayer");
      CRAFT_SERVER_CLASS = getOBCClass("CraftServer");
      CRAFT_ENTITY_CLASS = getOBCClass("entity.CraftEntity");
      CRAFT_ITEM_STACK_CLASS = getOBCClass("inventory.CraftItemStack");
      CRAFT_PARTICLE_CLASS = getOBCClass("CraftParticle");
      CHANNEL_CLASS = getNettyClass("channel.Channel");
      BYTE_BUF_CLASS = getNettyClass("buffer.ByteBuf");
      BYTE_TO_MESSAGE_DECODER = getNettyClass("handler.codec.ByteToMessageDecoder");
      MESSAGE_TO_BYTE_ENCODER = getNettyClass("handler.codec.MessageToByteEncoder");
      NMS_NBT_COMPOUND_CLASS = getServerClass(IS_OBFUSCATED ? "nbt.NBTTagCompound" : "nbt.CompoundTag", "NBTTagCompound");
      NMS_NBT_BASE_CLASS = getServerClass(IS_OBFUSCATED ? "nbt.NBTBase" : "nbt.Tag", "NBTBase");
      NBT_COMPRESSION_STREAM_TOOLS_CLASS = getServerClass(IS_OBFUSCATED ? "nbt.NBTCompressedStreamTools" : "nbt.NbtIo", "NBTCompressedStreamTools");
      NBT_ACCOUNTER = getServerClass(IS_OBFUSCATED ? "nbt.NBTReadLimiter" : "nbt.NbtAccounter", "NBTReadLimiter");
      CHUNK_PROVIDER_SERVER_CLASS = getServerClass(IS_OBFUSCATED ? "server.level.ChunkProviderServer" : "server.level.ServerChunkCache", "ChunkProviderServer");
      ICHUNKPROVIDER_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.chunk.IChunkProvider" : "world.level.chunk.ChunkSource", "IChunkProvider");
      CHUNK_STATUS_CLASS = getServerClass("world.level.chunk.status.ChunkStatus", "");
      if (CHUNK_STATUS_CLASS == null) {
         CHUNK_STATUS_CLASS = getServerClass("world.level.ChunkStatus", "");
      }

      BLOCK_POSITION_CLASS = getServerClass(IS_OBFUSCATED ? "core.BlockPosition" : "core.BlockPos", "BlockPosition");
      PLAYER_CHUNK_MAP_CLASS = getServerClass(IS_OBFUSCATED ? "server.level.PlayerChunkMap" : "server.level.ChunkMap", "");
      PLAYER_CHUNK_CLASS = getServerClass(IS_OBFUSCATED ? "server.level.PlayerChunk" : "server.level.ChunkHolder", "");
      CHUNK_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.chunk.Chunk" : "world.level.chunk.LevelChunk", "Chunk");
      IBLOCKACCESS_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.IBlockAccess" : "world.level.BlockGetter", "IBlockAccess");
      ICHUNKACCESS_CLASS = getServerClass(IS_OBFUSCATED ? "world.level.chunk.IChunkAccess" : "world.level.chunk.ChunkAccess", "IChunkAccess");
      if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         STREAM_CODEC = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamCodec");
         STREAM_DECODER = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamDecoder");
         STREAM_ENCODER = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamEncoder");
         REGISTRY_FRIENDLY_BYTE_BUF = Reflection.getClassByNameWithoutException("net.minecraft.network.RegistryFriendlyByteBuf");
      }

      REGISTRY_ACCESS = getServerClass(IS_OBFUSCATED ? "core.IRegistryCustom" : "core.RegistryAccess", "IRegistryCustom");
      REGISTRY_ACCESS_FROZEN = getServerClass(IS_OBFUSCATED ? "core.IRegistryCustom$Dimension" : "core.RegistryAccess$Frozen", "IRegistryCustom$Dimension");
      RESOURCE_KEY = getServerClass("resources.ResourceKey", "ResourceKey");
      REGISTRY = getServerClass(IS_OBFUSCATED ? "core.IRegistry" : "core.Registry", "IRegistry");
      WRITABLE_REGISTRY = getServerClass(IS_OBFUSCATED ? "core.IRegistryWritable" : "core.WritableRegistry", "IRegistryWritable");
      REMOTE_CHAT_SESSION_CLASS = Reflection.getClassByNameWithoutException("net.minecraft.network.chat.RemoteChatSession");
      DATA_WATCHER_CLASS = getServerClass("network.syncher.DataWatcher", "DataWatcher");
      if (DATA_WATCHER_CLASS == null) {
         DATA_WATCHER_CLASS = getServerClass("network.syncher.SynchedEntityData", "DataWatcher");
      }

      CLIENTBOUND_SET_ENTITY_DATA_PACKET_CLASS = getServerClass("network.protocol.game.ClientboundSetEntityDataPacket", "PacketPlayOutEntityMetadata");
      DATA_WATCHER_ITEM_CLASS = NestedClassUtil.getNestedClass(DATA_WATCHER_CLASS, 0);
      DATA_WATCHER_VALUE_CLASS = NestedClassUtil.getNestedClass(DATA_WATCHER_CLASS, 1);
   }

   private static void initObjects() {
      try {
         if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            ITEM_STACK_OPTIONAL_STREAM_CODEC = Reflection.getField(NMS_ITEM_STACK_CLASS, STREAM_CODEC, 0).get((Object)null);
         }
      } catch (IllegalAccessException var1) {
         var1.printStackTrace();
      }

   }

   public static void init() {
      VERSION = PacketEvents.getAPI().getServerManager().getVersion();
      V_1_19_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_19);
      V_1_17_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_17);
      V_1_12_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_12);
      initClasses();
      initFields();
      initMethods();
      initConstructors();
      initObjects();
   }

   @Nullable
   public static Class<?> getServerClass(String modern, String legacy) {
      return V_1_17_OR_HIGHER ? Reflection.getClassByNameWithoutException("net.minecraft." + modern) : Reflection.getClassByNameWithoutException(LEGACY_NMS_PACKAGE + legacy);
   }

   public static boolean isMinecraftServerInstanceDebugging() {
      Object minecraftServerInstance = getMinecraftServerInstance(Bukkit.getServer());
      if (minecraftServerInstance != null && IS_DEBUGGING != null) {
         try {
            return (Boolean)IS_DEBUGGING.invoke(minecraftServerInstance);
         } catch (InvocationTargetException | IllegalAccessException var2) {
            IS_DEBUGGING = null;
            return false;
         }
      } else {
         return false;
      }
   }

   public static Object getMinecraftServerInstance(Server server) {
      if (MINECRAFT_SERVER_INSTANCE == null) {
         try {
            Field f = Reflection.getField(CRAFT_SERVER_CLASS, MINECRAFT_SERVER_CLASS, 0);
            if (f == null) {
               MINECRAFT_SERVER_INSTANCE = Reflection.getField(MINECRAFT_SERVER_CLASS, MINECRAFT_SERVER_CLASS, 0).get((Object)null);
            } else {
               MINECRAFT_SERVER_INSTANCE = f.get(server);
            }
         } catch (IllegalAccessException var2) {
            var2.printStackTrace();
         }
      }

      return MINECRAFT_SERVER_INSTANCE;
   }

   public static Object getMinecraftServerConnectionInstance() {
      if (MINECRAFT_SERVER_CONNECTION_INSTANCE == null) {
         try {
            MINECRAFT_SERVER_CONNECTION_INSTANCE = Reflection.getField(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
         } catch (IllegalAccessException var1) {
            var1.printStackTrace();
         }
      }

      return MINECRAFT_SERVER_CONNECTION_INSTANCE;
   }

   public static double getTPS() {
      return recentTPS()[0];
   }

   public static double[] recentTPS() {
      if (GET_TPS != null) {
         try {
            return (double[])GET_TPS.invoke(Bukkit.getServer());
         } catch (InvocationTargetException | IllegalAccessException var1) {
            throw new RuntimeException(var1);
         }
      } else {
         return (new ReflectionObject(getMinecraftServerInstance(Bukkit.getServer()), MINECRAFT_SERVER_CLASS)).readDoubleArray(0);
      }
   }

   public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
      return Class.forName(LEGACY_NMS_PACKAGE + name);
   }

   public static Class<?> getOBCClass(String name) {
      return Reflection.getClassByNameWithoutException(OBC_PACKAGE + name);
   }

   public static Class<?> getNettyClass(String name) {
      return Reflection.getClassByNameWithoutException("io.netty." + name);
   }

   public static Entity getBukkitEntity(Object nmsEntity) {
      Object craftEntity = null;

      try {
         craftEntity = GET_BUKKIT_ENTITY_METHOD.invoke(nmsEntity);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
      }

      return (Entity)craftEntity;
   }

   public static Object getNMSEntity(final Entity entity) {
      Object craftEntity = CRAFT_ENTITY_CLASS.cast(entity);

      try {
         return GET_CRAFT_ENTITY_HANDLE_METHOD.invoke(craftEntity);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object getNMSAxisAlignedBoundingBox(Object nmsEntity) {
      try {
         return ENTITY_BOUNDING_BOX_FIELD.get(NMS_ENTITY_CLASS.cast(nmsEntity));
      } catch (IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object getCraftPlayer(Player player) {
      return CRAFT_PLAYER_CLASS.cast(player);
   }

   public static Object getEntityPlayer(Player player) {
      Object craftPlayer = getCraftPlayer(player);

      try {
         return GET_CRAFT_PLAYER_HANDLE_METHOD.invoke(craftPlayer);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object getPlayerConnection(Player player) {
      Object entityPlayer = getEntityPlayer(player);
      if (entityPlayer == null) {
         return null;
      } else {
         ReflectionObject wrappedEntityPlayer = new ReflectionObject(entityPlayer, ENTITY_PLAYER_CLASS);
         return TRANSFER_COOKIE_CONNECTION_CLASS != null ? wrappedEntityPlayer.readObject(0, TRANSFER_COOKIE_CONNECTION_CLASS) : wrappedEntityPlayer.readObject(0, PLAYER_CONNECTION_CLASS);
      }
   }

   public static Object getGameProfile(Player player) {
      Object entityPlayer = getEntityPlayer(player);
      ReflectionObject entityHumanWrapper = new ReflectionObject(entityPlayer, ENTITY_HUMAN_CLASS);
      return entityHumanWrapper.readObject(0, GAME_PROFILE_CLASS);
   }

   public static List<TextureProperty> getUserProfile(Player player) {
      if (PROPERTY_MAP_CLASS == null) {
         PROPERTY_MAP_CLASS = Reflection.getClassByNameWithoutException("com.mojang.authlib.properties.PropertyMap");
         PROPERTY_MAP_GET_METHOD = Reflection.getMethodExact(PROPERTY_MAP_CLASS, "get", Collection.class, Object.class);
      }

      Object nmsGameProfile = getGameProfile(player);
      ReflectionObject reflectGameProfile = new ReflectionObject(nmsGameProfile);
      Object nmsPropertyMap = reflectGameProfile.readObject(0, PROPERTY_MAP_CLASS);
      Collection nmsProperties = null;

      try {
         nmsProperties = (Collection)PROPERTY_MAP_GET_METHOD.invoke(nmsPropertyMap, "textures");
      } catch (InvocationTargetException | IllegalAccessException var13) {
         var13.printStackTrace();
      }

      List<TextureProperty> properties = new ArrayList();
      Iterator var6 = nmsProperties.iterator();

      while(var6.hasNext()) {
         Object nmsProperty = var6.next();
         ReflectionObject reflectProperty = new ReflectionObject(nmsProperty);
         String name = "textures";
         String value = reflectProperty.readString(1);
         String signature = reflectProperty.readString(2);
         TextureProperty textureProperty = new TextureProperty(name, value, signature);
         properties.add(textureProperty);
      }

      return properties;
   }

   public static Object getNetworkManager(Player player) {
      Object playerConnection = getPlayerConnection(player);
      if (playerConnection == null) {
         return null;
      } else {
         Class playerConnectionClass;
         if (SERVER_COMMON_PACKETLISTENER_IMPL_CLASS != null) {
            playerConnectionClass = playerConnection.getClass() == SERVER_LOGIN_PACKET_LISTENER_IMPL_CLASS ? SERVER_LOGIN_PACKET_LISTENER_IMPL_CLASS : SERVER_COMMON_PACKETLISTENER_IMPL_CLASS;
         } else {
            playerConnectionClass = PLAYER_CONNECTION_CLASS;
         }

         ReflectionObject wrapper = new ReflectionObject(playerConnection, playerConnectionClass);

         try {
            return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
         } catch (Exception var7) {
            try {
               playerConnection = wrapper.read(0, PLAYER_CONNECTION_CLASS);
               wrapper = new ReflectionObject(playerConnection, PLAYER_CONNECTION_CLASS);
               return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
            } catch (Exception var6) {
               var7.printStackTrace();
               return null;
            }
         }
      }
   }

   @Nullable
   public static Object getChannel(Player player) {
      Object networkManager = getNetworkManager(player);
      if (networkManager == null) {
         return null;
      } else {
         ReflectionObject wrapper = new ReflectionObject(networkManager, NETWORK_MANAGER_CLASS);
         return wrapper.readObject(0, CHANNEL_CLASS);
      }
   }

   public static Object getChannelFromPaperConnection(Object paperConnection) {
      try {
         Object packetlistener = PAPER_CONNECTION_HANDLE_FIELD.get(paperConnection);
         Object connection = PACKETLISTENER_CONNECTION_FIELD.get(packetlistener);
         return CONNECTION_CHANNEL_FIELD.get(connection);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   /** @deprecated */
   @Deprecated
   public static int getPlayerPingLegacy(Player player) {
      if (V_1_17_OR_HIGHER) {
         return -1;
      } else {
         if (ENTITY_PLAYER_PING_FIELD != null) {
            Object entityPlayer = getEntityPlayer(player);

            try {
               return ENTITY_PLAYER_PING_FIELD.getInt(entityPlayer);
            } catch (IllegalAccessException var3) {
               var3.printStackTrace();
            }
         }

         return -1;
      }
   }

   public static List<Object> getNetworkManagers() {
      ReflectionObject serverConnectionWrapper = new ReflectionObject(getMinecraftServerConnectionInstance());
      int i = 0;

      while(true) {
         try {
            List<?> list = (List)serverConnectionWrapper.readObject(i, List.class);
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               Object obj = var3.next();
               if (obj.getClass().isAssignableFrom(NETWORK_MANAGER_CLASS)) {
                  return list;
               }
            }
         } catch (Exception var5) {
            return (List)serverConnectionWrapper.readObject(1, List.class);
         }

         ++i;
      }
   }

   public static Object convertBukkitServerToNMSServer(Server server) {
      Object craftServer = CRAFT_SERVER_CLASS.cast(server);
      ReflectionObject wrapper = new ReflectionObject(craftServer);

      try {
         return wrapper.readObject(0, MINECRAFT_SERVER_CLASS);
      } catch (Exception var4) {
         wrapper.readObject(0, DEDICATED_SERVER_CLASS);
         return null;
      }
   }

   public static Object convertBukkitWorldToWorldServer(World world) {
      Object craftWorld = CRAFT_WORLD_CLASS.cast(world);

      try {
         return GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object convertWorldServerDimensionToNMSNbt(Object worldServer) {
      try {
         Object dimensionType = GET_DIMENSION_MANAGER.invoke(worldServer);
         Object dimensionTypeCodec = DIMENSION_CODEC_FIELD.get((Object)null);
         Object nbtOps = DYNAMIC_OPS_NBT_INSTANCE_FIELD.get((Object)null);
         if (VERSION.isOlderThan(ServerVersion.V_1_16_2)) {
            dimensionType = () -> {
               return dimensionType;
            };
         }

         Object encodedDimType = CODEC_ENCODE_METHOD.invoke(dimensionTypeCodec, nbtOps, dimensionType);
         Optional<?> optionalDimType = (Optional)DATA_RESULT_GET_METHOD.invoke(encodedDimType);
         return optionalDimType.orElse((Object)null);
      } catch (InvocationTargetException | IllegalAccessException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public static int getDimensionId(Object worldServer) {
      try {
         Object dimensionType = GET_DIMENSION_MANAGER.invoke(worldServer);
         Object dimensionTypeRegistry;
         if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            if (DIMENSION_TYPE_REGISTRY_KEY == null) {
               Object registryKeyLoc = NMS_MINECRAFT_KEY_CONSTRUCTOR.newInstance("minecraft", "dimension_type");
               DIMENSION_TYPE_REGISTRY_KEY = CREATE_REGISTRY_RESOURCE_KEY.invoke((Object)null, registryKeyLoc);
            }

            dimensionTypeRegistry = GET_REGISTRY_OR_THROW.invoke(getFrozenRegistryAccess(), DIMENSION_TYPE_REGISTRY_KEY);
         } else {
            dimensionTypeRegistry = GET_DIMENSION_TYPES.invoke(getFrozenRegistryAccess());
         }

         return (Integer)GET_REGISTRY_ID.invoke(dimensionTypeRegistry, dimensionType);
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException var4) {
         var4.printStackTrace();
         return 0;
      }
   }

   public static String getDimensionKey(Object worldServer) {
      try {
         Object resourceKey = GET_DIMENSION_KEY.invoke(worldServer);
         return REGISTRY_KEY_LOCATION_FIELD.get(resourceKey).toString();
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static String fromStringToJSON(String message) {
      return message == null ? null : "{\"text\": \"" + message + "\"}";
   }

   public static int generateEntityId() {
      Field field = Reflection.getField(NMS_ENTITY_CLASS, "entityCount");
      if (field == null) {
         field = Reflection.getField(NMS_ENTITY_CLASS, AtomicInteger.class, 0);
      }

      try {
         if (field.getType().equals(AtomicInteger.class)) {
            AtomicInteger atomicInteger = (AtomicInteger)field.get((Object)null);
            return atomicInteger.incrementAndGet();
         } else {
            int id = field.getInt((Object)null);
            field.set((Object)null, id + 1);
            return id;
         }
      } catch (IllegalAccessException var2) {
         var2.printStackTrace();
         throw new IllegalStateException("Failed to generate a new unique entity ID!");
      }
   }

   public static int getEffectId(Object nmsMobEffectList) {
      try {
         return (Integer)GET_MOB_EFFECT_LIST_ID_METHOD.invoke((Object)null, nmsMobEffectList);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return -1;
      }
   }

   public static Object getMobEffectListById(int effectID) {
      try {
         return GET_MOB_EFFECT_LIST_BY_ID_METHOD.invoke((Object)null, effectID);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static int getNMSItemId(Object nmsItem) {
      try {
         return (Integer)GET_ITEM_ID_METHOD.invoke((Object)null, nmsItem);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return -1;
      }
   }

   public static Object getNMSItemById(int id) {
      try {
         return GET_ITEM_BY_ID_METHOD.invoke((Object)null, id);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object createNMSItemStack(Object nmsItem, int count) {
      try {
         return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack decodeBukkitItemStack(ItemStack in) {
      ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();

      ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack var6;
      try {
         Object packetDataSerializer = createPacketDataSerializer(buffer);
         Object nmsItemStack = toNMSItemStack(in);
         writeNMSItemStackPacketDataSerializer(packetDataSerializer, nmsItemStack);
         PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
         ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack stack = wrapper.readItemStack();
         var6 = stack;
      } finally {
         ByteBufHelper.release(buffer);
      }

      return var6;
   }

   public static ItemStack encodeBukkitItemStack(ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack in) {
      ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();

      ItemStack var6;
      try {
         PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
         wrapper.writeItemStack(in);
         Object packetDataSerializer = createPacketDataSerializer(wrapper.getBuffer());
         Object nmsItemStack = readNMSItemStackPacketDataSerializer(packetDataSerializer);
         ItemStack stack = toBukkitItemStack(nmsItemStack);
         var6 = stack;
      } finally {
         ByteBufHelper.release(buffer);
      }

      return var6;
   }

   public static int getBlockDataCombinedId(MaterialData materialData) {
      int combinedID;
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
         combinedID = -1;
      } else {
         combinedID = materialData.getItemType().getId() << 4 | materialData.getData();
      }

      return combinedID;
   }

   public static MaterialData getBlockDataByCombinedId(int combinedID) {
      Object iBlockDataObj = null;

      try {
         iBlockDataObj = GET_BY_COMBINED_ID.invoke((Object)null, combinedID);
      } catch (InvocationTargetException | IllegalAccessException var6) {
         var6.printStackTrace();
      }

      try {
         Class<?> blockData = Reflection.getClassByNameWithoutException("org.bukkit.block.data.BlockData");
         Object bd = blockData.cast(GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA.invoke((Object)null, iBlockDataObj));
         Method materialMethod = Reflection.getMethod(blockData, (Class)Material.class, 0);
         return new MaterialData((Material)materialMethod.invoke(bd));
      } catch (InvocationTargetException | IllegalAccessException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static Object createNMSItemStack(int itemID, int count) {
      try {
         Object nmsItem = getNMSItemById(itemID);
         return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object createPacketDataSerializer(Object byteBuf) {
      try {
         return REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR != null ? REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR.newInstance(byteBuf, getFrozenRegistryAccess()) : NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(byteBuf);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object createBlockPosition(int x, int y, int z) {
      try {
         return BLOCK_POSITION_CONSTRUCTOR.newInstance(x, y, z);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static Object getFrozenRegistryAccess() {
      if (MINECRAFT_SERVER_REGISTRY_ACCESS == null) {
         try {
            if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
               MINECRAFT_SERVER_REGISTRY_ACCESS = Reflection.getMethod(MINECRAFT_SERVER_CLASS, (Class)(VERSION.isNewerThanOrEquals(ServerVersion.V_1_18_2) ? REGISTRY_ACCESS_FROZEN : REGISTRY_ACCESS), 0).invoke(getMinecraftServerInstance(Bukkit.getServer()));
            } else {
               MINECRAFT_SERVER_REGISTRY_ACCESS = Reflection.getField(MINECRAFT_SERVER_CLASS, REGISTRY_ACCESS_FROZEN, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
            }
         } catch (InvocationTargetException | IllegalAccessException var1) {
            var1.printStackTrace();
         }
      }

      return MINECRAFT_SERVER_REGISTRY_ACCESS;
   }

   public static ItemStack toBukkitItemStack(Object nmsItemStack) {
      try {
         return (ItemStack)CRAFT_ITEM_STACK_AS_BUKKIT_COPY.invoke((Object)null, nmsItemStack);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object toNMSItemStack(ItemStack itemStack) {
      try {
         return CRAFT_ITEM_STACK_AS_NMS_COPY.invoke((Object)null, itemStack);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object readNMSItemStackPacketDataSerializer(Object packetDataSerializer) {
      try {
         return READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD != null ? READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer) : STREAM_DECODER_DECODE.invoke(ITEM_STACK_OPTIONAL_STREAM_CODEC, packetDataSerializer);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object writeNMSItemStackPacketDataSerializer(Object packetDataSerializer, Object nmsItemStack) {
      try {
         return WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD != null ? WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer, nmsItemStack) : STREAM_ENCODER_ENCODE.invoke(ITEM_STACK_OPTIONAL_STREAM_CODEC, packetDataSerializer, nmsItemStack);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static NBTCompound fromMinecraftNBT(Object nbtCompound) {
      byte[] bytes;
      try {
         ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

         try {
            DataOutputStream stream = new DataOutputStream(byteStream);

            try {
               writeNmsNbtToStream(nbtCompound, stream);
               bytes = byteStream.toByteArray();
            } catch (Throwable var16) {
               try {
                  stream.close();
               } catch (Throwable var14) {
                  var16.addSuppressed(var14);
               }

               throw var16;
            }

            stream.close();
         } catch (Throwable var17) {
            try {
               byteStream.close();
            } catch (Throwable var13) {
               var17.addSuppressed(var13);
            }

            throw var17;
         }

         byteStream.close();
      } catch (IOException var18) {
         throw new RuntimeException(var18);
      }

      Object buffer = UnpooledByteBufAllocationHelper.wrappedBuffer(bytes);

      NBTCompound var4;
      try {
         PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
         var4 = wrapper.readNBT();
      } finally {
         ByteBufHelper.release(buffer);
      }

      return var4;
   }

   public static Object toMinecraftNBT(NBTCompound nbtCompound) {
      Object buffer = UnpooledByteBufAllocationHelper.buffer();

      byte[] bytes;
      try {
         PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
         wrapper.writeNBT(nbtCompound);
         bytes = ByteBufHelper.copyBytes(buffer);
      } finally {
         ByteBufHelper.release(buffer);
      }

      try {
         ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

         Object var5;
         try {
            DataInputStream stream = new DataInputStream(byteStream);

            try {
               var5 = readNmsNbtFromStream(stream);
            } catch (Throwable var15) {
               try {
                  stream.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }

               throw var15;
            }

            stream.close();
         } catch (Throwable var16) {
            try {
               byteStream.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }

            throw var16;
         }

         byteStream.close();
         return var5;
      } catch (IOException var17) {
         var17.printStackTrace();
         return null;
      }
   }

   public static void writeNmsNbtToStream(Object compound, DataOutput out) {
      try {
         WRITE_NBT_TO_STREAM_METHOD.invoke((Object)null, compound, out);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
      }

   }

   public static Object readNmsNbtFromStream(DataInputStream in) {
      try {
         if (NBT_ACCOUNTER_UNLIMITED_HEAP != null) {
            Object nbtAccounterUnlimitedHeap = NBT_ACCOUNTER_UNLIMITED_HEAP.invoke((Object)null);
            return READ_NBT_FROM_STREAM_METHOD.invoke((Object)null, in, nbtAccounterUnlimitedHeap);
         } else {
            return READ_NBT_FROM_STREAM_METHOD.invoke((Object)null, in);
         }
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   @Nullable
   private static Entity getEntityByIdWithWorldUnsafe(World world, int id) {
      if (world == null) {
         return null;
      } else {
         Entity cachedEntity = (Entity)ENTITY_ID_CACHE.getOrDefault(id, (Object)null);
         if (cachedEntity != null) {
            return cachedEntity;
         } else {
            try {
               Object serverLevel = GET_CRAFT_WORLD_HANDLE_METHOD.invoke(world);
               Object nmsEntity;
               if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
                  ReflectionObject reflectObj = PAPER_ENTITY_LOOKUP_LEGACY ? new ReflectionObject(serverLevel, SERVER_LEVEL_CLASS) : new ReflectionObject(serverLevel, LEVEL_CLASS);
                  Object levelEntityGetter;
                  if (PAPER_ENTITY_LOOKUP_EXISTS) {
                     levelEntityGetter = reflectObj.readObject(0, PAPER_ENTITY_LOOKUP_CLASS);
                  } else {
                     Object entitySectionManager = reflectObj.readObject(0, PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
                     ReflectionObject reflectEntitySectionManager = new ReflectionObject(entitySectionManager);
                     levelEntityGetter = reflectEntitySectionManager.readObject(0, LEVEL_ENTITY_GETTER_CLASS);
                  }

                  nmsEntity = GET_ENTITY_BY_ID_LEVEL_ENTITY_GETTER_METHOD.invoke(levelEntityGetter, id);
               } else {
                  nmsEntity = GET_ENTITY_BY_ID_METHOD.invoke(serverLevel, id);
               }

               if (nmsEntity == null) {
                  return null;
               } else {
                  Entity entity = getBukkitEntity(nmsEntity);
                  if (entity == null) {
                     return null;
                  } else {
                     ENTITY_ID_CACHE.put(id, entity);
                     return entity;
                  }
               }
            } catch (InvocationTargetException | IllegalAccessException var9) {
               throw new RuntimeException("Error while looking up entity by id " + id + " in " + world, var9);
            }
         }
      }
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static Entity getEntityById(@Nullable World origin, int id) {
      if (origin != null) {
         Entity e = getEntityByIdWithWorldUnsafe(origin, id);
         if (e != null) {
            return e;
         }
      }

      Iterator var5 = Bukkit.getWorlds().iterator();

      Entity entity;
      do {
         if (!var5.hasNext()) {
            return null;
         }

         World world = (World)var5.next();
         entity = getEntityByIdWithWorldUnsafe(world, id);
      } while(entity == null);

      return entity;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static Entity getEntityById(int entityID) {
      return getEntityById((World)null, entityID);
   }

   public static List<Entity> getEntityList(World world) {
      if (V_1_17_OR_HIGHER) {
         Object worldServer = convertBukkitWorldToWorldServer(world);
         ReflectionObject wrappedWorldServer = new ReflectionObject(worldServer);
         Object levelEntityGetter;
         if (PAPER_ENTITY_LOOKUP_EXISTS) {
            if (!PAPER_ENTITY_LOOKUP_LEGACY) {
               wrappedWorldServer = new ReflectionObject(worldServer, LEVEL_CLASS);
            }

            levelEntityGetter = wrappedWorldServer.readObject(0, PAPER_ENTITY_LOOKUP_CLASS);
         } else {
            Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
            ReflectionObject wrappedPersistentEntitySectionManager = new ReflectionObject(persistentEntitySectionManager);
            levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, LEVEL_ENTITY_GETTER_CLASS);
         }

         Iterable nmsEntitiesIterable = null;

         try {
            nmsEntitiesIterable = (Iterable)GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD.invoke(levelEntityGetter);
         } catch (InvocationTargetException | IllegalAccessException var9) {
            var9.printStackTrace();
         }

         List<Entity> entityList = new ArrayList();
         if (nmsEntitiesIterable != null) {
            Iterator var6 = nmsEntitiesIterable.iterator();

            while(var6.hasNext()) {
               Object nmsEntity = var6.next();
               Entity bukkitEntity = getBukkitEntity(nmsEntity);
               entityList.add(bukkitEntity);
            }
         }

         return entityList;
      } else {
         return world.getEntities();
      }
   }

   public static ParticleType<?> toPacketEventsParticle(Enum<?> particle) {
      try {
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            if (CRAFT_PARTICLE_PARTICLES_FIELD == null) {
               return ParticleTypes.getByName(((Particle)particle).getKey().toString());
            } else {
               BiMap<?, ?> map = (BiMap)CRAFT_PARTICLE_PARTICLES_FIELD.get((Object)null);
               if (particle.name().equals("BLOCK_DUST")) {
                  particle = Enum.valueOf(particle.getClass(), "BLOCK_CRACK");
               }

               Object minecraftKey = map.get(particle);
               return ParticleTypes.getByName(minecraftKey.toString());
            }
         } else {
            Object nmsParticle = BUKKIT_PARTICLE_TO_NMS_ENUM_PARTICLE.invoke((Object)null, particle);
            String key = (String)LEGACY_NMS_PARTICLE_KEY_FIELD.get(nmsParticle);
            Object minecraftKey = NMS_MINECRAFT_KEY_CONSTRUCTOR.newInstance("minecraft", key);
            return ParticleTypes.getByName(minecraftKey.toString());
         }
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static Enum<?> fromPacketEventsParticle(ParticleType<?> particle) {
      try {
         Object minecraftKey;
         Object bukkitParticle;
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            if (CRAFT_PARTICLE_PARTICLES_FIELD == null) {
               ResourceLocation particleName = particle.getName();
               return (Enum)Registry.PARTICLE_TYPE.get(new NamespacedKey(particleName.getNamespace(), particleName.getKey()));
            } else {
               BiMap<?, ?> map = (BiMap)CRAFT_PARTICLE_PARTICLES_FIELD.get((Object)null);
               minecraftKey = NMS_MINECRAFT_KEY_CONSTRUCTOR.newInstance(particle.getName().getNamespace(), particle.getName().getKey());
               bukkitParticle = map.inverse().get(minecraftKey);
               return (Enum)bukkitParticle;
            }
         } else {
            Map<String, ?> keyToParticleMap = (Map)LEGACY_NMS_KEY_TO_NMS_PARTICLE.get((Object)null);
            minecraftKey = keyToParticleMap.get(particle.getName().getKey());
            bukkitParticle = NMS_ENUM_PARTICLE_TO_BUKKIT_PARTICLE.invoke((Object)null, minecraftKey);
            return (Enum)bukkitParticle;
         }
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static Object getRemoteChatSession(Player player) {
      Object entityPlayer = getEntityPlayer(player);

      try {
         return REMOTE_CHAT_SESSION_FIELD.get(entityPlayer);
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static List<EntityData<?>> getEntityMetadata(@NotNull Entity entity) {
      Object byteBuf = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer();

      List var17;
      try {
         Object handle = getNMSEntity(entity);
         Object dataWatcher = DATA_WATCHER_FIELD.get(handle);
         Object packetDataSerializer = createPacketDataSerializer(byteBuf);
         if (LEGACY_DATA_WATCHER_WRITE_METHOD != null) {
            LEGACY_DATA_WATCHER_WRITE_METHOD.invoke(dataWatcher, packetDataSerializer);
         } else {
            Object[] dataItems = (new ReflectionObject(dataWatcher)).readObjectArray(0, DATA_WATCHER_ITEM_CLASS);
            List<Object> dataValues = new ArrayList(dataItems.length);
            Object[] var7 = dataItems;
            int var8 = dataItems.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Object dataItem = var7[var9];
               dataValues.add(GET_DATA_VALUE_FROM_DATA_ITEM_METHOD.invoke(dataItem));
            }

            CLIENTBOUND_SET_ENTITY_DATA_PACKET_WRITE_DATA_WATCHER_METHOD.invoke((Object)null, dataValues, packetDataSerializer);
         }

         PacketWrapper<?> packetWrapper = PacketWrapper.createUniversalPacketWrapper(byteBuf);
         var17 = packetWrapper.readEntityMetadata();
      } catch (Exception var14) {
         throw new RuntimeException(var14);
      } finally {
         ByteBufHelper.release(byteBuf);
      }

      return var17;
   }

   static {
      String cbPackage = Bukkit.getServer().getClass().getPackage().getName();

      String temp;
      try {
         temp = cbPackage.replace(".", ",").split(",")[3];
      } catch (Exception var3) {
         temp = "";
      }

      MODIFIED_PACKAGE_NAME = temp;
      LEGACY_NMS_PACKAGE = "net.minecraft.server." + MODIFIED_PACKAGE_NAME + ".";
      OBC_PACKAGE = cbPackage + ".";
      PAPER_ENTITY_LOOKUP_EXISTS = false;
      PAPER_ENTITY_LOOKUP_LEGACY = true;
      ENTITY_ID_CACHE = (new MapMaker()).weakValues().makeMap();
   }
}
