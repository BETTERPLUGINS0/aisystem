package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;

public class IrisScript extends IrisRegistrant {
   private final String source;

   public IrisScript() {
      this("");
   }

   public IrisScript(String source) {
      this.source = var1;
   }

   public String getFolderName() {
      return "scripts";
   }

   public String getTypeName() {
      return "Script";
   }

   public String toString() {
      return this.source;
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisScript)) {
         return false;
      } else {
         IrisScript var2 = (IrisScript)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            String var3 = this.getSource();
            String var4 = var2.getSource();
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
      return var1 instanceof IrisScript;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      String var3 = this.getSource();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var2;
   }

   @Generated
   public String getSource() {
      return this.source;
   }
}
