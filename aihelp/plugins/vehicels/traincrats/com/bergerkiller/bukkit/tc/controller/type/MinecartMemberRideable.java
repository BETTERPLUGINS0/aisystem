package com.bergerkiller.bukkit.tc.controller.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartRideable;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.common.wrappers.InteractionResult;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class MinecartMemberRideable extends MinecartMember<CommonMinecartRideable> {
   private List<Entity> oldPassengers = new ArrayList();

   public MinecartMemberRideable(TrainCarts plugin) {
      super(plugin);
   }

   public InteractionResult onInteractBy(HumanEntity interacter, HumanHand hand) {
      if (interacter instanceof Player && ((Player)interacter).isSneaking()) {
         return InteractionResult.PASS;
      } else if (this.getAvailableSeatCount(interacter) == 0) {
         return InteractionResult.PASS;
      } else {
         CartAttachmentSeat new_seat;
         if (((CommonMinecartRideable)this.entity).isPassenger(interacter)) {
            new_seat = this.getAttachments().findSeat(interacter);
            if (new_seat != null && new_seat.getTicksInSeat() < 10) {
               return InteractionResult.PASS;
            } else {
               TrainProperties tprop = this.getGroup().getProperties();
               if (tprop.getPlayersExit() && tprop.getPlayersEnter()) {
                  return this.getAttachments().changeSeatsLookingAt(interacter) ? InteractionResult.SUCCESS : InteractionResult.PASS;
               } else {
                  return InteractionResult.PASS;
               }
            }
         } else {
            new_seat = this.getAttachments().findNewSeatForEntity(interacter);
            if (new_seat == null) {
               return InteractionResult.PASS;
            } else {
               MinecartMember<?> previous = MinecartMemberStore.getFromEntity(interacter.getVehicle());
               if (previous != null) {
                  CartAttachmentSeat old_seat = previous.getAttachments().findSeat(interacter);
                  if (old_seat == new_seat) {
                     return InteractionResult.PASS;
                  } else {
                     return AttachmentControllerMember.handleSeatChange(interacter, old_seat, new_seat, true) ? InteractionResult.SUCCESS : InteractionResult.PASS;
                  }
               } else {
                  return AttachmentControllerMember.handleSeatChange(interacter, (CartAttachmentSeat)null, new_seat, true) ? InteractionResult.SUCCESS : InteractionResult.PASS;
               }
            }
         }
      }
   }

   public void onAttached() {
      super.onAttached();
      this.oldPassengers.clear();
      this.oldPassengers.addAll(((CommonMinecartRideable)this.entity).getPassengers());
   }

   public void onActivate() {
      super.onActivate();
      if (TCConfig.activatorEjectEnabled) {
         this.eject();
      }

   }

   public void onPhysicsPostMove() throws MemberMissingException, GroupUnloadedException {
      super.onPhysicsPostMove();
      List<Entity> newPassengers = ((CommonMinecartRideable)this.entity).getPassengers();
      if (!this.oldPassengers.equals(newPassengers)) {
         this.oldPassengers.clear();
         this.oldPassengers.addAll(newPassengers);
         this.onPropertiesChanged();
      }

   }
}
