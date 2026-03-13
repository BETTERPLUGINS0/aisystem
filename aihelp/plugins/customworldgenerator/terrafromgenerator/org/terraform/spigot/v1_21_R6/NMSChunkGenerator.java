package org.terraform.spigot.v1_21_R6;

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
import net.minecraft.CrashReportSystemDetails;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.SectionPosition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.FeatureSorter.b;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.WorldGenStage.Decoration;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.a;
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
   private final ArrayList<MinecraftKey> possibleStructureSets = new ArrayList();
   @NotNull
   private final TerraformMethodHandler getWriteableArea;
   @NotNull
   private final Supplier featuresPerStep;

   public NMSChunkGenerator(String worldName, long seed, @NotNull ChunkGenerator delegate) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
      super(delegate.d(), delegate.d);
      this.tw = TerraformWorld.get(worldName, seed);
      this.delegate = delegate;
      this.mapRendererBS = new MapRenderWorldProviderBiome(this.tw, delegate.d());
      this.twBS = new TerraformWorldProviderBiome(TerraformWorld.get(worldName, seed), delegate.d());
      this.featuresPerStep = (Supplier)(new TerraformFieldHandler(ChunkGenerator.class, new String[]{"featuresPerStep", "c"})).field.get(delegate);
      this.getWriteableArea = new TerraformMethodHandler(ChunkGenerator.class, new String[]{"getWritableArea", "a"}, new Class[]{IChunkAccess.class});
      StructurePopulator[] var5 = StructureRegistry.getAllPopulators();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         StructurePopulator pop = var5[var7];
         if (pop instanceof VanillaStructurePopulator) {
            VanillaStructurePopulator vsp = (VanillaStructurePopulator)pop;
            this.possibleStructureSets.add(MinecraftKey.a(vsp.structureRegistryKey));
         }
      }

      this.tryGenerateStructure = new TerraformMethodHandler(ChunkGenerator.class, new String[]{"tryGenerateStructure", "a"}, new Class[]{a.class, StructureManager.class, IRegistryCustom.class, RandomState.class, StructureTemplateManager.class, Long.TYPE, IChunkAccess.class, ChunkCoordIntPair.class, SectionPosition.class, ResourceKey.class});
   }

   @NotNull
   public WorldChunkManager d() {
      return this.mapRendererBS;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }

   @NotNull
   protected MapCodec<? extends ChunkGenerator> b() {
      return MapCodec.unit((Supplier)null);
   }

   @NotNull
   public CompletableFuture<IChunkAccess> a(RandomState randomstate, Blender blender, StructureManager structuremanager, @NotNull IChunkAccess ChunkAccess) {
      return CompletableFuture.supplyAsync(() -> {
         return ChunkAccess;
      }, SystemUtils.h().a("init_biomes"));
   }

   public Pair<BlockPosition, Holder<Structure>> a(WorldServer ServerLevel, @NotNull HolderSet<Structure> holderset, @NotNull BlockPosition BlockPos, int i, boolean flag) {
      int pX = BlockPos.u();
      int pZ = BlockPos.w();
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

            if (holder.a() instanceof JigsawStructure && ((IRegistry)MinecraftServer.getServer().bg().a(Registries.bm).orElseThrow()).a(MinecraftKey.a("trial_chambers")) == holder.a()) {
               coords = StructureLocator.locateSingleMegaChunkStructure(this.tw, pX, pZ, new TrialChamberPopulator(), TConfig.c.DEVSTUFF_VANILLA_LOCATE_TIMEOUTMILLIS);
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

   public void a(GeneratorAccessSeed worldGenLevel, IChunkAccess ChunkAccess, StructureManager structuremanager) {
      this.delegate.a(worldGenLevel, ChunkAccess, structuremanager);
      this.addVanillaDecorations(worldGenLevel, ChunkAccess, structuremanager);
   }

   public void addVanillaDecorations(GeneratorAccessSeed worldGenLevel, IChunkAccess chunkAccess, StructureManager structuremanager) {
      ChunkCoordIntPair ChunkPos = chunkAccess.f();
      if (!SharedConstants.a(ChunkPos)) {
         SectionPosition sectionPos = SectionPosition.a(ChunkPos, worldGenLevel.at());
         BlockPosition BlockPos = sectionPos.j();
         IRegistry<Structure> iregistry = worldGenLevel.L_().f(Registries.bm);
         Map<Integer, List<Structure>> map = (Map)iregistry.s().collect(Collectors.groupingBy((structurex) -> {
            return structurex.c().ordinal();
         }));
         List<b> list = (List)this.featuresPerStep.get();
         SeededRandom seededrandom = new SeededRandom(new XoroshiroRandomSource(RandomSupport.a()));
         long i = seededrandom.a(worldGenLevel.H(), BlockPos.u(), BlockPos.w());
         Set<Holder<BiomeBase>> set = new ObjectArraySet();
         ChunkCoordIntPair.a(sectionPos.r(), 1).forEach((ChunkPos1) -> {
            IChunkAccess ichunkaccess1 = worldGenLevel.a(ChunkPos1.h, ChunkPos1.i);
            ChunkSection[] var4 = ichunkaccess1.d();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               ChunkSection chunksection = var4[var6];
               PalettedContainerRO<Holder<BiomeBase>> palettedcontainerro = chunksection.i();
               Objects.requireNonNull(set);
               Objects.requireNonNull(set);
               palettedcontainerro.a(set::add);
            }

         });
         set.retainAll(this.b.c());
         int j = list.size();

         try {
            IRegistry iregistry1 = worldGenLevel.L_().f(Registries.bj);
            int k = Math.max(Decoration.values().length, j);

            for(int l = 0; l < k; ++l) {
               int i1 = 0;
               if (structuremanager.a()) {
                  Iterator var19 = ((List)map.getOrDefault(l, Collections.emptyList())).iterator();

                  while(var19.hasNext()) {
                     Structure structure = (Structure)var19.next();
                     seededrandom.b(i, i1, l);
                     Supplier supplier = () -> {
                        Optional optional = iregistry.d(structure).map(Object::toString);
                        Objects.requireNonNull(structure);
                        Objects.requireNonNull(structure);
                        return (String)optional.orElseGet(structure::toString);
                     };

                     try {
                        worldGenLevel.a(supplier);
                        structuremanager.a(sectionPos, structure).forEach((structurestart) -> {
                           try {
                              structurestart.a(worldGenLevel, structuremanager, this, seededrandom, (StructureBoundingBox)this.getWriteableArea.method.invoke((Object)null, chunkAccess), ChunkPos);
                           } catch (InvocationTargetException | IllegalAccessException var9) {
                              CrashReport crashreport = CrashReport.a(var9, "TerraformGenerator");
                              throw new ReportedException(crashreport);
                           }
                        });
                     } catch (Exception var25) {
                        CrashReport crashreport = CrashReport.a(var25, "Feature placement");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Feature");
                        Objects.requireNonNull(supplier);
                        Objects.requireNonNull(supplier);
                        crashreportsystemdetails.a("Description", supplier::get);
                        throw new ReportedException(crashreport);
                     }
                  }
               }
            }

            worldGenLevel.a((Supplier)null);
            if (SharedConstants.aW) {
               FeatureCountTracker.a(worldGenLevel.a());
            }
         } catch (Exception var26) {
            CrashReport crashreport2 = CrashReport.a(var26, "Biome decoration");
            crashreport2.a("Generation").a("CenterX", ChunkPos.h).a("CenterZ", ChunkPos.i).a("Decoration Seed", i);
            throw new ReportedException(crashreport2);
         }
      }

   }

   public void a(RegionLimitedWorldAccess worldGenRegion, long seed, RandomState randomstate, BiomeManager biomemanager, StructureManager structuremanager, @NotNull IChunkAccess chunkAccess) {
      chunkAccess.a(this.twBS, (Sampler)null);
      this.delegate.a(worldGenRegion, seed, randomstate, biomemanager, structuremanager, chunkAccess);
   }

   public int e() {
      return this.delegate.e();
   }

   public void a(IRegistryCustom registryAccess, @NotNull ChunkGeneratorStructureState chunkgeneratorstructurestate, StructureManager structuremanager, @NotNull IChunkAccess ChunkAccess, StructureTemplateManager structuretemplatemanager, ResourceKey<World> resourcekey) {
      ChunkCoordIntPair ChunkPos = ChunkAccess.f();
      SectionPosition sectionPos = SectionPosition.a(ChunkAccess);
      RandomState randomstate = chunkgeneratorstructurestate.c();
      MegaChunk mc = new MegaChunk(ChunkPos.h, ChunkPos.i);
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
                  return vpop.structureRegistryKey.equals(resourceLoc.a());
               }).map((resourceLoc) -> {
                  return (StructureSet)((IRegistry)MinecraftServer.getServer().bg().a(Registries.bl).orElseThrow()).a(resourceLoc);
               }).forEach((structureSet) -> {
                  StructurePlacement structureplacement = structureSet.b();
                  List<a> list = structureSet.a();
                  if (centerCoords[0] == ChunkPos.h && centerCoords[1] == ChunkPos.i) {
                     try {
                        Object retVal = this.tryGenerateStructure.method.invoke(this, list.getFirst(), structuremanager, registryAccess, randomstate, structuretemplatemanager, chunkgeneratorstructurestate.d(), ChunkAccess, ChunkPos, sectionPos, resourcekey);
                        int var10001 = ChunkPos.h;
                        TerraformGeneratorPlugin.logger.info(var10001 + "," + ChunkPos.i + " will spawn a vanilla structure, with tryGenerateStructure == " + String.valueOf(retVal));
                     } catch (Throwable var15) {
                        TerraformGeneratorPlugin.logger.info(ChunkPos.h + "," + ChunkPos.i + " Failed to generate a vanilla structure");
                        TerraformGeneratorPlugin.logger.stackTrace(var15);
                     }
                  }

               });
            }
         }

      }
   }

   public void a(GeneratorAccessSeed gas, StructureManager manager, IChunkAccess ica) {
      this.delegate.a(gas, manager, ica);
   }

   public int a(LevelHeightAccessor levelheightaccessor) {
      return 64;
   }

   public CompletableFuture<IChunkAccess> a(Blender blender, RandomState randomstate, StructureManager structuremanager, IChunkAccess ChunkAccess) {
      return this.delegate.a(blender, randomstate, structuremanager, ChunkAccess);
   }

   public void a(RegionLimitedWorldAccess worldGenRegion, StructureManager structuremanager, RandomState randomstate, IChunkAccess ChunkAccess) {
      this.delegate.a(worldGenRegion, structuremanager, randomstate, ChunkAccess);
   }

   public BlockColumn a(int i, int j, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.delegate.a(i, j, levelheightaccessor, randomstate);
   }

   public void a(RegionLimitedWorldAccess WorldGenRegion) {
      this.delegate.a(WorldGenRegion);
   }

   public int f() {
      return TerraformGenerator.seaLevel;
   }

   public int g() {
      return this.delegate.g();
   }

   public int b(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.b(i, j, heightmap_type, levelheightaccessor, randomstate);
   }

   public int c(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.c(i, j, heightmap_type, levelheightaccessor, randomstate) - 1;
   }

   public int a(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return 100;
   }

   public void a(List<String> list, RandomState randomstate, BlockPosition BlockPos) {
   }
}
