package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;

@Desc("Represents a structure piece pool")
public class IrisJigsawPool extends IrisRegistrant {
   @RegistryListResource(IrisJigsawPiece.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("A list of structure piece pools")
   private KList<String> pieces = new KList();

   public String getFolderName() {
      return "jigsaw-pools";
   }

   public String getTypeName() {
      return "Jigsaw Pool";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisJigsawPool() {
   }

   @Generated
   public IrisJigsawPool(final KList<String> pieces) {
      this.pieces = var1;
   }

   @Generated
   public KList<String> getPieces() {
      return this.pieces;
   }

   @Generated
   public IrisJigsawPool setPieces(final KList<String> pieces) {
      this.pieces = var1;
      return this;
   }

   @Generated
   public String toString() {
      return "IrisJigsawPool(pieces=" + String.valueOf(this.getPieces()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawPool)) {
         return false;
      } else {
         IrisJigsawPool var2 = (IrisJigsawPool)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getPieces();
            KList var4 = var2.getPieces();
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
      return var1 instanceof IrisJigsawPool;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getPieces();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
