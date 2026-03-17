package advancedplugins.pm2.cv.models.v1_21_R10.network.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

public class EntityActivityChecker {
   public static boolean validateEntityImmunities(Entity var0) {
      if (!var0.wasTouchingWater && var0.getRemainingFireTicks() <= 0) {
         if (var0 instanceof AbstractArrow) {
            return !((AbstractArrow)var0).isInGround();
         } else if (var0.onGround() && var0.passengers.isEmpty() && !var0.isPassenger()) {
            return var0 instanceof LivingEntity ? checkLivingEntityImmunities((LivingEntity)var0) : var0 instanceof ExperienceOrb;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private static boolean checkLivingEntityImmunities(LivingEntity var0) {
      if (var0.hurtTime <= 0 && var0.activeEffects.isEmpty()) {
         if (var0 instanceof PathfinderMob && ((PathfinderMob)var0).getTarget() != null) {
            return true;
         } else if (var0 instanceof Villager && ((Villager)var0).canBreed()) {
            return true;
         } else if (var0 instanceof Animal) {
            return checkAnimalImmunities((Animal)var0);
         } else {
            return var0 instanceof Creeper && ((Creeper)var0).isIgnited();
         }
      } else {
         return true;
      }
   }

   private static boolean checkAnimalImmunities(Animal var0) {
      if (!var0.isBaby() && !var0.isInLove()) {
         return var0 instanceof Sheep && ((Sheep)var0).isSheared();
      } else {
         return true;
      }
   }

   public static boolean determineActivityStatus(Entity var0) {
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

   private static boolean isAlwaysActive(Entity var0) {
      if (var0 instanceof FireworkRocketEntity) {
         return true;
      } else {
         return var0 instanceof ItemEntity && !shouldCheckItem((ItemEntity)var0);
      }
   }

   private static boolean shouldCheckItem(ItemEntity var0) {
      return (var0.tickCount + var0.getId() + 1) % 4 == 0;
   }

   private static boolean isCurrentlyActive(Entity var0) {
      return var0.activatedTick >= (long)MinecraftServer.currentTick || var0.defaultActivationState;
   }

   private static boolean performPeriodicCheck(Entity var0) {
      if (((long)MinecraftServer.currentTick - var0.activatedTick - 1L) % 20L == 0L) {
         if (validateEntityImmunities(var0)) {
            var0.activatedTick = (long)(MinecraftServer.currentTick + 20);
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean shouldDeactivate(Entity var0) {
      return var0.tickCount % 4 == 0 && !validateEntityImmunities(var0);
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
