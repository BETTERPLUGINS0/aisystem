package advancedplugins.pm2.cv.api.service;

import es.outlook.adriansrj.spigui.SpiGUI;
import org.jetbrains.annotations.NotNull;

public interface GuiBuilderService extends Service {
   @NotNull
   SpiGUI get();
}
