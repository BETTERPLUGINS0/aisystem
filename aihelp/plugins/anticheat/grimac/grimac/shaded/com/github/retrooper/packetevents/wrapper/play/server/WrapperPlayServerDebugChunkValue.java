package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector2i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerDebugChunkValue extends PacketWrapper<WrapperPlayServerDebugChunkValue> {
   private Vector2i chunkPos;
   private DebugSubscription.Update<?> update;

   public WrapperPlayServerDebugChunkValue(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDebugChunkValue(Vector2i chunkPos, DebugSubscription.Update<?> update) {
      super((PacketTypeCommon)PacketType.Play.Server.DEBUG_CHUNK_VALUE);
      this.chunkPos = chunkPos;
      this.update = update;
   }

   public void read() {
      this.chunkPos = Vector2i.read(this);
      this.update = DebugSubscription.Update.read(this);
   }

   public void write() {
      Vector2i.write(this, this.chunkPos);
      DebugSubscription.Update.write(this, this.update);
   }

   public void copy(WrapperPlayServerDebugChunkValue wrapper) {
      this.chunkPos = wrapper.chunkPos;
      this.update = wrapper.update;
   }

   public Vector2i getChunkPos() {
      return this.chunkPos;
   }

   public void setChunkPos(Vector2i chunkPos) {
      this.chunkPos = chunkPos;
   }

   public DebugSubscription.Update<?> getUpdate() {
      return this.update;
   }

   public void setUpdate(DebugSubscription.Update<?> update) {
      this.update = update;
   }
}
