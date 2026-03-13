package org.terraform.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.ancientcity.AncientCityPopulator;
import org.terraform.structure.catacombs.CatacombsPopulator;
import org.terraform.structure.caves.LargeCavePopulator;
import org.terraform.structure.mineshaft.BadlandsMinePopulator;
import org.terraform.structure.mineshaft.MineshaftPopulator;
import org.terraform.structure.monument.MonumentPopulator;
import org.terraform.structure.pillager.mansion.MansionPopulator;
import org.terraform.structure.pillager.outpost.OutpostPopulator;
import org.terraform.structure.pyramid.PyramidPopulator;
import org.terraform.structure.small.DesertWellPopulator;
import org.terraform.structure.small.WitchHutPopulator;
import org.terraform.structure.small.buriedtreasure.BuriedTreasurePopulator;
import org.terraform.structure.small.dungeon.SmallDungeonPopulator;
import org.terraform.structure.small.igloo.IglooPopulator;
import org.terraform.structure.small.ruinedportal.RuinedPortalPopulator;
import org.terraform.structure.small.shipwreck.ShipwreckPopulator;
import org.terraform.structure.stronghold.StrongholdPopulator;
import org.terraform.structure.trailruins.TrailRuinsPopulator;
import org.terraform.structure.trialchamber.TrialChamberPopulator;
import org.terraform.structure.village.VillagePopulator;
import org.terraform.structure.villagehouse.VillageHousePopulator;
import org.terraform.structure.warmoceanruins.WarmOceanRuinsPopulator;
import org.terraform.utils.datastructs.ConcurrentLRUCache;
import org.terraform.utils.version.Version;

