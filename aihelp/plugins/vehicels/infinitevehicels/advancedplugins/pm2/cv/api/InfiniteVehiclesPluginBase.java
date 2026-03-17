package advancedplugins.pm2.cv.api;

import advancedplugins.pm2.cv.api.menu.MenuManager;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class InfiniteVehiclesPluginBase extends JavaPlugin {
   @NotNull
   public abstract File getJarFile();

   @NotNull
   public abstract MenuManager getMenuManager();
}
