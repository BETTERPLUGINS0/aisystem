package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;

@Desc("Represents a marker")
public class IrisMarker extends IrisRegistrant {
   @Desc("A list of spawners to add to anywhere this marker is.")
   @RegistryListResource(IrisSpawner.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> spawners = new KList();
   @Desc("Remove this marker when the block it's assigned to is changed.")
   private boolean removeOnChange = true;
   @Desc("If true, markers will only be placed here if there is 2 air blocks above it.")
   private boolean emptyAbove = true;
   @Desc("If this marker is used, what is the chance it removes itself. For example 25% (0.25) would mean that on average 4 uses will remove a specific marker. Set this below 0 (-1) to never exhaust & set this to 1 or higher to always exhaust on first use.")
   private double exhaustionChance = 0.0D;

   public boolean shouldExhaust() {
      return this.exhaustionChance > RNG.r.nextDouble();
   }

   public String getFolderName() {
      return "markers";
   }

   public String getTypeName() {
      return "Marker";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisMarker(final KList<String> spawners, final boolean removeOnChange, final boolean emptyAbove, final double exhaustionChance) {
      this.spawners = var1;
      this.removeOnChange = var2;
      this.emptyAbove = var3;
      this.exhaustionChance = var4;
   }

   @Generated
   public IrisMarker() {
   }

   @Generated
   public KList<String> getSpawners() {
      return this.spawners;
   }

   @Generated
   public boolean isRemoveOnChange() {
      return this.removeOnChange;
   }

   @Generated
   public boolean isEmptyAbove() {
      return this.emptyAbove;
   }

   @Generated
   public double getExhaustionChance() {
      return this.exhaustionChance;
   }

   @Generated
   public IrisMarker setSpawners(final KList<String> spawners) {
      this.spawners = var1;
      return this;
   }

   @Generated
   public IrisMarker setRemoveOnChange(final boolean removeOnChange) {
      this.removeOnChange = var1;
      return this;
   }

   @Generated
   public IrisMarker setEmptyAbove(final boolean emptyAbove) {
      this.emptyAbove = var1;
      return this;
   }

   @Generated
   public IrisMarker setExhaustionChance(final double exhaustionChance) {
      this.exhaustionChance = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getSpawners());
      return "IrisMarker(spawners=" + var10000 + ", removeOnChange=" + this.isRemoveOnChange() + ", emptyAbove=" + this.isEmptyAbove() + ", exhaustionChance=" + this.getExhaustionChance() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMarker)) {
         return false;
      } else {
         IrisMarker var2 = (IrisMarker)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isRemoveOnChange() != var2.isRemoveOnChange()) {
            return false;
         } else if (this.isEmptyAbove() != var2.isEmptyAbove()) {
            return false;
         } else if (Double.compare(this.getExhaustionChance(), var2.getExhaustionChance()) != 0) {
            return false;
         } else {
            KList var3 = this.getSpawners();
            KList var4 = var2.getSpawners();
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
      return var1 instanceof IrisMarker;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + (this.isRemoveOnChange() ? 79 : 97);
      var6 = var6 * 59 + (this.isEmptyAbove() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getExhaustionChance());
      var6 = var6 * 59 + (int)(var3 >>> 32 ^ var3);
      KList var5 = this.getSpawners();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