public class StructureRegistry {
   public static final Map<StructureType, SingleMegaChunkStructurePopulator[]> largeStructureRegistry = new EnumMap(StructureType.class);
   public static final Collection<MultiMegaChunkStructurePopulator> smallStructureRegistry = new ArrayList();
   private static final ConcurrentLRUCache<StructureRegistry.MegaChunkKey, SingleMegaChunkStructurePopulator[]> queryCache = new ConcurrentLRUCache("structureQueryCache", 50, (key) -> {
      TerraformWorld tw = key.tw;
      MegaChunk mc = key.mc;
      Random structRand = tw.getHashedRand(9L, mc.getX(), mc.getZ());
      int maxStructures = 3;
      SingleMegaChunkStructurePopulator[] pops = new SingleMegaChunkStructurePopulator[maxStructures];
      int size = 0;
      SingleMegaChunkStructurePopulator[] returnVal;
      int var9;
      int var10;
      if (largeStructureRegistry.containsKey(StructureType.MEGA_DUNGEON) && ((SingleMegaChunkStructurePopulator[])largeStructureRegistry.get(StructureType.MEGA_DUNGEON)).length > 0) {
         SingleMegaChunkStructurePopulator[] available = (SingleMegaChunkStructurePopulator[])shuffleArray(structRand, (Object[])largeStructureRegistry.get(StructureType.MEGA_DUNGEON));
         returnVal = available;
         var9 = available.length;

         for(var10 = 0; var10 < var9; ++var10) {
            SingleMegaChunkStructurePopulator pop = returnVal[var10];
            int[] coords = mc.getCenterBiomeSectionBlockCoords();
            if (coords != null && TConfig.areStructuresEnabled() && pop.canSpawn(tw, coords[0] >> 4, coords[1] >> 4, mc.getCenterBiomeSection(tw).getBiomeBank())) {
               pops[size] = pop;
               ++size;
               break;
            }
         }
      }

      StructureType[] types = new StructureType[]{StructureType.LARGE_CAVE, StructureType.VILLAGE, StructureType.LARGE_MISC};
      types = (StructureType[])shuffleArray(structRand, types);
      StructureType[] var18 = types;
      var9 = types.length;

      for(var10 = 0; var10 < var9; ++var10) {
         StructureType type = var18[var10];
         if (largeStructureRegistry.containsKey(type)) {
            SingleMegaChunkStructurePopulator[] var20 = (SingleMegaChunkStructurePopulator[])largeStructureRegistry.get(type);
            int var13 = var20.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               SingleMegaChunkStructurePopulator popx = var20[var14];
               int[] coordsx = mc.getCenterBiomeSectionBlockCoords();
               if (TConfig.areStructuresEnabled() && popx.canSpawn(tw, coordsx[0] >> 4, coordsx[1] >> 4, mc.getCenterBiomeSection(tw).getBiomeBank())) {
                  pops[size] = popx;
                  ++size;
                  break;
               }
            }
         }

         if (size >= maxStructures) {
            break;
         }
      }

      returnVal = new SingleMegaChunkStructurePopulator[size];
      System.arraycopy(pops, 0, returnVal, 0, size);
      return returnVal;
   });

   public static void init() {
      registerStructure(StructureType.VILLAGE, new VillageHousePopulator());
      registerStructure(StructureType.VILLAGE, new VillagePopulator());
      registerStructure(StructureType.VILLAGE, new OutpostPopulator());
      registerStructure(StructureType.MEGA_DUNGEON, new PyramidPopulator());
      registerStructure(StructureType.MEGA_DUNGEON, new MonumentPopulator());
      registerStructure(StructureType.MEGA_DUNGEON, new StrongholdPopulator());
      registerStructure(StructureType.MEGA_DUNGEON, new MansionPopulator());
      registerStructure(StructureType.MEGA_DUNGEON, new AncientCityPopulator());
      if (Version.VERSION.isAtLeast(Version.v1_21)) {
         registerStructure(StructureType.MEGA_DUNGEON, new TrialChamberPopulator());
      }

      registerStructure(StructureType.LARGE_CAVE, new LargeCavePopulator());
      registerStructure(StructureType.LARGE_MISC, new MineshaftPopulator());
      registerStructure(StructureType.LARGE_MISC, new CatacombsPopulator());
      registerStructure(StructureType.LARGE_MISC, new BadlandsMinePopulator());
      registerStructure(StructureType.LARGE_MISC, new WarmOceanRuinsPopulator());
      registerStructure(StructureType.LARGE_MISC, new TrailRuinsPopulator());
      registerStructure(StructureType.SMALL, new SmallDungeonPopulator());
      registerStructure(StructureType.SMALL, new ShipwreckPopulator());
      registerStructure(StructureType.SMALL, new BuriedTreasurePopulator());
      registerStructure(StructureType.SMALL, new RuinedPortalPopulator());
      registerStructure(StructureType.SMALL, new IglooPopulator());
      registerStructure(StructureType.SMALL, new DesertWellPopulator());
      registerStructure(StructureType.SMALL, new WitchHutPopulator());
   }

   @Nullable
   public static StructureType getStructureType(@NotNull Class<? extends SingleMegaChunkStructurePopulator> populatorType) {
      Iterator var1 = largeStructureRegistry.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<StructureType, SingleMegaChunkStructurePopulator[]> entry = (Entry)var1.next();
         SingleMegaChunkStructurePopulator[] var3 = (SingleMegaChunkStructurePopulator[])entry.getValue();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SingleMegaChunkStructurePopulator pops = var3[var5];
            if (populatorType.isInstance(pops)) {
               return (StructureType)entry.getKey();
            }
         }
      }

      return null;
   }

   @NotNull
   public static SingleMegaChunkStructurePopulator[] getLargeStructureForMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      return (SingleMegaChunkStructurePopulator[])queryCache.get(new StructureRegistry.MegaChunkKey(tw, mc));
   }

   @NotNull
   private static Object[] shuffleArray(@NotNull Random rand, Object[] ar) {
      ar = (Object[])ar.clone();
      if (ar.length == 0) {
         return ar;
      } else {
         for(int i = ar.length - 1; i > 0; --i) {
            int index = rand.nextInt(i + 1);
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
         }

         return ar;
      }
   }

   public static void registerStructure(StructureType type, @NotNull StructurePopulator pop) {
      if (pop.isEnabled()) {
         if (pop instanceof SingleMegaChunkStructurePopulator) {
            SingleMegaChunkStructurePopulator[] pops = new SingleMegaChunkStructurePopulator[]{(SingleMegaChunkStructurePopulator)pop};
            if (largeStructureRegistry.containsKey(type)) {
               SingleMegaChunkStructurePopulator[] existing = (SingleMegaChunkStructurePopulator[])largeStructureRegistry.get(type);
               SingleMegaChunkStructurePopulator[] old = pops;
               pops = new SingleMegaChunkStructurePopulator[existing.length + 1];
               System.arraycopy(existing, 0, pops, 0, existing.length);
               System.arraycopy(old, 0, pops, existing.length, 1);
            }

            TerraformGeneratorPlugin.logger.info("[Structure Registry] Registered Large Structure: " + pop.getClass().getSimpleName());
            largeStructureRegistry.put(type, pops);
         } else if (pop instanceof MultiMegaChunkStructurePopulator) {
            TerraformGeneratorPlugin.logger.info("[Structure Registry] Registered Small Structure: " + pop.getClass().getSimpleName());
            smallStructureRegistry.add((MultiMegaChunkStructurePopulator)pop);
         }

      }
   }

   @NotNull
   public static StructurePopulator[] getAllPopulators() {
      int size = smallStructureRegistry.size();

      StructurePopulator[] types;
      for(Iterator var1 = largeStructureRegistry.values().iterator(); var1.hasNext(); size += types.length) {
         types = (StructurePopulator[])var1.next();
      }

      StructurePopulator[] pops = new StructurePopulator[size];
      int index = 0;

      Iterator var3;
      for(var3 = smallStructureRegistry.iterator(); var3.hasNext(); ++index) {
         StructurePopulator pop = (StructurePopulator)var3.next();
         pops[index] = pop;
      }

      var3 = largeStructureRegistry.values().iterator();

      while(var3.hasNext()) {
         StructurePopulator[] types = (StructurePopulator[])var3.next();
         StructurePopulator[] var5 = types;
         int var6 = types.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            StructurePopulator pop = var5[var7];
            pops[index] = pop;
            ++index;
         }
      }

      return pops;
   }

   private static class MegaChunkKey {
      private final TerraformWorld tw;
      private final MegaChunk mc;

      public MegaChunkKey(TerraformWorld tw, MegaChunk mc) {
         this.tw = tw;
         this.mc = mc;
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.tw.hashCode(), this.mc.getX(), this.mc.getZ()});
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof StructureRegistry.MegaChunkKey)) {
            return false;
         } else {
            StructureRegistry.MegaChunkKey other = (StructureRegistry.MegaChunkKey)obj;
            return this.tw.equals(other.tw) && this.mc.getX() == other.mc.getX() && this.mc.getZ() == other.mc.getZ();
         }
      }
   }
}
