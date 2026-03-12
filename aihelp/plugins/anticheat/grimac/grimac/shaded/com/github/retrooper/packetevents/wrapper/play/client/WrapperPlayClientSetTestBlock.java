package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetTestBlock extends PacketWrapper<WrapperPlayClientSetTestBlock> {
   private Vector3i position;
   private WrapperPlayClientSetTestBlock.TestBlockMode mode;
   private String message;

   public WrapperPlayClientSetTestBlock(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSetTestBlock(Vector3i position, WrapperPlayClientSetTestBlock.TestBlockMode mode, String message) {
      super((PacketTypeCommon)PacketType.Play.Client.SET_TEST_BLOCK);
      this.position = position;
      this.mode = mode;
      this.message = message;
   }

   public void read() {
      this.position = this.readBlockPosition();
      this.mode = (WrapperPlayClientSetTestBlock.TestBlockMode)this.readEnum(WrapperPlayClientSetTestBlock.TestBlockMode.class);
      this.message = this.readString();
   }

   public void write() {
      this.writeBlockPosition(this.position);
      this.writeEnum(this.mode);
      this.writeString(this.message);
   }

   public void copy(WrapperPlayClientSetTestBlock wrapper) {
      this.position = wrapper.position;
      this.mode = wrapper.mode;
      this.message = wrapper.message;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public WrapperPlayClientSetTestBlock.TestBlockMode getMode() {
      return this.mode;
   }

   public void setMode(WrapperPlayClientSetTestBlock.TestBlockMode mode) {
      this.mode = mode;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public static enum TestBlockMode {
      START,
      LOG,
      FAIL,
      ACCEPT;

      // $FF: synthetic method
      private static WrapperPlayClientSetTestBlock.TestBlockMode[] $values() {
         return new WrapperPlayClientSetTestBlock.TestBlockMode[]{START, LOG, FAIL, ACCEPT};
      }
   }
}
