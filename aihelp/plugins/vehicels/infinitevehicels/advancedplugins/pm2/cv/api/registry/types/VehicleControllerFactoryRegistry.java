package advancedplugins.pm2.cv.api.registry.types;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.interfaces.ControllerFactoryProvider;
import advancedplugins.pm2.cv.api.registry.RegistryBase;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.DrillGroundVehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.FixedWingVehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.GroundVehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.HybridVehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.RotorcraftVehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.SwapHotkeyCustomActionController;
import advancedplugins.pm2.cv.api.vehicle.controller.predefined.WatercraftVehicleController;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleControllerFactoryRegistry extends RegistryBase<VehicleController.Factory> {
   public VehicleControllerFactoryRegistry() {
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "fixed-wing-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new FixedWingVehicleController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "ground-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new GroundVehicleController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "hybrid-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new HybridVehicleController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "rotorcraft-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new RotorcraftVehicleController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "watercraft-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new WatercraftVehicleController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "swap-hotkey-custom-action";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new SwapHotkeyCustomActionController(var1, var2);
         }
      });
      this.register(new VehicleController.Factory() {
         @NotNull
         public String getControllerId() {
            return "drill-ground-vehicle";
         }

         @NotNull
         public VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
            return new DrillGroundVehicleController(var1, var2);
         }
      });
   }

   public void load() {
      File var1 = Constants.Files.VEHICLE_CONTROLLERS_FOLDER;
      File[] var2 = var1.listFiles((var0, var1x) -> {
         return var1x.toLowerCase().endsWith(".jar");
      });

      try {
         int var3 = 0;
         if (var2 != null && var2.length > 0) {
            var3 = this.load(var2);
         }

         InfiniteVehicles.getPlugin().getLogger().info(var3 > 0 ? var3 + " custom controllers were loaded!" : "No custom controllers were loaded!");
      } catch (MalformedURLException var4) {
         var4.printStackTrace();
      }

   }

   private int load(File... var1) {
      int var2 = 0;

      try {
         URLClassLoader var3 = new URLClassLoader(this.getFileURLs(var1), this.getClass().getClassLoader());

         try {
            File[] var4 = var1;
            int var5 = var1.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               File var7 = var4[var6];
               var2 += this.load(var3, var7);
            }
         } catch (Throwable var9) {
            try {
               var3.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var3.close();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      return var2;
   }

   private int load(URLClassLoader var1, File var2) {
      int var3 = 0;

      try {
         JarFile var4 = new JarFile(var2);

         try {
            Enumeration var5 = var4.entries();

            label47:
            while(var5.hasMoreElements()) {
               JarEntry var6 = (JarEntry)var5.nextElement();
               if (var6.getName().endsWith(".class")) {
                  String var7 = var6.getName().replaceAll("/", ".").replace(".class", "");
                  Class var8 = var1.loadClass(var7);
                  if (var8 != null && ControllerFactoryProvider.class.isAssignableFrom(var8)) {
                     ControllerFactoryProvider var9 = (ControllerFactoryProvider)var8.getDeclaredConstructor().newInstance();
                     Iterator var10 = var9.create().iterator();

                     while(true) {
                        if (!var10.hasNext()) {
                           break label47;
                        }

                        VehicleController.Factory var11 = (VehicleController.Factory)var10.next();
                        if (var11 != null) {
                           this.register(var11);
                           ++var3;
                        } else {
                           InfiniteVehicles.getPlugin().getLogger().info("Factory provider " + var8.getName() + " provided a null factory");
                        }
                     }
                  }
               }
            }
         } catch (Throwable var13) {
            try {
               var4.close();
            } catch (Throwable var12) {
               var13.addSuppressed(var12);
            }

            throw var13;
         }

         var4.close();
      } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IOException var14) {
         var14.printStackTrace();
      }

      return var3;
   }

   private URL[] getFileURLs(File... var1) {
      URL[] var2 = new URL[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1[var3].toURI().toURL();
      }

      return var2;
   }
}
