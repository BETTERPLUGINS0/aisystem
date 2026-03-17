package me.PM2.infinitevehicles.xseries.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ProfileLogger {
   public static final Logger LOGGER = LogManager.getLogger("XSkull");

   public static void debug(String var0, Object... var1) {
      LOGGER.debug(var0, var1);
   }
}
