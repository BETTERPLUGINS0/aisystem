package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;

public class WrapperPlayServerEndCombatEvent extends PacketWrapper<WrapperPlayServerEndCombatEvent> {
   private int duration;
   private Integer entityId;

   public WrapperPlayServerEndCombatEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEndCombatEvent(int duration, @Nullable Integer entityId) {
      super((PacketTypeCommon)PacketType.Play.Server.END_COMBAT_EVENT);
      this.duration = duration;
      this.entityId = entityId;
   }

   public void read() {
      this.duration = this.readVarInt();
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
         this.entityId = this.readInt();
      }

   }

   public void write() {
      this.writeVarInt(this.duration);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
         int id = this.entityId != null ? this.entityId : 0;
         this.writeInt(id);
      }

   }

   public void copy(WrapperPlayServerEndCombatEvent wrapper) {
      this.duration = wrapper.duration;
      this.entityId = wrapper.entityId;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public Optional<Integer> getEntityId() {
      return Optional.ofNullable(this.entityId);
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }
}
