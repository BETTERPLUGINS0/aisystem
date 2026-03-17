package com.bergerkiller.bukkit.tc.controller.type;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.MaterialTypeProperty;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartChest;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.common.wrappers.InteractionResult;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.PoweredCartSoundLoop;
import com.bergerkiller.bukkit.tc.controller.persistence.FuelPersistentCartAttribute;
import com.bergerkiller.bukkit.tc.events.MemberCoalUsedEvent;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MinecartMemberFurnace extends MinecartMember<CommonMinecartFurnace> {
   private int fuelCheckCounter = 0;
   private boolean isPushingForwards = true;
   private static final MaterialTypeProperty IS_FUEL_ITEM = Common.evaluateMCVersion(">=", "1.13") ? new MaterialTypeProperty(new Material[]{MaterialUtil.getMaterial("COAL"), MaterialUtil.getMaterial("CHARCOAL")}) : new MaterialTypeProperty(new Material[]{MaterialUtil.getMaterial("LEGACY_COAL")});

   public MinecartMemberFurnace(TrainCarts plugin) {
      super(plugin);
      this.addPersistentCartAttribute(new FuelPersistentCartAttribute());
   }

   public void onAttached() {
      super.onAttached();
      this.soundLoop = new PoweredCartSoundLoop(this);
      Vector fwd = this.getOrientationForward();
      Vector push;
      if (Math.abs(fwd.getY()) > Math.max(Math.abs(fwd.getX()), Math.abs(fwd.getZ()))) {
         push = new Vector(0.0D, ((CommonMinecartFurnace)this.entity).getPushX(), 0.0D);
      } else {
         push = new Vector(((CommonMinecartFurnace)this.entity).getPushX(), 0.0D, ((CommonMinecartFurnace)this.entity).getPushZ());
      }

      this.isPushingForwards = fwd.dot(push) >= 0.0D;
   }

   private void updatePushXZ() {
      Vector fwd = this.getOrientationForward();
      if (!this.isPushingForwards) {
         fwd.multiply(-1.0D);
      }

      if (Math.abs(fwd.getY()) > Math.max(Math.abs(fwd.getX()), Math.abs(fwd.getZ()))) {
         ((CommonMinecartFurnace)this.entity).setPushX(fwd.getY() >= 0.0D ? 1.0D : -1.0D);
         ((CommonMinecartFurnace)this.entity).setPushZ(0.0D);
      } else {
         fwd.setY(0.0D);
         if (fwd.lengthSquared() > 1.0E-10D) {
            fwd.multiply(MathUtil.getNormalizationFactorLS(fwd.lengthSquared()));
            ((CommonMinecartFurnace)this.entity).setPushX(fwd.getX());
            ((CommonMinecartFurnace)this.entity).setPushZ(fwd.getZ());
         }
      }

   }

   public InteractionResult onInteractBy(HumanEntity human, HumanHand hand) {
      if (!this.isInteractable()) {
         return InteractionResult.PASS;
      } else {
         ItemStack itemstack = HumanHand.getHeldItem(human, hand);
         if (itemstack != null && (Boolean)IS_FUEL_ITEM.get(itemstack)) {
            if (!(human instanceof Player) || ((Player)human).getGameMode() != GameMode.CREATIVE) {
               ItemUtil.subtractAmount(itemstack, 1);
               HumanHand.setHeldItem(human, hand, itemstack);
            }

            this.addFuelTicks(3600);
         }

         Location humanEye = human.getEyeLocation();
         Vector eyeFwd = MathUtil.getDirection(humanEye.getYaw(), humanEye.getPitch());
         this.isPushingForwards = this.getOrientationForward().dot(eyeFwd) >= 0.0D;
         this.updatePushXZ();
         return InteractionResult.CONSUME;
      }
   }

   public void addFuelTicks(int fuelTicks) {
      int newFuelTicks = ((CommonMinecartFurnace)this.entity).getFuelTicks() + fuelTicks;
      if (newFuelTicks <= 0) {
         newFuelTicks = 0;
      }

      ((CommonMinecartFurnace)this.entity).setFuelTicks(newFuelTicks);
   }

   public boolean onCoalUsed() {
      MemberCoalUsedEvent event = MemberCoalUsedEvent.call(this);
      return event.useCoal() ? this.getCoalFromNeighbours() : event.refill();
   }

   public boolean getCoalFromNeighbours() {
      MinecartMember[] var1 = this.getNeightbours();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MinecartMember<?> mm = var1[var3];
         if (mm instanceof MinecartMemberChest) {
            Inventory inv = ((CommonMinecartChest)((MinecartMemberChest)mm).getEntity()).getInventory();

            for(int i = 0; i < inv.getSize(); ++i) {
               ItemStack item = inv.getItem(i);
               if (!LogicUtil.nullOrEmpty(item) && item.getType() == Material.COAL) {
                  ItemUtil.subtractAmount(item, 1);
                  inv.setItem(i, item);
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void onPhysicsPostMove() throws MemberMissingException, GroupUnloadedException {
      super.onPhysicsPostMove();
      if (((CommonMinecartFurnace)this.entity).hasFuel()) {
         ((CommonMinecartFurnace)this.entity).addFuelTicks(-1);
         if (!((CommonMinecartFurnace)this.entity).hasFuel() && this.onCoalUsed()) {
            this.addFuelTicks(3600);
         }
      }

      if (!((CommonMinecartFurnace)this.entity).hasFuel()) {
         if (this.fuelCheckCounter++ % 20 == 0 && TCConfig.useCoalFromStorageCart && this.getCoalFromNeighbours()) {
            this.addFuelTicks(3600);
         }
      } else {
         this.fuelCheckCounter = 0;
      }

      if (!((CommonMinecartFurnace)this.entity).hasFuel()) {
         ((CommonMinecartFurnace)this.entity).setFuelTicks(0);
      }

      ((CommonMinecartFurnace)this.entity).setSmoking(((CommonMinecartFurnace)this.entity).hasFuel());
   }

   public void onPhysicsPreMove() {
      super.onPhysicsPreMove();
      if (!this.isDerailed()) {
         Vector dir;
         if (this.isMovementControlled()) {
            dir = FaceUtil.faceToVector(this.getDirection());
            double dot = this.getOrientationForward().dot(dir);
            if (dot < -1.0E-4D || dot > 1.0E-4D) {
               this.isPushingForwards = dot > 0.0D;
            }
         } else if (((CommonMinecartFurnace)this.entity).hasFuel()) {
            dir = this.getOrientationForward();
            if (!this.isPushingForwards) {
               dir.multiply(-1.0D);
            }

            dir.multiply(0.04D + TCConfig.poweredCartBoost);
            ((CommonMinecartFurnace)this.entity).vel.multiply(0.8D);
            ((CommonMinecartFurnace)this.entity).vel.add(dir);
         } else if (this.getGroup().getProperties().isSlowingDown(SlowdownMode.FRICTION)) {
            ((CommonMinecartFurnace)this.entity).vel.multiply(0.98D);
         }

         this.updatePushXZ();
      }

   }

   public void onItemSet(int index, ItemStack item) {
      super.onItemSet(index, item);
      this.onPropertiesChanged();
   }
}
