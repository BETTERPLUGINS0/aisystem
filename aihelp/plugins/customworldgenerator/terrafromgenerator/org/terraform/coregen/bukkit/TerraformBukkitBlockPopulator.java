package org.terraform.coregen.bukkit;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.TerraformWorld;

public class TerraformBukkitBlockPopulator extends BlockPopulator {
   protected final TerraformWorld tw;
   @NotNull
   private final TerraformStructurePopulator structurePopulator;
   @NotNull
   private final NativeGeneratorPatcherPopulator nativePatcherPopulator;
   @NotNull
   private final PhysicsUpdaterPopulator physicsUpdaterPopulator;
   @NotNull
   private final TerraformAnimalPopulator animalPopulator;

   public TerraformBukkitBlockPopulator(TerraformWorld tw) {
      this.tw = tw;
      this.nativePatcherPopulator = new NativeGeneratorPatcherPopulator();
      this.structurePopulator = new TerraformStructurePopulator(tw);
      this.physicsUpdaterPopulator = new PhysicsUpdaterPopulator();
      this.animalPopulator = new TerraformAnimalPopulator(tw);
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      this.nativePatcherPopulator.populate(world, random, chunk);
      this.physicsUpdaterPopulator.populate(world, random, chunk);
      this.structurePopulator.populate(world, random, chunk);
      this.animalPopulator.populate(world, random, chunk);
   }

   public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion lr) {
      this.structurePopulator.populate(worldInfo, random, chunkX, chunkZ, lr);
   }

   @NotNull
   public TerraformStructurePopulator getStructurePopulator() {
      return this.structurePopulator;
   }
}
