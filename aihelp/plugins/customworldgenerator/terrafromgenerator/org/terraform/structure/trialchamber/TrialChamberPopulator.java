package org.terraform.structure.trialchamber;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.VanillaStructurePopulator;
import org.terraform.utils.GenUtils;

public class TrialChamberPopulator extends VanillaStructurePopulator {
   public TrialChamberPopulator() {
      super("trial_chambers");
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      return this.isEnabled() && this.rollSpawnRatio(tw, chunkX, chunkZ);
   }

   public void populate(TerraformWorld tw, PopulatorDataAbstract data) {
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(670191632L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_TRIALCHAMBER_ENABLED;
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 19650), (int)(TConfig.c.STRUCTURES_TRIALCHAMBER_SPAWNRATIO * 10000.0D), 10000);
   }
}
