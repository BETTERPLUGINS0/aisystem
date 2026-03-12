package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class WrapperPlayServerSpawnExperienceOrb extends PacketWrapper<WrapperPlayServerSpawnExperienceOrb> {
   private int entityId;
   private double x;
   private double y;
   private double z;
   private short count;

   public WrapperPlayServerSpawnExperienceOrb(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSpawnExperienceOrb(int entityId, double x, double y, double z, short count) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_EXPERIENCE_ORB);
      this.entityId = entityId;
      this.x = x;
      this.y = y;
      this.z = z;
      this.count = count;
   }

   public void read() {
      this.entityId = this.readVarInt();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.x = this.readDouble();
         this.y = this.readDouble();
         this.z = this.readDouble();
      } else {
         this.x = (double)this.readInt() / 32.0D;
         this.y = (double)this.readInt() / 32.0D;
         this.z = (double)this.readInt() / 32.0D;
      }

      this.count = this.readShort();
   }

   public void write() {
      this.writeVarInt(this.entityId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.writeDouble(this.x);
         this.writeDouble(this.y);
         this.writeDouble(this.z);
      } else {
         this.writeInt(MathUtil.floor(this.x * 32.0D));
         this.writeInt(MathUtil.floor(this.y * 32.0D));
         this.writeInt(MathUtil.floor(this.z * 32.0D));
      }

      this.writeShort(this.count);
   }

   public void copy(WrapperPlayServerSpawnExperienceOrb wrapper) {
      this.entityId = wrapper.entityId;
      this.x = wrapper.x;
      this.y = wrapper.y;
      this.z = wrapper.z;
      this.count = wrapper.count;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public short getCount() {
      return this.count;
   }

   public void setCount(short count) {
      this.count = count;
   }
}
