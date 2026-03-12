package fr.xephi.authme.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportUtils {
   private static MethodHandle teleportAsyncMethodHandle;

   public static void teleport(Player player, Location location) {
      if (teleportAsyncMethodHandle != null) {
         try {
            teleportAsyncMethodHandle.invoke(player, location);
         } catch (Throwable var3) {
            player.teleport(location);
         }
      } else {
         player.teleport(location);
      }

   }

   static {
      try {
         Lookup lookup = MethodHandles.lookup();
         teleportAsyncMethodHandle = lookup.findVirtual(Player.class, "teleportAsync", MethodType.methodType(CompletableFuture.class, Location.class));
      } catch (IllegalAccessException | NoSuchMethodException var1) {
         teleportAsyncMethodHandle = null;
      }

   }
}
