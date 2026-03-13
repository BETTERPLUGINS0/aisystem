package org.terraform.coregen.bukkit;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.populators.AnimalPopulator;
import org.terraform.utils.version.V_1_20_5;
import org.terraform.utils.version.Version;

public class TerraformAnimalPopulator extends BlockPopulator {
   private static final AnimalPopulator[] ANIMAL_POPULATORS;
   private final TerraformWorld tw;

   public TerraformAnimalPopulator(TerraformWorld tw) {
      this.tw = tw;
      ANIMAL_POPULATORS[0] = new AnimalPopulator(EntityType.valueOf("GOAT"), TConfig.c.ANIMALS_GOAT_MINHERDSIZE, TConfig.c.ANIMALS_GOAT_MAXHERDSIZE, TConfig.c.ANIMALS_GOAT_CHANCE, true, new BiomeBank[]{BiomeBank.ROCKY_MOUNTAINS, BiomeBank.SNOWY_MOUNTAINS});
      if (Version.VERSION.isAtLeast(Version.v1_20_5)) {
         ANIMAL_POPULATORS[1] = new AnimalPopulator(V_1_20_5.ARMADILLO, TConfig.c.ANIMALS_ARMADILLO_MINHERDSIZE, TConfig.c.ANIMALS_ARMADILLO_MAXHERDSIZE, TConfig.c.ANIMALS_ARMADILLO_CHANCE, true, new BiomeBank[]{BiomeBank.SAVANNA, BiomeBank.SHATTERED_SAVANNA, BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON});
      }

      if (Version.VERSION.isAtLeast(Version.v1_19_4)) {
         ANIMAL_POPULATORS[2] = new AnimalPopulator(EntityType.valueOf("FROG"), TConfig.c.ANIMALS_FROG_MINHERDSIZE, TConfig.c.ANIMALS_FROG_MAXHERDSIZE, TConfig.c.ANIMALS_FROG_CHANCE, true, new BiomeBank[]{BiomeBank.SWAMP, BiomeBank.MUDDY_BOG, BiomeBank.MUDFLATS, BiomeBank.MANGROVE});
      }

   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      PopulatorDataPostGen data = new PopulatorDataPostGen(chunk);
      AnimalPopulator[] var5 = ANIMAL_POPULATORS;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         AnimalPopulator pop = var5[var7];
         if (pop != null && pop.canSpawn(this.tw.getHashedRand((long)chunk.getX(), pop.hashCode(), chunk.getZ()))) {
            pop.populate(this.tw, this.tw.getHashedRand((long)chunk.getX(), 111 + pop.hashCode(), chunk.getZ()), data);
         }
      }

   }

   static {
      ANIMAL_POPULATORS = new AnimalPopulator[]{null, null, null, new AnimalPopulator(EntityType.PIG, TConfig.c.ANIMALS_PIG_MINHERDSIZE, TConfig.c.ANIMALS_PIG_MAXHERDSIZE, TConfig.c.ANIMALS_PIG_CHANCE, false, new BiomeBank[]{BiomeBank.BLACK_OCEAN, BiomeBank.MUSHROOM_ISLANDS, BiomeBank.MUSHROOM_BEACH, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER, BiomeBank.OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.DEEP_FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN, BiomeBank.SWAMP, BiomeBank.DESERT, BiomeBank.DESERT_MOUNTAINS, BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON}), new AnimalPopulator(EntityType.COW, TConfig.c.ANIMALS_COW_MINHERDSIZE, TConfig.c.ANIMALS_COW_MAXHERDSIZE, TConfig.c.ANIMALS_COW_CHANCE, false, new BiomeBank[]{BiomeBank.BLACK_OCEAN, BiomeBank.MUSHROOM_ISLANDS, BiomeBank.MUSHROOM_BEACH, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER, BiomeBank.OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.DEEP_FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN, BiomeBank.SWAMP, BiomeBank.DESERT, BiomeBank.DESERT_MOUNTAINS, BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON}), new AnimalPopulator(EntityType.SHEEP, TConfig.c.ANIMALS_SHEEP_MINHERDSIZE, TConfig.c.ANIMALS_SHEEP_MAXHERDSIZE, TConfig.c.ANIMALS_SHEEP_CHANCE, false, new BiomeBank[]{BiomeBank.BLACK_OCEAN, BiomeBank.MUSHROOM_ISLANDS, BiomeBank.MUSHROOM_BEACH, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER, BiomeBank.OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.DEEP_FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN, BiomeBank.SWAMP, BiomeBank.DESERT, BiomeBank.DESERT_MOUNTAINS, BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON}), new AnimalPopulator(EntityType.CHICKEN, TConfig.c.ANIMALS_CHICKEN_MINHERDSIZE, TConfig.c.ANIMALS_CHICKEN_MAXHERDSIZE, TConfig.c.ANIMALS_CHICKEN_CHANCE, false, new BiomeBank[]{BiomeBank.BLACK_OCEAN, BiomeBank.MUSHROOM_ISLANDS, BiomeBank.MUSHROOM_BEACH, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER, BiomeBank.OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.DEEP_FROZEN_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN, BiomeBank.SWAMP, BiomeBank.DESERT, BiomeBank.DESERT_MOUNTAINS, BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON}), new AnimalPopulator(EntityType.HORSE, TConfig.c.ANIMALS_HORSE_MINHERDSIZE, TConfig.c.ANIMALS_HORSE_MAXHERDSIZE, TConfig.c.ANIMALS_HORSE_CHANCE, true, new BiomeBank[]{BiomeBank.PLAINS, BiomeBank.SAVANNA}), new AnimalPopulator(EntityType.DONKEY, TConfig.c.ANIMALS_DONKEY_MINHERDSIZE, TConfig.c.ANIMALS_DONKEY_MAXHERDSIZE, TConfig.c.ANIMALS_DONKEY_CHANCE, true, new BiomeBank[]{BiomeBank.PLAINS, BiomeBank.SAVANNA}), new AnimalPopulator(EntityType.RABBIT, TConfig.c.ANIMALS_RABBIT_MINHERDSIZE, TConfig.c.ANIMALS_RABBIT_MAXHERDSIZE, TConfig.c.ANIMALS_RABBIT_CHANCE, true, new BiomeBank[]{BiomeBank.DESERT, BiomeBank.FOREST, BiomeBank.TAIGA, BiomeBank.SNOWY_TAIGA, BiomeBank.ROCKY_BEACH, BiomeBank.SNOWY_WASTELAND}), new AnimalPopulator(EntityType.POLAR_BEAR, TConfig.c.ANIMALS_POLAR_BEAR_MINHERDSIZE, TConfig.c.ANIMALS_POLAR_BEAR_MAXHERDSIZE, TConfig.c.ANIMALS_POLAR_BEAR_CHANCE, true, new BiomeBank[]{BiomeBank.ICE_SPIKES, BiomeBank.SNOWY_TAIGA, BiomeBank.ICY_BEACH, BiomeBank.SNOWY_WASTELAND}), new AnimalPopulator(EntityType.PANDA, TConfig.c.ANIMALS_PANDA_MINHERDSIZE, TConfig.c.ANIMALS_PANDA_MAXHERDSIZE, TConfig.c.ANIMALS_PANDA_CHANCE, true, new BiomeBank[]{BiomeBank.BAMBOO_FOREST}), new AnimalPopulator(EntityType.FOX, TConfig.c.ANIMALS_FOX_MINHERDSIZE, TConfig.c.ANIMALS_FOX_MAXHERDSIZE, TConfig.c.ANIMALS_FOX_CHANCE, true, new BiomeBank[]{BiomeBank.TAIGA, BiomeBank.SNOWY_TAIGA}), new AnimalPopulator(EntityType.LLAMA, TConfig.c.ANIMALS_LLAMA_MINHERDSIZE, TConfig.c.ANIMALS_LLAMA_MAXHERDSIZE, TConfig.c.ANIMALS_LLAMA_CHANCE, true, new BiomeBank[]{BiomeBank.SAVANNA, BiomeBank.ROCKY_MOUNTAINS}), new AnimalPopulator(EntityType.PARROT, TConfig.c.ANIMALS_PARROT_MINHERDSIZE, TConfig.c.ANIMALS_PARROT_MAXHERDSIZE, TConfig.c.ANIMALS_PARROT_CHANCE, true, new BiomeBank[]{BiomeBank.JUNGLE}), new AnimalPopulator(EntityType.OCELOT, TConfig.c.ANIMALS_OCELOT_MINHERDSIZE, TConfig.c.ANIMALS_OCELOT_MAXHERDSIZE, TConfig.c.ANIMALS_OCELOT_CHANCE, true, new BiomeBank[]{BiomeBank.JUNGLE, BiomeBank.BAMBOO_FOREST}), new AnimalPopulator(EntityType.WOLF, TConfig.c.ANIMALS_WOLF_MINHERDSIZE, TConfig.c.ANIMALS_WOLF_MAXHERDSIZE, TConfig.c.ANIMALS_WOLF_CHANCE, true, new BiomeBank[]{BiomeBank.FOREST, BiomeBank.TAIGA, BiomeBank.SNOWY_TAIGA, BiomeBank.DARK_FOREST}), new AnimalPopulator(EntityType.TURTLE, TConfig.c.ANIMALS_TURTLE_MINHERDSIZE, TConfig.c.ANIMALS_TURTLE_MAXHERDSIZE, TConfig.c.ANIMALS_TURTLE_CHANCE, true, new BiomeBank[]{BiomeBank.SANDY_BEACH}), (new AnimalPopulator(EntityType.DOLPHIN, TConfig.c.ANIMALS_DOLPHIN_MINHERDSIZE, TConfig.c.ANIMALS_DOLPHIN_MAXHERDSIZE, TConfig.c.ANIMALS_DOLPHIN_CHANCE, true, new BiomeBank[]{BiomeBank.OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN})).setAquatic(true), (new AnimalPopulator(EntityType.COD, TConfig.c.ANIMALS_COD_MINHERDSIZE, TConfig.c.ANIMALS_COD_MAXHERDSIZE, TConfig.c.ANIMALS_COD_CHANCE, true, new BiomeBank[]{BiomeBank.OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.DEEP_COLD_OCEAN})).setAquatic(true), (new AnimalPopulator(EntityType.SQUID, TConfig.c.ANIMALS_SQUID_MINHERDSIZE, TConfig.c.ANIMALS_SQUID_MAXHERDSIZE, TConfig.c.ANIMALS_SQUID_CHANCE, true, new BiomeBank[]{BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_FROZEN_OCEAN, BiomeBank.COLD_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.BLACK_OCEAN, BiomeBank.DEEP_BLACK_OCEAN, BiomeBank.OCEAN, BiomeBank.DEEP_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER, BiomeBank.JUNGLE_RIVER})).setAquatic(true), (new AnimalPopulator(EntityType.SALMON, TConfig.c.ANIMALS_SALMON_MINHERDSIZE, TConfig.c.ANIMALS_SALMON_MAXHERDSIZE, TConfig.c.ANIMALS_SALMON_CHANCE, true, new BiomeBank[]{BiomeBank.COLD_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.FROZEN_OCEAN, BiomeBank.DEEP_COLD_OCEAN, BiomeBank.RIVER, BiomeBank.FROZEN_RIVER})).setAquatic(true), (new AnimalPopulator(EntityType.PUFFERFISH, TConfig.c.ANIMALS_PUFFERFISH_MINHERDSIZE, TConfig.c.ANIMALS_PUFFERFISH_MAXHERDSIZE, TConfig.c.ANIMALS_PUFFERFISH_CHANCE, true, new BiomeBank[]{BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.WARM_OCEAN, BiomeBank.DEEP_WARM_OCEAN})).setAquatic(true), (new AnimalPopulator(EntityType.TROPICAL_FISH, TConfig.c.ANIMALS_TROPICALFISH_MINHERDSIZE, TConfig.c.ANIMALS_TROPICALFISH_MAXHERDSIZE, TConfig.c.ANIMALS_TROPICALFISH_CHANCE, true, new BiomeBank[]{BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.CORAL_REEF_OCEAN, BiomeBank.DEEP_LUKEWARM_OCEAN, BiomeBank.WARM_OCEAN})).setAquatic(true), new AnimalPopulator(EntityType.MUSHROOM_COW, TConfig.c.ANIMALS_MOOSHROOM_MINHERDSIZE, TConfig.c.ANIMALS_MOOSHROOM_MAXHERDSIZE, TConfig.c.ANIMALS_MOOSHROOM_CHANCE, true, new BiomeBank[]{BiomeBank.MUSHROOM_BEACH, BiomeBank.MUSHROOM_ISLANDS})};
   }
}
