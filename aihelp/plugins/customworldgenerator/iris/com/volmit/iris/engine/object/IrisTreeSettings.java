package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("tree-settings")
@Desc("Tree growth override settings")
public class IrisTreeSettings {
   @Required
   @Desc("Turn replacing on and off")
   boolean enabled = false;
   @Desc("Object picking modes")
   IrisTreeModes mode;

   @Generated
   public IrisTreeSettings(final boolean enabled, final IrisTreeModes mode) {
      this.mode = IrisTreeModes.FIRST;
      this.enabled = var1;
      this.mode = var2;
   }

   @Generated
   public IrisTreeSettings() {
      this.mode = IrisTreeModes.FIRST;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public IrisTreeModes getMode() {
      return this.mode;
   }

   @Generated
   public IrisTreeSettings setEnabled(final boolean enabled) {
      this.enabled = var1;
      return this;
   }

   @Generated
   public IrisTreeSettings setMode(final IrisTreeModes mode) {
      this.mode = var1;
      return this;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isEnabled();
      return "IrisTreeSettings(enabled=" + var10000 + ", mode=" + String.valueOf(this.getMode()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisTreeSettings)) {
         return false;
      } else {
         IrisTreeSettings var2 = (IrisTreeSettings)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isEnabled() != var2.isEnabled()) {
            return false;
         } else {
            IrisTreeModes var3 = this.getMode();
            IrisTreeModes var4 = var2.getMode();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisTreeSettings;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + (this.isEnabled() ? 79 : 97);
      IrisTreeModes var3 = this.getMode();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
