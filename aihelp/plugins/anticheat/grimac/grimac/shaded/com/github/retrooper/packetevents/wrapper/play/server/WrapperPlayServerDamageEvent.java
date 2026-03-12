package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayServerDamageEvent extends PacketWrapper<WrapperPlayServerDamageEvent> {
   private int entityId;
   private DamageType sourceType;
   private int sourceCauseId;
   private int sourceDirectId;
   @Nullable
   private Vector3d sourcePosition;

   public WrapperPlayServerDamageEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDamageEvent(int entityId, DamageType sourceType, int sourceCauseId, int sourceDirectId, @Nullable Vector3d sourcePosition) {
      super((PacketTypeCommon)PacketType.Play.Server.DAMAGE_EVENT);
      this.entityId = entityId;
      this.sourceType = sourceType;
      this.sourceCauseId = sourceCauseId;
      this.sourceDirectId = sourceDirectId;
      this.sourcePosition = sourcePosition;
   }

   public void read() {
      this.entityId = this.readVarInt();
      this.sourceType = (DamageType)this.readMappedEntity(DamageTypes.getRegistry());
      this.sourceCauseId = this.readVarInt();
      this.sourceDirectId = this.readVarInt();
      this.sourcePosition = (Vector3d)this.readOptional(Vector3d::read);
   }

   public void write() {
      this.writeVarInt(this.entityId);
      this.writeMappedEntity(this.sourceType);
      this.writeVarInt(this.sourceCauseId);
      this.writeVarInt(this.sourceDirectId);
      this.writeOptional(this.sourcePosition, Vector3d::write);
   }

   public void copy(WrapperPlayServerDamageEvent wrapper) {
      this.entityId = wrapper.entityId;
      this.sourceType = wrapper.sourceType;
      this.sourceCauseId = wrapper.sourceCauseId;
      this.sourceDirectId = wrapper.sourceDirectId;
      this.sourcePosition = wrapper.sourcePosition;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public DamageType getSourceType() {
      return this.sourceType;
   }

   public void setSourceType(DamageType sourceType) {
      this.sourceType = sourceType;
   }

   public int getSourceCauseId() {
      return this.sourceCauseId;
   }

   public void setSourceCauseId(int sourceCauseId) {
      this.sourceCauseId = sourceCauseId;
   }

   public int getSourceDirectId() {
      return this.sourceDirectId;
   }

   public void setSourceDirectId(int sourceDirectId) {
      this.sourceDirectId = sourceDirectId;
   }

   @Nullable
   public Vector3d getSourcePosition() {
      return this.sourcePosition;
   }

   public void setSourcePosition(@Nullable Vector3d sourcePosition) {
      this.sourcePosition = sourcePosition;
   }
}
