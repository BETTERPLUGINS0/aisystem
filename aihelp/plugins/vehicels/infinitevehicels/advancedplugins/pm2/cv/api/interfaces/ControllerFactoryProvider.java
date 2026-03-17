package advancedplugins.pm2.cv.api.interfaces;

import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface ControllerFactoryProvider {
   @NotNull
   Collection<VehicleController.Factory> create();
}
