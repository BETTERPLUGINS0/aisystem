package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerInput extends PacketWrapper<WrapperPlayClientPlayerInput> {
   private boolean forward;
   private boolean backward;
   private boolean left;
   private boolean right;
   private boolean jump;
   private boolean shift;
   private boolean sprint;

   public WrapperPlayClientPlayerInput(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerInput(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean shift, boolean sprint) {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_INPUT);
      this.forward = forward;
      this.backward = backward;
      this.left = left;
      this.right = right;
      this.jump = jump;
      this.shift = shift;
      this.sprint = sprint;
   }

   public void read() {
      byte flags = this.readByte();
      this.forward = (flags & 1) != 0;
      this.backward = (flags & 2) != 0;
      this.left = (flags & 4) != 0;
      this.right = (flags & 8) != 0;
      this.jump = (flags & 16) != 0;
      this.shift = (flags & 32) != 0;
      this.sprint = (flags & 64) != 0;
   }

   public void write() {
      byte flags = 0;
      byte flags = (byte)(flags | (this.forward ? 1 : 0));
      flags = (byte)(flags | (this.backward ? 2 : 0));
      flags = (byte)(flags | (this.left ? 4 : 0));
      flags = (byte)(flags | (this.right ? 8 : 0));
      flags = (byte)(flags | (this.jump ? 16 : 0));
      flags = (byte)(flags | (this.shift ? 32 : 0));
      flags = (byte)(flags | (this.sprint ? 64 : 0));
      this.writeByte(flags);
   }

   public void copy(WrapperPlayClientPlayerInput wrapper) {
      this.forward = wrapper.forward;
      this.backward = wrapper.backward;
      this.left = wrapper.left;
      this.right = wrapper.right;
      this.jump = wrapper.jump;
      this.shift = wrapper.shift;
      this.sprint = wrapper.sprint;
   }

   public boolean isForward() {
      return this.forward;
   }

   public void setForward(boolean forward) {
      this.forward = forward;
   }

   public boolean isBackward() {
      return this.backward;
   }

   public void setBackward(boolean backward) {
      this.backward = backward;
   }

   public boolean isLeft() {
      return this.left;
   }

   public void setLeft(boolean left) {
      this.left = left;
   }

   public boolean isRight() {
      return this.right;
   }

   public void setRight(boolean right) {
      this.right = right;
   }

   public boolean isJump() {
      return this.jump;
   }

   public void setJump(boolean jump) {
      this.jump = jump;
   }

   public boolean isShift() {
      return this.shift;
   }

   public void setShift(boolean shift) {
      this.shift = shift;
   }

   public boolean isSprint() {
      return this.sprint;
   }

   public void setSprint(boolean sprint) {
      this.sprint = sprint;
   }
}
