package com.volmit.iris.engine.object.matter;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.matter.IrisMatter;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import lombok.Generated;

public class IrisMatterObject extends IrisRegistrant {
   private final Matter matter;

   public IrisMatterObject() {
      this(1, 1, 1);
   }

   public IrisMatterObject(int w, int h, int d) {
      this(new IrisMatter(var1, var2, var3));
   }

   public IrisMatterObject(Matter matter) {
      this.matter = var1;
   }

   public static IrisMatterObject from(IrisObject object) {
      return new IrisMatterObject(Matter.from(var0));
   }

   public static IrisMatterObject from(File j) {
      return new IrisMatterObject(Matter.read(var0));
   }

   public String getFolderName() {
      return "matter";
   }

   public String getTypeName() {
      return "Matter";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public Matter getMatter() {
      return this.matter;
   }

   @Generated
   public String toString() {
      return "IrisMatterObject(matter=" + String.valueOf(this.getMatter()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMatterObject)) {
         return false;
      } else {
         IrisMatterObject var2 = (IrisMatterObject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Matter var3 = this.getMatter();
            Matter var4 = var2.getMatter();
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
      return var1 instanceof IrisMatterObject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Matter var3 = this.getMatter();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
