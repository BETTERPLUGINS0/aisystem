package me.SuperRonanCraft.BetterRTP.lib.folialib.impl;

import java.util.logging.Logger;
import me.SuperRonanCraft.BetterRTP.lib.folialib.FoliaLib;
import org.bukkit.plugin.java.JavaPlugin;

public class UnsupportedImplementation extends LegacySpigotImplementation {
   public UnsupportedImplementation(FoliaLib foliaLib) {
      super(foliaLib);
      JavaPlugin plugin = foliaLib.getPlugin();
      Logger logger = plugin.getLogger();
      logger.warning(String.format("\n---------------------------------------------------------------------\nFoliaLib does not support this server software! (%s)\nFoliaLib will attempt to use the default spigot implementation.\n---------------------------------------------------------------------\n", plugin.getServer().getVersion()));
   }
}
