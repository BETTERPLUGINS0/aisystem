package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.LegacyConvertible;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.LegacyParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerParticle extends PacketWrapper<WrapperPlayServerParticle> {
   private Particle<?> particle;
   private boolean longDistance;
   private Vector3d position;
   private Vector3f offset;
   private float maxSpeed;
   private int particleCount;
   private boolean alwaysShow;

   public WrapperPlayServerParticle(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerParticle(Particle<?> particle, boolean longDistance, Vector3d position, Vector3f offset, float maxSpeed, int particleCount) {
      this(particle, longDistance, position, offset, maxSpeed, particleCount, false);
   }

   public WrapperPlayServerParticle(Particle<?> particle, boolean longDistance, Vector3d position, Vector3f offset, float maxSpeed, int particleCount, boolean alwaysShow) {
      super((PacketTypeCommon)PacketType.Play.Server.PARTICLE);
      this.particle = particle;
      this.longDistance = longDistance;
      this.position = position;
      this.offset = offset;
      this.maxSpeed = maxSpeed;
      this.particleCount = particleCount;
      this.alwaysShow = alwaysShow;
   }

   public void read() {
      int particleTypeId = 0;
      ParticleType<?> particleType = null;
      boolean v1205 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         String particleName = this.readString(64);
         particleType = ParticleTypes.getByName("minecraft:" + particleName);
      } else if (!v1205) {
         particleTypeId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) ? this.readVarInt() : this.readInt();
         particleType = ParticleTypes.getById(this.serverVersion.toClientVersion(), particleTypeId);
      }

      this.longDistance = this.readBoolean();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         this.alwaysShow = this.readBoolean();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
         this.position = new Vector3d(this.readDouble(), this.readDouble(), this.readDouble());
      } else {
         this.position = new Vector3d((double)this.readFloat(), (double)this.readFloat(), (double)this.readFloat());
      }

      this.offset = new Vector3f(this.readFloat(), this.readFloat(), this.readFloat());
      this.maxSpeed = this.readFloat();
      this.particleCount = this.readInt();
      if (v1205) {
         this.particle = Particle.read(this);
      } else {
         Object data;
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            data = particleType.readData(this);
         } else {
            data = ParticleData.emptyData();
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
               data = LegacyParticleData.read(this, particleTypeId);
            }
         }

         this.particle = new Particle(particleType, (ParticleData)data);
      }

   }

   public void write() {
      int id;
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeString(this.particle.getType().getName().getKey(), 64);
      } else if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
         id = this.particle.getType().getId(this.serverVersion.toClientVersion());
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.writeVarInt(id);
         } else {
            this.writeInt(id);
         }
      }

      this.writeBoolean(this.longDistance);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         this.writeBoolean(this.alwaysShow);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
         this.writeDouble(this.position.getX());
         this.writeDouble(this.position.getY());
         this.writeDouble(this.position.getZ());
      } else {
         this.writeFloat((float)this.position.getX());
         this.writeFloat((float)this.position.getY());
         this.writeFloat((float)this.position.getZ());
      }

      this.writeFloat(this.offset.getX());
      this.writeFloat(this.offset.getY());
      this.writeFloat(this.offset.getZ());
      this.writeFloat(this.maxSpeed);
      this.writeInt(this.particleCount);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         Particle.write(this, this.particle);
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.particle.getType().writeData(this, this.particle.getData());
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         id = this.particle.getType().getId(this.serverVersion.toClientVersion());
         LegacyParticleData legacyData = this.particle.getData() instanceof LegacyConvertible ? ((LegacyConvertible)this.particle.getData()).toLegacy(this.serverVersion.toClientVersion()) : LegacyParticleData.nullValue(id);
         LegacyParticleData.write(this, id, legacyData);
      }

   }

   public void copy(WrapperPlayServerParticle wrapper) {
      this.particle = wrapper.particle;
      this.longDistance = wrapper.longDistance;
      this.position = wrapper.position;
      this.offset = wrapper.offset;
      this.maxSpeed = wrapper.maxSpeed;
      this.particleCount = wrapper.particleCount;
      this.alwaysShow = wrapper.alwaysShow;
   }

   public Particle<?> getParticle() {
      return this.particle;
   }

   public void setParticle(Particle<?> particle) {
      this.particle = particle;
   }

   public boolean isLongDistance() {
      return this.longDistance;
   }

   public void setLongDistance(boolean longDistance) {
      this.longDistance = longDistance;
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public Vector3f getOffset() {
      return this.offset;
   }

   public void setOffset(Vector3f offset) {
      this.offset = offset;
   }

   public float getMaxSpeed() {
      return this.maxSpeed;
   }

   public void setMaxSpeed(float maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   public int getParticleCount() {
      return this.particleCount;
   }

   public void setParticleCount(int particleCount) {
      this.particleCount = particleCount;
   }

   public boolean isAlwaysShow() {
      return this.alwaysShow;
   }

   public void setAlwaysShow(boolean alwaysShow) {
      this.alwaysShow = alwaysShow;
   }
}
