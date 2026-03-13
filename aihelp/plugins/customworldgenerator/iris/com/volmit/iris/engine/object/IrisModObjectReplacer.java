package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import lombok.Generated;

@Snippet("object-replacer")
@Desc("A biome replacer")
public class IrisModObjectReplacer {
   @Required
   @Desc("A list of objects to find")
   @RegistryListResource(IrisObject.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> find = new KList();
   @Required
   @Desc("An object to replace it with")
   @RegistryListResource(IrisBiome.class)
   private String replace = "";

   @Generated
   public IrisModObjectReplacer() {
   }

   @Generated
   public IrisModObjectReplacer(final KList<String> find, final String replace) {
      this.find = var1;
      this.replace = var2;
   }

   @Generated
   public KList<String> getFind() {
      return this.find;
   }

   @Generated
   public String getReplace() {
      return this.replace;
   }

   @Generated
   public IrisModObjectReplacer setFind(final KList<String> find) {
      this.find = var1;
      return this;
   }

   @Generated
   public IrisModObjectReplacer setReplace(final String replace) {
      this.replace = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisModObjectReplacer)) {
         return false;
      } else {
         IrisModObjectReplacer var2 = (IrisModObjectReplacer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getFind();
            KList var4 = var2.getFind();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getReplace();
            String var6 = var2.getReplace();
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
      return var1 instanceof IrisModObjectReplacer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getFind();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getReplace();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFind());
      return "IrisModObjectReplacer(find=" + var10000 + ", replace=" + this.getReplace() + ")";
   }
}
