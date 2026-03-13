package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import lombok.Generated;

@Snippet("biome-injector")
@Desc("A biome injector")
public class IrisModBiomeInjector {
   @Required
   @Desc("The region to find")
   @RegistryListResource(IrisRegion.class)
   private String region = "";
   @Required
   @Desc("A biome to inject into the region")
   @RegistryListResource(IrisBiome.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> inject = new KList();

   @Generated
   public IrisModBiomeInjector() {
   }

   @Generated
   public IrisModBiomeInjector(final String region, final KList<String> inject) {
      this.region = var1;
      this.inject = var2;
   }

   @Generated
   public String getRegion() {
      return this.region;
   }

   @Generated
   public KList<String> getInject() {
      return this.inject;
   }

   @Generated
   public IrisModBiomeInjector setRegion(final String region) {
      this.region = var1;
      return this;
   }

   @Generated
   public IrisModBiomeInjector setInject(final KList<String> inject) {
      this.inject = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisModBiomeInjector)) {
         return false;
      } else {
         IrisModBiomeInjector var2 = (IrisModBiomeInjector)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            String var3 = this.getRegion();
            String var4 = var2.getRegion();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getInject();
            KList var6 = var2.getInject();
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
      return var1 instanceof IrisModBiomeInjector;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getRegion();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getInject();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = this.getRegion();
      return "IrisModBiomeInjector(region=" + var10000 + ", inject=" + String.valueOf(this.getInject()) + ")";
   }
}
