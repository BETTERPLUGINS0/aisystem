package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("deposit")
@Desc("Creates ore & other block deposits underground")
public class IrisDepositGenerator {
   private final transient AtomicCache<KList<IrisObject>> objects = new AtomicCache();
   private final transient AtomicCache<KList<BlockData>> blockData = new AtomicCache();
   @Required
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("The minimum height this deposit can generate at")
   private int minHeight = 1;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("The maximum height this deposit can generate at")
   private int maxHeight = 75;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("The minimum amount of deposit blocks per clump")
   private int minSize = 0;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("The maximum amount of deposit blocks per clump")
   private int maxSize = 128;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(2048.0D)
   @Desc("The maximum amount of clumps per chunk")
   private int maxPerChunk = 3;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(2048.0D)
   @Desc("The minimum amount of clumps per chunk")
   private int minPerChunk = 0;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The change of the deposit spawning in a chunk")
   private double spawnChance = 1.0D;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The change of the a clump spawning in a chunk")
   private double perClumpSpawnChance = 1.0D;
   @Required
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The palette of blocks to be used in this deposit generator")
   private KList<IrisBlockData> palette = new KList();
   @MinNumber(1.0D)
   @MaxNumber(64.0D)
   @Desc("Ore varience is how many different objects clumps iris will create")
   private int varience = 3;
   @Desc("If set to true, this deposit will replace bedrock")
   private boolean replaceBedrock = false;

   public IrisObject getClump(Engine engine, RNG rng, IrisData rdata) {
      KList var4 = (KList)this.objects.aquire(() -> {
         RNG var3x = new RNG(var1.getSeedManager().getDeposit() + (long)this.hashCode());
         KList var4 = new KList();

         for(int var5 = 0; var5 < this.varience; ++var5) {
            var4.add((Object)this.generateClumpObject(var3x.nextParallelRNG(2349 * var5 + 3598), var3));
         }

         return var4;
      });
      return (IrisObject)var4.get(var2.i(0, var4.size()));
   }

   public int getMaxDimension() {
      return Math.min(11, (int)Math.ceil(Math.cbrt((double)this.maxSize)));
   }

   private IrisObject generateClumpObject(RNG rngv, IrisData rdata) {
      int var3 = var1.i(this.minSize, this.maxSize + 1);
      if (var3 == 1) {
         IrisObject var10 = new IrisObject(1, 1, 1);
         var10.getBlocks().put(var10.getCenter(), this.nextBlock(var1, var2));
         return var10;
      } else {
         int var4 = Math.min(11, (int)Math.ceil(Math.cbrt((double)var3)));
         IrisObject var5 = new IrisObject(var4, var4, var4);
         int var6 = var4 * var4 * var4;
         if (var3 >= var6) {
            int var11 = 0;
            int var12 = 0;
            int var9 = 0;

            while(var9 < var4) {
               var5.setUnsigned(var11++, var12, var9, this.nextBlock(var1, var2));
               if (var11 == var4) {
                  var11 = 0;
                  ++var12;
               }

               if (var12 == var4) {
                  var12 = 0;
                  ++var9;
               }
            }

            return var5;
         } else {
            KSet var7 = new KSet(new BlockPosition[0]);

            while(var3 > 0) {
               BlockPosition var8 = new BlockPosition(var1.i(0, var4), var1.i(0, var4), var1.i(0, var4));
               if (var7.add(var8)) {
                  --var3;
                  var5.setUnsigned(var8.getX(), var8.getY(), var8.getZ(), this.nextBlock(var1, var2));
               }
            }

            return var5;
         }
      }
   }

   private BlockData nextBlock(RNG rngv, IrisData rdata) {
      return (BlockData)this.getBlockData(var2).get(var1.i(0, this.getBlockData(var2).size()));
   }

