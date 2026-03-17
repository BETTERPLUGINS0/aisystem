package advancedplugins.pm2.cv.api.vehicle.controller;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.upgrade.Upgrade;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.upgrade.UpgradeTier;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class VehicleController {
   @NotNull
   protected final Vehicle vehicle;
   @NotNull
   protected VehicleControllerProperties properties;
   @NotNull
   protected final VehicleControllerProperties finalProperties;

   public VehicleController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      this.vehicle = var1;
      this.properties = var2 != null ? var2 : new VehicleControllerProperties();
      this.finalProperties = this.properties;
      this.loadProperties();
   }

   public abstract void loadProperties();

   @NotNull
   public Vehicle getVehicle() {
      return this.vehicle;
   }

   @NotNull
   public VehicleControllerProperties getProperties() {
      return this.properties;
   }

   public void tick() {
      UpgradeConfiguration var1 = this.vehicle.getUpgradeConfiguration();
      VehicleControllerProperties var2 = new VehicleControllerProperties();
      var2.merge(this.finalProperties);
      if (var1 != null) {
         Iterator var3 = var1.getUpgrades().iterator();

         while(var3.hasNext()) {
            Upgrade var4 = (Upgrade)var3.next();
            int var5 = (Integer)this.vehicle.getUpgradeTiers().getOrDefault(var4.getId(), -1);
            int var6 = ((UpgradeTier)var4.getUpgradeTiers().values().iterator().next()).getTier();
            if (var5 < var6) {
               var5 = var6 - 1;
            }

            if (var5 != -1) {
               UpgradeTier var7 = (UpgradeTier)var4.getUpgradeTiers().get(var5);
               if (var7 != null) {
                  var2.merge(var7.getUpgradeProperties());
                  this.properties = var2;
               }
            }
         }
      }

      this.loadProperties();
   }

   public abstract void process(@NotNull PlayerSteerInput var1);

   public void process(@NotNull PlayerInput var1) {
   }

   public abstract void standby();

   public abstract static class Factory implements IDeyed {
      @NotNull
      public abstract String getControllerId();

      @NotNull
      public abstract VehicleController createInstance(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2);

      @NotNull
      public final String getId() {
         return IDeyed.idCheck(this.getControllerId());
      }
   }
}
