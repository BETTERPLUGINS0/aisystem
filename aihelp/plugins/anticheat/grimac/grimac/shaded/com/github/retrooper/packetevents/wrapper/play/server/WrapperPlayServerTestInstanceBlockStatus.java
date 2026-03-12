package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerTestInstanceBlockStatus extends PacketWrapper<WrapperPlayServerTestInstanceBlockStatus> {
   private Component status;
   @Nullable
   private Vector3i size;

   public WrapperPlayServerTestInstanceBlockStatus(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTestInstanceBlockStatus(Component status, @Nullable Vector3i size) {
      super((PacketTypeCommon)PacketType.Play.Server.TEST_INSTANCE_BLOCK_STATUS);
      this.status = status;
      this.size = size;
   }

   public void read() {
      this.status = this.readComponent();
      this.size = (Vector3i)this.readOptional(Vector3i::read);
   }

   public void write() {
      this.writeComponent(this.status);
      this.writeOptional(this.size, Vector3i::write);
   }

   public void copy(WrapperPlayServerTestInstanceBlockStatus wrapper) {
      this.status = wrapper.status;
      this.size = wrapper.size;
   }

   public Component getStatus() {
      return this.status;
   }

   public void setStatus(Component status) {
      this.status = status;
   }

   @Nullable
   public Vector3i getSize() {
      return this.size;
   }

   public void setSize(@Nullable Vector3i size) {
      this.size = size;
   }
}
