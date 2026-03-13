package org.terraform.v1_18_R2;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.levelgen.ChunkGeneratorAbstract;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.WorldGenStage.Features;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
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
   private static boolean biomeDebug = false;
   @NotNull
   private final ChunkGenerator delegate;
   @NotNull
   private final TerraformWorld tw;

   public NMSChunkGenerator(String worldname, long seed, @NotNull ChunkGenerator delegate) {
      super(delegate.b, delegate.e, new TerraformWorldProviderBiome(TerraformWorld.get(worldname, seed), delegate.e()));
      this.tw = TerraformWorld.get(worldname, seed);
      this.delegate = delegate;
   }

   public WorldChunkManager e() {
      if (!(this.d instanceof TerraformWorldProviderBiome)) {
         TerraformGeneratorPlugin.logger.error("d was not an instance of TerraformWorldProviderBiome!");
      }

      return this.d;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }

   @NotNull
   public CompletableFuture<IChunkAccess> a(IRegistry<BiomeBase> iregistry, Executor executor, Blender blender, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess) {
      return CompletableFuture.supplyAsync(SystemUtils.a("init_biomes", () -> {
         return ichunkaccess;
      }), SystemUtils.f());
   }

   public Pair<BlockPosition, Holder<StructureFeature<?, ?>>> a(WorldServer worldserver, @NotNull HolderSet<StructureFeature<?, ?>> holderset, @NotNull BlockPosition blockposition, int i, boolean flag) {
      int pX = blockposition.u();
      int pZ = blockposition.w();
      Iterator var8 = holderset.iterator();

      while(var8.hasNext()) {
         Holder<StructureFeature<?, ?>> holder = (Holder)var8.next();
         StructureFeature<?, ?> feature = (StructureFeature)holder.a();
         StructureGenerator<?> structuregenerator = feature.d;
         TerraformGeneratorPlugin.logger.info("Vanilla locate for " + structuregenerator.getClass().getName() + " invoked.");
         int[] coords;
         if (structuregenerator == StructureGenerator.k) {
            coords = (new StrongholdPopulator()).getNearestFeature(this.tw, pX, pZ);
            return new Pair(new BlockPosition(coords[0], 20, coords[1]), holder);
         }

         if (!TConfig.c.DEVSTUFF_VANILLA_LOCATE_DISABLE) {
            if (structuregenerator == StructureGenerator.l) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MonumentPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPosition(coords[0], 50, coords[1]), holder);
            }

            if (structuregenerator == StructureGenerator.d) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MansionPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPosition(coords[0], 50, coords[1]), holder);
            }

            if (structuregenerator.getClass().getName().equals("net.minecraft.world.level.levelgen.feature.WorldGenBuriedTreasure")) {
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

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, long var2, BiomeManager var4, StructureManager var5, @NotNull IChunkAccess ichunkaccess, Features var7) {
      ichunkaccess.a(this.e(), (Sampler)null);
      this.delegate.a(regionlimitedworldaccess, var2, var4, var5, ichunkaccess, var7);
   }

   public int g() {
      return 256;
   }

   public void a(IRegistryCustom iregistrycustom, StructureManager structuremanager, IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i) {
   }

   public int a(LevelHeightAccessor levelheightaccessor) {
      return 64;
   }

   public CompletableFuture<IChunkAccess> a(Executor executor, Blender blender, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      return this.delegate.a(executor, blender, structuremanager, ichunkaccess);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      this.delegate.a(regionlimitedworldaccess, structuremanager, ichunkaccess);
   }

   public void a(GeneratorAccessSeed gas, StructureManager manager, IChunkAccess ica) {
      this.delegate.a(gas, manager, ica);
   }

   protected Codec<? extends ChunkGenerator> b() {
      return ChunkGeneratorAbstract.a;
   }

   public BlockColumn a(int var0, int var1, LevelHeightAccessor var2) {
      return this.delegate.a(var0, var1, var2);
   }

   @NotNull
   public ChunkGenerator a(long seed) {
      return new NMSChunkGenerator(this.tw.getName(), (long)((int)seed), this.delegate);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess) {
      this.delegate.a(regionlimitedworldaccess);
   }

   public int f() {
      return this.delegate.f();
   }

   public Sampler d() {
      return this.delegate.d();
   }

   public int h() {
      return this.delegate.h();
   }

   public int b(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor) {
      return this.a(i, j, heightmap_type, levelheightaccessor);
   }

   public int c(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor) {
      return this.a(i, j, heightmap_type, levelheightaccessor) - 1;
   }

   public int a(int x, int z, Type var2, LevelHeightAccessor var3) {
      return 100;
   }

   public Holder<BiomeBase> getNoiseBiome(int x, int y, int z) {
      if (!biomeDebug) {
         biomeDebug = true;
         TerraformGeneratorPlugin.logger.info("[getNoiseBiome] called for " + x + "," + y + "," + z);
      }

      return this.c.getNoiseBiome(x, y, z, (Sampler)null);
   }

   public void a(List<String> arg0, BlockPosition arg1) {
   }
}
