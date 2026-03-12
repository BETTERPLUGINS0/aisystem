package libs.com.ryderbelserion.vital.paper;

import java.io.File;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.paper.api.files.FileManager;
import libs.com.ryderbelserion.vital.paper.modules.EventRegistry;
import libs.com.ryderbelserion.vital.paper.modules.ModuleLoader;
import libs.com.ryderbelserion.vital.paper.util.scheduler.PaperScheduler;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Vital extends JavaPlugin implements VitalAPI {
   private PaperScheduler scheduler;
   private FileManager fileManager;
   private ModuleLoader loader;

   public Vital() {
      this.start();
   }

   public void start() {
      VitalAPI.super.start();
      this.loader = new ModuleLoader(new EventRegistry(this));
      this.scheduler = new PaperScheduler(this);
      this.fileManager = new FileManager();
      this.getDirectory().mkdirs();
   }

   public final File getDirectory() {
      return this.getDataFolder();
   }

   public final FileManager getFileManager() {
      return this.fileManager;
   }

   public final File getModsDirectory() {
      return this.getServer().getPluginsFolder();
   }

   public void saveResource(@NotNull String fileName, boolean replace) {
      super.saveResource(fileName, replace);
   }

   @NotNull
   public final ComponentLogger getComponentLogger() {
      return super.getComponentLogger();
   }

   public final PaperScheduler getScheduler() {
      return this.scheduler;
   }

   public final String getPluginName() {
      return this.getName();
   }

   public final ModuleLoader getLoader() {
      return this.loader;
   }
}
