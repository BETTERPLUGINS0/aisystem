package com.bergerkiller.bukkit.tc.controller.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartTNT;
import com.bergerkiller.bukkit.common.wrappers.DamageSource;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import org.bukkit.Effect;
import org.bukkit.block.BlockFace;

public class MinecartMemberTNT extends MinecartMember<CommonMinecartTNT> {
   private boolean ignoreDamage = false;

   public MinecartMemberTNT(TrainCarts plugin) {
      super(plugin);
   }

   public boolean onDamage(DamageSource damagesource, double damage) {
      if (this.isInteractable() && !this.ignoreDamage) {
         boolean result = super.onDamage(damagesource, damage);
         if (((CommonMinecartTNT)this.entity).isRemoved() && !Util.canInstantlyBuild(damagesource.getEntity()) && (damagesource.isFireDamage() || damagesource.isExplosive() || ((CommonMinecartTNT)this.entity).isMovingFast())) {
            this.ignoreDamage = true;
            ((CommonMinecartTNT)this.entity).explode();
            this.ignoreDamage = false;
         }

         return result;
      } else {
         return false;
      }
   }

   public void onActivate() {
      super.onActivate();
      if (!((CommonMinecartTNT)this.entity).isTNTPrimed()) {
         ((CommonMinecartTNT)this.entity).primeTNT();
      }

   }

   public void onPhysicsPostMove() throws MemberMissingException, GroupUnloadedException {
      super.onPhysicsPostMove();
      int ticks = ((CommonMinecartTNT)this.entity).getFuseTicks();
      if (ticks > 0) {
         ((CommonMinecartTNT)this.entity).setFuseTicks(ticks - 1);
         ((CommonMinecartTNT)this.entity).getWorld().playEffect(((CommonMinecartTNT)this.entity).getLocation().add(0.0D, 0.5D, 0.0D), Effect.SMOKE, BlockFace.SELF);
      } else if (ticks == 0) {
         ((CommonMinecartTNT)this.entity).explode();
      }

      if (((CommonMinecartTNT)this.entity).isMovementBlocked() && ((CommonMinecartTNT)this.entity).isMovingFast()) {
         ((CommonMinecartTNT)this.entity).explode();
      }

   }
}
