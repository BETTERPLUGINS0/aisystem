package advancedplugins.pm2.cv.models.v1_21_R1.network.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacket;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R1.network.ChannelManagerImpl;
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
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PacketTransmissionUtility {
   private static final ProtocolInfo<PacketListenerPlayOut> CLIENT_PROTOCOL_INFO;
   private static final Map<Class<?>, Constructor<?>> CACHED_CONSTRUCTORS;

   public static RegistryFriendlyByteBuf allocateBuffer() {
      return new RegistryFriendlyByteBuf(Unpooled.buffer(), MinecraftServer.getServer().bc());
   }

   public static RegistryFriendlyByteBuf allocateBufferWithData(Consumer<RegistryFriendlyByteBuf> dataWriter) {
      RegistryFriendlyByteBuf var1 = allocateBuffer();
      var0.accept(var1);
      return var1;
   }

   public static <T extends Packet<PacketListenerPlayOut>> T instantiatePacket(Class<T> packetType, RegistryFriendlyByteBuf buffer) {
      Constructor var2 = (Constructor)CACHED_CONSTRUCTORS.computeIfAbsent(var0, (var0x) -> {
         return locateConstructor(var0x);
      });
      return (Packet)invokeConstructor(var2, var1);
   }

   private static Constructor<?> locateConstructor(Class<?> cls) {
      try {
         Constructor var1 = var0.getDeclaredConstructor(RegistryFriendlyByteBuf.class);
         var1.setAccessible(true);
         return var1;
      } catch (NoSuchMethodException var4) {
         try {
            Constructor var2 = var0.getDeclaredConstructor(PacketDataSerializer.class);
            var2.setAccessible(true);
            return var2;
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   private static <T> T invokeConstructor(Constructor<?> ctor, Object buffer) {
      try {
         return var0.newInstance(var1);
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static PacketDataSerializer decodeIncomingPacket(Packet<? super PacketListenerPlayIn> packet, EntityPlayer player) {
      RegistryFriendlyByteBuf var2 = allocateBuffer();
      GameProtocols.a.a(RegistryFriendlyByteBuf.a(MinecraftServer.getServer().bc())).c().encode(var2, var0);
      var2.l();
      return var2;
   }

   public static PacketDataSerializer decodeOutgoingPacket(Packet<? super PacketListenerPlayOut> packet) {
      RegistryFriendlyByteBuf var1 = allocateBuffer();
      CLIENT_PROTOCOL_INFO.c().encode(var1, var0);
      var1.l();
      return var1;
   }

   public static PacketBundleProvider.PacketFactory generateAnchorSpawn(int entityId, UUID entityUuid, Vector3f position) {
      return (var3) -> {
         double var4 = ModelAPI.getPlayerProtocolVersion(var3) >= 764 ? 0.5D : 0.375D;
         return new PacketPlayOutSpawnEntity(var0, var1, (double)var2.x, (double)var2.y - var4, (double)var2.z, 0.0F, 0.0F, EntityTypes.b, 0, Vec3D.b, 0.0D);
      };
   }

   public static PacketBundleProvider.PacketFactory generateAnchorRelocation(int entityId, Vector3f position) {
      return (var2) -> {
         double var3 = ModelAPI.getPlayerProtocolVersion(var2) >= 764 ? 0.5D : 0.375D;
         RegistryFriendlyByteBuf var5 = allocateBufferWithData((var4) -> {
            var4.c(var0);
            var4.a((double)var1.x);
            var4.a((double)var1.y - var3);
            var4.a((double)var1.z);
            var4.a(0.0F);
            var4.a(0.0F);
            var4.a(false);
         });
         return instantiatePacket(PacketPlayOutEntityTeleport.class, var5);
      };
   }

   private static EntityRelationship createPositionedRelation(int id, double x, double y, double z) {
      EntityRelationship var7 = EntityRelationship.of(var0);
      var7.o(var1, var3, var5);
      return var7;
   }

   public static PacketBundleProvider.PacketFactory wrapWithLevelOfDetail(IModelContainer container, BiFunction<UUID, AnimationLODHandler.LODTracker, Packet<PacketListenerPlayOut>> packetGenerator) {
      return (var2) -> {
         AnimationLODHandler.LODTracker var3 = var0.getAnimationLodHandler().tick(var2);
         return var3.isCanSkip() ? null : (Packet)var1.apply(var2, var3);
      };
   }

   public static void transmitToPlayer(UUID playerId, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         ChannelManagerImpl var2 = ChannelManagerImpl.instance;
         if (var2.isBatching()) {
            var2.appendPacket(var0, var1);
         } else {
            dispatchProtectedPacket(var0, var1);
         }

      }
   }

   public static void broadcastToPlayers(Set<UUID> playerIds, @Nullable Packet<? super PacketListenerPlayOut> packet) {
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

   public static void broadcastConditionally(Set<UUID> playerIds, @Nullable Packet<? super PacketListenerPlayOut> packet, Predicate<Player> condition) {
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

   private static void dispatchProtectedPacket(UUID playerId, Packet<? super PacketListenerPlayOut> packet) {
      ProtectedPacket var2 = new ProtectedPacket(var1);
      dispatchSecurePacket(var0, var2);
   }

   private static void dispatchSecurePacket(UUID playerId, ProtectedPacket securePacket) {
      ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
         var1x.writeAndFlush(var1);
      });
   }

   public static void transmitDirectly(UUID playerId, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
            var1x.writeAndFlush(var1);
         });
      }

   }

   public static void transmitDirectlyToAll(Set<UUID> playerIds, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         var0.forEach((var1x) -> {
            transmitDirectly(var1x, var1);
         });
      }

   }

   public static void transmitDirectlyConditional(Set<UUID> playerIds, @Nullable Packet<? super PacketListenerPlayOut> packet, Predicate<Player> condition) {
      if (var1 != null) {
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var1x) -> {
            transmitDirectly(var1x.getUniqueId(), var1);
         });
      }
   }

   public static void deliverPacketBundle(UUID playerId, PacketBundleProvider bundle) {
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

   public static void deliverBundleToMultiple(Set<UUID> playerIds, PacketBundleProvider bundle) {
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

   public static void deliverBundleConditionally(Set<UUID> playerIds, PacketBundleProvider bundle, Predicate<Player> condition) {
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

   private static void sendCompiledBundle(UUID playerId, Collection<Packet<? super PacketListenerPlayOut>> packets) {
      ProtectedPacket var2 = new ProtectedPacket(new ClientboundBundlePacket(var1));
      ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
         var1x.writeAndFlush(var2);
      });
   }

   public static void deliverRawBundle(UUID playerId, PacketBundleProvider bundle) {
      if (!var1.isEmpty()) {
         var1.compile(var0, (var1x) -> {
            ClientboundBundlePacket var2 = new ClientboundBundlePacket(var1x);
            ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1) -> {
               var1.writeAndFlush(var2);
            });
         });
      }
   }

   public static void deliverRawBundleToAll(Set<UUID> playerIds, PacketBundleProvider bundle) {
      if (!var1.isEmpty()) {
         var0.forEach((var1x) -> {
            deliverRawBundle(var1x, var1);
         });
      }
   }

   public static void deliverRawBundleConditional(Set<UUID> playerIds, PacketBundleProvider bundle, Predicate<Player> condition) {
      if (!var1.isEmpty()) {
         var0.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).filter(var2).forEach((var1x) -> {
            deliverRawBundle(var1x.getUniqueId(), var1);
         });
      }
   }

   static {
      CLIENT_PROTOCOL_INFO = GameProtocols.b.a(RegistryFriendlyByteBuf.a(MinecraftServer.getServer().bc()));
      CACHED_CONSTRUCTORS = new ConcurrentHashMap();
   }
}
