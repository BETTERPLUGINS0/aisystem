package com.volmit.iris.core.nms.v1_21_R5;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.ResultLocator;
import com.volmit.iris.engine.framework.WrongEngineBroException;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisStructurePopulator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.reflect.WrappedField;
import com.volmit.iris.util.reflect.WrappedReturningMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportSystemDetails;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.SectionPosition;
import net.minecraft.core.Holder.c;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.generator.CustomChunkGenerator;
import org.bukkit.craftbukkit.v1_21_R5.generator.structure.CraftStructure;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.util.BoundingBox;
import org.spigotmc.SpigotWorldConfig;

public class IrisChunkGenerator extends CustomChunkGenerator {
   private static final WrappedField<ChunkGenerator, WorldChunkManager> BIOME_SOURCE;
   private static final WrappedReturningMethod<HeightMap, Object> SET_HEIGHT;
   private final ChunkGenerator delegate;
   private final Engine engine;
   private final KMap<ResourceKey<Structure>, KSet<String>> structures = new KMap();
   private final IrisStructurePopulator populator;

   public IrisChunkGenerator(ChunkGenerator delegate, long seed, Engine engine, World world) {
      super(((CraftWorld)var5).getHandle(), edit(var1, new CustomBiomeSource(var2, var4, var5)), (org.bukkit.generator.ChunkGenerator)null);
      this.delegate = var1;
      this.engine = var4;
      this.populator = new IrisStructurePopulator(var4);
      IrisDimension var6 = var4.getDimension();
      KSet var7 = new KSet(new IrisJigsawStructure[0]);
      this.addAll(var6.getJigsawStructures(), var7);
      Iterator var8 = var6.getAllRegions(var4).iterator();

      Iterator var10;
      while(var8.hasNext()) {
         IrisRegion var9 = (IrisRegion)var8.next();
         this.addAll(var9.getJigsawStructures(), var7);
         var10 = var9.getAllBiomes(var4).iterator();

         while(var10.hasNext()) {
            IrisBiome var11 = (IrisBiome)var10.next();
            this.addAll(var11.getJigsawStructures(), var7);
         }
      }

      String var21 = var6.getStronghold();
      if (var21 != null) {
         var7.add((IrisJigsawStructure)var4.getData().getJigsawStructureLoader().load(var21));
      }

      var7.removeIf(Objects::isNull);
      IRegistry var22 = (IRegistry)((CraftWorld)var5).getHandle().K_().a(Registries.bj).orElseThrow();
      var10 = var7.iterator();

      while(var10.hasNext()) {
         IrisJigsawStructure var23 = (IrisJigsawStructure)var10.next();

         try {
            String var12 = var23.getStructureKey();
            if (var12 != null) {
               boolean var13 = var12.startsWith("#");
               if (var13) {
                  var12 = var12.substring(1);
               }

               MinecraftKey var14 = MinecraftKey.a(var12);
               if (!var13) {
                  ((KSet)this.structures.computeIfAbsent(ResourceKey.a(Registries.bj, var14), (var0) -> {
                     return new KSet(new String[0]);
                  })).add(var23.getLoadKey());
               } else {
                  TagKey var15 = TagKey.a(Registries.bj, var14);
                  Named var16 = (Named)var22.a(var15).orElse((Object)null);
                  if (var16 == null) {
                     Iris.error("Could not find structure tag: " + var12);
                  } else {
                     Iterator var17 = var16.iterator();

                     while(var17.hasNext()) {
                        Holder var18 = (Holder)var17.next();
                        ResourceKey var19 = (ResourceKey)var18.e().orElse((Object)null);
                        if (var19 != null) {
                           ((KSet)this.structures.computeIfAbsent(var19, (var0) -> {
                              return new KSet(new String[0]);
                           })).add(var23.getLoadKey());
                        }
                     }
                  }
               }
            }
         } catch (Throwable var20) {
            Iris.error("Failed to load structure: " + var23.getLoadKey());
            var20.printStackTrace();
         }
      }

   }

   private void addAll(KList<IrisJigsawStructurePlacement> placements, KSet<IrisJigsawStructure> structures) {
      if (var1 != null) {
         Stream var10000 = var1.stream().map(IrisJigsawStructurePlacement::getStructure);
         ResourceLoader var10001 = this.engine.getData().getJigsawStructureLoader();
         Objects.requireNonNull(var10001);
         var10000 = var10000.map(var10001::load).filter(Objects::nonNull);
         Objects.requireNonNull(var2);
         var10000.forEach(var2::add);
      }
   }

