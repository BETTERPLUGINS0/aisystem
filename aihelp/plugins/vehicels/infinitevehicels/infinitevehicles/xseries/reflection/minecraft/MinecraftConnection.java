package me.PM2.infinitevehicles.xseries.reflection.minecraft;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Objects;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MinecraftConnection {
   public static final MinecraftClassHandle ServerPlayer;
   public static final MinecraftClassHandle CraftPlayer;
   public static final MinecraftClassHandle ServerPlayerConnection;
   public static final MinecraftClassHandle ServerGamePacketListenerImpl;
   public static final MinecraftClassHandle Packet;
   private static final MethodHandle PLAYER_CONNECTION;
   private static final MethodHandle GET_HANDLE;
   private static final MethodHandle SEND_PACKET;

   @NotNull
   public static Object getHandle(@NotNull Player var0) {
      Objects.requireNonNull(var0, "Cannot get handle of null player");

      try {
         return GET_HANDLE.invoke(var0);
      } catch (Throwable var2) {
         throw XReflection.throwCheckedException(var2);
      }
   }

   @Nullable
   public static Object getConnection(@NotNull Player var0) {
      Objects.requireNonNull(var0, "Cannot get connection of null player");

      try {
         Object var1 = GET_HANDLE.invoke(var0);
         return PLAYER_CONNECTION.invoke(var1);
      } catch (Throwable var2) {
         throw XReflection.throwCheckedException(var2);
      }
   }

   @NotNull
   public static void sendPacket(@NotNull Player var0, @NotNull Object... var1) {
      Objects.requireNonNull(var0, () -> {
         return "Can't send packet to null player: " + Arrays.toString(var1);
      });
      Objects.requireNonNull(var1, () -> {
         return "Can't send null packets to player: " + var0;
      });

      try {
         Object var2 = GET_HANDLE.invoke(var0);
         Object var3 = PLAYER_CONNECTION.invoke(var2);
         if (var3 != null) {
            Object[] var4 = var1;
            int var5 = var1.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Object var7 = var4[var6];
               Objects.requireNonNull(var7, "Null packet detected between packets array");
               SEND_PACKET.invoke(var3, var7);
            }
         }

      } catch (Throwable var8) {
         throw new IllegalStateException("Failed to send packet to " + var0 + ": " + Arrays.toString(var1), var8);
      }
   }

   static {
      ServerPlayer = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerPlayer").map(MinecraftMapping.SPIGOT, "EntityPlayer");
      CraftPlayer = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftPlayer");
      ServerPlayerConnection = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.network").map(MinecraftMapping.MOJANG, "ServerPlayerConnection").map(MinecraftMapping.SPIGOT, "PlayerConnection");
      ServerGamePacketListenerImpl = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.network").map(MinecraftMapping.MOJANG, "ServerGamePacketListenerImpl").map(MinecraftMapping.SPIGOT, "PlayerConnection");
      Packet = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol").map(MinecraftMapping.SPIGOT, "Packet");
      PLAYER_CONNECTION = (MethodHandle)ServerPlayer.field().getter().returns((ClassHandle)ServerGamePacketListenerImpl).map(MinecraftMapping.MOJANG, "connection").map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, 6, (Object)"g").v(21, 2, (Object)"f").v(20, "c").v(17, "b").orElse((Object)"playerConnection")).unreflect();
      GET_HANDLE = (MethodHandle)CraftPlayer.method().named("getHandle").returns((ClassHandle)ServerPlayer).unreflect();
      SEND_PACKET = (MethodHandle)ServerPlayerConnection.method().returns(Void.TYPE).parameters(Packet).map(MinecraftMapping.MOJANG, "send").map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(20, 2, (Object)"b").v(18, "a").orElse((Object)"sendPacket")).unreflect();
   }
}
