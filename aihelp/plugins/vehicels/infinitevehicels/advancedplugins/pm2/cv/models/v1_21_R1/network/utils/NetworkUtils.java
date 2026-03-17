package advancedplugins.pm2.cv.models.v1_21_R1.network.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacket;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R1.network.NetworkHandlerImpl;
import io.netty.buffer.Unpooled;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
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
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class NetworkUtils {
   public static final ProtocolInfo<PacketListenerPlayOut> CLIENTBOUND_CODEC;
   public static final ProtocolInfo<PacketListenerPlayIn> SERVERBOUND_CODEC;

   public static PacketDataSerializer createByteBuf() {
      return new PacketDataSerializer(Unpooled.buffer());
   }

   public static PacketDataSerializer readServerbound(Packet<? super PacketListenerPlayIn> packet) {
      PacketDataSerializer var1 = createByteBuf();
      SERVERBOUND_CODEC.c().encode(var1, var0);
      var1.l();
      return var1;
   }

   public static PacketDataSerializer readClientbound(Packet<? super PacketListenerPlayOut> packet) {
      PacketDataSerializer var1 = createByteBuf();
      CLIENTBOUND_CODEC.c().encode(var1, var0);
      var1.l();
      return var1;
   }

   public static Packets.PacketSupplier createPivotSpawn(int id, UUID uuid, Vector3f pos) {
      return (var3) -> {
         return ModelAPI.getPlayerProtocolVersion(var3) >= 764 ? new PacketPlayOutSpawnEntity(var0, var1, (double)var2.x, (double)var2.y - 0.5D, (double)var2.z, 0.0F, 0.0F, EntityTypes.b, 0, Vec3D.b, 0.0D) : new PacketPlayOutSpawnEntity(var0, var1, (double)var2.x, (double)var2.y - 0.375D, (double)var2.z, 0.0F, 0.0F, EntityTypes.b, 0, Vec3D.b, 0.0D);
      };
   }

   public static Packets.PacketSupplier createPivotTeleport(int id, Vector3f pos) {
      EntityContainer var2 = EntityContainer.of(var0);
      var2.o((double)var1.x, (double)var1.y - 0.375D, (double)var1.z);
      PacketPlayOutEntityTeleport var3 = new PacketPlayOutEntityTeleport(var2);
      var2 = EntityContainer.of(var0);
      var2.e(var0);
      var2.o((double)var1.x, (double)var1.y - 0.5D, (double)var1.z);
      PacketPlayOutEntityTeleport var4 = new PacketPlayOutEntityTeleport(var2);
      return (var2x) -> {
         return ModelAPI.getPlayerProtocolVersion(var2x) >= 764 ? var4 : var3;
      };
   }

   public static Packets.PacketSupplier lodWrapper(IModelContainer modelContainer, BiFunction<UUID, AnimationLODHandler.LODTracker, Packet<PacketListenerPlayOut>> packetFunction) {
      return (var2) -> {
         AnimationLODHandler.LODTracker var3 = var0.getAnimationLodHandler().tick(var2);
         return var3.isCanSkip() ? null : (Packet)var1.apply(var2, var3);
      };
   }

   public static void send(UUID target, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         NetworkHandlerImpl var2 = NetworkHandlerImpl.instance;
         if (var2.isBatching()) {
            var2.appendPacket(var0, var1);
         } else {
            ProtectedPacket var3 = new ProtectedPacket(var1);
            ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
               var1x.writeAndFlush(var3);
            });
         }
      }

   }

   public static void send(Set<UUID> targets, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         NetworkHandlerImpl var2 = NetworkHandlerImpl.instance;
         if (var2.isBatching()) {
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               UUID var4 = (UUID)var3.next();
               var2.appendPacket(var4, var1);
            }
         } else {
            ProtectedPacket var6 = new ProtectedPacket(var1);
            Iterator var7 = var0.iterator();

            while(var7.hasNext()) {
               UUID var5 = (UUID)var7.next();
               ModelAPI.getNetworkHandler().getPipeline(var5).ifPresent((var1x) -> {
                  var1x.writeAndFlush(var6);
               });
            }
         }
      }

   }

   public static void send(Set<UUID> targets, @Nullable Packet<? super PacketListenerPlayOut> packet, Predicate<Player> predicate) {
      if (var1 != null) {
         NetworkHandlerImpl var3 = NetworkHandlerImpl.instance;
         if (var3.isBatching()) {
            Iterator var4 = var0.iterator();

            while(var4.hasNext()) {
               UUID var5 = (UUID)var4.next();
               Player var6 = Bukkit.getPlayer(var5);
               if (var6 != null && var2.test(var6)) {
                  var3.appendPacket(var5, var1);
               }
            }
         } else {
            ProtectedPacket var8 = new ProtectedPacket(var1);
            Iterator var9 = var0.iterator();

            while(var9.hasNext()) {
               UUID var10 = (UUID)var9.next();
               Player var7 = Bukkit.getPlayer(var10);
               if (var7 != null && var2.test(var7)) {
                  ModelAPI.getNetworkHandler().getPipeline(var10).ifPresent((var1x) -> {
                     var1x.writeAndFlush(var8);
                  });
               }
            }
         }
      }

   }

   public static void sendRaw(UUID target, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
            var1x.writeAndFlush(var1);
         });
      }

   }

   public static void sendRaw(Set<UUID> targets, @Nullable Packet<? super PacketListenerPlayOut> packet) {
      if (var1 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            UUID var3 = (UUID)var2.next();
            ModelAPI.getNetworkHandler().getPipeline(var3).ifPresent((var1x) -> {
               var1x.writeAndFlush(var1);
            });
         }
      }

   }

   public static void sendRaw(Set<UUID> targets, @Nullable Packet<? super PacketListenerPlayOut> packet, Predicate<Player> predicate) {
      if (var1 != null) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            UUID var4 = (UUID)var3.next();
            Player var5 = Bukkit.getPlayer(var4);
            if (var5 != null && var2.test(var5)) {
               ModelAPI.getNetworkHandler().getPipeline(var4).ifPresent((var1x) -> {
                  var1x.writeAndFlush(var1);
               });
            }
         }
      }

   }

   public static void sendBundled(UUID target, Packets collection) {
      if (!var1.isEmpty()) {
         Collection var2 = var1.compile(var0);
         NetworkHandlerImpl var3 = NetworkHandlerImpl.instance;
         if (var3.isBatching()) {
            var3.appendPackets(var0, var2);
         } else {
            ProtectedPacket var4 = new ProtectedPacket(new ClientboundBundlePacket(var2));
            var3.getPipeline(var0).ifPresent((var1x) -> {
               var1x.writeAndFlush(var4);
            });
         }
      }

   }

   public static void sendBundled(Set<UUID> targets, Packets collection) {
      if (!var1.isEmpty()) {
         NetworkHandlerImpl var2 = NetworkHandlerImpl.instance;
         Iterator var3;
         UUID var4;
         if (var2.isBatching()) {
            var3 = var0.iterator();

            while(var3.hasNext()) {
               var4 = (UUID)var3.next();
               var2.appendPackets(var4, var1.compile(var4));
            }
         } else {
            var3 = var0.iterator();

            while(var3.hasNext()) {
               var4 = (UUID)var3.next();
               ModelAPI.getNetworkHandler().getPipeline(var4).ifPresent((var2x) -> {
                  ProtectedPacket var3 = new ProtectedPacket(new ClientboundBundlePacket(var1.compile(var4)));
                  var2x.writeAndFlush(var3);
               });
            }
         }
      }

   }

   public static void sendBundled(Set<UUID> targets, Packets collection, Predicate<Player> predicate) {
      if (!var1.isEmpty()) {
         NetworkHandlerImpl var3 = NetworkHandlerImpl.instance;
         Iterator var4;
         UUID var5;
         Player var6;
         if (var3.isBatching()) {
            var4 = var0.iterator();

            while(var4.hasNext()) {
               var5 = (UUID)var4.next();
               var6 = Bukkit.getPlayer(var5);
               if (var6 != null && var2.test(var6)) {
                  var3.appendPackets(var5, var1.compile(var5));
               }
            }
         } else {
            var4 = var0.iterator();

            while(var4.hasNext()) {
               var5 = (UUID)var4.next();
               var6 = Bukkit.getPlayer(var5);
               if (var6 != null && var2.test(var6)) {
                  ModelAPI.getNetworkHandler().getPipeline(var5).ifPresent((var2x) -> {
                     ProtectedPacket var3 = new ProtectedPacket(new ClientboundBundlePacket(var1.compile(var5)));
                     var2x.writeAndFlush(var3);
                  });
               }
            }
         }
      }

   }

   public static void sendBundledRaw(UUID target, Packets collection) {
      if (!var1.isEmpty()) {
         ClientboundBundlePacket var2 = new ClientboundBundlePacket(var1.compile(var0));
         ModelAPI.getNetworkHandler().getPipeline(var0).ifPresent((var1x) -> {
            var1x.writeAndFlush(var2);
         });
      }

   }

   public static void sendBundledRaw(Set<UUID> targets, Packets collection) {
      if (!var1.isEmpty()) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            UUID var3 = (UUID)var2.next();
            ModelAPI.getNetworkHandler().getPipeline(var3).ifPresent((var2x) -> {
               ClientboundBundlePacket var3x = new ClientboundBundlePacket(var1.compile(var3));
               var2x.writeAndFlush(var3x);
            });
         }
      }

   }

   public static void sendBundledRaw(Set<UUID> targets, Packets collection, Predicate<Player> predicate) {
      if (!var1.isEmpty()) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            UUID var4 = (UUID)var3.next();
            Player var5 = Bukkit.getPlayer(var4);
            if (var5 != null && var2.test(var5)) {
               ModelAPI.getNetworkHandler().getPipeline(var4).ifPresent((var2x) -> {
                  ClientboundBundlePacket var3 = new ClientboundBundlePacket(var1.compile(var4));
                  var2x.writeAndFlush(var3);
               });
            }
         }
      }

   }

   static {
      CLIENTBOUND_CODEC = GameProtocols.b.a(RegistryFriendlyByteBuf.a(MinecraftServer.getServer().bc()));
      SERVERBOUND_CODEC = GameProtocols.a.a(RegistryFriendlyByteBuf.a(MinecraftServer.getServer().bc()));
   }
}
