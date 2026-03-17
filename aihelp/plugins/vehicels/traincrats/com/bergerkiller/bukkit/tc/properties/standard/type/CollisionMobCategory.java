package com.bergerkiller.bukkit.tc.properties.standard.type;

import com.bergerkiller.bukkit.common.utils.EntityGroupingUtil;
import com.bergerkiller.bukkit.common.utils.EntityGroupingUtil.EntityCategory;
import com.bergerkiller.bukkit.tc.CollisionMode;
import java.util.Set;
import org.bukkit.entity.Entity;

public enum CollisionMobCategory {
   PETS("pet", "pets", EntityCategory.TAMED.getEntityClasses(), "Pets", (CollisionMode)null),
   JOCKEYS("jockey", "jockeys", EntityCategory.JOCKEY.getEntityClasses(), "Jockeys", (CollisionMode)null),
   KILLER_BUNNIES("killer_bunny", "killer_bunnies", EntityCategory.KILLER_BUNNY.getEntityClasses(), "Killer Bunnies", (CollisionMode)null),
   NPCS("npc", "npcs", EntityCategory.NPC.getEntityClasses(), "NPCs", (CollisionMode)null),
   ANIMALS("animal", "animals", EntityCategory.ANIMAL.getEntityClasses(), "Animals", (CollisionMode)null),
   MONSTERS("monster", "monsters", EntityCategory.MONSTER.getEntityClasses(), "Monsters", (CollisionMode)null),
   PASSIVE_MOBS("passive", "passives", EntityCategory.PASSIVE.getEntityClasses(), "Passive Mobs", CollisionMode.DEFAULT),
   NEUTRAL_MOBS("neutral", "neutrals", EntityCategory.NEUTRAL.getEntityClasses(), "Neutral Mobs", CollisionMode.DEFAULT),
   HOSTILE_MOBS("hostile", "hostiles", EntityCategory.HOSTILE.getEntityClasses(), "Hostile Mobs", CollisionMode.DEFAULT),
   TAMEABLE_MOBS("tameable", "tameables", EntityCategory.TAMEABLE.getEntityClasses(), "Tameable Mobs", CollisionMode.DEFAULT),
   UTILITY_MOBS("utility", "utilities", EntityCategory.UTILITY.getEntityClasses(), "Utility Mobs", CollisionMode.DEFAULT),
   BOSS_MOBS("boss", "bosses", EntityCategory.BOSS.getEntityClasses(), "Boss Mobs", CollisionMode.DEFAULT);

   private final String mobType;
   private final String pluralMobType;
   private final String friendlyMobName;
   private CollisionMode defaultCollisionMode;
   private Set<Class<?>> entityClasses;

   private CollisionMobCategory(String mobType, String pluralMobType, Set<Class<?>> entityClasses, String friendlyMobName, CollisionMode defaultCollisionMode) {
      this.mobType = mobType;
      this.pluralMobType = pluralMobType;
      this.friendlyMobName = friendlyMobName;
      this.setDefaultCollisionMode(defaultCollisionMode);
      this.setEntityClasses(entityClasses);
   }

   public boolean isMobType(Entity entity) {
      return this.entityClasses != null && !this.entityClasses.isEmpty() && EntityGroupingUtil.isEntityTypeClass(entity, this.entityClasses);
   }

   public String getMobType() {
      return this.mobType;
   }

   public boolean isMobCategory() {
      return this.name().endsWith("_MOBS");
   }

   public static CollisionMobCategory findMobType(Entity entity) {
      CollisionMobCategory[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CollisionMobCategory collisionConfigObject = var1[var3];
         if (collisionConfigObject.isMobType(entity)) {
            return collisionConfigObject;
         }
      }

      return null;
   }

   public static CollisionMobCategory findMobType(String entityType) {
      CollisionMobCategory[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CollisionMobCategory collisionConfigObject = var1[var3];
         if (collisionConfigObject.getMobType().equals(entityType)) {
            return collisionConfigObject;
         }
      }

      return null;
   }

   public static CollisionMobCategory findMobType(String entityType, String prefix) {
      return prefix == null ? findMobType(entityType) : findMobType(entityType.substring(prefix.length()));
   }

   public static CollisionMobCategory findMobType(String entityType, String prefix, String suffix) {
      if (prefix == null && suffix == null) {
         return findMobType(entityType);
      } else {
         return suffix == null ? findMobType(entityType, prefix) : findMobType(entityType.substring(0, entityType.length() - suffix.length()), prefix);
      }
   }

   public String getFriendlyMobName() {
      return this.friendlyMobName;
   }

   public String getPluralMobType() {
      return this.pluralMobType;
   }

   public CollisionMode getDefaultCollisionMode() {
      return this.defaultCollisionMode;
   }

   public void setDefaultCollisionMode(CollisionMode defaultCollisionMode) {
      this.defaultCollisionMode = defaultCollisionMode;
   }

   public Set<Class<?>> getEntityClasses() {
      return this.entityClasses;
   }

   public void setEntityClasses(Set<Class<?>> entityClasses) {
      this.entityClasses = entityClasses;
   }

   // $FF: synthetic method
   private static CollisionMobCategory[] $values() {
      return new CollisionMobCategory[]{PETS, JOCKEYS, KILLER_BUNNIES, NPCS, ANIMALS, MONSTERS, PASSIVE_MOBS, NEUTRAL_MOBS, HOSTILE_MOBS, TAMEABLE_MOBS, UTILITY_MOBS, BOSS_MOBS};
   }
}
