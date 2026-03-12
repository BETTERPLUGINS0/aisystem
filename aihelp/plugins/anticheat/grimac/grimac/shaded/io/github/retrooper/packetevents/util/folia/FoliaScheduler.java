package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class FoliaScheduler {
   static final boolean isFolia;
   private static Class<? extends Event> regionizedServerInitEventClass;
   private static AsyncScheduler asyncScheduler;
   private static EntityScheduler entityScheduler;
   private static GlobalRegionScheduler globalRegionScheduler;
   private static RegionScheduler regionScheduler;

   public static boolean isFolia() {
      return isFolia;
   }

   public static AsyncScheduler getAsyncScheduler() {
      if (asyncScheduler == null) {
         asyncScheduler = new AsyncScheduler();
      }

      return asyncScheduler;
   }

   public static EntityScheduler getEntityScheduler() {
      if (entityScheduler == null) {
         entityScheduler = new EntityScheduler();
      }

      return entityScheduler;
   }

   public static GlobalRegionScheduler getGlobalRegionScheduler() {
      if (globalRegionScheduler == null) {
         globalRegionScheduler = new GlobalRegionScheduler();
      }

      return globalRegionScheduler;
   }

   public static RegionScheduler getRegionScheduler() {
      if (regionScheduler == null) {
         regionScheduler = new RegionScheduler();
      }

      return regionScheduler;
   }

   public static void runTaskOnInit(Plugin plugin, Runnable run) {
      if (!isFolia) {
         Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
      } else {
         Bukkit.getServer().getPluginManager().registerEvent(regionizedServerInitEventClass, new Listener() {
         }, EventPriority.HIGHEST, (listener, event) -> {
            run.run();
         }, plugin);
      }
   }

   static {
      boolean folia;
      try {
         Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
         folia = true;
         regionizedServerInitEventClass = Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
      } catch (ClassNotFoundException var2) {
         folia = false;
      }

      isFolia = folia;
   }
}
