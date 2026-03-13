package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.BukkitAddon;
import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.VanillaBiomeProperties;

public class NMSAddon extends BukkitAddon {
   public NMSAddon(PlatformImpl platform) {
      super(platform);
   }

   public void initialize() {
      super.initialize();
      ((FunctionalEventHandler)this.terraBukkitPlugin.getEventManager().getHandler(FunctionalEventHandler.class)).register(this, ConfigurationLoadEvent.class).then((event) -> {
         if (event.is(Biome.class)) {
            ((Biome)event.getLoadedObject(Biome.class)).getContext().put((Properties)event.load(new VanillaBiomeProperties()));
         }

      }).global();
   }
}
