package com.volmit.iris.engine.decorator;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedComponent;
import com.volmit.iris.engine.framework.EngineDecorator;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

public abstract class IrisEngineDecorator extends EngineAssignedComponent implements EngineDecorator {
   private final IrisDecorationPart part;
   private final long seed;
   private final long modX;
   private final long modZ;

   public IrisEngineDecorator(Engine engine, String name, IrisDecorationPart part) {
      super(var1, var2 + " Decorator");
      this.part = var3;
      this.seed = this.getSeed() + 29356788L - (long)var3.ordinal() * 10439677L;
      this.modX = (long)(29356788 ^ var3.ordinal() + 6);
      this.modZ = (long)(10439677 ^ var3.ordinal() + 1);
   }

   @BlockCoordinates
   protected RNG getRNG(int x, int z) {
      return new RNG((long)var1 * this.modX + (long)var2 * this.modZ + this.seed);
   }

   protected IrisDecorator getDecorator(RNG rng, IrisBiome biome, double realX, double realZ) {
      KList var7 = new KList();
      RNG var8 = new RNG(this.seed);
      Iterator var9 = var2.getDecorators().iterator();

      while(var9.hasNext()) {
         IrisDecorator var10 = (IrisDecorator)var9.next();

         try {
            if (var10.getPartOf().equals(this.part) && var10.getBlockData(var2, var8, var3, var5, this.getData()) != null) {
               var7.add((Object)var10);
            }
         } catch (Throwable var12) {
            Iris.reportError(var12);
            Iris.error("PART OF: " + var2.getLoadFile().getAbsolutePath() + " HAS AN INVALID DECORATOR near 'partOf'!!!");
         }
      }

      return var7.isNotEmpty() ? (IrisDecorator)var7.get(var1.nextInt(var7.size())) : null;
   }

   protected BlockData fixFaces(BlockData b, Hunk<BlockData> hunk, int rX, int rZ, int x, int y, int z) {
      if (B.isVineBlock(var1)) {
         MultipleFacing var8 = (MultipleFacing)var1.clone();
         var8.getFaces().forEach((var1x) -> {
            var8.setFace(var1x, false);
         });
         boolean var9 = false;
         BlockFace[] var10 = BlockFace.values();
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            BlockFace var13 = var10[var12];
            if (var13.isCartesian()) {
               int var14 = var6 + var13.getModY();
               BlockData var15 = this.getEngine().getMantle().get(var5 + var13.getModX(), var14, var7 + var13.getModZ());
               if (var15.isFaceSturdy(var13.getOppositeFace(), BlockSupport.FULL)) {
                  var9 = true;
                  var8.setFace(var13, true);
               } else {
                  int var16 = var3 + var13.getModX();
                  int var17 = var4 + var13.getModZ();
                  if (var16 >= 0 && var16 <= 15 && var17 >= 0 && var17 <= 15 && var14 >= 0 && var14 <= var2.getHeight()) {
                     var15 = (BlockData)var2.get(var16, var14, var17);
                     if (var15.isFaceSturdy(var13.getOppositeFace(), BlockSupport.FULL)) {
                        var9 = true;
                        var8.setFace(var13, true);
                     }
                  }
               }
            }
         }

         if (!var9) {
            var8.setFace(BlockFace.DOWN, true);
         }

         return var8;
      } else {
         return var1;
      }
   }

   @Generated
   public IrisDecorationPart getPart() {
      return this.part;
   }
}
