package me.SuperRonanCraft.BetterRTP.references;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;

public class RTPLogger {
   private String format;
   private File file;
   private FileHandler handler;

   public void setup(BetterRTP plugin) {
      FileOther.FILETYPE config = plugin.getFiles().getType(FileOther.FILETYPE.CONFIG);
      boolean enabled = config.getBoolean("Settings.Logger.Enabled");
      Logger logger = plugin.getLogger();
      logger.setUseParentHandlers(true);
      if (this.handler != null) {
         logger.removeHandler(this.handler);
         this.handler.close();
      }

      if (enabled) {
         this.format = config.getString("Settings.Logger.Format");
         boolean toConsole = config.getBoolean("Settings.Logger.LogToConsole");

         try {
            this.file = new File(plugin.getDataFolder() + File.separator + "log.txt");
            Files.deleteIfExists(this.file.toPath());
            this.handler = new FileHandler(this.file.getPath(), true);
            this.handler.setFormatter(new RTPLogger.MyFormatter());
            logger.setUseParentHandlers(toConsole);
            logger.addHandler(this.handler);
         } catch (IOException var7) {
            var7.printStackTrace();
         }

      }
   }

   private static String getDate() {
      SimpleDateFormat format = new SimpleDateFormat(BetterRTP.getInstance().getRtpLogger().getFormat());
      return format.format(new Date());
   }

   public void unload() {
      if (this.handler != null) {
         this.handler.close();
      }

   }

   public String getFormat() {
      return this.format;
   }

   public File getFile() {
      return this.file;
   }

   static class MyFormatter extends Formatter {
      public String format(LogRecord record) {
         return RTPLogger.getDate() + " [" + record.getLevel().getName() + "]: " + record.getMessage() + '\n';
      }
   }
}
