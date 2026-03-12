package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerGameTestHighlightPos extends PacketWrapper<WrapperPlayServerGameTestHighlightPos> {
   private Vector3i absolutePos;
   private Vector3i relativePos;

   public WrapperPlayServerGameTestHighlightPos(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerGameTestHighlightPos(Vector3i absolutePos, Vector3i relativePos) {
      super((PacketTypeCommon)PacketType.Play.Server.GAME_TEST_HIGHLIGHT_POS);
      this.absolutePos = absolutePos;
      this.relativePos = relativePos;
   }

   public void read() {
      this.absolutePos = this.readBlockPosition();
      this.relativePos = this.readBlockPosition();
   }

   public void write() {
      this.writeBlockPosition(this.absolutePos);
      this.writeBlockPosition(this.relativePos);
   }

   public void copy(WrapperPlayServerGameTestHighlightPos wrapper) {
      this.absolutePos = wrapper.absolutePos;
      this.relativePos = wrapper.relativePos;
   }

   public Vector3i getAbsolutePos() {
      return this.absolutePos;
   }

   public void setAbsolutePos(Vector3i absolutePos) {
      this.absolutePos = absolutePos;
   }

   public Vector3i getRelativePos() {
      return this.relativePos;
   }

   public void setRelativePos(Vector3i relativePos) {
      this.relativePos = relativePos;
   }
}
