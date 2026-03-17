package advancedplugins.pm2.cv.api;

import advancedplugins.pm2.cv.api.configuration.AdminLogs;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.configuration.LeaderboardGuiConfiguration;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.handler.PluginHandler;
import advancedplugins.pm2.cv.api.handler.VehicleHandler;
import advancedplugins.pm2.cv.api.service.BlockInfoService;
import advancedplugins.pm2.cv.api.service.Service;
import advancedplugins.pm2.cv.api.service.TexturedHeadService;
import advancedplugins.pm2.cv.api.util.Run;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class InfiniteVehicles {
   private static InfiniteVehiclesPluginBase PLUGIN_INSTANCE;
   private static final Map<Class<? extends PluginHandler>, PluginHandler> HANDLER_MAP = new HashMap();
   private static final Map<Class<? extends Service>, Service> SERVICE_MAP = new HashMap();

   @NotNull
   public static InfiniteVehiclesPluginBase getPlugin() {
      return PLUGIN_INSTANCE;
   }

   @Internal
   public static void initialize(@NotNull InfiniteVehiclesPluginBase var0) {
      if (PLUGIN_INSTANCE != null) {
         throw new IllegalStateException("api already initialized");
      } else {
         PLUGIN_INSTANCE = var0;
         Configuration.load(var0);
         LangConfiguration.load(var0);

         try {
            GuiConfiguration.load(var0);
         } catch (InvalidConfigurationException var4) {
            var4.printStackTrace();
         }

         try {
            LeaderboardGuiConfiguration.load(var0);
         } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
         }

         try {
            AdminLogs.load(var0);
         } catch (InvalidConfigurationException var2) {
            var2.printStackTrace();
         }

      }
   }

   @Internal
   public static void disable() {
      if (PLUGIN_INSTANCE != null) {
         Iterator var0 = HANDLER_MAP.values().iterator();

         while(var0.hasNext()) {
            PluginHandler var1 = (PluginHandler)var0.next();
            var1.onPluginDisable();
         }

         var0 = Run.ASYNC_TASKS.iterator();

         while(var0.hasNext()) {
            int var2 = (Integer)var0.next();
            Bukkit.getScheduler().cancelTask(var2);
         }

         Run.ASYNC_TASKS.clear();
      }
   }

   @Nullable
   public static <T extends PluginHandler> T getHandler(@NotNull Class<T> var0) {
      return (PluginHandler)var0.cast(HANDLER_MAP.get(var0));
   }

   @NotNull
   public static PlayerWrapperHandler getPlayerWrapperHandler() {
      return (PlayerWrapperHandler)getVitalHandler(PlayerWrapperHandler.class);
   }

   @NotNull
   public static VehicleHandler getVehicleHandler() {
      return (VehicleHandler)getVitalHandler(VehicleHandler.class);
   }

   public static void registerHandler(@NotNull Class<? extends PluginHandler> var0, @NotNull PluginHandler var1) {
      if (HANDLER_MAP.containsKey(var0)) {
         throw new IllegalArgumentException("handler already registered: " + var0.getName());
      } else {
         HANDLER_MAP.put(var0, var1);
      }
   }

   @NotNull
   private static <T extends PluginHandler> T getVitalHandler(@NotNull Class<T> var0) {
      PluginHandler var1 = getHandler(var0);
      return var1;
   }

   @Nullable
   public static <T extends Service> T getService(@NotNull Class<T> var0) {
      return (Service)var0.cast(SERVICE_MAP.get(var0));
   }

   @NotNull
   public static BlockInfoService getBlockInfoService() {
      return (BlockInfoService)getVitalService(BlockInfoService.class);
   }

   @NotNull
   public static TexturedHeadService getTexturedHeadService() {
      return (TexturedHeadService)getVitalService(TexturedHeadService.class);
   }

   public static void registerService(@NotNull Class<? extends Service> var0, @NotNull Service var1) {
      if (SERVICE_MAP.containsKey(var0)) {
         throw new IllegalArgumentException("service already registered");
      } else {
         SERVICE_MAP.put(var0, var1);
      }
   }

   @NotNull
   public static <T extends Service> T getVitalService(@NotNull Class<T> var0) {
      Service var1 = getService(var0);
      return var1;
   }
}
