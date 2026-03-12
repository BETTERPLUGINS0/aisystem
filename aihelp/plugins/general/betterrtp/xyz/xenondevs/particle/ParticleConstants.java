package xyz.xenondevs.particle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class ParticleConstants {
   public static final Class ITEM_STACK_CLASS;
   public static final Class PACKET_CLASS;
   public static final Class PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS;
   public static final Class PARTICLE_ENUM;
   public static final Class PARTICLE_CLASS;
   public static final Class MINECRAFT_KEY_CLASS;
   public static final Class VECTOR_3FA_CLASS;
   public static final Class REGISTRY_CLASS;
   public static final Class BUILT_IN_REGISTRIES_CLASS;
   public static final Class BLOCK_CLASS;
   public static final Class BLOCK_POSITION_CLASS;
   public static final Class BLOCK_DATA_INTERFACE;
   public static final Class BLOCKS_CLASS;
   public static final Class POSITION_SOURCE_CLASS;
   public static final Class BLOCK_POSITION_SOURCE_CLASS;
   public static final Class ENTITY_POSITION_SOURCE_CLASS;
   public static final Class VIBRATION_PATH_CLASS;
   public static final Class ENTITY_CLASS;
   public static final Class ENTITY_PLAYER_CLASS;
   public static final Class PLAYER_CONNECTION_CLASS;
   public static final Class CRAFT_ENTITY_CLASS;
   public static final Class CRAFT_PLAYER_CLASS;
   public static final Class CRAFT_ITEM_STACK_CLASS;
   public static final Class PARTICLE_PARAM_CLASS;
   public static final Class PARTICLE_PARAM_REDSTONE_CLASS;
   public static final Class PARTICLE_PARAM_DUST_COLOR_TRANSITION_CLASS;
   public static final Class PARTICLE_PARAM_BLOCK_CLASS;
   public static final Class PARTICLE_PARAM_ITEM_CLASS;
   public static final Class PARTICLE_PARAM_VIBRATION_CLASS;
   public static final Class PARTICLE_PARAM_SHRIEK_CLASS;
   public static final Class PARTICLE_PARAM_SCULK_CHARGE_CLASS;
   public static final Method REGISTRY_GET_METHOD;
   public static final Method PLAYER_CONNECTION_SEND_PACKET_METHOD;
   public static final Method CRAFT_ENTITY_GET_HANDLE_METHOD;
   public static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;
   public static final Method BLOCK_GET_BLOCK_DATA_METHOD;
   public static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;
   public static final Field ENTITY_PLAYER_PLAYER_CONNECTION_FIELD;
   public static final Constructor PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;
   public static final Constructor MINECRAFT_KEY_CONSTRUCTOR;
   public static final Constructor VECTOR_3FA_CONSTRUCTOR;
   public static final Constructor BLOCK_POSITION_CONSTRUCTOR;
   public static final Constructor BLOCK_POSITION_SOURCE_CONSTRUCTOR;
   public static final Constructor ENTITY_POSITION_SOURCE_CONSTRUCTOR;
   public static final Constructor VIBRATION_PATH_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_REDSTONE_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_BLOCK_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_ITEM_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_VIBRATION_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_SHRIEK_CONSTRUCTOR;
   public static final Constructor PARTICLE_PARAM_SCULK_CHARGE_CONSTRUCTOR;
   public static final Object PARTICLE_TYPE_REGISTRY;
   public static final Object BLOCK_REGISTRY;

   static {
      double version = ReflectionUtils.MINECRAFT_VERSION;
      ITEM_STACK_CLASS = ParticleMappings.getMappedClass("ItemStack");
      PACKET_CLASS = ParticleMappings.getMappedClass("Packet");
      PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS = ParticleMappings.getMappedClass("PacketPlayOutWorldParticles");
      PARTICLE_ENUM = ParticleMappings.getMappedClass("EnumParticle");
      PARTICLE_CLASS = ParticleMappings.getMappedClass("Particle");
      MINECRAFT_KEY_CLASS = ParticleMappings.getMappedClass("MinecraftKey");
      VECTOR_3FA_CLASS = version < 17.0D ? ReflectionUtils.getNMSClass("Vector3f") : (version < 19.3D ? ReflectionUtils.getClassSafe("com.mojang.math.Vector3fa") : ReflectionUtils.getClassSafe("org.joml.Vector3f"));
      REGISTRY_CLASS = ParticleMappings.getMappedClass("IRegistry");
      BUILT_IN_REGISTRIES_CLASS = ParticleMappings.getMappedClass("BuiltInRegistries");
      BLOCK_CLASS = ParticleMappings.getMappedClass("Block");
      BLOCK_POSITION_CLASS = ParticleMappings.getMappedClass("BlockPosition");
      BLOCK_DATA_INTERFACE = ParticleMappings.getMappedClass("IBlockData");
      BLOCKS_CLASS = ParticleMappings.getMappedClass("Blocks");
      POSITION_SOURCE_CLASS = ParticleMappings.getMappedClass("PositionSource");
      BLOCK_POSITION_SOURCE_CLASS = ParticleMappings.getMappedClass("BlockPositionSource");
      ENTITY_POSITION_SOURCE_CLASS = ParticleMappings.getMappedClass("EntityPositionSource");
      VIBRATION_PATH_CLASS = ParticleMappings.getMappedClass("VibrationPath");
      ENTITY_CLASS = ParticleMappings.getMappedClass("Entity");
      ENTITY_PLAYER_CLASS = ParticleMappings.getMappedClass("EntityPlayer");
      PLAYER_CONNECTION_CLASS = ParticleMappings.getMappedClass("PlayerConnection");
      CRAFT_ENTITY_CLASS = ReflectionUtils.getCraftBukkitClass("entity.CraftEntity");
      CRAFT_PLAYER_CLASS = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
      CRAFT_ITEM_STACK_CLASS = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");
      PARTICLE_PARAM_CLASS = ParticleMappings.getMappedClass("ParticleParam");
      PARTICLE_PARAM_REDSTONE_CLASS = ParticleMappings.getMappedClass("ParticleParamRedstone");
      PARTICLE_PARAM_DUST_COLOR_TRANSITION_CLASS = ParticleMappings.getMappedClass("ParticleParamDustColorTransition");
      PARTICLE_PARAM_BLOCK_CLASS = ParticleMappings.getMappedClass("ParticleParamBlock");
      PARTICLE_PARAM_ITEM_CLASS = ParticleMappings.getMappedClass("ParticleParamItem");
      PARTICLE_PARAM_VIBRATION_CLASS = ParticleMappings.getMappedClass("ParticleParamVibration");
      PARTICLE_PARAM_SHRIEK_CLASS = ParticleMappings.getMappedClass("ParticleParamShriek");
      PARTICLE_PARAM_SCULK_CHARGE_CLASS = ParticleMappings.getMappedClass("ParticleParamSculkCharge");
      REGISTRY_GET_METHOD = ParticleMappings.getMappedMethod(REGISTRY_CLASS, "Registry.get", MINECRAFT_KEY_CLASS);
      PLAYER_CONNECTION_SEND_PACKET_METHOD = ParticleMappings.getMappedMethod(PLAYER_CONNECTION_CLASS, "PlayerConnection.sendPacket", PACKET_CLASS);
      CRAFT_ENTITY_GET_HANDLE_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_ENTITY_CLASS, "getHandle");
      CRAFT_PLAYER_GET_HANDLE_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_PLAYER_CLASS, "getHandle");
      BLOCK_GET_BLOCK_DATA_METHOD = ParticleMappings.getMappedMethod(BLOCK_CLASS, "Block.getBlockData");
      CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", ItemStack.class);
      ENTITY_PLAYER_PLAYER_CONNECTION_FIELD = ParticleMappings.getMappedField(ENTITY_PLAYER_CLASS, "EntityPlayer.playerConnection", false);
      if (version < 13.0D) {
         PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_ENUM, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class);
      } else if (version < 15.0D) {
         PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_PARAM_CLASS, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE);
      } else {
         PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, PARTICLE_PARAM_CLASS, Boolean.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE);
      }

      MINECRAFT_KEY_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(MINECRAFT_KEY_CLASS, String.class);
      VECTOR_3FA_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(VECTOR_3FA_CLASS, Float.TYPE, Float.TYPE, Float.TYPE);
      BLOCK_POSITION_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(BLOCK_POSITION_CLASS, Double.TYPE, Double.TYPE, Double.TYPE);
      BLOCK_POSITION_SOURCE_CONSTRUCTOR = version < 17.0D ? null : ReflectionUtils.getConstructorOrNull(BLOCK_POSITION_SOURCE_CLASS, BLOCK_POSITION_CLASS);
      if (version < 17.0D) {
         ENTITY_POSITION_SOURCE_CONSTRUCTOR = null;
      } else if (version < 19.0D) {
         ENTITY_POSITION_SOURCE_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(ENTITY_POSITION_SOURCE_CLASS, Integer.TYPE);
      } else {
         ENTITY_POSITION_SOURCE_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(ENTITY_POSITION_SOURCE_CLASS, ENTITY_CLASS, Float.TYPE);
      }

      VIBRATION_PATH_CONSTRUCTOR = version < 17.0D ? null : ReflectionUtils.getConstructorOrNull(VIBRATION_PATH_CLASS, BLOCK_POSITION_CLASS, POSITION_SOURCE_CLASS, Integer.TYPE);
      if (version < 13.0D) {
         PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = null;
      } else if (version < 17.0D) {
         PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_REDSTONE_CLASS, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE);
      } else {
         PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_REDSTONE_CLASS, VECTOR_3FA_CLASS, Float.TYPE);
      }

      PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR = version < 17.0D ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_DUST_COLOR_TRANSITION_CLASS, VECTOR_3FA_CLASS, VECTOR_3FA_CLASS, Float.TYPE);
      PARTICLE_PARAM_BLOCK_CONSTRUCTOR = version < 13.0D ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_BLOCK_CLASS, PARTICLE_CLASS, BLOCK_DATA_INTERFACE);
      PARTICLE_PARAM_ITEM_CONSTRUCTOR = version < 13.0D ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_ITEM_CLASS, PARTICLE_CLASS, ITEM_STACK_CLASS);
      if (version < 17.0D) {
         PARTICLE_PARAM_VIBRATION_CONSTRUCTOR = null;
      } else if (version < 19.0D) {
         PARTICLE_PARAM_VIBRATION_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_VIBRATION_CLASS, VIBRATION_PATH_CLASS);
      } else {
         PARTICLE_PARAM_VIBRATION_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_VIBRATION_CLASS, POSITION_SOURCE_CLASS, Integer.TYPE);
      }

      PARTICLE_PARAM_SHRIEK_CONSTRUCTOR = version < 19.0D ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_SHRIEK_CLASS, Integer.TYPE);
      PARTICLE_PARAM_SCULK_CHARGE_CONSTRUCTOR = version < 19.0D ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_SCULK_CHARGE_CLASS, Float.TYPE);
      PARTICLE_TYPE_REGISTRY = ReflectionUtils.readField(version < 19.3D ? ParticleMappings.getMappedField(REGISTRY_CLASS, "Registry.ParticleTypeRegistry", false) : ParticleMappings.getMappedField(BUILT_IN_REGISTRIES_CLASS, "BuiltInRegistries.ParticleTypeRegistry", false), (Object)null);
      BLOCK_REGISTRY = ReflectionUtils.readField(version < 19.3D ? ParticleMappings.getMappedField(REGISTRY_CLASS, "Registry.BlockRegistry", false) : ParticleMappings.getMappedField(BUILT_IN_REGISTRIES_CLASS, "BuiltInRegistries.BlockRegistry", false), (Object)null);
   }
}
