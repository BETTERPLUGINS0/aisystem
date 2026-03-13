package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedComponent;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.List;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.Nullable;

public class IrisStructurePopulator extends EngineAssignedComponent {
   private final CNG cng;
   private final long mantle;
   private final long jigsaw;

   public IrisStructurePopulator(Engine engine) {
      super(var1, "Datapack Structures");
      this.mantle = var1.getSeedManager().getMantle();
      this.jigsaw = var1.getSeedManager().getJigsaw();
      this.cng = NoiseStyle.STATIC.create(new RNG(this.jigsaw));
   }

   @ChunkCoordinates
   public void populateStructures(int x, int z, BiPredicate<String, Boolean> placer) {
      int var4 = var1 << 4;
      int var5 = var2 << 4;
      IrisDimension var6 = this.getDimension();
      IrisRegion var7 = this.getEngine().getRegion(var4 + 8, var5 + 8);
      IrisBiome var8 = this.getEngine().getSurfaceBiome(var4 + 8, var5 + 8);
      ResourceLoader var9 = this.getData().getJigsawStructureLoader();
      long var10 = (long)this.cng.fit(Integer.MIN_VALUE, Integer.MAX_VALUE, (double)var1, (double)var2);
      if (var6.getStronghold() != null) {
         KList var12 = this.getDimension().getStrongholds(this.mantle);
         if (var12 != null && var12.contains(new Position2(var4, var5))) {
            this.place(var3, (IrisJigsawStructure)var9.load(var6.getStronghold()), new RNG(var10), true);
            return;
         }
      }

      boolean var13 = this.place(var3, var8.getJigsawStructures(), var10, var1, var2);
      if (!var13) {
         var13 = this.place(var3, var7.getJigsawStructures(), var10, var1, var2);
      }

      if (!var13) {
         this.place(var3, var6.getJigsawStructures(), var10, var1, var2);
      }

   }

   private boolean place(BiPredicate<String, Boolean> placer, KList<IrisJigsawStructurePlacement> placements, long seed, int x, int z) {
      IrisJigsawStructurePlacement var7 = this.pick(var2, var3, var5, var6);
      return var7 == null ? false : this.place(var1, (IrisJigsawStructure)this.getData().getJigsawStructureLoader().load(var7.getStructure()), new RNG(var3), false);
   }

   @Nullable
   @ChunkCoordinates
   private IrisJigsawStructurePlacement pick(List<IrisJigsawStructurePlacement> structures, long seed, int x, int z) {
      return (IrisJigsawStructurePlacement)IRare.pick(var1.stream().filter((var3) -> {
         return var3.shouldPlace(this.getData(), this.getDimension().getJigsawStructureDivisor(), this.jigsaw, var4, var5);
      }).toList(), (new RNG(var2)).nextDouble());
   }

   @ChunkCoordinates
   private boolean place(BiPredicate<String, Boolean> placer, IrisJigsawStructure structure, RNG rng, boolean ignoreBiomes) {
      if (var2 != null && !var2.getDatapackStructures().isEmpty()) {
         KList var5 = var2.getDatapackStructures().shuffleCopy(var3);

         String var6;
         do {
            do {
               if (!var5.isNotEmpty()) {
                  return false;
               }

               var6 = (String)var5.removeFirst();
            } while(var6 == null);
         } while(!var1.test(var6, var4 || var2.isForcePlace()));

         return true;
      } else {
         return false;
      }
   }
}
