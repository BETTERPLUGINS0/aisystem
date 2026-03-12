package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {
   private InteractionHand hand;
   private int sequence;
   private float yaw;
   private float pitch;

   public WrapperPlayClientUseItem(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientUseItem(InteractionHand hand) {
      this(hand, 0);
   }

   public WrapperPlayClientUseItem(InteractionHand hand, int sequence) {
      this(hand, sequence, 0.0F, 0.0F);
   }

   public WrapperPlayClientUseItem(InteractionHand hand, int sequence, float yaw, float pitch) {
      super((PacketTypeCommon)PacketType.Play.Client.USE_ITEM);
      this.hand = hand;
      this.sequence = sequence;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public void read() {
      this.hand = InteractionHand.getById(this.readVarInt());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.sequence = this.readVarInt();
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
            this.yaw = this.readFloat();
            this.pitch = this.readFloat();
         }
      }

   }

   public void write() {
      this.writeVarInt(this.hand.getId());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeVarInt(this.sequence);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
            this.writeFloat(this.yaw);
            this.writeFloat(this.pitch);
         }
      }

   }

   public void copy(WrapperPlayClientUseItem packet) {
      this.hand = packet.hand;
      this.sequence = packet.sequence;
      this.yaw = packet.yaw;
      this.pitch = packet.pitch;
   }

   public InteractionHand getHand() {
      return this.hand;
   }

   public void setHand(InteractionHand hand) {
      this.hand = hand;
   }

   public int getSequence() {
      return this.sequence;
   }

   public void setSequence(int sequence) {
      this.sequence = sequence;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }
}
