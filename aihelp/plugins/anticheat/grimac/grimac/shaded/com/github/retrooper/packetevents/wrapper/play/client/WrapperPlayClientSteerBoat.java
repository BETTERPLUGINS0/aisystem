package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSteerBoat extends PacketWrapper<WrapperPlayClientSteerBoat> {
   private boolean leftPaddleTurning;
   private boolean rightPaddleTurning;

   public WrapperPlayClientSteerBoat(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSteerBoat(boolean leftPaddleTurning, boolean rightPaddleTurning) {
      super((PacketTypeCommon)PacketType.Play.Client.STEER_BOAT);
      this.leftPaddleTurning = leftPaddleTurning;
      this.rightPaddleTurning = rightPaddleTurning;
   }

   public void read() {
      this.leftPaddleTurning = this.readBoolean();
      this.rightPaddleTurning = this.readBoolean();
   }

   public void write() {
      this.writeBoolean(this.leftPaddleTurning);
      this.writeBoolean(this.rightPaddleTurning);
   }

   public void copy(WrapperPlayClientSteerBoat wrapper) {
      this.leftPaddleTurning = wrapper.leftPaddleTurning;
      this.rightPaddleTurning = wrapper.rightPaddleTurning;
   }

   public boolean isLeftPaddleTurning() {
      return this.leftPaddleTurning;
   }

   public void setLeftPaddleTurning(boolean leftPaddleTurning) {
      this.leftPaddleTurning = leftPaddleTurning;
   }

   public boolean isRightPaddleTurning() {
      return this.rightPaddleTurning;
   }

   public void setRightPaddleTurning(boolean rightPaddleTurning) {
      this.rightPaddleTurning = rightPaddleTurning;
   }
}
