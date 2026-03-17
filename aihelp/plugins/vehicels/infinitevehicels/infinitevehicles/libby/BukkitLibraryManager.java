package me.PM2.infinitevehicles.libby;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;
import me.PM2.infinitevehicles.libby.classloader.URLClassLoaderHelper;
import me.PM2.infinitevehicles.libby.logging.adapters.JDKLogAdapter;
import org.bukkit.plugin.Plugin;

public class BukkitLibraryManager extends LibraryManager {
   private final URLClassLoaderHelper classLoader;

   public BukkitLibraryManager(Plugin plugin) {
      this(var1, "lib");
   }

   public BukkitLibraryManager(Plugin plugin, String directoryName) {
      super(new JDKLogAdapter(((Plugin)Objects.requireNonNull(var1, "plugin")).getLogger()), var1.getDataFolder().toPath(), var2);
      this.classLoader = new URLClassLoaderHelper((URLClassLoader)var1.getClass().getClassLoader(), this);
   }

   protected void addToClasspath(Path file) {
      this.classLoader.addToClasspath(var1);
   }
}
