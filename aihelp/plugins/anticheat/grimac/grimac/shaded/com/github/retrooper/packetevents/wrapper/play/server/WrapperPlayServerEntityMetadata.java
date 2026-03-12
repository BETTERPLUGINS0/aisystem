package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperPlayServerEntityMetadata extends PacketWrapper<WrapperPlayServerEntityMetadata> {
   private int entityID;
   private List<EntityData<?>> entityMetadata;

   public WrapperPlayServerEntityMetadata(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityMetadata(int entityID, List<EntityData<?>> entityMetadata) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_METADATA);
      this.entityID = entityID;
      this.entityMetadata = entityMetadata;
   }

   public WrapperPlayServerEntityMetadata(int entityID, EntityMetadataProvider metadata) {
      this(entityID, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
   }

   public void read() {
      this.entityID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? this.readVarInt() : this.readInt();
      this.entityMetadata = this.readEntityMetadata();
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeVarInt(this.entityID);
      } else {
         this.writeInt(this.entityID);
      }

      this.writeEntityMetadata(this.entityMetadata);
   }

   public void copy(WrapperPlayServerEntityMetadata wrapper) {
      this.entityID = wrapper.entityID;
      this.entityMetadata = wrapper.entityMetadata;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public List<EntityData<?>> getEntityMetadata() {
      return this.entityMetadata;
   }

   public void setEntityMetadata(List<EntityData<?>> entityMetadata) {
      this.entityMetadata = entityMetadata;
   }

   public void setEntityMetadata(EntityMetadataProvider metadata) {
      this.entityMetadata = metadata.entityData(this.serverVersion.toClientVersion());
   }
}
