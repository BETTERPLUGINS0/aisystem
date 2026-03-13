package org.terraform.v1_21_R7;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.FeatureSorter.StepFeatureData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasureStructure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.StructureLocator;
import org.terraform.structure.StructurePopulator;
import org.terraform.structure.StructureRegistry;
import org.terraform.structure.VanillaStructurePopulator;
import org.terraform.structure.monument.MonumentPopulator;
import org.terraform.structure.pillager.mansion.MansionPopulator;
import org.terraform.structure.small.buriedtreasure.BuriedTreasurePopulator;
import org.terraform.structure.stronghold.StrongholdPopulator;
import org.terraform.structure.trialchamber.TrialChamberPopulator;
import org.terraform.utils.version.TerraformFieldHandler;
import org.terraform.utils.version.TerraformMethodHandler;

public class NMSChunkGenerator extends ChunkGenerator {
   @NotNull
   private final ChunkGenerator delegate;
   @NotNull
   private final TerraformWorld tw;
   @NotNull
   private final MapRenderWorldProviderBiome mapRendererBS;
   @NotNull
   private final TerraformWorldProviderBiome twBS;
   @NotNull
   private final TerraformMethodHandler tryGenerateStructure;
   private final ArrayList<Identifier> possibleStructureSets = new ArrayList();
   @NotNull
   private final TerraformMethodHandler getWriteableArea;
   @NotNull
   private final Supplier featuresPerStep;

