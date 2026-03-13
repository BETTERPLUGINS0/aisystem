package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("rare-object")
@Desc("Represents a structure tile")
public class IrisRareObject {
   @Required
   @MinNumber(1.0D)
   @Desc("The rarity is 1 in X")
   private int rarity = 1;
   @RegistryListResource(IrisObject.class)
   @Required
   @Desc("The object to place if rarity check passed")
   private String object = "";

   @Generated
   public IrisRareObject() {
   }

   @Generated
   public IrisRareObject(final int rarity, final String object) {
      this.rarity = var1;
      this.object = var2;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public String getObject() {
      return this.object;
   }

   @Generated
   public IrisRareObject setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisRareObject setObject(final String object) {
      this.object = var1;
      return this;
   }

   @Generated
   public String toString() {
      int var10000 = this.getRarity();
      return "IrisRareObject(rarity=" + var10000 + ", object=" + this.getObject() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRareObject)) {
         return false;
      } else {
         IrisRareObject var2 = (IrisRareObject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            String var3 = this.getObject();
            String var4 = var2.getObject();
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
      return var1 instanceof IrisRareObject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getRarity();
      String var3 = this.getObject();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
