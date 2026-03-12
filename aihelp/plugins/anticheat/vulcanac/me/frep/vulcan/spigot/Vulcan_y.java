package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: synthetic class
class Vulcan_y {
   static final int[] Vulcan_I;
   static final int[] Vulcan_u;
   static final int[] Vulcan_v;
   static final int[] Vulcan__ = new int[DamageCause.values().length];

   static {
      try {
         Vulcan__[DamageCause.ENTITY_ATTACK.ordinal()] = 1;
      } catch (NoSuchFieldError var28) {
      }

      try {
         Vulcan__[DamageCause.FIRE.ordinal()] = 2;
      } catch (NoSuchFieldError var27) {
      }

      try {
         Vulcan__[DamageCause.FIRE_TICK.ordinal()] = 3;
      } catch (NoSuchFieldError var26) {
      }

      try {
         Vulcan__[DamageCause.FALL.ordinal()] = 4;
      } catch (NoSuchFieldError var25) {
      }

      try {
         Vulcan__[DamageCause.MAGIC.ordinal()] = 5;
      } catch (NoSuchFieldError var24) {
      }

      try {
         Vulcan__[DamageCause.POISON.ordinal()] = 6;
      } catch (NoSuchFieldError var23) {
      }

      try {
         Vulcan__[DamageCause.CONTACT.ordinal()] = 7;
      } catch (NoSuchFieldError var22) {
      }

      try {
         Vulcan__[DamageCause.PROJECTILE.ordinal()] = 8;
      } catch (NoSuchFieldError var21) {
      }

      try {
         Vulcan__[DamageCause.LAVA.ordinal()] = 9;
      } catch (NoSuchFieldError var20) {
      }

      Vulcan_v = new int[EntityType.values().length];

      try {
         Vulcan_v[EntityType.ENDER_DRAGON.ordinal()] = 1;
      } catch (NoSuchFieldError var19) {
      }

      try {
         Vulcan_v[EntityType.IRON_GOLEM.ordinal()] = 2;
      } catch (NoSuchFieldError var18) {
      }

      try {
         Vulcan_v[EntityType.FIREBALL.ordinal()] = 3;
      } catch (NoSuchFieldError var17) {
      }

      try {
         Vulcan_v[EntityType.SMALL_FIREBALL.ordinal()] = 4;
      } catch (NoSuchFieldError var16) {
      }

      try {
         Vulcan_v[EntityType.DRAGON_FIREBALL.ordinal()] = 5;
      } catch (NoSuchFieldError var15) {
      }

      try {
         Vulcan_v[EntityType.WITHER.ordinal()] = 6;
      } catch (NoSuchFieldError var14) {
      }

      try {
         Vulcan_v[EntityType.WITHER_SKULL.ordinal()] = 7;
      } catch (NoSuchFieldError var13) {
      }

      try {
         Vulcan_v[EntityType.ENDER_CRYSTAL.ordinal()] = 8;
      } catch (NoSuchFieldError var12) {
      }

      Vulcan_u = new int[DiggingAction.values().length];

      try {
         Vulcan_u[DiggingAction.START_DIGGING.ordinal()] = 1;
      } catch (NoSuchFieldError var11) {
      }

      try {
         Vulcan_u[DiggingAction.FINISHED_DIGGING.ordinal()] = 2;
      } catch (NoSuchFieldError var10) {
      }

      try {
         Vulcan_u[DiggingAction.CANCELLED_DIGGING.ordinal()] = 3;
      } catch (NoSuchFieldError var9) {
      }

      try {
         Vulcan_u[DiggingAction.RELEASE_USE_ITEM.ordinal()] = 4;
      } catch (NoSuchFieldError var8) {
      }

      try {
         Vulcan_u[DiggingAction.DROP_ITEM.ordinal()] = 5;
      } catch (NoSuchFieldError var7) {
      }

      try {
         Vulcan_u[DiggingAction.DROP_ITEM_STACK.ordinal()] = 6;
      } catch (NoSuchFieldError var6) {
      }

      Vulcan_I = new int[Action.values().length];

      try {
         Vulcan_I[Action.START_SPRINTING.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         Vulcan_I[Action.STOP_SPRINTING.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_I[Action.START_SNEAKING.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_I[Action.STOP_SNEAKING.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_I[Action.START_FLYING_WITH_ELYTRA.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }

   }
}
