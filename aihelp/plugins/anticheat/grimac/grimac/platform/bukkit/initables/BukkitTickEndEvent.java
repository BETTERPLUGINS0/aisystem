package ac.grim.grimac.platform.bukkit.initables;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.start.AbstractTickEndEvent;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayer;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.lists.HookedListWrapper;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import sun.misc.Unsafe;

public class BukkitTickEndEvent extends AbstractTickEndEvent implements Listener {
   private Boolean getLateBindState() {
      Class<?> spigotConfig = ReflectionUtils.getClass("org.spigotmc.SpigotConfig");
      Field field = ReflectionUtils.getField(spigotConfig, "lateBind");
      if (field == null) {
         return null;
      } else {
         try {
            return (Boolean)field.get((Object)null);
         } catch (Exception var4) {
            return null;
         }
      }
   }

   public void start() {
      if (super.shouldInjectEndTick()) {
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_11_2) && !Boolean.getBoolean("paper.explicit-flush")) {
            LogUtil.warn("Reach.enable-post-packet=true but paper.explicit-flush=false, add \"-Dpaper.explicit-flush=true\" to your server's startup flags for fully functional extra reach accuracy.");
         }

         if (GrimAPI.INSTANCE.getPlatform() == Platform.FOLIA) {
            PaperUtils.registerTickEndEvent(this, this::tickAllFoliaPlayers);
         } else {
            if (!PaperUtils.registerTickEndEvent(this, this::tickAllPlayers) && !this.injectWithReflection()) {
               LogUtil.error("Failed to inject into the end of tick event!");
               if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_14_4)) {
                  Boolean lateBind = this.getLateBindState();
                  if (lateBind == null) {
                     LogUtil.error("Failed to determine the late-bind state. Perhaps you are using a custom server fork? Check the fork configuration for a late-bind option and disable it.");
                  } else if (lateBind) {
                     LogUtil.error("Injection failed because the late-bind option is enabled. Disable it in spigot.yml.");
                  }
               }
            }

         }
      }
   }

   private void tickAllPlayers() {
      Iterator var1 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var1.hasNext()) {
         GrimPlayer player = (GrimPlayer)var1.next();
         if (!player.disableGrim) {
            super.onEndOfTick(player);
         }
      }

   }

   private void tickAllFoliaPlayers() {
      Iterator var1 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var1.hasNext()) {
         GrimPlayer player = (GrimPlayer)var1.next();
         if (!player.disableGrim && player.platformPlayer != null) {
            Player p = ((BukkitPlatformPlayer)player.platformPlayer).getNative();
            if (Bukkit.isOwnedByCurrentRegion(p)) {
               super.onEndOfTick(player);
            }
         }
      }

   }

   private boolean injectWithReflection() {
      try {
         Object connection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
         if (connection == null) {
            return false;
         } else {
            Field connectionsList = Reflection.getField(connection.getClass(), List.class, 1);
            List<Object> endOfTickObject = (List)connectionsList.get(connection);
            List<?> wrapper = Collections.synchronizedList(new HookedListWrapper<Object>(endOfTickObject) {
               public void onIterator() {
                  BukkitTickEndEvent.this.tickAllPlayers();
               }
            });
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe)unsafeField.get((Object)null);
            unsafe.putObject(connection, unsafe.objectFieldOffset(connectionsList), wrapper);
            return true;
         }
      } catch (IllegalAccessException | NoSuchFieldException var7) {
         LogUtil.error("Failed to inject into the end of tick event via reflection", var7);
         return false;
      }
   }
}
