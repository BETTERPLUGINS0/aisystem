package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityPositionSync extends PacketWrapper<WrapperPlayServerEntityPositionSync> {
   private int id;
   private EntityPositionData values;
   private boolean onGround;

   public WrapperPlayServerEntityPositionSync(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityPositionSync(int id, EntityPositionData values, boolean onGround) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_POSITION_SYNC);
      this.id = id;
      this.values = values;
      this.onGround = onGround;
   }

   public void read() {
      this.id = this.readVarInt();
      this.values = EntityPositionData.read(this);
      this.onGround = this.readBoolean();
   }

   public void write() {
      this.writeVarInt(this.id);
      EntityPositionData.write(this, this.values);
      this.writeBoolean(this.onGround);
   }

   public void copy(WrapperPlayServerEntityPositionSync wrapper) {
      this.id = wrapper.id;
      this.values = wrapper.values;
      this.onGround = wrapper.onGround;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public EntityPositionData getValues() {
      return this.values;
   }

   public void setValues(EntityPositionData values) {
      this.values = values;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}
