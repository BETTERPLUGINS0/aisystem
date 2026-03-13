package libs.com.ryderbelserion.vital.common;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.lang.reflect.Field;
import libs.com.ryderbelserion.vital.common.api.Provider;
import libs.com.ryderbelserion.vital.common.api.interfaces.IScheduler;
import libs.com.ryderbelserion.vital.common.config.ConfigManager;
import libs.com.ryderbelserion.vital.common.config.beans.Plugin;
import libs.com.ryderbelserion.vital.common.config.keys.ConfigKeys;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

public interface VitalAPI {
   default void start() {
      try {
         Field api = Provider.class.getDeclaredField("api");
         api.setAccessible(true);
         api.set((Object)null, this);
      } catch (IllegalAccessException | NoSuchFieldException var2) {
         throw new RuntimeException(var2);
      }

      ConfigManager.load();
   }

   default void reload() {
      ConfigManager.reload();
   }

   default void stop() {
      ConfigManager.reload();
   }

   default boolean isVerbose() {
      return this.getPluginData().verbose;
   }

   default <F> F getFileManager() {
      return null;
   }

   default File getModsDirectory() {
      return null;
   }

   default File getDirectory() {
      return null;
   }

   default void saveResource(@NotNull String fileName, boolean replace) {
   }

   default ComponentLogger getComponentLogger() {
      return null;
   }

   default IScheduler getScheduler() {
      return null;
   }

   default String getPluginName() {
      return null;
   }

   default GsonBuilder getBuilder() {
      return (new GsonBuilder()).disableHtmlEscaping().enableComplexMapKeySerialization();
   }

   default Plugin getPluginData() {
      return (Plugin)ConfigManager.getConfig().getProperty(ConfigKeys.settings);
   }
}
