package advancedplugins.pm2.cv.api.configuration;

import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class AdminLogs {
   private static final Logger logger = Logger.getLogger("AdminLogs");
   private static File logFile;
   private static YamlConfiguration logConfig;
   private static List<String> logs;

   public static void load(@NotNull InfiniteVehiclesPluginBase var0) {
      logFile = new File(var0.getDataFolder(), "AdminLogs.yml");
      boolean var1 = !logFile.exists();
      if (var1) {
         logFile.getParentFile().mkdirs();

         try {
            Files.createFile(logFile.toPath());
         } catch (IOException var3) {
            throw new IllegalStateException("Couldn't generate admin logs file", var3);
         }
      }

      logConfig = YamlConfiguration.loadConfiguration(logFile);
      logs = logConfig.getStringList("logs");
      if (var1) {
         save();
      }

   }

   public static void logVehiclePickup(String var0, String var1, String var2) {
      String var3 = System.currentTimeMillis() + " - " + var0 + " picked up a " + var1 + " vehicle. Original Owner: " + var2;
      logs.add(var3);
      save();
   }

   private static void save() {
      logConfig.set("logs", logs);

      try {
         logConfig.save(logFile);
      } catch (IOException var1) {
         logger.severe("Failed to save admin logs: " + var1.getMessage());
      }

   }
}
