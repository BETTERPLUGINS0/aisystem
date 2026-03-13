package com.dfsek.terra.bukkit;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;

public class BukkitAddon implements BaseAddon {
   private static final Version VERSION = Versions.getVersion(1, 0, 0);
   protected final PlatformImpl terraBukkitPlugin;

   public BukkitAddon(PlatformImpl terraBukkitPlugin) {
      this.terraBukkitPlugin = terraBukkitPlugin;
   }

   public void initialize() {
      ((FunctionalEventHandler)this.terraBukkitPlugin.getEventManager().getHandler(FunctionalEventHandler.class)).register(this, ConfigPackPreLoadEvent.class).then((event) -> {
         event.getPack().getContext().put((Properties)event.loadTemplate(new PreLoadCompatibilityOptions()));
      }).global();
   }

   public Version getVersion() {
      return VERSION;
   }

   public String getID() {
      return "terra-bukkit";
   }
}
