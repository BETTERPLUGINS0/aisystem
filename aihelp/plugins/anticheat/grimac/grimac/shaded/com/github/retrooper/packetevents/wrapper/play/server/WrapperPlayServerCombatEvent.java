package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.Combat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;

public class WrapperPlayServerCombatEvent extends PacketWrapper<WrapperPlayServerCombatEvent> {
   private Combat combat;
   private int duration;
   private int entityId;
   private int playerId;
   @Nullable
   private Component deathMessage;

   public WrapperPlayServerCombatEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCombatEvent(Combat combat) {
      super((PacketTypeCommon)PacketType.Play.Server.COMBAT_EVENT);
      this.combat = combat;
   }

   public WrapperPlayServerCombatEvent(Combat combat, int duration, int entityId) {
      super((PacketTypeCommon)PacketType.Play.Server.COMBAT_EVENT);
      this.combat = combat;
      this.duration = duration;
      this.entityId = entityId;
   }

   public WrapperPlayServerCombatEvent(Combat combat, int playerId, int entityId, @Nullable Component deathMessage) {
      super((PacketTypeCommon)PacketType.Play.Server.COMBAT_EVENT);
      this.combat = combat;
      this.playerId = playerId;
      this.entityId = entityId;
      this.deathMessage = deathMessage;
   }

   public void read() {
      this.combat = Combat.getById(this.readVarInt());
      switch(this.combat) {
      case END_COMBAT:
         this.duration = this.readVarInt();
         this.entityId = this.readInt();
         break;
      case ENTITY_DEAD:
         this.playerId = this.readVarInt();
         this.entityId = this.readInt();
         this.deathMessage = this.readComponent();
      case ENTER_COMBAT:
      }

   }

   public void write() {
      this.writeVarInt(this.combat.getId());
      switch(this.combat) {
      case END_COMBAT:
         this.writeVarInt(this.duration);
         this.writeInt(this.entityId);
         break;
      case ENTITY_DEAD:
         this.writeVarInt(this.playerId);
         this.writeInt(this.entityId);
         this.writeComponent(this.deathMessage);
      case ENTER_COMBAT:
      }

   }

   public void copy(WrapperPlayServerCombatEvent wrapper) {
      this.combat = wrapper.combat;
      this.duration = wrapper.duration;
      this.entityId = wrapper.entityId;
      this.playerId = wrapper.playerId;
      this.deathMessage = wrapper.deathMessage;
   }

   public Combat getCombat() {
      return this.combat;
   }

   public void setCombat(Combat combat) {
      this.combat = combat;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public int getPlayerId() {
      return this.playerId;
   }

   public void setPlayerId(int playerId) {
      this.playerId = playerId;
   }

   public Optional<Component> getDeathMessage() {
      return Optional.ofNullable(this.deathMessage);
   }

   public void setDeathMessage(@Nullable Component deathMessage) {
      this.deathMessage = deathMessage;
   }
}
