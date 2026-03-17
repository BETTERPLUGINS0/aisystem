package advancedplugins.pm2.cv.models.v1_21_R1.network.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityFireworks;

public class EntityActivityChecker {
   public static boolean validateEntityImmunities(Entity entity) {
      if (!var0.aj && var0.aC() <= 0) {
         if (var0 instanceof EntityArrow) {
            return !((EntityArrow)var0).b;
         } else if (var0.aF() && var0.p.isEmpty() && !var0.bS()) {
            return var0 instanceof EntityLiving ? checkLivingEntityImmunities((EntityLiving)var0) : var0 instanceof EntityExperienceOrb;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private static boolean checkLivingEntityImmunities(EntityLiving livingEntity) {
      if (var0.aO <= 0 && var0.bW.isEmpty()) {
         if (var0 instanceof EntityCreature && ((EntityCreature)var0).p() != null) {
            return true;
         } else if (var0 instanceof EntityVillager && ((EntityVillager)var0).ab_()) {
            return true;
         } else if (var0 instanceof EntityAnimal) {
            return checkAnimalImmunities((EntityAnimal)var0);
         } else {
            return var0 instanceof EntityCreeper && ((EntityCreeper)var0).x();
         }
      } else {
         return true;
      }
   }

   private static boolean checkAnimalImmunities(EntityAnimal animal) {
      if (!var0.o_() && !var0.gs()) {
         return var0 instanceof EntitySheep && ((EntitySheep)var0).x();
      } else {
         return true;
      }
   }

   public static boolean determineActivityStatus(Entity entity) {
      if (isAlwaysActive(var0)) {
         return true;
      } else {
         boolean var1 = isCurrentlyActive(var0);
         if (!var1) {
            var1 = performPeriodicCheck(var0);
         } else if (!var0.defaultActivationState && shouldDeactivate(var0)) {
            var1 = false;
         }

         return var1;
      }
   }

   private static boolean isAlwaysActive(Entity entity) {
      if (var0 instanceof EntityFireworks) {
         return true;
      } else {
         return var0 instanceof EntityItem && !shouldCheckItem((EntityItem)var0);
      }
   }

   private static boolean shouldCheckItem(EntityItem item) {
      return (var0.ai + var0.an() + 1) % 4 == 0;
   }

   private static boolean isCurrentlyActive(Entity entity) {
      return var0.activatedTick >= (long)MinecraftServer.currentTick || var0.defaultActivationState;
   }

   private static boolean performPeriodicCheck(Entity entity) {
      if (((long)MinecraftServer.currentTick - var0.activatedTick - 1L) % 20L == 0L) {
         if (validateEntityImmunities(var0)) {
            var0.activatedTick = (long)(MinecraftServer.currentTick + 20);
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean shouldDeactivate(Entity entity) {
      return var0.ai % 4 == 0 && !validateEntityImmunities(var0);
   }

   public static enum ActivityCategory {
      HOSTILE,
      PASSIVE,
      RAIDER,
      MISCELLANEOUS;

      // $FF: synthetic method
      private static EntityActivityChecker.ActivityCategory[] $values() {
         return new EntityActivityChecker.ActivityCategory[]{HOSTILE, PASSIVE, RAIDER, MISCELLANEOUS};
      }
   }
}
