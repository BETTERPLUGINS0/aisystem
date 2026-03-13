package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("object-block-replacer")
@Desc("Find and replace object materials")
public class IrisObjectReplace {
   private final transient AtomicCache<CNG> replaceGen = new AtomicCache();
   private final transient AtomicCache<KList<BlockData>> findData = new AtomicCache();
   private final transient AtomicCache<KList<BlockData>> replaceData = new AtomicCache();
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Required
   @Desc("Find this block")
   private KList<IrisBlockData> find = new KList();
   @Required
   @Desc("Replace it with this block palette")
   private IrisMaterialPalette replace = new IrisMaterialPalette();
   @Desc("Exactly match the block data or not")
   private boolean exact = false;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("Modifies the chance the block is replaced")
   private float chance = 1.0F;

   public KList<BlockData> getFind(IrisData rdata) {
      return (KList)this.findData.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.find.iterator();

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

   public BlockData getReplace(RNG seed, double x, double y, double z, IrisData rdata) {
      return this.getReplace().get(var1, var2, var4, var6, var8);
   }

   @Generated
   public IrisObjectReplace() {
   }

   @Generated
   public IrisObjectReplace(final KList<IrisBlockData> find, final IrisMaterialPalette replace, final boolean exact, final float chance) {
      this.find = var1;
      this.replace = var2;
      this.exact = var3;
      this.chance = var4;
   }

   @Generated
   public AtomicCache<CNG> getReplaceGen() {
      return this.replaceGen;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getFindData() {
      return this.findData;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getReplaceData() {
      return this.replaceData;
   }

   @Generated
   public KList<IrisBlockData> getFind() {
      return this.find;
   }

   @Generated
   public IrisMaterialPalette getReplace() {
      return this.replace;
   }

   @Generated
   public boolean isExact() {
      return this.exact;
   }

   @Generated
   public float getChance() {
      return this.chance;
   }

   @Generated
   public IrisObjectReplace setFind(final KList<IrisBlockData> find) {
      this.find = var1;
      return this;
   }

   @Generated
   public IrisObjectReplace setReplace(final IrisMaterialPalette replace) {
      this.replace = var1;
      return this;
   }

   @Generated
   public IrisObjectReplace setExact(final boolean exact) {
      this.exact = var1;
      return this;
   }

   @Generated
   public IrisObjectReplace setChance(final float chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectReplace)) {
         return false;
      } else {
         IrisObjectReplace var2 = (IrisObjectReplace)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isExact() != var2.isExact()) {
            return false;
         } else if (Float.compare(this.getChance(), var2.getChance()) != 0) {
            return false;
         } else {
            label40: {
               KList var3 = this.getFind();
               KList var4 = var2.getFind();
               if (var3 == null) {
                  if (var4 == null) {
                     break label40;
                  }
               } else if (var3.equals(var4)) {
                  break label40;
               }

               return false;
            }

            IrisMaterialPalette var5 = this.getReplace();
            IrisMaterialPalette var6 = var2.getReplace();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectReplace;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isExact() ? 79 : 97);
      var5 = var5 * 59 + Float.floatToIntBits(this.getChance());
      KList var3 = this.getFind();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisMaterialPalette var4 = this.getReplace();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getReplaceGen());
      return "IrisObjectReplace(replaceGen=" + var10000 + ", findData=" + String.valueOf(this.getFindData()) + ", replaceData=" + String.valueOf(this.getReplaceData()) + ", find=" + String.valueOf(this.getFind()) + ", replace=" + String.valueOf(this.getReplace()) + ", exact=" + this.isExact() + ", chance=" + this.getChance() + ")";
   }
}
