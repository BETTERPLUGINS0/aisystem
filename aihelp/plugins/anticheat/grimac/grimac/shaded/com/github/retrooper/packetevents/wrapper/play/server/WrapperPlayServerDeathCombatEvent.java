package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;

public class WrapperPlayServerDeathCombatEvent extends PacketWrapper<WrapperPlayServerDeathCombatEvent> {
   private int playerId;
   private Integer entityId;
   private Component deathMessage;

   public WrapperPlayServerDeathCombatEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDeathCombatEvent(int playerId, @Nullable Integer entityId, Component deathMessage) {
      super((PacketTypeCommon)PacketType.Play.Server.DEATH_COMBAT_EVENT);
      this.playerId = playerId;
      this.entityId = entityId;
      this.deathMessage = deathMessage;
   }

   public void read() {
      this.playerId = this.readVarInt();
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
         this.entityId = this.readInt();
      }

      this.deathMessage = this.readComponent();
   }

   public void write() {
      this.writeVarInt(this.playerId);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
         int id = this.entityId != null ? this.entityId : 0;
         this.writeInt(id);
      }

      this.writeComponent(this.deathMessage);
   }

   public void copy(WrapperPlayServerDeathCombatEvent wrapper) {
      this.playerId = wrapper.playerId;
      this.entityId = wrapper.entityId;
      this.deathMessage = wrapper.deathMessage;
   }

   public int getPlayerId() {
      return this.playerId;
   }

   public void setPlayerId(int playerId) {
      this.playerId = playerId;
   }

   public Optional<Integer> getEntityId() {
      return Optional.ofNullable(this.entityId);
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public Component getDeathMessage() {
      return this.deathMessage;
   }

   public void setDeathMessage(Component deathMessage) {
      this.deathMessage = deathMessage;
   }
}
