package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListFunction;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.functions.ResourceLoadersFunction;
import com.volmit.iris.util.collection.KList;
import lombok.Generated;

@Desc("Represents global preprocessors")
public class IrisPreProcessors {
   @Required
   @Desc("The preprocessor type")
   @RegistryListFunction(ResourceLoadersFunction.class)
   private String type = "dimension";
   @Required
   @Desc("The preprocessor scripts\nFile extension: .proc.kts")
   @RegistryListResource(IrisScript.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> scripts = new KList();

   @Generated
   public String getType() {
      return this.type;
   }

   @Generated
   public KList<String> getScripts() {
      return this.scripts;
   }

   @Generated
   public void setType(final String type) {
      this.type = var1;
   }

   @Generated
   public void setScripts(final KList<String> scripts) {
      this.scripts = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPreProcessors)) {
         return false;
      } else {
         IrisPreProcessors var2 = (IrisPreProcessors)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            String var3 = this.getType();
            String var4 = var2.getType();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getScripts();
            KList var6 = var2.getScripts();
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
      return var1 instanceof IrisPreProcessors;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getType();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getScripts();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = this.getType();
      return "IrisPreProcessors(type=" + var10000 + ", scripts=" + String.valueOf(this.getScripts()) + ")";
   }

   @Generated
   public IrisPreProcessors(final String type, final KList<String> scripts) {
      this.type = var1;
      this.scripts = var2;
   }

   @Generated
   public IrisPreProcessors() {
   }
}