   public NMSChunkGenerator(String worldName, long seed, @NotNull ChunkGenerator delegate) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
      super(delegate.getBiomeSource(), delegate.generationSettingsGetter);
      this.tw = TerraformWorld.get(worldName, seed);
      this.delegate = delegate;
      this.mapRendererBS = new MapRenderWorldProviderBiome(this.tw, delegate.getBiomeSource());
      this.twBS = new TerraformWorldProviderBiome(TerraformWorld.get(worldName, seed), delegate.getBiomeSource());
      this.featuresPerStep = (Supplier)(new TerraformFieldHandler(ChunkGenerator.class, new String[]{"featuresPerStep", "c"})).field.get(delegate);
      this.getWriteableArea = new TerraformMethodHandler(ChunkGenerator.class, new String[]{"getWritableArea", "a"}, new Class[]{ChunkAccess.class});
      StructurePopulator[] var5 = StructureRegistry.getAllPopulators();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         StructurePopulator pop = var5[var7];
         if (pop instanceof VanillaStructurePopulator) {
            VanillaStructurePopulator vsp = (VanillaStructurePopulator)pop;
            this.possibleStructureSets.add(Identifier.parse(vsp.structureRegistryKey));
         }
      }

      this.tryGenerateStructure = new TerraformMethodHandler(ChunkGenerator.class, new String[]{"tryGenerateStructure", "a"}, new Class[]{StructureSelectionEntry.class, StructureManager.class, RegistryAccess.class, RandomState.class, StructureTemplateManager.class, Long.TYPE, ChunkAccess.class, ChunkPos.class, SectionPos.class, ResourceKey.class});
   }

   @NotNull
   public BiomeSource getBiomeSource() {
      return this.mapRendererBS;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }

   @NotNull
   protected MapCodec<? extends ChunkGenerator> codec() {
      return MapCodec.unit((Supplier)null);
   }

   @NotNull
   public CompletableFuture<ChunkAccess> createBiomes(RandomState randomstate, Blender blender, StructureManager structuremanager, @NotNull ChunkAccess ChunkAccess) {
      return CompletableFuture.supplyAsync(() -> {
         return ChunkAccess;
      }, Util.backgroundExecutor().forName("init_biomes"));
   }

   public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel ServerLevel, @NotNull HolderSet<Structure> holderset, @NotNull BlockPos BlockPos, int i, boolean flag) {
      int pX = BlockPos.getX();
      int pZ = BlockPos.getZ();
      Iterator var8 = holderset.iterator();

      while(var8.hasNext()) {
         Holder<Structure> holder = (Holder)var8.next();
         Structure feature = (Structure)holder.value();
         TerraformGeneratorPlugin.logger.info("Vanilla locate for " + feature.getClass().getName() + " invoked.");
         int[] coords;
         if (((Structure)holder.value()).getClass() == StrongholdStructure.class) {
            coords = (new StrongholdPopulator()).getNearestFeature(this.tw, pX, pZ);
            return new Pair(new BlockPos(coords[0], 20, coords[1]), holder);
         }

         if (!TConfig.c.DEVSTUFF_VANILLA_LOCATE_DISABLE) {
            if (((Structure)holder.value()).getClass() == OceanMonumentStructure.class) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MonumentPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPos(coords[0], 50, coords[1]), holder);
            }

            if (((Structure)holder.value()).getClass() == WoodlandMansionStructure.class) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new MansionPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPos(coords[0], 50, coords[1]), holder);
            }

            if (holder.value() instanceof JigsawStructure && ((Registry)MinecraftServer.getServer().registryAccess().lookup(Registries.STRUCTURE).orElseThrow()).getValue(Identifier.parse("trial_chambers")) == holder.value()) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new TrialChamberPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               return new Pair(new BlockPos(coords[0], 50, coords[1]), holder);
            }

            if (((Structure)holder.value()).getClass() == BuriedTreasureStructure.class) {
               coords = StructureLocator.locateMultiMegaChunkStructure(this.tw, new MegaChunk(pX, 0, pZ), new BuriedTreasurePopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
               if (coords == null) {
                  return null;
               }

               return new Pair(new BlockPos(coords[0], 50, coords[1]), holder);
            }
         }
      }

      return null;
   }

   public void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess ChunkAccess, StructureManager structuremanager) {
      this.delegate.applyBiomeDecoration(worldGenLevel, ChunkAccess, structuremanager);
      this.addVanillaDecorations(worldGenLevel, ChunkAccess, structuremanager);
   }

   public void addVanillaDecorations(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structuremanager) {
      ChunkPos ChunkPos = chunkAccess.getPos();
      if (!SharedConstants.debugVoidTerrain(ChunkPos)) {
         SectionPos sectionPos = SectionPos.of(ChunkPos, worldGenLevel.getMinSectionY());
         BlockPos BlockPos = sectionPos.origin();
         Registry<Structure> iregistry = worldGenLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE);
         Map<Integer, List<Structure>> map = (Map)iregistry.stream().collect(Collectors.groupingBy((structurex) -> {
            return structurex.step().ordinal();
         }));
         List<StepFeatureData> list = (List)this.featuresPerStep.get();
         WorldgenRandom seededrandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
         long i = seededrandom.setDecorationSeed(worldGenLevel.getSeed(), BlockPos.getX(), BlockPos.getZ());
         Set<Holder<Biome>> set = new ObjectArraySet();
         ChunkPos.rangeClosed(sectionPos.chunk(), 1).forEach((ChunkPos1) -> {
            ChunkAccess ichunkaccess1 = worldGenLevel.getChunk(ChunkPos1.x, ChunkPos1.z);
            LevelChunkSection[] var4 = ichunkaccess1.getSections();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               LevelChunkSection chunksection = var4[var6];
               PalettedContainerRO<Holder<Biome>> palettedcontainerro = chunksection.getBiomes();
               Objects.requireNonNull(set);
               Objects.requireNonNull(set);
               palettedcontainerro.getAll(set::add);
            }

         });
         set.retainAll(this.biomeSource.possibleBiomes());
         int j = list.size();

         try {
            Registry iregistry1 = worldGenLevel.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
            int k = Math.max(Decoration.values().length, j);

            for(int l = 0; l < k; ++l) {
               int i1 = 0;
               if (structuremanager.shouldGenerateStructures()) {
                  Iterator var19 = ((List)map.getOrDefault(l, Collections.emptyList())).iterator();

                  while(var19.hasNext()) {
                     Structure structure = (Structure)var19.next();
                     seededrandom.setFeatureSeed(i, i1, l);
                     Supplier supplier = () -> {
                        Optional optional = iregistry.getResourceKey(structure).map(Object::toString);
                        Objects.requireNonNull(structure);
                        Objects.requireNonNull(structure);
                        return (String)optional.orElseGet(structure::toString);
                     };

                     try {
                        worldGenLevel.setCurrentlyGenerating(supplier);
                        structuremanager.startsForStructure(sectionPos, structure).forEach((structurestart) -> {
                           try {
                              structurestart.placeInChunk(worldGenLevel, structuremanager, this, seededrandom, (BoundingBox)this.getWriteableArea.method.invoke((Object)null, chunkAccess), ChunkPos);
                           } catch (InvocationTargetException | IllegalAccessException var9) {
                              CrashReport crashreport = CrashReport.forThrowable(var9, "TerraformGenerator");
                              throw new ReportedException(crashreport);
                           }
                        });
                     } catch (Exception var25) {
                        CrashReport crashreport = CrashReport.forThrowable(var25, "Feature placement");
                        CrashReportCategory crashreportsystemdetails = crashreport.addCategory("Feature");
                        Objects.requireNonNull(supplier);
                        Objects.requireNonNull(supplier);
                        crashreportsystemdetails.setDetail("Description", supplier::get);
                        throw new ReportedException(crashreport);
                     }
                  }
               }
            }

            worldGenLevel.setCurrentlyGenerating((Supplier)null);
            if (SharedConstants.DEBUG_FEATURE_COUNT) {
               FeatureCountTracker.chunkDecorated(worldGenLevel.getLevel());
            }
         } catch (Exception var26) {
            CrashReport crashreport2 = CrashReport.forThrowable(var26, "Biome decoration");
            crashreport2.addCategory("Generation").setDetail("CenterX", ChunkPos.x).setDetail("CenterZ", ChunkPos.z).setDetail("Decoration Seed", i);
            throw new ReportedException(crashreport2);
         }
      }

   }

   public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomstate, BiomeManager biomemanager, StructureManager structuremanager, @NotNull ChunkAccess chunkAccess) {
      chunkAccess.fillBiomesFromNoise(this.twBS, (Sampler)null);
      this.delegate.applyCarvers(worldGenRegion, seed, randomstate, biomemanager, structuremanager, chunkAccess);
   }

   public int getGenDepth() {
      return this.delegate.getGenDepth();
   }

   public void createStructures(RegistryAccess registryAccess, @NotNull ChunkGeneratorStructureState chunkgeneratorstructurestate, StructureManager structuremanager, @NotNull ChunkAccess ChunkAccess, StructureTemplateManager structuretemplatemanager, ResourceKey<Level> resourcekey) {
      ChunkPos ChunkPos = ChunkAccess.getPos();
      SectionPos sectionPos = SectionPos.bottomOf(ChunkAccess);
      RandomState randomstate = chunkgeneratorstructurestate.randomState();
      MegaChunk mc = new MegaChunk(ChunkPos.x, ChunkPos.z);
      SingleMegaChunkStructurePopulator[] spops = StructureRegistry.getLargeStructureForMegaChunk(this.tw, mc);
      int[] centerCoords = mc.getCenterBiomeSectionChunkCoords();
      if (spops != null) {
         SingleMegaChunkStructurePopulator[] var13 = spops;
         int var14 = spops.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            SingleMegaChunkStructurePopulator pop = var13[var15];
            if (pop instanceof VanillaStructurePopulator) {
               VanillaStructurePopulator vpop = (VanillaStructurePopulator)pop;
               this.possibleStructureSets.stream().filter((resourceLoc) -> {
                  return vpop.structureRegistryKey.equals(resourceLoc.getPath());
               }).map((resourceLoc) -> {
                  return (StructureSet)((Registry)MinecraftServer.getServer().registryAccess().lookup(Registries.STRUCTURE_SET).orElseThrow()).getValue(resourceLoc);
               }).forEach((structureSet) -> {
                  StructurePlacement structureplacement = structureSet.placement();
                  List<StructureSelectionEntry> list = structureSet.structures();
                  if (centerCoords[0] == ChunkPos.x && centerCoords[1] == ChunkPos.z) {
                     try {
                        Object retVal = this.tryGenerateStructure.method.invoke(this, list.getFirst(), structuremanager, registryAccess, randomstate, structuretemplatemanager, chunkgeneratorstructurestate.getLevelSeed(), ChunkAccess, ChunkPos, sectionPos, resourcekey);
                        int var10001 = ChunkPos.x;
                        TerraformGeneratorPlugin.logger.info(var10001 + "," + ChunkPos.z + " will spawn a vanilla structure, with tryGenerateStructure == " + String.valueOf(retVal));
                     } catch (Throwable var15) {
                        TerraformGeneratorPlugin.logger.info(ChunkPos.x + "," + ChunkPos.z + " Failed to generate a vanilla structure");
                        TerraformGeneratorPlugin.logger.stackTrace(var15);
                     }
                  }

               });
            }
         }

      }
   }

   public void createReferences(WorldGenLevel gas, StructureManager manager, ChunkAccess ica) {
      this.delegate.createReferences(gas, manager, ica);
   }

   public int getSpawnHeight(LevelHeightAccessor levelheightaccessor) {
      return 64;
   }

   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomstate, StructureManager structuremanager, ChunkAccess ChunkAccess) {
      return this.delegate.fillFromNoise(blender, randomstate, structuremanager, ChunkAccess);
   }

   public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structuremanager, RandomState randomstate, ChunkAccess ChunkAccess) {
      this.delegate.buildSurface(worldGenRegion, structuremanager, randomstate, ChunkAccess);
   }

   public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.delegate.getBaseColumn(i, j, levelheightaccessor, randomstate);
   }

   public void spawnOriginalMobs(WorldGenRegion WorldGenRegion) {
      this.delegate.spawnOriginalMobs(WorldGenRegion);
   }

   public int getSeaLevel() {
      return TerraformGenerator.seaLevel;
   }

   public int getMinY() {
      return this.delegate.getMinY();
   }

   public int getFirstFreeHeight(int i, int j, Types heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.getFirstFreeHeight(i, j, heightmap_type, levelheightaccessor, randomstate);
   }

   public int getFirstOccupiedHeight(int i, int j, Types heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.getFirstOccupiedHeight(i, j, heightmap_type, levelheightaccessor, randomstate) - 1;
   }

   public int getBaseHeight(int i, int j, Types heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return 100;
   }

   public void addDebugScreenInfo(List<String> list, RandomState randomstate, BlockPos BlockPos) {
   }
}
