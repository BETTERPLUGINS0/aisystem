package org.terraform.utils.blockdata;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;

public class AgeableBuilder {
   @NotNull
   private final Ageable blockData;

   public AgeableBuilder(@NotNull Material mat) {
      this.blockData = (Ageable)Bukkit.createBlockData(mat);
   }

   @NotNull
   public AgeableBuilder setRandomAge(@NotNull Random rand) {
      this.blockData.setAge(rand.nextInt(this.blockData.getMaximumAge() + 1));
      return this;
   }

   @NotNull
   public AgeableBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }
}
