package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerDebugBlockValue extends PacketWrapper<WrapperPlayServerDebugBlockValue> {
   private Vector3i blockPos;
   private DebugSubscription.Update<?> update;

   public WrapperPlayServerDebugBlockValue(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDebugBlockValue(Vector3i blockPos, DebugSubscription.Update<?> update) {
      super((PacketTypeCommon)PacketType.Play.Server.DEBUG_BLOCK_VALUE);
      this.blockPos = blockPos;
      this.update = update;
   }

   public void read() {
      this.blockPos = this.readBlockPosition();
      this.update = DebugSubscription.Update.read(this);
   }

   public void write() {
      this.writeBlockPosition(this.blockPos);
      DebugSubscription.Update.write(this, this.update);
   }

   public void copy(WrapperPlayServerDebugBlockValue wrapper) {
      this.blockPos = wrapper.blockPos;
      this.update = wrapper.update;
   }

   public Vector3i getBlockPos() {
      return this.blockPos;
   }

   public void setBlockPos(Vector3i blockPos) {
      this.blockPos = blockPos;
   }

   public DebugSubscription.Update<?> getUpdate() {
      return this.update;
   }

   public void setUpdate(DebugSubscription.Update<?> update) {
      this.update = update;
   }
}
