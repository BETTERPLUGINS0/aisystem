package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineMode;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("dimension-mode")
@Desc("Represents a dimensional mode")
public class IrisDimensionMode {
   @Desc("The dimension type")
   private IrisDimensionModeType type;
   @RegistryListResource(IrisScript.class)
   @Desc("The script to create the dimension mode instead of using provided types\nFile extension: .engine.kts")
   private String script;

   public EngineMode create(Engine engine) {
      if (this.script == null) {
         return this.type.create(var1);
      } else {
         Object var2 = var1.getExecution().evaluate(this.script);
         if (var2 instanceof EngineMode) {
            return (EngineMode)var2;
         } else {
            throw new IllegalStateException("The script '" + this.script + "' did not return an engine mode!");
         }
      }
   }

   @Generated
   public IrisDimensionMode() {
      this.type = IrisDimensionModeType.OVERWORLD;
   }

   @Generated
   public IrisDimensionMode(final IrisDimensionModeType type, final String script) {
      this.type = IrisDimensionModeType.OVERWORLD;
      this.type = var1;
      this.script = var2;
   }

   @Generated
   public IrisDimensionModeType getType() {
      return this.type;
   }

   @Generated
   public String getScript() {
      return this.script;
   }

   @Generated
   public IrisDimensionMode setType(final IrisDimensionModeType type) {
      this.type = var1;
      return this;
   }

   @Generated
   public IrisDimensionMode setScript(final String script) {
      this.script = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDimensionMode)) {
         return false;
      } else {
         IrisDimensionMode var2 = (IrisDimensionMode)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            IrisDimensionModeType var3 = this.getType();
            IrisDimensionModeType var4 = var2.getType();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getScript();
            String var6 = var2.getScript();
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
      return var1 instanceof IrisDimensionMode;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      IrisDimensionModeType var3 = this.getType();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getScript();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "IrisDimensionMode(type=" + var10000 + ", script=" + this.getScript() + ")";
   }
}