   @Nullable
   public Pair<BlockPosition, Holder<Structure>> a(WorldServer level, HolderSet<Structure> holders, BlockPosition pos, int radius, boolean findUnexplored) {
      if (var2.b() == 0) {
         return null;
      } else if (var2.e().orElse((Object)null) == StructureTags.a) {
         Position2 var14 = this.engine.getNearestStronghold(new Position2(var3.u(), var3.w()));
         return var14 == null ? null : new Pair(new BlockPosition(var14.getX(), 0, var14.getZ()), var2.a(0));
      } else if (this.engine.getDimension().isDisableExplorerMaps()) {
         return null;
      } else {
         KMap var6 = new KMap();
         Iterator var7 = var2.iterator();

         while(true) {
            Holder var8;
            KSet var10;
            do {
               do {
                  if (!var7.hasNext()) {
                     if (var6.isEmpty()) {
                        return null;
                     }

                     ResultLocator var15 = ResultLocator.locateStructure(var6.keySet()).then((var1x, var2x, var3x) -> {
                        return (Holder)var6.get(var3x.getLoadKey());
                     });
                     if (var5) {
                        var15 = var15.then((var0, var1x, var2x) -> {
                           return var0.getMantle().getMantle().getChunk(var1x.getX(), var1x.getZ()).isFlagged(MantleFlag.DISCOVERED) ? null : var2x;
                        });
                     }

                     try {
                        ResultLocator.Result var16 = (ResultLocator.Result)var15.find(this.engine, new Position2(var3.u() >> 4, var3.w() >> 4), (long)var4 * 10L, (var0) -> {
                        }, false).get();
                        if (var16 == null) {
                           return null;
                        }

                        BlockPosition var17 = new BlockPosition(var16.getBlockX(), 0, var16.getBlockZ());
                        return Pair.of(var17, (Holder)var16.obj());
                     } catch (ExecutionException | InterruptedException | WrongEngineBroException var13) {
                        return null;
                     }
                  }

                  var8 = (Holder)var7.next();
               } while(var8 == null);

               ResourceKey var9 = (ResourceKey)var8.e().orElse((Object)null);
               var10 = (KSet)this.structures.get(var9);
            } while(var10 == null);

            Iterator var11 = var10.iterator();

            while(var11.hasNext()) {
               String var12 = (String)var11.next();
               var6.put(var12, var8);
            }
         }
      }
   }

   protected MapCodec<? extends ChunkGenerator> b() {
      return MapCodec.unit((Supplier)null);
   }

   public ChunkGenerator getDelegate() {
      ChunkGenerator var2 = this.delegate;
      if (var2 instanceof CustomChunkGenerator) {
         CustomChunkGenerator var1 = (CustomChunkGenerator)var2;
         return var1.getDelegate();
      } else {
         return this.delegate;
      }
   }

   public int g() {
      return this.delegate.g();
   }

   public int f() {
      return this.delegate.f();
   }

   public void a(IRegistryCustom registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, IChunkAccess access, StructureTemplateManager templateManager, ResourceKey<net.minecraft.world.level.World> levelKey) {
      if (var3.a()) {
         ChunkCoordIntPair var7 = var4.f();
         SectionPosition var8 = SectionPosition.a(var4);
         IRegistry var9 = var1.f(Registries.bj);
         this.populator.populateStructures(var7.h, var7.i, (var10, var11) -> {
            MinecraftKey var12 = MinecraftKey.c(var10);
            if (var12 == null) {
               return false;
            } else {
               c var13 = (c)var9.c(var12).orElse((Object)null);
               if (var13 == null) {
                  return false;
               } else {
                  Structure var14 = (Structure)var13.a();
                  HolderSet var15 = var14.a();
                  StructureStart var16 = var14.a(var13, var6, var1, this, this.b, var2.c(), var5, var2.d(), var7, fetchReferences(var3, var4, var8, var14), var4, (var2x) -> {
                     return var11 || var15.a(var2x);
                  });
                  if (!var16.b()) {
                     return false;
                  } else {
                     StructureBoundingBox var17 = var16.a();
                     AsyncStructureSpawnEvent var18 = new AsyncStructureSpawnEvent(var3.a.getMinecraftWorld().getWorld(), CraftStructure.minecraftToBukkit(var14), new BoundingBox((double)var17.h(), (double)var17.i(), (double)var17.j(), (double)var17.k(), (double)var17.l(), (double)var17.m()), var7.h, var7.i);
                     Bukkit.getPluginManager().callEvent(var18);
                     if (!var18.isCancelled()) {
                        var3.a(var8, var14, var16, var4);
                     }

                     return true;
                  }
               }
            }
         });
      }
   }

