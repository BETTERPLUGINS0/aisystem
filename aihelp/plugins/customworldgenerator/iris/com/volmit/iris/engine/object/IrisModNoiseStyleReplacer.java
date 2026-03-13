package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("noise-style-replacer")
@Desc("A noise style replacer")
public class IrisModNoiseStyleReplacer {
   @Required
   @Desc("A noise style to find")
   @ArrayType(
      type = String.class,
      min = 1
   )
   private NoiseStyle find;
   @Required
   @Desc("If replaceTypeOnly is set to true, Iris will keep the existing generator style and only replace the type itself. Otherwise it will use the replace tag for every style using the find type.")
   private boolean replaceTypeOnly;
   @Required
   @Desc("A noise style to replace it with")
   @RegistryListResource(IrisBiome.class)
   private IrisGeneratorStyle replace;

   @Generated
   public IrisModNoiseStyleReplacer() {
      this.find = NoiseStyle.IRIS;
      this.replaceTypeOnly = false;
      this.replace = new IrisGeneratorStyle();
   }

   @Generated
   public IrisModNoiseStyleReplacer(final NoiseStyle find, final boolean replaceTypeOnly, final IrisGeneratorStyle replace) {
      this.find = NoiseStyle.IRIS;
      this.replaceTypeOnly = false;
      this.replace = new IrisGeneratorStyle();
      this.find = var1;
      this.replaceTypeOnly = var2;
      this.replace = var3;
   }

   @Generated
   public NoiseStyle getFind() {
      return this.find;
   }

   @Generated
   public boolean isReplaceTypeOnly() {
      return this.replaceTypeOnly;
   }

   @Generated
   public IrisGeneratorStyle getReplace() {
      return this.replace;
   }

   @Generated
   public IrisModNoiseStyleReplacer setFind(final NoiseStyle find) {
      this.find = var1;
      return this;
   }

   @Generated
   public IrisModNoiseStyleReplacer setReplaceTypeOnly(final boolean replaceTypeOnly) {
      this.replaceTypeOnly = var1;
      return this;
   }

   @Generated
   public IrisModNoiseStyleReplacer setReplace(final IrisGeneratorStyle replace) {
      this.replace = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisModNoiseStyleReplacer)) {
         return false;
      } else {
         IrisModNoiseStyleReplacer var2 = (IrisModNoiseStyleReplacer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isReplaceTypeOnly() != var2.isReplaceTypeOnly()) {
            return false;
         } else {
            NoiseStyle var3 = this.getFind();
            NoiseStyle var4 = var2.getFind();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisGeneratorStyle var5 = this.getReplace();
            IrisGeneratorStyle var6 = var2.getReplace();
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
      return var1 instanceof IrisModNoiseStyleReplacer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isReplaceTypeOnly() ? 79 : 97);
      NoiseStyle var3 = this.getFind();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisGeneratorStyle var4 = this.getReplace();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFind());
      return "IrisModNoiseStyleReplacer(find=" + var10000 + ", replaceTypeOnly=" + this.isReplaceTypeOnly() + ", replace=" + String.valueOf(this.getReplace()) + ")";
   }
}
