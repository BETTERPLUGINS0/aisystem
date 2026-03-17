package advancedplugins.pm2.cv.compatibility;

import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompatibilityHandler extends PluginHandlerAdapter {
   public CompatibilityHandler(JavaPlugin plugin) {
      this.setup(var1);
   }

   private void setup(JavaPlugin plugin) {
      if (Configuration.COMPATIBILITY_WEAPON_MECHANICS.booleanValue()) {
         new WeaponMechanicsCompatibility(var1);
      }

   }
}
