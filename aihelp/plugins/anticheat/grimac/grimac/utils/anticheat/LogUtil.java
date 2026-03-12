package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import lombok.Generated;

public final class LogUtil {
   public static void info(String info) {
      getLogger().info(info);
   }

   public static void warn(String warn) {
      getLogger().warning(warn);
   }

   public static void warn(String description, Throwable throwable) {
      Logger logger = getLogger();
      if (logger != null) {
         logger.warning(description + ": " + getStackTrace(throwable));
      } else {
         throwable.printStackTrace();
      }

   }

   public static void error(String error) {
      getLogger().severe(error);
   }

   public static void error(String description, Throwable throwable) {
      Logger logger = getLogger();
      if (logger != null) {
         logger.severe(description + ": " + getStackTrace(throwable));
      } else {
         throwable.printStackTrace();
      }

   }

   public static void error(Throwable throwable) {
      Logger logger = getLogger();
      if (logger != null) {
         logger.severe(getStackTrace(throwable));
      } else {
         throwable.printStackTrace();
      }

   }

   public static Logger getLogger() {
      return GrimAPI.INSTANCE.getGrimPlugin().getLogger();
   }

   public static void console(String info) {
      GrimAPI.INSTANCE.getPlatformServer().getConsoleSender().sendMessage(MessageUtil.translateAlternateColorCodes('&', info));
   }

   public static void console(Component info) {
      GrimAPI.INSTANCE.getPlatformServer().getConsoleSender().sendMessage(info);
   }

   private static String getStackTrace(Throwable throwable) {
      String message = throwable.getMessage();

      try {
         StringWriter sw = new StringWriter();

         try {
            PrintWriter pw = new PrintWriter(sw);

            try {
               throwable.printStackTrace(pw);
               message = sw.toString();
            } catch (Throwable var8) {
               try {
                  pw.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            pw.close();
         } catch (Throwable var9) {
            try {
               sw.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         sw.close();
      } catch (Exception var10) {
      }

      return message;
   }

   @Generated
   private LogUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
