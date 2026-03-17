package com.bergerkiller.bukkit.tc.events.seat;

import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MemberSeatChangeEvent extends MemberSeatExitEvent {
   private CartAttachmentSeat newSeat;

   public MemberSeatChangeEvent(CartAttachmentSeat oldSeat, CartAttachmentSeat newSeat, Entity entity, boolean playerInitiated) {
      this(oldSeat, newSeat, entity, oldSeat.getPosition(entity), newSeat.getPosition(entity), playerInitiated);
   }

   public MemberSeatChangeEvent(CartAttachmentSeat oldSeat, CartAttachmentSeat newSeat, Entity entity, Location seatPosition, Location exitPosition, boolean playerInitiated) {
      super(oldSeat, entity, seatPosition, exitPosition, true, playerInitiated);
      this.newSeat = newSeat;
   }

   public boolean isSeatChange() {
      return true;
   }

   public boolean isMemberVehicleChange() {
      return this.getMember() != this.getEnteredMember();
   }

   public Entity getEntity() {
      return super.getEntity();
   }

   public boolean isPlayer() {
      return super.isPlayer();
   }

   public CartAttachmentSeat getSeat() {
      return super.getSeat();
   }

   public CartAttachmentSeat getEnteredSeat() {
      return this.newSeat;
   }

   public MinecartMember<?> getEnteredMember() {
      return this.newSeat.getMember();
   }

   public String toString() {
      return "MemberSeatChangeEvent{member=" + this.getMember() + ", passenger=" + this.getEntity() + ", playerInitiated=" + this.isPlayerInitiated() + ", vehicleChange=" + this.isMemberVehicleChange() + ", newMember=" + this.getEnteredMember() + "}";
   }
}
