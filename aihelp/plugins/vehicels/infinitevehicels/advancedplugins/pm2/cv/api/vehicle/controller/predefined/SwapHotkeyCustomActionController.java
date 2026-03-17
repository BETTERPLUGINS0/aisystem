package advancedplugins.pm2.cv.api.vehicle.controller.predefined;

import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwapHotkeyCustomActionController extends VehicleController {
   @Nullable
   protected String operatorCommand;

   public SwapHotkeyCustomActionController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      super(var1, var2);
   }

   public void loadProperties() {
      this.operatorCommand = this.properties.getStringProperty("operator-command", (String)null);
   }

   public void process(@NotNull PlayerInput var1) {
      if (var1.isSwapHotkey()) {
         Entity var2 = this.vehicle.getOperator();
         if (StringUtils.isNotBlank(this.operatorCommand) && var2 != null) {
            Run.sync(() -> {
               Bukkit.getServer().dispatchCommand(var2, this.operatorCommand);
            });
         }

      }
   }

   public void tick() {
   }

   public void process(@NotNull PlayerSteerInput var1) {
   }

   public void standby() {
   }
}
