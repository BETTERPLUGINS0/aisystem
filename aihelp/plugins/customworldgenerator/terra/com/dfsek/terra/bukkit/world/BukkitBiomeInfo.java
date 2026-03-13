package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.properties.Properties;
import org.bukkit.NamespacedKey;

public record BukkitBiomeInfo(NamespacedKey biomeKey) implements Properties {
   public BukkitBiomeInfo(NamespacedKey biomeKey) {
      this.biomeKey = biomeKey;
   }

   public NamespacedKey biomeKey() {
      return this.biomeKey;
   }
}
