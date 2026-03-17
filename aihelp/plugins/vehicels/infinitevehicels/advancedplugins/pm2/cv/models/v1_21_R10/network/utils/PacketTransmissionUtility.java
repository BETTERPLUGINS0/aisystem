package advancedplugins.pm2.cv.models.v1_21_R10.network.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacket;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R10.network.ChannelManagerImpl;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PacketTransmissionUtility {
   private static final ProtocolInfo<ClientGamePacketListener> CLIENT_PROTOCOL_INFO;
   private static final Map<Class<?>, Constructor<?>> CACHED_CONSTRUCTORS;

   public static RegistryFriendlyByteBuf allocateBuffer() {
      return new RegistryFriendlyByteBuf(Unpooled.buffer(), MinecraftServer.getServer().registryAccess());
   }

   public static RegistryFriendlyByteBuf allocateBufferWithData(Consumer<RegistryFriendlyByteBuf> var0) {
      RegistryFriendlyByteBuf var1 = allocateBuffer();
      var0.accept(var1);
      return var1;
   }

   public static <T extends Packet<ClientGamePacketListener>> T instantiatePacket(Class<T> var0, RegistryFriendlyByteBuf var1) {
      Constructor var2 = (Constructor)CACHED_CONSTRUCTORS.computeIfAbsent(var0, (var0x) -> {
         return locateConstructor(var0x);
      });
      return (Packet)invokeConstructor(var2, var1);
   }

   private static Constructor<?> locateConstructor(Class<?> var0) {
      try {
         Constructor var1 = var0.getDeclaredConstructor(RegistryFriendlyByteBuf.class);
         var1.setAccessible(true);
         return var1;
      } catch (NoSuchMethodException var4) {
         try {
            Constructor var2 = var0.getDeclaredConstructor(FriendlyByteBuf.class);
            var2.setAccessible(true);
            return var2;
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   private static <T> T invokeConstructor(Constructor<?> var0, Object var1) {
      try {
         return var0.newInstance(var1);
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static FriendlyByteBuf decodeIncomingPacket(Packet<? super ServerGamePacketListener> var0, ServerPlayer var1) {
      RegistryFriendlyByteBuf var2 = allocateBuffer();
      GameProtocols.SERVERBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess()), var1.connection).codec().encode(var2, var0);
      var2.readVarInt();
      return var2;
   }

   public static FriendlyByteBuf decodeOutgoingPacket(Packet<? super ClientGamePacketListener> var0) {
      RegistryFriendlyByteBuf var1 = allocateBuffer();
      CLIENT_PROTOCOL_INFO.codec().encode(var1, var0);
      var1.readVarInt();
      return var1;
   }

   public static PacketBundleProvider.PacketFactory generateAnchorSpawn(int var0, UUID var1, Vector3f var2) {
      return (var3) -> {
         double var4 = ModelAPI.getPlayerProtocolVersion(var3) >= 764 ? 0.5D : 0.375D;
         return new ClientboundAddEntityPacket(var0, var1, (double)var2.x, (double)var2.y - var4, (double)var2.z, 0.0F, 0.0F, EntityType.AREA_EFFECT_CLOUD, 0, Vec3.ZERO, 0.0D);
      };
   }

   public static PacketBundleProvider.PacketFactory generateAnchorRelocation(int var0, Vector3f var1) {
      EntityRelationship var2 = createPositionedRelation(var0, (double)var1.x, (double)var1.y - 0.375D, (double)var1.z);
      EntityRelationship var3 = createPositionedRelation(var0, (double)var1.x, (double)var1.y - 0.5D, (double)var1.z);
      ClientboundEntityPositionSyncPacket var4 = createPositionSync(var2);
      ClientboundEntityPositionSyncPacket var5 = createPositionSync(var3);
      return (var2x) -> {
         return ModelAPI.getPlayerProtocolVersion(var2x) >= 764 ? var5 : var4;
      };
   }

   private static EntityRelationship createPositionedRelation(int var0, double var1, double var3, double var5) {
      EntityRelationship var7 = EntityRelationship.of(var0);
      var7.setPosRaw(var1, var3, var5);
      return var7;
   }

   private static ClientboundEntityPositionSyncPacket createPositionSync(EntityRelationship var0) {
      return new ClientboundEntityPositionSyncPacket(var0.getId(), PositionMoveRotation.of(var0), var0.onGround());
   }

   public static PacketBundleProvider.PacketFactory wrapWithLevelOfDetail(IModelContainer var0, BiFunction<UUID, AnimationLODHandler.LODTracker, Packet<ClientGamePacketListener>> var1) {
      return (var2) -> {
         AnimationLODHandler.LODTracker var3 = var0.getAnimationLodHandler().tick(var2);
         return var3.isCanSkip() ? null : (Packet)var1.apply(var2, var3);
      };
   }

   public static void transmitToPlayer(UUID var0, @Nullable Packet<? super ClientGamePacketListener> var1) {
      if (var1 != null) {
         ChannelManagerImpl var2 = ChannelManagerImpl.instance;
         if (var2.isBatching()) {
            var2.appendPacket(var0, var1);
         } else {
            dispatchProtectedPacket(var0, var1);
         }

      }
   }

   public static void broadcastToPlayers(Set<UUID> var0, @Nullable Packet<? super ClientGamePacketListener> var1) {
      if (var1 != null) {
         ChannelManagerImpl var2 = ChannelManagerImpl.instance;
         if (var2.isBatching()) {
            var0.forEach((var2x) -> {
               var2.appendPacket(var2x, var1);
            });
         } else {
            ProtectedPacket var3 = new ProtectedPacket(var1);
            var0.forEach((var1x) -> {
               dispatchSecurePacket(var1x, var3);
            });
         }

      }
   }

   public static void broadcastConditionally(Set<UUID> var0, @Nullable Packet<? super ClientGamePacketListener> var1, Predicate<Player> var2) {
      if (var1 != null) {
         ChannelManagerImpl var3 = ChannelManagerImpl.instance;
         ProtectedPacket var4 = var3.isBatching() ? null : new ProtectedPacket(var1);
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var3x) -> {
            if (var3.isBatching()) {
               var3.appendPacket(var3x.getUniqueId(), var1);
            } else {
               dispatchSecurePacket(var3x.getUniqueId(), var4);
            }

         });
      }
   }

   private static void dispatchProtectedPacket(UUID var0, Packet<? super ClientGamePacketListener> var1) {
      ProtectedPacket var2 = new ProtectedPacket(var1);
      dispatchSecurePacket(var0, var2);
   }

   private static void dispatchSecurePacket(UUID var0, ProtectedPacket var1) {
      ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
         var1x.writeAndFlush(var1);
      });
   }

   public static void transmitDirectly(UUID var0, @Nullable Packet<? super ClientGamePacketListener> var1) {
      if (var1 != null) {
         ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
            var1x.writeAndFlush(var1);
         });
      }

   }

   public static void transmitDirectlyToAll(Set<UUID> var0, @Nullable Packet<? super ClientGamePacketListener> var1) {
      if (var1 != null) {
         var0.forEach((var1x) -> {
            transmitDirectly(var1x, var1);
         });
      }

   }

   public static void transmitDirectlyConditional(Set<UUID> var0, @Nullable Packet<? super ClientGamePacketListener> var1, Predicate<Player> var2) {
      if (var1 != null) {
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var1x) -> {
            transmitDirectly(var1x.getUniqueId(), var1);
         });
      }
   }

   public static void deliverPacketBundle(UUID var0, PacketBundleProvider var1) {
      if (!var1.isEmpty()) {
         var1.compile(var0, (var1x) -> {
            ChannelManagerImpl var2 = ChannelManagerImpl.instance;
            if (var2.isBatching()) {
               var2.appendPackets(var0, var1x);
            } else {
               sendCompiledBundle(var0, var1x);
            }

         });
      }
   }

   public static void deliverBundleToMultiple(Set<UUID> var0, PacketBundleProvider var1) {
      if (!var1.isEmpty()) {
         ChannelManagerImpl var2 = ChannelManagerImpl.instance;
         var0.forEach((var2x) -> {
            if (var2.isBatching()) {
               var1.compile(var2x, (var2xx) -> {
                  var2.appendPackets(var2x, var2xx);
               });
            } else {
               ModelAPI.getNetworkHandler().getPipeline(var2x).ifPresent((var2xx) -> {
                  var1.compile(var2x, (var1x) -> {
                     sendCompiledBundle(var2x, var1x);
                  });
               });
            }

         });
      }
   }

   public static void deliverBundleConditionally(Set<UUID> var0, PacketBundleProvider var1, Predicate<Player> var2) {
      if (!var1.isEmpty()) {
         ChannelManagerImpl var3 = ChannelManagerImpl.instance;
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var2x) -> {
            UUID var3x = var2x.getUniqueId();
            if (var3.isBatching()) {
               var1.compile(var3x, (var2) -> {
                  var3.appendPackets(var3x, var2);
               });
            } else {
               ModelAPI.getNetworkHandler().getPipeline(var3x).ifPresent((var2) -> {
                  var1.compile(var3x, (var1x) -> {
                     sendCompiledBundle(var3x, var1x);
                  });
               });
            }

         });
      }
   }

   private static void sendCompiledBundle(UUID var0, Collection<Packet<? super ClientGamePacketListener>> var1) {
      ProtectedPacket var2 = new ProtectedPacket(new ClientboundBundlePacket(var1));
      ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
         var1x.writeAndFlush(var2);
      });
   }

   public static void deliverRawBundle(UUID var0, PacketBundleProvider var1) {
      if (!var1.isEmpty()) {
         var1.compile(var0, (var1x) -> {
            ClientboundBundlePacket var2 = new ClientboundBundlePacket(var1x);
            ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1) -> {
               var1.writeAndFlush(var2);
            });
         });
      }
   }

   public static void deliverRawBundleToAll(Set<UUID> var0, PacketBundleProvider var1) {
      if (!var1.isEmpty()) {
         var0.forEach((var1x) -> {
            deliverRawBundle(var1x, var1);
         });
      }
   }

   public static void deliverRawBundleConditional(Set<UUID> var0, PacketBundleProvider var1, Predicate<Player> var2) {
      if (!var1.isEmpty()) {
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var1x) -> {
            deliverRawBundle(var1x.getUniqueId(), var1);
         });
      }
   }

   static {
      CLIENT_PROTOCOL_INFO = GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess()));
      CACHED_CONSTRUCTORS = new ConcurrentHashMap();
   }
}
