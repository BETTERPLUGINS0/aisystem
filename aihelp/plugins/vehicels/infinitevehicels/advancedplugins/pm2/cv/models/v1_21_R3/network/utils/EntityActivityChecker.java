package advancedplugins.pm2.cv.models.v1_21_R3.network.utils;

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
      if (!var0.ag && var0.aG() <= 0) {
         if (var0 instanceof EntityArrow) {
            return !((EntityArrow)var0).l();
         } else if (var0.aJ() && var0.q.isEmpty() && !var0.bZ()) {
            return var0 instanceof EntityLiving ? checkLivingEntityImmunities((EntityLiving)var0) : var0 instanceof EntityExperienceOrb;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private static boolean checkLivingEntityImmunities(EntityLiving livingEntity) {
      if (var0.aN <= 0 && var0.bT.isEmpty()) {
         if (var0 instanceof EntityCreature && ((EntityCreature)var0).O_() != null) {
            return true;
         } else if (var0 instanceof EntityVillager && ((EntityVillager)var0).X_()) {
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
      if (!var0.e_() && !var0.gC()) {
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
      return (var0.af + var0.ar() + 1) % 4 == 0;
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
      return var0.af % 4 == 0 && !validateEntityImmunities(var0);
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
