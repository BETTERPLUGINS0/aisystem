package advancedplugins.pm2.cv.api.registry;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.registry.types.ItemConfigurationRegistry;
import advancedplugins.pm2.cv.api.registry.types.UpgradeConfigurationRegistry;
import advancedplugins.pm2.cv.api.registry.types.VehicleConfigurationRegistry;
import advancedplugins.pm2.cv.api.registry.types.VehicleControllerFactoryRegistry;
import advancedplugins.pm2.cv.api.registry.types.VehicleModelConfigurationRegistry;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Registries {
   private static final Map<Class<? extends IDeyed>, Registry<? extends IDeyed>> REGISTRY_MAP = new LinkedHashMap();

   public static void load() {
      Registry var1;
      for(Iterator var0 = REGISTRY_MAP.values().iterator(); var0.hasNext(); var1.load()) {
         var1 = (Registry)var0.next();
         if (var1 instanceof ConfigurationRegistry) {
            ConfigurationRegistry var2 = (ConfigurationRegistry)var1;

            try {
               var2.saveDefaults();
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   public static void reload() {
      Iterator var0 = REGISTRY_MAP.values().iterator();

      while(var0.hasNext()) {
         Registry var1 = (Registry)var0.next();
         if (var1 instanceof ConfigurationRegistry) {
            ConfigurationRegistry var2 = (ConfigurationRegistry)var1;
            var2.reload();
         }
      }

   }

   public static <T extends IDeyed, R extends Registry<T>> R getRegistry(Class<T> var0) {
      return (Registry)REGISTRY_MAP.get(var0);
   }

   static {
      REGISTRY_MAP.put(UpgradeConfiguration.class, new UpgradeConfigurationRegistry());
      REGISTRY_MAP.put(ItemConfiguration.class, new ItemConfigurationRegistry());
      REGISTRY_MAP.put(VehicleModelConfiguration.class, new VehicleModelConfigurationRegistry());
      REGISTRY_MAP.put(VehicleController.Factory.class, new VehicleControllerFactoryRegistry());
      REGISTRY_MAP.put(VehicleConfiguration.class, new VehicleConfigurationRegistry());
   }
}
