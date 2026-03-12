package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Dimension;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.SimpleTypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

public final class SpigotConversionUtil {
   private SpigotConversionUtil() {
   }

   public static Location fromBukkitLocation(org.bukkit.Location location) {
      return new Location(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   public static org.bukkit.Location toBukkitLocation(World world, Location location) {
      return new org.bukkit.Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   public static PotionType fromBukkitPotionEffectType(PotionEffectType potionEffectType) {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      int id = potionEffectType.getId();
      if (version.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         --id;
      }

      return PotionTypes.getById(id, version);
   }

   public static PotionEffectType toBukkitPotionEffectType(PotionType potionType) {
      ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      int id = potionType.getId(version);
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_2)) {
         ++id;
      }

      return PotionEffectType.getById(id);
   }

   public static GameMode fromBukkitGameMode(org.bukkit.GameMode gameMode) {
      return GameMode.getById(gameMode.getValue());
   }

   public static org.bukkit.GameMode toBukkitGameMode(GameMode gameMode) {
      return org.bukkit.GameMode.getByValue(gameMode.getId());
   }

   public static WrappedBlockState fromBukkitBlockData(BlockData blockData) {
      String string = blockData.getAsString(false);
      return WrappedBlockState.getByString(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), string);
   }

   public static BlockData toBukkitBlockData(WrappedBlockState blockState) {
      return Bukkit.createBlockData(blockState.toString());
   }

   public static EntityType fromBukkitEntityType(org.bukkit.entity.EntityType entityType) {
      ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         return EntityTypes.getByName(entityType.getKey().toString());
      } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         return EntityTypes.getByName("minecraft:" + entityType.getName());
      } else {
         return entityType.getTypeId() == -1 ? null : EntityTypes.getById(serverVersion.toClientVersion(), entityType.getTypeId());
      }
   }

   public static org.bukkit.entity.EntityType toBukkitEntityType(EntityType entityType) {
      ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
      return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? org.bukkit.entity.EntityType.fromName(entityType.getName().getKey()) : org.bukkit.entity.EntityType.fromId(entityType.getId(serverVersion.toClientVersion()));
   }

   public static ItemType fromBukkitItemMaterial(Material material) {
      ItemStack bukkitStack = new ItemStack(material);
      ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack stack = fromBukkitItemStack(bukkitStack);
      return stack.getType();
   }

   public static Material toBukkitItemMaterial(ItemType itemType) {
      ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack stack = ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack.builder().type(itemType).build();
      ItemStack bukkitStack = toBukkitItemStack(stack);
      return bukkitStack.getType();
   }

   public static WrappedBlockState fromBukkitMaterialData(MaterialData materialData) {
      int combinedID = SpigotReflectionUtil.getBlockDataCombinedId(materialData);
      ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
      return WrappedBlockState.getByGlobalId(serverVersion.toClientVersion(), combinedID);
   }

   public static MaterialData toBukkitMaterialData(WrappedBlockState state) {
      return SpigotReflectionUtil.getBlockDataByCombinedId(state.getGlobalId());
   }

   public static ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack fromBukkitItemStack(ItemStack itemStack) {
      return SpigotReflectionUtil.decodeBukkitItemStack(itemStack);
   }

   public static ItemStack toBukkitItemStack(ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack itemStack) {
      return SpigotReflectionUtil.encodeBukkitItemStack(itemStack);
   }

   public static DimensionType typeFromBukkitWorld(World world) {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      if (version.isOlderThan(ServerVersion.V_1_14)) {
         int environmentId = world.getEnvironment().getId();
         return (DimensionType)DimensionTypes.getRegistry().getById(version.toClientVersion(), environmentId);
      } else {
         Object serverLevel;
         if (version.isOlderThan(ServerVersion.V_1_16)) {
            serverLevel = SpigotReflectionUtil.convertBukkitWorldToWorldServer(world);
            int dimensionTypeId = SpigotReflectionUtil.getDimensionId(serverLevel);
            return (DimensionType)DimensionTypes.getRegistry().getById(version.toClientVersion(), dimensionTypeId);
         } else {
            serverLevel = SpigotReflectionUtil.convertBukkitWorldToWorldServer(world);
            Object nbt = SpigotReflectionUtil.convertWorldServerDimensionToNMSNbt(serverLevel);
            NBTCompound peNbt = SpigotReflectionUtil.fromMinecraftNBT(nbt);
            ResourceLocation dimensionTypeName = new ResourceLocation(SpigotReflectionUtil.getDimensionKey(serverLevel));
            int dimensionTypeId = SpigotReflectionUtil.getDimensionId(serverLevel);
            return (DimensionType)((DimensionType)DimensionType.CODEC.decode(peNbt, PacketWrapper.createDummyWrapper(version.toClientVersion()))).copy(new SimpleTypesBuilderData(dimensionTypeName, dimensionTypeId));
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public static Dimension fromBukkitWorld(World world) {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      if (version.isOlderThan(ServerVersion.V_1_14)) {
         return new Dimension(world.getEnvironment().getId());
      } else {
         Object serverLevel;
         if (version.isOlderThan(ServerVersion.V_1_16)) {
            serverLevel = SpigotReflectionUtil.convertBukkitWorldToWorldServer(world);
            return new Dimension(SpigotReflectionUtil.getDimensionId(serverLevel));
         } else {
            serverLevel = SpigotReflectionUtil.convertBukkitWorldToWorldServer(world);
            Object nbt = SpigotReflectionUtil.convertWorldServerDimensionToNMSNbt(serverLevel);
            Dimension dimension = new Dimension(SpigotReflectionUtil.fromMinecraftNBT(nbt));
            if (version.isOlderThan(ServerVersion.V_1_16_2)) {
               dimension.setDimensionName(SpigotReflectionUtil.getDimensionKey(serverLevel));
            }

            dimension.setId(SpigotReflectionUtil.getDimensionId(serverLevel));
            return dimension;
         }
      }
   }

   public static ParticleType<?> fromBukkitParticle(Enum<?> particle) {
      return SpigotReflectionUtil.toPacketEventsParticle(particle);
   }

   public static Enum<?> toBukkitParticle(ParticleType<?> particle) {
      return SpigotReflectionUtil.fromPacketEventsParticle(particle);
   }

   @Nullable
   public static Entity getEntityById(@Nullable World world, int entityId) {
      return SpigotReflectionUtil.getEntityById(world, entityId);
   }

   public static Pose toBukkitPose(EntityPose pose) {
      return Pose.values()[pose.ordinal()];
   }

   public static EntityPose fromBukkitPose(Pose pose) {
      return EntityPose.values()[pose.ordinal()];
   }

   public static MainHand toBukkitHand(HumanoidArm arm) {
      return MainHand.values()[arm.ordinal()];
   }

   public static List<EntityData<?>> getEntityMetadata(Entity entity) {
      return SpigotReflectionUtil.getEntityMetadata(entity);
   }
}
