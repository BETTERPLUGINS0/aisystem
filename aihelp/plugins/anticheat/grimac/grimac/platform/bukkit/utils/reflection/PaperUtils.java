package ac.grim.grimac.platform.bukkit.utils.reflection;

import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PaperUtils {
   public static final boolean PAPER = ReflectionUtils.hasClass("com.destroystokyo.paper.PaperConfig") || ReflectionUtils.hasClass("io.papermc.paper.configuration.Configuration");

   public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
      return PAPER ? entity.teleportAsync(location) : CompletableFuture.completedFuture(entity.teleport(location));
   }

   public static boolean registerTickEndEvent(Listener listener, Runnable runnable) {
      try {
         Class<?> clazz = ReflectionUtils.getClass("com.destroystokyo.paper.event.server.ServerTickEndEvent");
         if (clazz == null) {
            return false;
         } else {
            GrimACBukkitLoaderPlugin.LOADER.getServer().getPluginManager().registerEvent(clazz, listener, EventPriority.NORMAL, (l, event) -> {
               runnable.run();
            }, GrimACBukkitLoaderPlugin.LOADER);
            return true;
         }
      } catch (Exception var3) {
         LogUtil.error("Failed to register tick end event", var3);
         return false;
      }
   }
}
