package advancedplugins.pm2.cv.models.v1_21_R7.network.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.sheep.EntitySheep;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityFireworks;

public class EntityActivityChecker {
   public static boolean validateEntityImmunities(Entity var0) {
      if (!var0.au && var0.aP() <= 0) {
         if (var0 instanceof EntityArrow) {
            return !((EntityArrow)var0).e();
         } else if (var0.aS() && var0.aS.isEmpty() && !var0.cl()) {
            return var0 instanceof EntityLiving ? checkLivingEntityImmunities((EntityLiving)var0) : var0 instanceof EntityExperienceOrb;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private static boolean checkLivingEntityImmunities(EntityLiving var0) {
      if (var0.bt <= 0 && var0.cl.isEmpty()) {
         if (var0 instanceof EntityCreature && ((EntityCreature)var0).e() != null) {
            return true;
         } else if (var0 instanceof EntityVillager && ((EntityVillager)var0).Z_()) {
            return true;
         } else if (var0 instanceof EntityAnimal) {
            return checkAnimalImmunities((EntityAnimal)var0);
         } else {
            return var0 instanceof EntityCreeper && ((EntityCreeper)var0).s();
         }
      } else {
         return true;
      }
   }

   private static boolean checkAnimalImmunities(EntityAnimal var0) {
      if (!var0.g_() && !var0.gS()) {
         return var0 instanceof EntitySheep && ((EntitySheep)var0).s();
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
      if (var0 instanceof EntityFireworks) {
         return true;
      } else {
         return var0 instanceof EntityItem && !shouldCheckItem((EntityItem)var0);
      }
   }

   private static boolean shouldCheckItem(EntityItem var0) {
      return (var0.at + var0.az() + 1) % 4 == 0;
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
      return var0.at % 4 == 0 && !validateEntityImmunities(var0);
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
