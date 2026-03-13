package org.terraform.v1_21_R4;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
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
   private final Method tryGenerateStructure;
   private final ArrayList<MinecraftKey> possibleStructureSets = new ArrayList();
   @NotNull
   private final Method getWriteableArea;
   @NotNull
   private final Supplier featuresPerStep;

   public NMSChunkGenerator(String worldName, long seed, @NotNull ChunkGenerator delegate) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
      super(delegate.d(), delegate.d);
      this.tw = TerraformWorld.get(worldName, seed);
      this.delegate = delegate;
      this.mapRendererBS = new MapRenderWorldProviderBiome(this.tw, delegate.d());
      this.twBS = new TerraformWorldProviderBiome(TerraformWorld.get(worldName, seed), delegate.d());
      Field f = ChunkGenerator.class.getDeclaredField("c");
      f.setAccessible(true);
      this.featuresPerStep = (Supplier)f.get(delegate);
      this.getWriteableArea = ChunkGenerator.class.getDeclaredMethod("a", IChunkAccess.class);
      this.getWriteableArea.setAccessible(true);
      StructurePopulator[] var6 = StructureRegistry.getAllPopulators();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         StructurePopulator pop = var6[var8];
         if (pop instanceof VanillaStructurePopulator) {
            VanillaStructurePopulator vsp = (VanillaStructurePopulator)pop;
            this.possibleStructureSets.add(MinecraftKey.a(vsp.structureRegistryKey));
         }
      }

      this.tryGenerateStructure = ChunkGenerator.class.getDeclaredMethod("a", a.class, StructureManager.class, IRegistryCustom.class, RandomState.class, StructureTemplateManager.class, Long.TYPE, IChunkAccess.class, ChunkCoordIntPair.class, SectionPosition.class, ResourceKey.class);
      this.tryGenerateStructure.setAccessible(true);
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
   public CompletableFuture<IChunkAccess> a(RandomState randomstate, Blender blender, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess) {
      return CompletableFuture.supplyAsync(() -> {
         return ichunkaccess;
      }, SystemUtils.h().a("init_biomes"));
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

            if (holder.a() instanceof JigsawStructure && ((IRegistry)MinecraftServer.getServer().ba().a(Registries.be).orElseThrow()).a(MinecraftKey.a("trial_chambers")) == holder.a()) {
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

   public void a(GeneratorAccessSeed generatoraccessseed, IChunkAccess ichunkaccess, StructureManager structuremanager) {
      this.delegate.a(generatoraccessseed, ichunkaccess, structuremanager);
      this.addVanillaDecorations(generatoraccessseed, ichunkaccess, structuremanager);
   }

   public void addVanillaDecorations(GeneratorAccessSeed generatoraccessseed, IChunkAccess ichunkaccess, StructureManager structuremanager) {
      ChunkCoordIntPair chunkcoordintpair = ichunkaccess.f();
      if (!SharedConstants.a(chunkcoordintpair)) {
         SectionPosition sectionposition = SectionPosition.a(chunkcoordintpair, generatoraccessseed.ao());
         BlockPosition blockposition = sectionposition.j();
         IRegistry<Structure> iregistry = generatoraccessseed.J_().f(Registries.be);
         Map<Integer, List<Structure>> map = (Map)iregistry.s().collect(Collectors.groupingBy((structurex) -> {
            return structurex.c().ordinal();
         }));
         List<b> list = (List)this.featuresPerStep.get();
         SeededRandom seededrandom = new SeededRandom(new XoroshiroRandomSource(RandomSupport.a()));
         long i = seededrandom.a(generatoraccessseed.E(), blockposition.u(), blockposition.w());
         Set<Holder<BiomeBase>> set = new ObjectArraySet();
         ChunkCoordIntPair.a(sectionposition.r(), 1).forEach((chunkcoordintpair1) -> {
            IChunkAccess ichunkaccess1 = generatoraccessseed.a(chunkcoordintpair1.h, chunkcoordintpair1.i);
            ChunkSection[] achunksection = ichunkaccess1.d();
            int j = achunksection.length;

            for(int k = 0; k < j; ++k) {
               ChunkSection chunksection = achunksection[k];
               PalettedContainerRO<Holder<BiomeBase>> palettedcontainerro = chunksection.i();
               Objects.requireNonNull(set);
               Objects.requireNonNull(set);
               palettedcontainerro.a(set::add);
            }

         });
         set.retainAll(this.b.c());
         int j = list.size();

         try {
            IRegistry<PlacedFeature> iregistry1 = generatoraccessseed.J_().f(Registries.bb);
            int k = Math.max(Decoration.values().length, j);

            for(int l = 0; l < k; ++l) {
               int i1 = 0;
               if (structuremanager.a()) {
                  List<Structure> list1 = (List)map.getOrDefault(l, Collections.emptyList());

                  for(Iterator iterator = list1.iterator(); iterator.hasNext(); ++i1) {
                     Structure structure = (Structure)iterator.next();
                     seededrandom.b(i, i1, l);
                     Supplier supplier = () -> {
                        Optional optional = iregistry.d(structure).map(Object::toString);
                        Objects.requireNonNull(structure);
                        Objects.requireNonNull(structure);
                        return (String)optional.orElseGet(structure::toString);
                     };

                     try {
                        generatoraccessseed.a(supplier);
                        structuremanager.a(sectionposition, structure).forEach((structurestart) -> {
                           try {
                              structurestart.a(generatoraccessseed, structuremanager, this, seededrandom, (StructureBoundingBox)this.getWriteableArea.invoke((Object)null, ichunkaccess), chunkcoordintpair);
                           } catch (InvocationTargetException | IllegalAccessException var9) {
                              CrashReport crashreport = CrashReport.a(var9, "TerraformGenerator");
                              throw new ReportedException(crashreport);
                           }
                        });
                     } catch (Exception var26) {
                        CrashReport crashreport = CrashReport.a(var26, "Feature placement");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Feature");
                        Objects.requireNonNull(supplier);
                        Objects.requireNonNull(supplier);
                        crashreportsystemdetails.a("Description", supplier::get);
                        throw new ReportedException(crashreport);
                     }
                  }
               }
            }

            generatoraccessseed.a((Supplier)null);
         } catch (Exception var27) {
            CrashReport crashreport2 = CrashReport.a(var27, "Biome decoration");
            crashreport2.a("Generation").a("CenterX", ChunkCoordIntPair.e).a("CenterZ", 32).a("Decoration Seed", i);
            throw new ReportedException(crashreport2);
         }
      }

   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, long seed, RandomState randomstate, BiomeManager biomemanager, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess) {
      ichunkaccess.a(this.twBS, (Sampler)null);
      this.delegate.a(regionlimitedworldaccess, seed, randomstate, biomemanager, structuremanager, ichunkaccess);
   }

   public int e() {
      return this.delegate.e();
   }

   public void a(IRegistryCustom iregistrycustom, @NotNull ChunkGeneratorStructureState chunkgeneratorstructurestate, StructureManager structuremanager, @NotNull IChunkAccess ichunkaccess, StructureTemplateManager structuretemplatemanager, ResourceKey<World> resourcekey) {
      ChunkCoordIntPair chunkcoordintpair = ichunkaccess.f();
      SectionPosition sectionposition = SectionPosition.a(ichunkaccess);
      RandomState randomstate = chunkgeneratorstructurestate.c();
      MegaChunk mc = new MegaChunk(chunkcoordintpair.h, chunkcoordintpair.i);
      SingleMegaChunkStructurePopulator[] spops = StructureRegistry.getLargeStructureForMegaChunk(this.tw, mc);
      int[] centerCoords = mc.getCenterBiomeSectionChunkCoords();
      SingleMegaChunkStructurePopulator[] var13 = spops;
      int var14 = spops.length;

      for(int var15 = 0; var15 < var14; ++var15) {
         SingleMegaChunkStructurePopulator pop = var13[var15];
         if (pop instanceof VanillaStructurePopulator) {
            VanillaStructurePopulator vpop = (VanillaStructurePopulator)pop;
            this.possibleStructureSets.stream().filter((resourceLoc) -> {
               return vpop.structureRegistryKey.equals(resourceLoc.a());
            }).map((resourceLoc) -> {
               return (StructureSet)((IRegistry)MinecraftServer.getServer().ba().a(Registries.bd).orElseThrow()).a(resourceLoc);
            }).forEach((structureSet) -> {
               StructurePlacement structureplacement = structureSet.b();
               List<a> list = structureSet.a();
               if (centerCoords[0] == chunkcoordintpair.h && centerCoords[1] == chunkcoordintpair.i) {
                  try {
                     Object retVal = this.tryGenerateStructure.invoke(this, list.getFirst(), structuremanager, iregistrycustom, randomstate, structuretemplatemanager, chunkgeneratorstructurestate.d(), ichunkaccess, chunkcoordintpair, sectionposition, resourcekey);
                     int var10001 = chunkcoordintpair.h;
                     TerraformGeneratorPlugin.logger.info(var10001 + "," + chunkcoordintpair.i + " will spawn a vanilla structure, with tryGenerateStructure == " + String.valueOf(retVal));
                  } catch (Throwable var15) {
                     TerraformGeneratorPlugin.logger.info(chunkcoordintpair.h + "," + chunkcoordintpair.i + " Failed to generate a vanilla structure");
                     TerraformGeneratorPlugin.logger.stackTrace(var15);
                  }
               }

            });
         }
      }

   }

   public void a(GeneratorAccessSeed gas, StructureManager manager, IChunkAccess ica) {
      this.delegate.a(gas, manager, ica);
   }

   public int a(LevelHeightAccessor levelheightaccessor) {
      return 64;
   }

   public CompletableFuture<IChunkAccess> a(Blender blender, RandomState randomstate, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      return this.delegate.a(blender, randomstate, structuremanager, ichunkaccess);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager, RandomState randomstate, IChunkAccess ichunkaccess) {
      this.delegate.a(regionlimitedworldaccess, structuremanager, randomstate, ichunkaccess);
   }

   public BlockColumn a(int i, int j, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return this.delegate.a(i, j, levelheightaccessor, randomstate);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess) {
      this.delegate.a(regionlimitedworldaccess);
   }

   public int f() {
      return TerraformGenerator.seaLevel;
   }

   public int g() {
      return this.delegate.g();
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
