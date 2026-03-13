package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListFunction;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.engine.object.annotations.functions.LootTableKeyFunction;
import com.volmit.iris.util.collection.KList;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("object-vanilla-loot")
@Desc("Represents vanilla loot within this object or jigsaw piece")
public class IrisObjectVanillaLoot implements IObjectLoot {
   private final transient AtomicCache<KList<BlockData>> filterCache = new AtomicCache();
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The list of blocks this loot table should apply to")
   private KList<IrisBlockData> filter = new KList();
   @Desc("Exactly match the block data or not")
   private boolean exact = false;
   @Desc("The vanilla loot table key")
   @Required
   @RegistryListFunction(LootTableKeyFunction.class)
   private String name;
   @Desc("The weight of this loot table being chosen")
   private int weight = 1;

   public KList<BlockData> getFilter(IrisData rdata) {
      return (KList)this.filterCache.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.filter.iterator();

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
   public IrisObjectVanillaLoot() {
   }

   @Generated
   public IrisObjectVanillaLoot(final KList<IrisBlockData> filter, final boolean exact, final String name, final int weight) {
      this.filter = var1;
      this.exact = var2;
      this.name = var3;
      this.weight = var4;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getFilterCache() {
      return this.filterCache;
   }

   @Generated
   public KList<IrisBlockData> getFilter() {
      return this.filter;
   }

   @Generated
   public boolean isExact() {
      return this.exact;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public int getWeight() {
      return this.weight;
   }

   @Generated
   public IrisObjectVanillaLoot setFilter(final KList<IrisBlockData> filter) {
      this.filter = var1;
      return this;
   }

   @Generated
   public IrisObjectVanillaLoot setExact(final boolean exact) {
      this.exact = var1;
      return this;
   }

   @Generated
   public IrisObjectVanillaLoot setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisObjectVanillaLoot setWeight(final int weight) {
      this.weight = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectVanillaLoot)) {
         return false;
      } else {
         IrisObjectVanillaLoot var2 = (IrisObjectVanillaLoot)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isExact() != var2.isExact()) {
            return false;
         } else if (this.getWeight() != var2.getWeight()) {
            return false;
         } else {
            label40: {
               KList var3 = this.getFilter();
               KList var4 = var2.getFilter();
               if (var3 == null) {
                  if (var4 == null) {
                     break label40;
                  }
               } else if (var3.equals(var4)) {
                  break label40;
               }

               return false;
            }

            String var5 = this.getName();
            String var6 = var2.getName();
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
      return var1 instanceof IrisObjectVanillaLoot;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isExact() ? 79 : 97);
      var5 = var5 * 59 + this.getWeight();
      KList var3 = this.getFilter();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getName();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFilterCache());
      return "IrisObjectVanillaLoot(filterCache=" + var10000 + ", filter=" + String.valueOf(this.getFilter()) + ", exact=" + this.isExact() + ", name=" + this.getName() + ", weight=" + this.getWeight() + ")";
   }
}
