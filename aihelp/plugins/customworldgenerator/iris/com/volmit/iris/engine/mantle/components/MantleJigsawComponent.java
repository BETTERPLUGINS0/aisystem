package com.volmit.iris.engine.mantle.components;

import com.volmit.iris.engine.jigsaw.PlannedStructure;
import com.volmit.iris.engine.mantle.ComponentFlag;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.IrisMantleComponent;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.mantle.flag.ReservedFlag;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.slices.container.JigsawStructuresContainer;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@ComponentFlag(ReservedFlag.JIGSAW)
public class MantleJigsawComponent extends IrisMantleComponent {
   private final CNG cng;

   public MantleJigsawComponent(EngineMantle engineMantle) {
      super(var1, ReservedFlag.JIGSAW, 1);
      this.cng = NoiseStyle.STATIC.create(new RNG(this.jigsaw()));
   }

   public void generateLayer(MantleWriter writer, int x, int z, ChunkContext context) {
      int var5 = 8 + (var2 << 4);
      int var6 = 8 + (var3 << 4);
      IrisRegion var7 = (IrisRegion)this.getComplex().getRegionStream().get((double)var5, (double)var6);
      IrisBiome var8 = (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)var5, (double)var6);
      this.generateJigsaw(var1, var2, var3, var8, var7);
   }

   @ChunkCoordinates
   private void generateJigsaw(MantleWriter writer, int x, int z, IrisBiome biome, IrisRegion region) {
      long var6 = (long)this.cng.fit(Integer.MIN_VALUE, Integer.MAX_VALUE, (double)var2, (double)var3);
      if (this.getDimension().getStronghold() != null) {
         KList var8 = this.getDimension().getStrongholds(this.seed());
         if (var8 != null) {
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               Position2 var10 = (Position2)var9.next();
               if (var2 == var10.getX() >> 4 && var3 == var10.getZ() >> 4) {
                  IrisJigsawStructure var11 = (IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(this.getDimension().getStronghold());
                  this.place(var1, var10.toIris(), var11, new RNG(var6), true);
                  return;
               }
            }
         }
      }

      KSet var12 = new KSet(new Position2[0]);
      KMap var13 = new KMap();
      KMap var14 = new KMap();
      boolean var15 = this.placeStructures(var1, var6, var2, var3, var4.getJigsawStructures(), var12, var13, var14);
      if (!var15) {
         var15 = this.placeStructures(var1, var6, var2, var3, var5.getJigsawStructures(), var12, var13, var14);
      }

      if (!var15) {
         this.placeStructures(var1, var6, var2, var3, this.getDimension().getJigsawStructures(), var12, var13, var14);
      }

   }

   @ChunkCoordinates
   private boolean placeStructures(MantleWriter writer, long seed, int x, int z, KList<IrisJigsawStructurePlacement> structures, KSet<Position2> cachedRegions, KMap<String, KSet<Position2>> cache, KMap<Position2, Double> distanceCache) {
      IrisJigsawStructurePlacement var10 = this.pick(var6, var2, var4, var5);

      try {
         if (var10 == null || this.checkMinDistances(var10.collectMinDistances(), var4, var5, var7, var8, var9)) {
            return false;
         }
      } catch (Throwable var14) {
      }

      RNG var11 = new RNG(var2);
      IrisPosition var12 = new IrisPosition((var4 << 4) + var11.nextInt(15), 0, (var5 << 4) + var11.nextInt(15));
      IrisJigsawStructure var13 = (IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(var10.getStructure());
      return this.place(var1, var12, var13, var11, false);
   }

   @ChunkCoordinates
   private boolean checkMinDistances(KMap<String, Integer> minDistances, int x, int z, KSet<Position2> cachedRegions, KMap<String, KSet<Position2>> cache, KMap<Position2, Double> distanceCache) {
      int var7 = 0;

      int var9;
      for(Iterator var8 = var1.values().iterator(); var8.hasNext(); var7 = Math.max(var7, var9)) {
         var9 = (Integer)var8.next();
      }

      for(int var17 = -var7; var17 <= var7; ++var17) {
         for(var9 = -var7; var9 <= var7; ++var9) {
            Position2 var10 = new Position2(var17 + var2 >> 5, var9 + var3 >> 5);
            if (!var4.contains(var10)) {
               var4.add(var10);
               JigsawStructuresContainer var11 = (JigsawStructuresContainer)this.getMantle().get(var10.getX(), 0, var10.getZ(), JigsawStructuresContainer.class);
               if (var11 != null) {
                  Iterator var12 = var11.getStructures().iterator();

                  while(var12.hasNext()) {
                     String var13 = (String)var12.next();
                     ((KSet)var5.computeIfAbsent(var13, (var0) -> {
                        return new KSet(new Position2[0]);
                     })).addAll(var11.getPositions(var13));
                  }
               }
            }
         }
      }

      Position2 var18 = new Position2(var2, var3);
      Iterator var19 = var1.keySet().iterator();

      while(true) {
         String var20;
         do {
            if (!var19.hasNext()) {
               return false;
            }

            var20 = (String)var19.next();
         } while(!var5.containsKey(var20));

         double var21 = (double)(Integer)var1.get(var20);
         var21 *= var21;
         Iterator var22 = ((KSet)var5.get(var20)).iterator();

         while(var22.hasNext()) {
            Position2 var14 = (Position2)var22.next();
            double var15 = (Double)var6.computeIfAbsent(var14, (var1x) -> {
               return var1x.distance(var18);
            });
            if (var21 > var15) {
               return true;
            }
         }
      }
   }

   @ChunkCoordinates
   public IrisJigsawStructure guess(int x, int z) {
      long var3 = (long)this.cng.fit(Integer.MIN_VALUE, Integer.MAX_VALUE, (double)var1, (double)var2);
      IrisBiome var5 = this.getEngineMantle().getEngine().getSurfaceBiome((var1 << 4) + 8, (var2 << 4) + 8);
      IrisRegion var6 = this.getEngineMantle().getEngine().getRegion((var1 << 4) + 8, (var2 << 4) + 8);
      if (this.getDimension().getStronghold() != null) {
         KList var7 = this.getDimension().getStrongholds(this.seed());
         if (var7 != null) {
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               Position2 var9 = (Position2)var8.next();
               if (var1 == var9.getX() >> 4 && var2 == var9.getZ() >> 4) {
                  return (IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(this.getDimension().getStronghold());
               }
            }
         }
      }

      IrisJigsawStructurePlacement var10 = this.pick(var5.getJigsawStructures(), var3, var1, var2);
      if (var10 == null) {
         var10 = this.pick(var6.getJigsawStructures(), var3, var1, var2);
      }

      if (var10 == null) {
         var10 = this.pick(this.getDimension().getJigsawStructures(), var3, var1, var2);
      }

      return var10 != null ? (IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(var10.getStructure()) : null;
   }

   @Nullable
   @ChunkCoordinates
   private IrisJigsawStructurePlacement pick(List<IrisJigsawStructurePlacement> structures, long seed, int x, int z) {
      return (IrisJigsawStructurePlacement)IRare.pick(var1.stream().filter((var3) -> {
         return var3.shouldPlace(this.getData(), this.getDimension().getJigsawStructureDivisor(), this.jigsaw(), var4, var5);
      }).toList(), (new RNG(var2)).nextDouble());
   }

   @BlockCoordinates
   private boolean place(MantleWriter writer, IrisPosition position, IrisJigsawStructure structure, RNG rng, boolean forcePlace) {
      return var3 != null && !var3.getDatapackStructures().isNotEmpty() ? (new PlannedStructure(var3, var2, var4, var5)).place(var1, this.getMantle(), var1.getEngine()) : false;
   }

   private long jigsaw() {
      return this.getEngineMantle().getEngine().getSeedManager().getJigsaw();
   }

   protected int computeRadius() {
      IrisDimension var1 = this.getDimension();
      KSet var2 = new KSet(new String[0]);
      if (var1.getStronghold() != null) {
         var2.add(var1.getStronghold());
      }

      Iterator var3 = var1.getJigsawStructures().iterator();

      while(var3.hasNext()) {
         IrisJigsawStructurePlacement var4 = (IrisJigsawStructurePlacement)var3.next();
         var2.add(var4.getStructure());
      }

      var3 = var1.getAllRegions(this::getData).iterator();

      Iterator var5;
      IrisJigsawStructurePlacement var6;
      while(var3.hasNext()) {
         IrisRegion var8 = (IrisRegion)var3.next();
         var5 = var8.getJigsawStructures().iterator();

         while(var5.hasNext()) {
            var6 = (IrisJigsawStructurePlacement)var5.next();
            var2.add(var6.getStructure());
         }
      }

      var3 = var1.getAllBiomes(this::getData).iterator();

      while(var3.hasNext()) {
         IrisBiome var9 = (IrisBiome)var3.next();
         var5 = var9.getJigsawStructures().iterator();

         while(var5.hasNext()) {
            var6 = (IrisJigsawStructurePlacement)var5.next();
            var2.add(var6.getStructure());
         }
      }

      int var7 = 0;

      String var11;
      for(Iterator var10 = var2.iterator(); var10.hasNext(); var7 = Math.max(var7, ((IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(var11)).getMaxDimension())) {
         var11 = (String)var10.next();
      }

      return var7;
   }
}
