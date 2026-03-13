package org.terraform.watchdog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.Nullable;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class TfgWatchdogSuppressant {
   @Nullable
   Field instanceField = null;
   @Nullable
   Field lastTickField = null;
   @Nullable
   Class<?> watchdogThreadClass = null;
   @Nullable
   Method tickMethod = null;
   @Nullable
   Object watchdogThreadInstance = null;

   public TfgWatchdogSuppressant() {
      if (TConfig.c.DEVSTUFF_SUPPRESS_WATCHDOG) {
         try {
            TerraformGeneratorPlugin.logger.info("[NOTICE] TerraformGenerator will suppress the server's watchdog while generating chunks to prevent unnecessary stacktrace warnings. Unless you specifically need thewatchdog now (to take aikar timings or debug lag), you don't need to take any action.");
            TerraformGeneratorPlugin.logger.info("It is recommended to pregenerate to reduce lag problems.");
            Class<?> watchdogThreadClass = Class.forName("org.spigotmc.WatchdogThread");
            this.instanceField = watchdogThreadClass.getDeclaredField("instance");
            this.instanceField.setAccessible(true);
            this.lastTickField = watchdogThreadClass.getDeclaredField("lastTick");
            this.lastTickField.setAccessible(true);
            this.tickMethod = watchdogThreadClass.getDeclaredMethod("tick");
            this.tickMethod.setAccessible(true);
            this.watchdogThreadInstance = this.instanceField.get((Object)null);
            TerraformGeneratorPlugin.logger.info("Watchdog Thread hooked.");
         } catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | SecurityException var2) {
            TerraformGeneratorPlugin.logger.info("Watchdog instance could not be found.");
            TerraformGeneratorPlugin.logger.stackTrace(var2);
            this.instanceField = null;
            this.lastTickField = null;
            this.watchdogThreadClass = null;
            this.watchdogThreadInstance = null;
            this.tickMethod = null;
         }
      }

   }

   public void tickWatchdog() {
      if (this.watchdogThreadInstance != null) {
         try {
            if ((Long)this.lastTickField.get(this.watchdogThreadInstance) != 0L) {
               this.tickMethod.invoke(this.watchdogThreadInstance);
            }
         } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var2) {
            TerraformGeneratorPlugin.logger.stackTrace(var2);
            TerraformGeneratorPlugin.logger.info("Failed to tick watchdog");
         }

      }
   }
}
