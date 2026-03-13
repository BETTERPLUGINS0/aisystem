package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.data.B;
import lombok.Generated;
import org.bukkit.Material;

@Desc("Find and replace object items for compatability")
public class IrisCompatabilityItemFilter {
   private final transient AtomicCache<Material> findData = new AtomicCache(true);
   private final transient AtomicCache<Material> replaceData = new AtomicCache(true);
   @Required
   @Desc("When iris sees this block, and it's not reconized")
   private String when = "";
   @Required
   @Desc("Replace it with this block. Dont worry if this block is also not reconized, iris repeat this compat check.")
   private String supplement = "";

   public IrisCompatabilityItemFilter(String when, String supplement) {
      this.when = var1;
      this.supplement = var2;
   }

   public Material getFind() {
      return (Material)this.findData.aquire(() -> {
         return B.getMaterial(this.when);
      });
   }

   public Material getReplace() {
      return (Material)this.replaceData.aquire(() -> {
         Material var1 = B.getMaterialOrNull(this.supplement);
         if (var1 == null) {
            return null;
         } else {
            Iris.verbose("Compat: Using " + this.supplement + " in place of " + this.when + " since this server doesnt support '" + this.supplement + "'");
            return var1;
         }
      });
   }

   @Generated
   public IrisCompatabilityItemFilter() {
   }

   @Generated
   public AtomicCache<Material> getFindData() {
      return this.findData;
   }

   @Generated
   public AtomicCache<Material> getReplaceData() {
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
   public IrisCompatabilityItemFilter setWhen(final String when) {
      this.when = var1;
      return this;
   }

   @Generated
   public IrisCompatabilityItemFilter setSupplement(final String supplement) {
      this.supplement = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCompatabilityItemFilter)) {
         return false;
      } else {
         IrisCompatabilityItemFilter var2 = (IrisCompatabilityItemFilter)var1;
         if (!var2.canEqual(this)) {
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
      return var1 instanceof IrisCompatabilityItemFilter;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getWhen();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getSupplement();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFindData());
      return "IrisCompatabilityItemFilter(findData=" + var10000 + ", replaceData=" + String.valueOf(this.getReplaceData()) + ", when=" + this.getWhen() + ", supplement=" + this.getSupplement() + ")";
   }
}