   private static int fetchReferences(StructureManager structureManager, IChunkAccess access, SectionPosition sectionPos, Structure structure) {
      StructureStart var4 = var0.a(var2, var3, var1);
      return var4 != null ? var4.f() : 0;
   }

   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> holderlookup, RandomState randomstate, long i, SpigotWorldConfig conf) {
      return this.delegate.createState(var1, var2, var3, var5);
   }

   public void a(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      this.delegate.a(var1, var2, var3);
   }

   public CompletableFuture<IChunkAccess> a(RandomState randomstate, Blender blender, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      return this.delegate.a(var1, var2, var3, var4);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager, RandomState randomstate, IChunkAccess ichunkaccess) {
      this.delegate.a(var1, var2, var3, var4);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess, long seed, RandomState randomstate, BiomeManager biomemanager, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      this.delegate.a(var1, var2, var4, var5, var6, var7);
   }

   public CompletableFuture<IChunkAccess> a(Blender blender, RandomState randomstate, StructureManager structuremanager, IChunkAccess ichunkaccess) {
      return this.delegate.a(var1, var2, var3, var4);
   }

   public WeightedList<net.minecraft.world.level.biome.BiomeSettingsMobs.c> a(Holder<BiomeBase> holder, StructureManager structuremanager, EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
      return this.delegate.a(var1, var2, var3, var4);
   }

   public void a(GeneratorAccessSeed generatoraccessseed, IChunkAccess ichunkaccess, StructureManager structuremanager) {
      this.applyBiomeDecoration(var1, var2, var3, true);
   }

   public void a(List<String> list, RandomState randomstate, BlockPosition blockposition) {
      this.delegate.a(var1, var2, var3);
   }

   public void applyBiomeDecoration(GeneratorAccessSeed generatoraccessseed, IChunkAccess ichunkaccess, StructureManager structuremanager, boolean vanilla) {
      this.addVanillaDecorations(var1, var2, var3);
      this.delegate.applyBiomeDecoration(var1, var2, var3, false);
   }

   public void addVanillaDecorations(GeneratorAccessSeed level, IChunkAccess chunkAccess, StructureManager structureManager) {
      if (var3.a()) {
         SectionPosition var4 = SectionPosition.a(var2.f(), var1.aq());
         BlockPosition var5 = var4.j();
         SeededRandom var6 = new SeededRandom(new XoroshiroRandomSource(RandomSupport.a()));
         long var7 = var6.a(var1.F(), var5.u(), var5.w());
         IRegistry var9 = var1.K_().f(Registries.bj);
         List var10 = var9.s().sorted(Comparator.comparingInt((var0) -> {
            return var0.c().ordinal();
         })).toList();
         HeightMap var11 = var2.a(Type.a);
         HeightMap var12 = var2.a(Type.c);
         HeightMap var13 = var2.a(Type.e);
         HeightMap var14 = var2.a(Type.f);

         int var15;
         for(var15 = 0; var15 < 16; ++var15) {
            for(int var16 = 0; var16 < 16; ++var16) {
               int var17 = var15 + var5.u();
               int var18 = var16 + var5.w();
               int var19 = this.engine.getHeight(var17, var18, false) + this.engine.getMinHeight() + 1;
               int var20 = this.engine.getHeight(var17, var18, true) + this.engine.getMinHeight() + 1;
               SET_HEIGHT.invoke(var12, var15, var16, Math.min(var20, var12.a(var15, var16)));
               SET_HEIGHT.invoke(var11, var15, var16, Math.min(var19, var11.a(var15, var16)));
               SET_HEIGHT.invoke(var13, var15, var16, Math.min(var19, var13.a(var15, var16)));
               SET_HEIGHT.invoke(var14, var15, var16, Math.min(var19, var14.a(var15, var16)));
            }
         }

         for(var15 = 0; var15 < var10.size(); ++var15) {
            Structure var22 = (Structure)var10.get(var15);
            var6.b(var7, var15, var22.c().ordinal());
            Supplier var23 = () -> {
               Optional var10000 = var9.d(var22).map(Object::toString);
               Objects.requireNonNull(var22);
               return (String)var10000.orElseGet(var22::toString);
            };

            try {
               var1.a(var23);
               var3.a(var4, var22).forEach((var5x) -> {
                  var5x.a(var1, var3, this, var6, getWritableArea(var2), var2.f());
               });
            } catch (Exception var21) {
               CrashReport var24 = CrashReport.a(var21, "Feature placement");
               CrashReportSystemDetails var25 = var24.a("Feature");
               Objects.requireNonNull(var23);
               var25.a("Description", var23::get);
               throw new ReportedException(var24);
            }
         }

         HeightMap.a(var2, ChunkStatus.b);
      }
   }

   private static StructureBoundingBox getWritableArea(IChunkAccess ichunkaccess) {
      ChunkCoordIntPair var1 = var0.f();
      int var2 = var1.d();
      int var3 = var1.e();
      LevelHeightAccessor var4 = var0.B();
      int var5 = var4.L_() + 1;
      int var6 = var4.ao();
      return new StructureBoundingBox(var2, var5, var3, var2 + 15, var6, var3 + 15);
   }

   public void a(RegionLimitedWorldAccess regionlimitedworldaccess) {
      this.delegate.a(var1);
   }

   public int a(LevelHeightAccessor levelheightaccessor) {
      return this.delegate.a(var1);
   }

   public int e() {
      return this.delegate.e();
   }

   public int a(int i, int j, Type heightmap_type, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      return var4.L_() + this.engine.getHeight(var1, var2, !var3.e().test(Blocks.J.m())) + 1;
   }

   public BlockColumn a(int i, int j, LevelHeightAccessor levelheightaccessor, RandomState randomstate) {
      int var5 = this.engine.getHeight(var1, var2, true);
      int var6 = this.engine.getHeight(var1, var2, false);
      IBlockData[] var7 = new IBlockData[var3.M_()];

      for(int var8 = 0; var8 < var7.length; ++var8) {
         if (var8 <= var5) {
            var7[var8] = Blocks.b.m();
         } else if (var8 <= var6) {
            var7[var8] = Blocks.J.m();
         } else {
            var7[var8] = Blocks.a.m();
         }
      }

      return new BlockColumn(var3.L_(), var7);
   }

   public Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> c() {
      return this.delegate.c();
   }

   public void a() {
      this.delegate.a();
   }

   public BiomeSettingsGeneration a(Holder<BiomeBase> holder) {
      return this.delegate.a(var1);
   }

   private static ChunkGenerator edit(ChunkGenerator generator, WorldChunkManager source) {
      try {
         BIOME_SOURCE.set(var0, var1);
         if (var0 instanceof CustomChunkGenerator) {
            CustomChunkGenerator var2 = (CustomChunkGenerator)var0;
            BIOME_SOURCE.set(var2.getDelegate(), var1);
         }

         return var0;
      } catch (IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   static {
      Field var0 = null;
      Field[] var1 = ChunkGenerator.class.getDeclaredFields();
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];
         if (var4.getType().equals(WorldChunkManager.class)) {
            var0 = var4;
            break;
         }
      }

      if (var0 == null) {
         throw new RuntimeException("Could not find biomeSource field in ChunkGenerator!");
      } else {
         Method var7 = null;
         Method[] var8 = HeightMap.class.getDeclaredMethods();
         var3 = var8.length;

         for(int var9 = 0; var9 < var3; ++var9) {
            Method var5 = var8[var9];
            Class[] var6 = var5.getParameterTypes();
            if (var6.length == 3 && Arrays.equals(var6, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}) && var5.getReturnType().equals(Void.TYPE)) {
               var7 = var5;
               break;
            }
         }

         if (var7 == null) {
            throw new RuntimeException("Could not find setHeight method in Heightmap!");
         } else {
            BIOME_SOURCE = new WrappedField(ChunkGenerator.class, var0.getName());
            SET_HEIGHT = new WrappedReturningMethod(HeightMap.class, var7.getName(), var7.getParameterTypes());
         }
      }
   }
}
