package com.bergerkiller.bukkit.tc.properties.standard.type;

import com.bergerkiller.bukkit.tc.CollisionMode;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class CollisionOptions {
   private static final EnumMap<CollisionMobCategory, CollisionMode> NO_MOB_MODES = new EnumMap(CollisionMobCategory.class);
   public static final CollisionOptions DEFAULT;
   public static final CollisionOptions CANCEL;
   private final EnumMap<CollisionMobCategory, CollisionMode> mobModes;
   private final CollisionMode playerMode;
   private final CollisionMode miscMode;
   private final CollisionMode trainMode;
   private final CollisionMode blockMode;

   private CollisionOptions(EnumMap<CollisionMobCategory, CollisionMode> mobModes, CollisionMode playerMode, CollisionMode miscMode, CollisionMode trainMode, CollisionMode blockMode) {
      this.mobModes = mobModes;
      this.playerMode = playerMode;
      this.miscMode = miscMode;
      this.trainMode = trainMode;
      this.blockMode = blockMode;
   }

   public Map<CollisionMobCategory, CollisionMode> mobModes() {
      return Collections.unmodifiableMap(this.mobModes);
   }

   public CollisionMode mobMode(CollisionMobCategory category) {
      return (CollisionMode)this.mobModes.get(category);
   }

   public CollisionMode forEntity(Entity entity) {
      if (entity instanceof Player) {
         return this.playerMode;
      } else {
         CollisionMobCategory[] var2 = CollisionMobCategory.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CollisionMobCategory collisionConfigObject = var2[var4];
            CollisionMode collisionMode = this.mobMode(collisionConfigObject);
            if (collisionMode != null && collisionConfigObject.isMobType(entity)) {
               return collisionMode;
            }
         }

         return this.miscMode;
      }
   }

   public CollisionMode playerMode() {
      return this.playerMode;
   }

   public CollisionMode miscMode() {
      return this.miscMode;
   }

   public CollisionMode trainMode() {
      return this.trainMode;
   }

   public CollisionMode blockMode() {
      return this.blockMode;
   }

   public boolean collidesWithEntities() {
      if (this.playerMode == CollisionMode.CANCEL && this.trainMode == CollisionMode.CANCEL && this.miscMode == CollisionMode.CANCEL) {
         Iterator var1 = this.mobModes.entrySet().iterator();

         Entry entry;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            entry = (Entry)var1.next();
         } while(entry.getValue() == CollisionMode.CANCEL);

         return true;
      } else {
         return true;
      }
   }

   public CollisionOptions cloneAndSetPlayerMode(CollisionMode mode) {
      return this.playerMode == mode ? this : new CollisionOptions(this.mobModes, mode, this.miscMode, this.trainMode, this.blockMode);
   }

   public CollisionOptions cloneAndSetMiscMode(CollisionMode mode) {
      return this.miscMode == mode ? this : new CollisionOptions(this.mobModes, this.playerMode, mode, this.trainMode, this.blockMode);
   }

   public CollisionOptions cloneAndSetTrainMode(CollisionMode mode) {
      return this.trainMode == mode ? this : new CollisionOptions(this.mobModes, this.playerMode, this.miscMode, mode, this.blockMode);
   }

   public CollisionOptions cloneAndSetBlockMode(CollisionMode mode) {
      return this.blockMode == mode ? this : new CollisionOptions(this.mobModes, this.playerMode, this.miscMode, this.trainMode, mode);
   }

   public CollisionOptions cloneCompareAndSetForAllMobs(CollisionMode expected, CollisionMode newModeIfExpected) {
      EnumMap<CollisionMobCategory, CollisionMode> modes = this.mobModes.clone();
      CollisionMobCategory[] var4;
      int var5;
      int var6;
      CollisionMobCategory category;
      if (newModeIfExpected == null) {
         var4 = CollisionMobCategory.values();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            category = var4[var6];
            if (category.isMobCategory() && modes.get(category) == expected) {
               modes.remove(category);
            }
         }
      } else {
         var4 = CollisionMobCategory.values();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            category = var4[var6];
            if (category.isMobCategory() && modes.get(category) == expected) {
               modes.put(category, newModeIfExpected);
            }
         }
      }

      return new CollisionOptions(modes, this.playerMode, this.miscMode, this.trainMode, this.blockMode);
   }

   public CollisionOptions cloneAndSetForAllMobs(CollisionMode mode) {
      EnumMap<CollisionMobCategory, CollisionMode> modes = this.mobModes.clone();
      CollisionMobCategory[] var3;
      int var4;
      int var5;
      CollisionMobCategory category;
      if (mode == null) {
         var3 = CollisionMobCategory.values();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            category = var3[var5];
            if (category.isMobCategory()) {
               modes.remove(category);
            }
         }
      } else {
         var3 = CollisionMobCategory.values();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            category = var3[var5];
            if (category.isMobCategory()) {
               modes.put(category, mode);
            }
         }
      }

      return new CollisionOptions(modes, this.playerMode, this.miscMode, this.trainMode, this.blockMode);
   }

   public CollisionOptions cloneAndSetMobMode(CollisionMobCategory category, CollisionMode mode) {
      if (category == null) {
         throw new IllegalArgumentException("Collision mob category can not be null");
      } else if (this.mobModes.get(category) == mode) {
         return this;
      } else {
         EnumMap modes;
         if (mode == null && this.mobModes.size() == 1 && this.mobModes.containsKey(category)) {
            modes = DEFAULT.mobModes;
         } else {
            modes = this.mobModes.clone();
            if (mode == null) {
               modes.remove(category);
            } else {
               modes.put(category, mode);
            }
         }

         return new CollisionOptions(modes, this.playerMode, this.miscMode, this.trainMode, this.blockMode);
      }
   }

   public int hashCode() {
      return this.playerMode.ordinal();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CollisionOptions)) {
         return false;
      } else {
         CollisionOptions other = (CollisionOptions)o;
         return this.playerMode == other.playerMode && this.miscMode == other.miscMode && this.trainMode == other.trainMode && this.blockMode == other.blockMode && this.mobModes.equals(other.mobModes);
      }
   }

   public String toString() {
      StringBuilder str = new StringBuilder();
      str.append("CollisionConfig{");
      str.append("player=").append(this.playerMode.name());
      str.append(",misc=").append(this.miscMode.name());
      str.append(",train=").append(this.trainMode.name());
      str.append(",block=").append(this.blockMode.name());
      Iterator var2 = this.mobModes.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<CollisionMobCategory, CollisionMode> entry = (Entry)var2.next();
         str.append(',').append(((CollisionMobCategory)entry.getKey()).getMobType());
         str.append('=').append(((CollisionMode)entry.getValue()).name());
      }

      str.append('}');
      return str.toString();
   }

   public static CollisionOptions.Builder builder() {
      return new CollisionOptions.Builder(DEFAULT);
   }

   public static CollisionOptions.Builder builder(CollisionOptions initial) {
      return new CollisionOptions.Builder(initial);
   }

   // $FF: synthetic method
   CollisionOptions(EnumMap x0, CollisionMode x1, CollisionMode x2, CollisionMode x3, CollisionMode x4, Object x5) {
      this(x0, x1, x2, x3, x4);
   }

   static {
      DEFAULT = new CollisionOptions(NO_MOB_MODES, CollisionMode.DEFAULT, CollisionMode.PUSH, CollisionMode.LINK, CollisionMode.DEFAULT);
      CANCEL = new CollisionOptions(NO_MOB_MODES, CollisionMode.CANCEL, CollisionMode.CANCEL, CollisionMode.CANCEL, CollisionMode.CANCEL);
   }

   public static final class Builder {
      private final EnumMap<CollisionMobCategory, CollisionMode> mobModes;
      private CollisionMode playerMode;
      private CollisionMode miscMode;
      private CollisionMode trainMode;
      private CollisionMode blockMode;

      private Builder(CollisionOptions initial) {
         this.mobModes = new EnumMap(CollisionMobCategory.class);
         this.mobModes.putAll(initial.mobModes());
         this.playerMode = initial.playerMode();
         this.miscMode = initial.miscMode();
         this.trainMode = initial.trainMode();
         this.blockMode = initial.blockMode();
      }

      public CollisionOptions.Builder setPlayerMode(CollisionMode mode) {
         this.playerMode = mode;
         return this;
      }

      public CollisionOptions.Builder setMiscMode(CollisionMode mode) {
         this.miscMode = mode;
         return this;
      }

      public CollisionOptions.Builder setTrainMode(CollisionMode mode) {
         this.trainMode = mode;
         return this;
      }

      public CollisionOptions.Builder setBlockMode(CollisionMode mode) {
         this.blockMode = mode;
         return this;
      }

      public CollisionOptions.Builder setMobMode(CollisionMobCategory category, CollisionMode mode) {
         if (category == null) {
            throw new IllegalArgumentException("Collision mob category cannot be null");
         } else {
            if (mode == null) {
               this.mobModes.remove(category);
            } else {
               this.mobModes.put(category, mode);
            }

            return this;
         }
      }

      public CollisionOptions.Builder setModeForAllMobs(CollisionMode mode) {
         CollisionMobCategory[] var2 = CollisionMobCategory.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CollisionMobCategory category = var2[var4];
            if (category.isMobCategory()) {
               this.setMobMode(category, mode);
            }
         }

         return this;
      }

      public CollisionOptions build() {
         return new CollisionOptions(this.mobModes.isEmpty() ? CollisionOptions.NO_MOB_MODES : this.mobModes, this.playerMode, this.miscMode, this.trainMode, this.blockMode);
      }

      // $FF: synthetic method
      Builder(CollisionOptions x0, Object x1) {
         this(x0);
      }
   }
}
