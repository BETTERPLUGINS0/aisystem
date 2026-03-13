package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.terra.api.world.info.WorldProperties;
import net.minecraft.world.level.LevelHeightAccessor;

public class NMSWorldProperties implements WorldProperties {
   private final long seed;
   private final LevelHeightAccessor height;

   public NMSWorldProperties(long seed, LevelHeightAccessor height) {
      this.seed = seed;
      this.height = height;
   }

   public Object getHandle() {
      return this.height;
   }

   public long getSeed() {
      return this.seed;
   }

   public int getMaxHeight() {
      return this.height.getMaxY();
   }

   public int getMinHeight() {
      return this.height.getMinY();
   }
}
