package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.FileUtils;
import java.nio.file.Path;
import org.bukkit.plugin.Plugin;

public final class PluginUtils {
   public static Path relativize(Plugin plugin, Path path) {
      Path pluginDataFolder = plugin.getDataFolder().toPath();
      return FileUtils.relativize(pluginDataFolder, path);
   }

   private PluginUtils() {
   }
}
