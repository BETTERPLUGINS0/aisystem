package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.data.B;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Desc("Find and replace object materials for compatability")
public class IrisCompatabilityBlockFilter {
   private final transient AtomicCache<BlockData> findData;
   private final transient AtomicCache<BlockData> replaceData;
   @Required
   @Desc("When iris sees this block, and it's not reconized")
   private String when;
   @Required
   @Desc("Replace it with this block. Dont worry if this block is also not reconized, iris repeat this compat check.")
   private String supplement;
   @Desc("If exact is true, it compares block data for example minecraft:some_log[axis=x]")
   private boolean exact;

   public IrisCompatabilityBlockFilter(String when, String supplement) {
      this(var1, var2, false);
   }

   public BlockData getFind() {
      return (BlockData)this.findData.aquire(() -> {
         return B.get(this.when);
      });
   }

   public BlockData getReplace() {
      return (BlockData)this.replaceData.aquire(() -> {
         BlockData var1 = B.getOrNull(this.supplement, false);
         if (var1 == null) {
            return null;
         } else {
            Iris.warn("Compat: Using '%s' in place of '%s' since this server doesnt support '%s'", this.supplement, this.when, this.when);
            return var1;
         }
      });
   }

   @Generated
   public IrisCompatabilityBlockFilter() {
      this.findData = new AtomicCache(true);
      this.replaceData = new AtomicCache(true);
      this.when = "";
      this.supplement = "";
      this.exact = false;
   }

   @Generated
   public IrisCompatabilityBlockFilter(final String when, final String supplement, final boolean exact) {
      this.findData = new AtomicCache(true);
      this.replaceData = new AtomicCache(true);
      this.when = "";
      this.supplement = "";
      this.exact = false;
      this.when = var1;
      this.supplement = var2;
      this.exact = var3;
   }

   @Generated
   public AtomicCache<BlockData> getFindData() {
      return this.findData;
   }

   @Generated
   public AtomicCache<BlockData> getReplaceData() {
      return this.replaceData;
   }

   @Generated
   public String getWhen() {
      return this.when;
   }

   @Generated
   public String getSupplement() {
      return this.supplement;
   }

   @Generated
   public boolean isExact() {
      return this.exact;
   }

   @Generated
   public IrisCompatabilityBlockFilter setWhen(final String when) {
      this.when = var1;
      return this;
   }

   @Generated
   public IrisCompatabilityBlockFilter setSupplement(final String supplement) {
      this.supplement = var1;
      return this;
   }

   @Generated
   public IrisCompatabilityBlockFilter setExact(final boolean exact) {
      this.exact = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCompatabilityBlockFilter)) {
         return false;
      } else {
         IrisCompatabilityBlockFilter var2 = (IrisCompatabilityBlockFilter)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isExact() != var2.isExact()) {
            return false;
         } else {
            String var3 = this.getWhen();
            String var4 = var2.getWhen();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getSupplement();
            String var6 = var2.getSupplement();
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
      return var1 instanceof IrisCompatabilityBlockFilter;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isExact() ? 79 : 97);
      String var3 = this.getWhen();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getSupplement();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFindData());
      return "IrisCompatabilityBlockFilter(findData=" + var10000 + ", replaceData=" + String.valueOf(this.getReplaceData()) + ", when=" + this.getWhen() + ", supplement=" + this.getSupplement() + ", exact=" + this.isExact() + ")";
   }
}
