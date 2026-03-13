package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.world.info.WorldProperties;
import org.bukkit.generator.WorldInfo;

public class BukkitWorldProperties implements WorldProperties {
   private final WorldInfo delegate;

   public BukkitWorldProperties(WorldInfo delegate) {
      this.delegate = delegate;
   }

   public Object getHandle() {
      return this.delegate;
   }

   public long getSeed() {
      return this.delegate.getSeed();
   }

   public int getMaxHeight() {
      return this.delegate.getMaxHeight();
   }

   public int getMinHeight() {
      return this.delegate.getMinHeight();
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj instanceof WorldProperties) {
         WorldProperties that = (WorldProperties)obj;
         return this.delegate.equals(that.getHandle());
      } else {
         return false;
      }
   }
}