   public KList<BlockData> getBlockData(IrisData rdata) {
      return (KList)this.blockData.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.palette.iterator();

         while(var3.hasNext()) {
            IrisBlockData var4 = (IrisBlockData)var3.next();
            BlockData var5 = var4.getBlockData(var1);
            if (var5 != null) {
               var2.add((Object)var5);
            }
         }

         return var2;
      });
   }

   @Generated
   public IrisDepositGenerator() {
   }

   @Generated
   public IrisDepositGenerator(final int minHeight, final int maxHeight, final int minSize, final int maxSize, final int maxPerChunk, final int minPerChunk, final double spawnChance, final double perClumpSpawnChance, final KList<IrisBlockData> palette, final int varience, final boolean replaceBedrock) {
      this.minHeight = var1;
      this.maxHeight = var2;
      this.minSize = var3;
      this.maxSize = var4;
      this.maxPerChunk = var5;
      this.minPerChunk = var6;
      this.spawnChance = var7;
      this.perClumpSpawnChance = var9;
      this.palette = var11;
      this.varience = var12;
      this.replaceBedrock = var13;
   }

   @Generated
   public AtomicCache<KList<IrisObject>> getObjects() {
      return this.objects;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getBlockData() {
      return this.blockData;
   }

   @Generated
   public int getMinHeight() {
      return this.minHeight;
   }

   @Generated
   public int getMaxHeight() {
      return this.maxHeight;
   }

   @Generated
   public int getMinSize() {
      return this.minSize;
   }

   @Generated
   public int getMaxSize() {
      return this.maxSize;
   }

   @Generated
   public int getMaxPerChunk() {
      return this.maxPerChunk;
   }

   @Generated
   public int getMinPerChunk() {
      return this.minPerChunk;
   }

   @Generated
   public double getSpawnChance() {
      return this.spawnChance;
   }

   @Generated
   public double getPerClumpSpawnChance() {
      return this.perClumpSpawnChance;
   }

   @Generated
   public KList<IrisBlockData> getPalette() {
      return this.palette;
   }

   @Generated
   public int getVarience() {
      return this.varience;
   }

   @Generated
   public boolean isReplaceBedrock() {
      return this.replaceBedrock;
   }

   @Generated
   public IrisDepositGenerator setMinHeight(final int minHeight) {
      this.minHeight = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setMaxHeight(final int maxHeight) {
      this.maxHeight = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setMinSize(final int minSize) {
      this.minSize = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setMaxSize(final int maxSize) {
      this.maxSize = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setMaxPerChunk(final int maxPerChunk) {
      this.maxPerChunk = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setMinPerChunk(final int minPerChunk) {
      this.minPerChunk = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setSpawnChance(final double spawnChance) {
      this.spawnChance = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setPerClumpSpawnChance(final double perClumpSpawnChance) {
      this.perClumpSpawnChance = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setPalette(final KList<IrisBlockData> palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setVarience(final int varience) {
      this.varience = var1;
      return this;
   }

   @Generated
   public IrisDepositGenerator setReplaceBedrock(final boolean replaceBedrock) {
      this.replaceBedrock = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDepositGenerator)) {
         return false;
      } else {
         IrisDepositGenerator var2 = (IrisDepositGenerator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinHeight() != var2.getMinHeight()) {
            return false;
         } else if (this.getMaxHeight() != var2.getMaxHeight()) {
            return false;
         } else if (this.getMinSize() != var2.getMinSize()) {
            return false;
         } else if (this.getMaxSize() != var2.getMaxSize()) {
            return false;
         } else if (this.getMaxPerChunk() != var2.getMaxPerChunk()) {
            return false;
         } else if (this.getMinPerChunk() != var2.getMinPerChunk()) {
            return false;
         } else if (Double.compare(this.getSpawnChance(), var2.getSpawnChance()) != 0) {
            return false;
         } else if (Double.compare(this.getPerClumpSpawnChance(), var2.getPerClumpSpawnChance()) != 0) {
            return false;
         } else if (this.getVarience() != var2.getVarience()) {
            return false;
         } else if (this.isReplaceBedrock() != var2.isReplaceBedrock()) {
            return false;
         } else {
            KList var3 = this.getPalette();
            KList var4 = var2.getPalette();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisDepositGenerator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.getMinHeight();
      var8 = var8 * 59 + this.getMaxHeight();
      var8 = var8 * 59 + this.getMinSize();
      var8 = var8 * 59 + this.getMaxSize();
      var8 = var8 * 59 + this.getMaxPerChunk();
      var8 = var8 * 59 + this.getMinPerChunk();
      long var3 = Double.doubleToLongBits(this.getSpawnChance());
      var8 = var8 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getPerClumpSpawnChance());
      var8 = var8 * 59 + (int)(var5 >>> 32 ^ var5);
      var8 = var8 * 59 + this.getVarience();
      var8 = var8 * 59 + (this.isReplaceBedrock() ? 79 : 97);
      KList var7 = this.getPalette();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getObjects());
      return "IrisDepositGenerator(objects=" + var10000 + ", blockData=" + String.valueOf(this.getBlockData()) + ", minHeight=" + this.getMinHeight() + ", maxHeight=" + this.getMaxHeight() + ", minSize=" + this.getMinSize() + ", maxSize=" + this.getMaxSize() + ", maxPerChunk=" + this.getMaxPerChunk() + ", minPerChunk=" + this.getMinPerChunk() + ", spawnChance=" + this.getSpawnChance() + ", perClumpSpawnChance=" + this.getPerClumpSpawnChance() + ", palette=" + String.valueOf(this.getPalette()) + ", varience=" + this.getVarience() + ", replaceBedrock=" + this.isReplaceBedrock() + ")";
   }
}
