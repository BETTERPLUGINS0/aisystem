package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.wrappers.IntHashMap;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotationHandle;

public class SeatAttachmentMap implements PacketListener {
   public static PacketType[] LISTENED_TYPES;
   private final IntHashMap<CartAttachmentSeat> _map = new IntHashMap();

   public void set(int passengerEntityId, CartAttachmentSeat seat) {
      this._map.put(passengerEntityId, seat);
   }

   public void remove(int passengerEntityId, CartAttachmentSeat seat) {
      CartAttachmentSeat removed = (CartAttachmentSeat)this._map.remove(passengerEntityId);
      if (removed != seat) {
         this._map.put(passengerEntityId, removed);
      }

   }

   public CartAttachmentSeat get(int passengerEntityId) {
      CartAttachmentSeat seat = (CartAttachmentSeat)this._map.get(passengerEntityId);
      if (seat != null && (seat.getEntity() == null || seat.getEntity().getEntityId() != passengerEntityId)) {
         this._map.remove(passengerEntityId);
         seat = null;
      }

      return seat;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
   }

   public void onPacketSend(PacketSendEvent event) {
      PacketPlayOutEntityHandle packet;
      CartAttachmentSeat seat;
      if (event.getType() == PacketType.OUT_ENTITY_MOVE_LOOK) {
         packet = PacketPlayOutEntityHandle.createHandle(event.getPacket().getHandle());
         seat = this.get(packet.getEntityId());
         if (seat != null && seat.isRotationLocked()) {
            packet.setYaw(seat.getPassengerYaw());
            packet.setPitch(seat.getPassengerPitch());
         }
      } else if (event.getType() == PacketType.OUT_ENTITY_LOOK) {
         packet = PacketPlayOutEntityHandle.createHandle(event.getPacket().getHandle());
         seat = this.get(packet.getEntityId());
         if (seat != null && seat.isRotationLocked()) {
            packet.setYaw(seat.getPassengerYaw());
            packet.setPitch(seat.getPassengerPitch());
         }
      } else if (event.getType() == PacketType.OUT_ENTITY_HEAD_ROTATION) {
         PacketPlayOutEntityHeadRotationHandle packet = PacketPlayOutEntityHeadRotationHandle.createHandle(event.getPacket().getHandle());
         seat = this.get(packet.getEntityId());
         if (seat != null && seat.isRotationLocked()) {
            packet.setHeadYaw(seat.getPassengerHeadYaw());
         }
      }

   }

   static {
      LISTENED_TYPES = new PacketType[]{PacketType.OUT_ENTITY_MOVE_LOOK, PacketType.OUT_ENTITY_LOOK, PacketType.OUT_ENTITY_HEAD_ROTATION};
   }
}
