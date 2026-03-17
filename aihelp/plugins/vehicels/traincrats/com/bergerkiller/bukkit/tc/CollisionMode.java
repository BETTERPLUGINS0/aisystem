package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeNormalA;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

public enum CollisionMode {
   DEFAULT("is stopped by"),
   PUSH("pushes"),
   CANCEL("ignores"),
   KILL("kills"),
   KILLNODROPS("kills without drops"),
   ENTER("takes in"),
   LINK("forms a group with"),
   DAMAGE("damages"),
   DAMAGENODROPS("damages without drops");

   private final String operationName;

   private CollisionMode(String operationName) {
      this.operationName = operationName;
   }

   public static CollisionMode parse(String text) {
      if (text.equalsIgnoreCase("skip")) {
         return CANCEL;
      } else {
         CollisionMode tf = ParseUtil.isBool(text) ? (ParseUtil.parseBool(text) ? DEFAULT : CANCEL) : null;
         return (CollisionMode)ParseUtil.parseEnum(CollisionMode.class, text, tf);
      }
   }

   public static CollisionMode fromLinking(boolean state) {
      return state ? LINK : DEFAULT;
   }

   public static CollisionMode fromPushing(boolean state) {
      return state ? PUSH : DEFAULT;
   }

   public boolean execute(MinecartMember<?> member, Entity entity) {
      CommonMinecart<?> minecart = (CommonMinecart)member.getEntity();
      MinecartMember<?> other = MinecartMemberStore.getFromEntity(entity);
      if (member.isInteractable() && !entity.isDead() && !member.isCollisionIgnored(entity)) {
         if (entity.isInsideVehicle() && entity.getVehicle() instanceof Minecart) {
            return false;
         } else {
            if (other != null) {
               if (!other.isInteractable()) {
                  return false;
               }

               if (member.getGroup() == other.getGroup()) {
                  return false;
               }

               RailLogic logic1 = member.getRailLogic();
               if (logic1 instanceof RailLogicVerticalSlopeNormalA) {
                  RailLogic logic2 = other.getRailLogic();
                  if (logic2 instanceof RailLogicVerticalSlopeNormalA) {
                     Block b1 = member.getBlock(logic1.getDirection());
                     Block b2 = other.getBlock(logic2.getDirection());
                     if (BlockUtil.equals(b1, b2)) {
                        return false;
                     }
                  }
               }
            }

            double trainX;
            if (entity instanceof Player && this.isHitCollision()) {
               trainX = member.getLimitedVelocity().getX();
               double trainZ = member.getLimitedVelocity().getZ();
               double playerSpeed = (double)((Player)entity).getWalkSpeed();
               Vector playerVelocity = ((Player)entity).getEyeLocation().getDirection();
               playerVelocity.multiply(playerSpeed);
               double playerX = playerVelocity.getX();
               double playerZ = playerVelocity.getZ();
               if (Math.abs(playerX) + Math.abs(playerZ) > 0.03D) {
                  if (Math.abs(trainX) + Math.abs(trainZ) < 0.03D) {
                     return TCConfig.allowPlayerCollisionFromBehind;
                  }

                  if (Math.abs(trainX) > Math.abs(trainZ) && playerX * trainX > 0.0D && Math.abs(playerX) > Math.abs(trainX)) {
                     return TCConfig.allowPlayerCollisionFromBehind;
                  }

                  if (Math.abs(trainX) <= Math.abs(trainZ) && playerZ * trainZ >= 0.0D && Math.abs(playerZ) > Math.abs(trainZ)) {
                     return TCConfig.allowPlayerCollisionFromBehind;
                  }
               }
            }

            switch(this) {
            case ENTER:
               if (member.getAvailableSeatCount(entity) > 0 && Util.canBePassenger(entity) && member.canCollisionEnter()) {
                  minecart.addPassenger(entity);
               }

               return false;
            case PUSH:
               this.push(member, entity);
               return false;
            case CANCEL:
               return false;
            case DAMAGE:
            case DAMAGENODROPS:
               if (member.isMoving() && member.isHeadingTo(entity)) {
                  if (this == DAMAGENODROPS) {
                     TCListener.cancelNextDrops = true;
                  }

                  trainX = ((CommonMinecart)member.getEntity()).vel.lengthSquared() * member.getProperties().getTrainProperties().getCollisionDamage();
                  this.damage(member, entity, trainX);
                  this.push(member, entity);
                  if (this == DAMAGENODROPS) {
                     TCListener.cancelNextDrops = false;
                  }
               }

               return false;
            case KILLNODROPS:
            case KILL:
               if (member.isMoving() && member.isHeadingTo(entity)) {
                  if (this == KILLNODROPS) {
                     TCListener.cancelNextDrops = true;
                  }

                  MinecartMember oldKilledByMember = TCListener.killedByMember;

                  try {
                     TCListener.killedByMember = member;
                     this.damage(member, entity, 32767.0D);
                  } finally {
                     TCListener.killedByMember = oldKilledByMember;
                  }

                  if (this == KILLNODROPS) {
                     TCListener.cancelNextDrops = false;
                  }
               }

               return false;
            case LINK:
               if (other != null) {
                  return !MinecartGroupStore.link(member, other).isCancelCollision();
               }

               return true;
            default:
               if (member.isMovementControlled()) {
                  return false;
               } else if (other != null) {
                  if (member.isHeadingTo(entity)) {
                     member.getGroup().stop();
                  }

                  return false;
               } else {
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void push(MinecartMember<?> member, Entity entity) {
      if (entity instanceof Minecart) {
         if (member.isHeadingTo(entity)) {
            double gap = member.getCartCouplerLength();
            MinecartMember<?> otherMember = MinecartMemberStore.getFromEntity(entity);
            if (otherMember != null) {
               gap += otherMember.getCartCouplerLength();
            } else {
               gap += 0.5D * TCConfig.cartDistanceGap;
            }

            double force = gap + 1.0D - ((CommonMinecart)member.getEntity()).loc.distanceSquared(entity);
            force *= TCConfig.cartDistanceForcer;
            force += member.getRealSpeed() - entity.getVelocity().length();
            if (force > 0.0D) {
               member.push(entity, force);
            }
         }
      } else {
         member.pushSideways(entity);
      }

   }

   private void damage(MinecartMember<?> member, Entity entity, double damageAmount) {
      if (entity instanceof LivingEntity) {
         boolean old = EntityUtil.isInvulnerable(entity);
         EntityUtil.setInvulnerable(entity, false);
         ((LivingEntity)entity).damage(damageAmount, ((CommonMinecart)member.getEntity()).getEntity());
         EntityUtil.setInvulnerable(entity, old);
      } else {
         EntityUtil.damage(entity, DamageCause.CUSTOM, 32767.0D);
         entity.remove();
      }

   }

   public String getOperationName() {
      return this.operationName;
   }

   public boolean permitsKnockback() {
      return this == DEFAULT;
   }

   public boolean isHitCollision() {
      switch(this) {
      case PUSH:
      case DAMAGE:
      case DAMAGENODROPS:
      case KILLNODROPS:
      case KILL:
         return true;
      case CANCEL:
      default:
         return false;
      }
   }

   // $FF: synthetic method
   private static CollisionMode[] $values() {
      return new CollisionMode[]{DEFAULT, PUSH, CANCEL, KILL, KILLNODROPS, ENTER, LINK, DAMAGE, DAMAGENODROPS};
   }
}
