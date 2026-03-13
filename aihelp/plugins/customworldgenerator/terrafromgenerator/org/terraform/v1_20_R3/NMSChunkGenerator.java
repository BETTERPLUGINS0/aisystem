package org.terraform.v1_20_R3;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.WorldGenStage.Features;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasureStructure;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.StructureLocator;
import org.terraform.structure.monument.MonumentPopulator;
import org.terraform.structure.pillager.mansion.MansionPopulator;
import org.terraform.structure.small.buriedtreasure.BuriedTreasurePopulator;
import org.terraform.structure.stronghold.StrongholdPopulator;

public class NMSChunkGenerator extends ChunkGenerator {
   @NotNull
   private final ChunkGenerator delegate;
   @NotNull
   private final TerraformWorld tw;
   @NotNull
   private final MapRenderWorldProviderBiome mapRendererBS;
   @NotNull
   private final TerraformWorldProviderBiome twBS;

   public NMSChunkGenerator(String worldName, long seed, @NotNull ChunkGenerator delegate) {
      super(delegate.c(), delegate.d);
      this.tw = TerraformWorld.get(worldName, seed);
      this.delegate = delegate;
      this.mapRendererBS = new MapRenderWorldProviderBiome(this.tw, delegate.c());
      this.twBS = new TerraformWorldProviderBiome(this.tw, delegate.c());
   }

   @NotNull
   public WorldChunkManager c() {
      return this.mapRendererBS;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }

   @NotNull
   public CompletableFuture<IChunkAccess> a(Executor executor, RandomState randomstate, Blender blender, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess) {
      return CompletableFuture.supplyAsync(SystemUtils.a("init_biomes", () -> {
         return ichunkaccess;
      }), SystemUtils.f());
   }

   public Pair<BlockPosition, Holder<Structure>> a(WorldServer worldserver, @NotNull HolderSet<Structure> holderset, @NotNull BlockPosition blockposition, int i, boolean flag) {
      int pX = blockposition.u();
      int pZ = blockposition.w();
      Iterator var8 = holderset.iterator();

      while(var8.hasNext()) {
         Holder<Structure> holder = (Holder)var8.next();
         Structure feature = (Structure)holder.a();
         TerraformGeneratorPlugin.logger.info("Vanilla locate for " + feature.getClass().getName() + " invoked.");
         int[] coords;
         if (((Structure)holder.a()).getClass() == StrongholdStructure.class) {
            coords = (new StrongholdPopulator()).getNearestFeature(this.tw, pX, pZ);
            return new Pair(new BlockPosition(coords[0], 20, coords[1]), holder);
         }

         if (!TConfig.c.DEVSTUFF_VANILLA_LOCATE_DISABLE) {
            if (((Structure)holder.a()).getClass() == OceanMonumentStructure.class) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MonumentPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPosition(coords[0], 50, coords[1]), holder);
            }

            if (((Structure)holder.a()).getClass() == WoodlandMansionStructure.class) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MansionPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPosition(coords[0], 50, coords[1]), holder);
            }

            if (((Structure)holder.a()).getClass() == BuriedTreasureStructure.class) {
               coords = StructureLocator.locateMultiMegaChunkStructure(this.tw, new MegaChunk(pX, 0, pZ), new BuriedTreasurePopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               if (coords == null) {
                  return null;
               }

               return new Pair(new BlockPosition(coords[0], 50, coords[1]), holder);
            }
         }
      }

      return null;
   }

   public void a(GeneratorAccessSeed generatoraccessseed, IChunkAccess ichunkaccess, StructureManager structuremanager) {
      this.delegate.a(generatoraccessseed, ichunkaccess, structuremanager);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, long seed, RandomState randomstate, BiomeManager biomemanager, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess, Features worldgenstage_features) {
      ichunkaccess.a(this.twBS, (Sampler)null);
      this.delegate.a(regionlimitedworldaccess, seed, randomstate, biomemanager, structuremanager, ichunkaccess, worldgenstage_features);
   }

   public int e() {
      return this.delegate.e();
   }

   public void a(IRegistryCustom iregistrycustom, ChunkGeneratorStructureState chunkgeneratorstructurestate, StructureManager structuremanager, IChunkAccess ichunkaccess, StructureTemplateManager structuretemplatemanager) {
   }

   public int a(LevelHeightAccessor levelheightaccessor) {
      return 64;
   }

   public CompletableFuture<IChunkAccess> a(Executor executor, Blender blender, RandomState randomstate, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      return this.delegate.a(executor, blender, randomstate, structuremanager, ichunkaccess);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager, RandomState randomstate, IChunkAccess ichunkaccess) {
      this.delegate.a(regionlimitedworldaccess, structuremanager, randomstate, ichunkaccess);
   }

   public void a(GeneratorAccessSeed gas, StructureManager manager, IChunkAccess ica) {
      this.delegate.a(gas, manager, ica);
   }

   protected Codec<? extends ChunkGenerator> a() {
      return Codec.unit((Supplier)null);
   }

   public BlockColumn a(int i, int j, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.delegate.a(i, j, levelheightaccessor, randomstate);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess) {
      this.delegate.a(regionlimitedworldaccess);
   }

   public int d() {
      return this.delegate.d();
   }

   public int f() {
      return this.delegate.f();
   }

   public int b(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.a(i, j, heightmap_type, levelheightaccessor, randomstate);
   }

   public int c(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.a(i, j, heightmap_type, levelheightaccessor, randomstate) - 1;
   }

   public int a(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return 100;
   }

   public void a(List<String> list, RandomState randomstate, BlockPosition blockposition) {
   }
}
