package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.entity.EntityType;

@Snippet("custom-biome-spawn")
@Desc("A custom biome spawn")
public class IrisBiomeCustomSpawn {
   @Required
   @Desc("The biome's entity type")
   private EntityType type;
   @MinNumber(1.0D)
   @Desc("The min to spawn")
   private int minCount;
   @MinNumber(1.0D)
   @Desc("The max to spawn")
   private int maxCount;
   @MinNumber(1.0D)
   @MaxNumber(1000.0D)
   @Desc("The weight in this group. Higher weight, the more common this type is spawned")
   private int weight;
   @Desc("The rarity")
   private IrisBiomeCustomSpawnType group;

   @Generated
   public IrisBiomeCustomSpawn() {
      this.type = EntityType.COW;
      this.minCount = 2;
      this.maxCount = 5;
      this.weight = 1;
      this.group = IrisBiomeCustomSpawnType.MISC;
   }

   @Generated
   public IrisBiomeCustomSpawn(final EntityType type, final int minCount, final int maxCount, final int weight, final IrisBiomeCustomSpawnType group) {
      this.type = EntityType.COW;
      this.minCount = 2;
      this.maxCount = 5;
      this.weight = 1;
      this.group = IrisBiomeCustomSpawnType.MISC;
      this.type = var1;
      this.minCount = var2;
      this.maxCount = var3;
      this.weight = var4;
      this.group = var5;
   }

   @Generated
   public EntityType getType() {
      return this.type;
   }

   @Generated
   public int getMinCount() {
      return this.minCount;
   }

   @Generated
   public int getMaxCount() {
      return this.maxCount;
   }

   @Generated
   public int getWeight() {
      return this.weight;
   }

   @Generated
   public IrisBiomeCustomSpawnType getGroup() {
      return this.group;
   }

   @Generated
   public IrisBiomeCustomSpawn setType(final EntityType type) {
      this.type = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustomSpawn setMinCount(final int minCount) {
      this.minCount = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustomSpawn setMaxCount(final int maxCount) {
      this.maxCount = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustomSpawn setWeight(final int weight) {
      this.weight = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustomSpawn setGroup(final IrisBiomeCustomSpawnType group) {
      this.group = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiomeCustomSpawn)) {
         return false;
      } else {
         IrisBiomeCustomSpawn var2 = (IrisBiomeCustomSpawn)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinCount() != var2.getMinCount()) {
            return false;
         } else if (this.getMaxCount() != var2.getMaxCount()) {
            return false;
         } else if (this.getWeight() != var2.getWeight()) {
            return false;
         } else {
            EntityType var3 = this.getType();
            EntityType var4 = var2.getType();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisBiomeCustomSpawnType var5 = this.getGroup();
            IrisBiomeCustomSpawnType var6 = var2.getGroup();
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
      return var1 instanceof IrisBiomeCustomSpawn;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getMinCount();
      var5 = var5 * 59 + this.getMaxCount();
      var5 = var5 * 59 + this.getWeight();
      EntityType var3 = this.getType();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisBiomeCustomSpawnType var4 = this.getGroup();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "IrisBiomeCustomSpawn(type=" + var10000 + ", minCount=" + this.getMinCount() + ", maxCount=" + this.getMaxCount() + ", weight=" + this.getWeight() + ", group=" + String.valueOf(this.getGroup()) + ")";
   }
}
