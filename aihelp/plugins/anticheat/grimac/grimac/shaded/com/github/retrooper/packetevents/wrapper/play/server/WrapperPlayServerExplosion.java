package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.StaticSound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.WeightedList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {
   private Vector3d position;
   private float strength;
   private int blockCount;
   private List<Vector3i> records;
   @Nullable
   private Vector3d knockback;
   @ApiStatus.Obsolete
   private Particle<?> smallParticle;
   private Particle<?> particle;
   @ApiStatus.Obsolete
   private WrapperPlayServerExplosion.BlockInteraction blockInteraction;
   private Sound explosionSound;
   private WeightedList<WrapperPlayServerExplosion.ParticleInfo> blockParticles;

   public WrapperPlayServerExplosion(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion) {
      this(position, strength, records, (Vector3f)playerMotion, new Particle(ParticleTypes.EXPLOSION), new Particle(ParticleTypes.EXPLOSION_EMITTER), WrapperPlayServerExplosion.BlockInteraction.DESTROY_BLOCKS, (ResourceLocation)(new ResourceLocation("minecraft:entity.generic.explode")), (Float)null);
   }

   public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion, Particle<?> smallExplosionParticles, Particle<?> largeExplosionParticles, WrapperPlayServerExplosion.BlockInteraction blockInteraction, ResourceLocation explosionSoundKey, @Nullable Float explosionSoundRange) {
      this(position, strength, records, (Vector3f)playerMotion, smallExplosionParticles, largeExplosionParticles, blockInteraction, new StaticSound(explosionSoundKey, explosionSoundRange));
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion, Particle<?> smallParticle, Particle<?> particle, WrapperPlayServerExplosion.BlockInteraction blockInteraction, Sound explosionSound) {
      this(position, strength, records, new Vector3d((double)playerMotion.x, (double)playerMotion.y, (double)playerMotion.z), smallParticle, particle, blockInteraction, explosionSound);
   }

   public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3d playerMotion, Particle<?> smallParticle, Particle<?> particle, WrapperPlayServerExplosion.BlockInteraction blockInteraction, Sound explosionSound) {
      this(position, strength, records, playerMotion, smallParticle, particle, blockInteraction, explosionSound, new WeightedList());
   }

   public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3d playerMotion, Particle<?> smallParticle, Particle<?> particle, WrapperPlayServerExplosion.BlockInteraction blockInteraction, Sound explosionSound, WeightedList<WrapperPlayServerExplosion.ParticleInfo> blockParticles) {
      super((PacketTypeCommon)PacketType.Play.Server.EXPLOSION);
      this.position = position;
      this.strength = strength;
      this.blockCount = records.size();
      this.records = records;
      this.knockback = playerMotion;
      this.smallParticle = smallParticle;
      this.particle = particle;
      this.blockInteraction = blockInteraction;
      this.explosionSound = explosionSound;
      this.blockParticles = blockParticles;
   }

   public WrapperPlayServerExplosion(Vector3d position, @Nullable Vector3d playerMotion) {
      this(position, playerMotion, new Particle(ParticleTypes.EXPLOSION_EMITTER), Sounds.ENTITY_GENERIC_EXPLODE);
   }

   public WrapperPlayServerExplosion(Vector3d position, @Nullable Vector3d playerMotion, Particle<?> particle, Sound explosionSound) {
      this(position, 0.0F, 0, playerMotion, particle, explosionSound, new WeightedList());
   }

   public WrapperPlayServerExplosion(Vector3d position, float strength, int blockCount, @Nullable Vector3d playerMotion, Particle<?> particle, Sound explosionSound, WeightedList<WrapperPlayServerExplosion.ParticleInfo> blockParticles) {
      super((PacketTypeCommon)PacketType.Play.Server.EXPLOSION);
      this.position = position;
      this.strength = strength;
      this.blockCount = blockCount;
      this.knockback = playerMotion;
      this.particle = particle;
      this.explosionSound = explosionSound;
      this.blockParticles = blockParticles;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         this.position = Vector3d.read(this);
      } else {
         this.position = new Vector3d((double)this.readFloat(), (double)this.readFloat(), (double)this.readFloat());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.strength = this.readFloat();
            this.blockCount = this.readInt();
         }

         this.knockback = (Vector3d)this.readOptional(Vector3d::read);
         this.particle = Particle.read(this);
         this.explosionSound = Sound.read(this);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.blockParticles = WeightedList.read(this, WrapperPlayServerExplosion.ParticleInfo::read);
         }

         this.blockInteraction = WrapperPlayServerExplosion.BlockInteraction.DESTROY_BLOCKS;
      } else {
         this.strength = this.readFloat();
         int recordsLength = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? this.readVarInt() : this.readInt();
         this.records = new ArrayList(recordsLength);
         Vector3i floor = this.toFloor(this.position);

         for(int i = 0; i < recordsLength; ++i) {
            int chunkPosX = this.readByte() + floor.getX();
            int chunkPosY = this.readByte() + floor.getY();
            int chunkPosZ = this.readByte() + floor.getZ();
            this.records.add(new Vector3i(chunkPosX, chunkPosY, chunkPosZ));
         }

         float motX = this.readFloat();
         float motY = this.readFloat();
         float motZ = this.readFloat();
         this.knockback = new Vector3d((double)motX, (double)motY, (double)motZ);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.blockInteraction = WrapperPlayServerExplosion.BlockInteraction.values()[this.readVarInt()];
            this.smallParticle = Particle.read(this);
            this.particle = Particle.read(this);
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
               this.explosionSound = Sound.read(this);
            } else {
               ResourceLocation explosionSoundKey = this.readIdentifier();
               Float explosionSoundRange = (Float)this.readOptional(PacketWrapper::readFloat);
               this.explosionSound = new StaticSound(explosionSoundKey, explosionSoundRange);
            }
         } else {
            this.blockInteraction = WrapperPlayServerExplosion.BlockInteraction.DESTROY_BLOCKS;
            this.explosionSound = Sounds.INTENTIONALLY_EMPTY;
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         Vector3d.write(this, this.position);
      } else {
         this.writeFloat((float)this.position.getX());
         this.writeFloat((float)this.position.getY());
         this.writeFloat((float)this.position.getZ());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.writeFloat(this.strength);
            this.writeInt(this.blockCount);
         }

         this.writeOptional(this.knockback, Vector3d::write);
         Particle.write(this, this.particle);
         Sound.write(this, this.explosionSound);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            WeightedList.write(this, this.blockParticles, WrapperPlayServerExplosion.ParticleInfo::write);
         }
      } else {
         this.writeFloat(this.strength);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.writeVarInt(this.records.size());
         } else {
            this.writeInt(this.records.size());
         }

         Vector3i floor = this.toFloor(this.position);
         Iterator var2 = this.records.iterator();

         while(var2.hasNext()) {
            Vector3i record = (Vector3i)var2.next();
            this.writeByte(record.x - floor.getX());
            this.writeByte(record.y - floor.getY());
            this.writeByte(record.z - floor.getZ());
         }

         this.writeFloat((float)this.knockback.x);
         this.writeFloat((float)this.knockback.y);
         this.writeFloat((float)this.knockback.z);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeVarInt(this.blockInteraction.ordinal());
            Particle.write(this, this.smallParticle);
            Particle.write(this, this.particle);
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
               Sound.write(this, this.explosionSound);
            } else {
               this.writeIdentifier(this.explosionSound.getSoundId());
               this.writeOptional(this.explosionSound.getRange(), PacketWrapper::writeFloat);
            }
         }
      }

   }

   public void copy(WrapperPlayServerExplosion wrapper) {
      this.position = wrapper.position;
      this.strength = wrapper.strength;
      this.blockCount = wrapper.blockCount;
      this.records = wrapper.records;
      this.knockback = wrapper.knockback;
      this.smallParticle = wrapper.smallParticle;
      this.particle = wrapper.particle;
      this.blockInteraction = wrapper.blockInteraction;
      this.explosionSound = wrapper.explosionSound;
      this.blockParticles = wrapper.blockParticles;
   }

   private Vector3i toFloor(Vector3d position) {
      int floorX;
      int floorY;
      int floorZ;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         floorX = (int)Math.floor(position.x);
         floorY = (int)Math.floor(position.y);
         floorZ = (int)Math.floor(position.z);
      } else {
         floorX = (int)position.x;
         floorY = (int)position.y;
         floorZ = (int)position.z;
      }

      return new Vector3i(floorX, floorY, floorZ);
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getStrength() {
      return this.strength;
   }

   public void setStrength(float strength) {
      this.strength = strength;
   }

   public int getBlockCount() {
      return this.serverVersion.isOlderThan(ServerVersion.V_1_21_9) && this.blockCount == 0 ? this.records.size() : this.blockCount;
   }

   public void setBlockCount(int blockCount) {
      this.blockCount = blockCount;
   }

   public List<Vector3i> getRecords() {
      if (this.records == null) {
         this.records = new ArrayList();
      }

      return this.records;
   }

   public void setRecords(List<Vector3i> records) {
      this.records = records;
   }

   @Nullable
   public Vector3d getKnockback() {
      return this.knockback;
   }

   public void setKnockback(@Nullable Vector3d knockback) {
      this.knockback = knockback;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public Vector3f getPlayerMotion() {
      return this.knockback == null ? null : new Vector3f((float)this.knockback.x, (float)this.knockback.y, (float)this.knockback.z);
   }

   /** @deprecated */
   @Deprecated
   public void setPlayerMotion(@Nullable Vector3f playerMotion) {
      this.knockback = playerMotion == null ? null : new Vector3d((double)playerMotion.x, (double)playerMotion.y, (double)playerMotion.z);
   }

   @ApiStatus.Obsolete
   public Particle<?> getSmallExplosionParticles() {
      return this.smallParticle == null ? new Particle(ParticleTypes.EXPLOSION) : this.smallParticle;
   }

   @ApiStatus.Obsolete
   public void setSmallExplosionParticles(Particle<?> smallExplosionParticles) {
      this.smallParticle = smallExplosionParticles;
   }

   public Particle<?> getParticle() {
      return this.particle == null ? new Particle(ParticleTypes.EXPLOSION_EMITTER) : this.particle;
   }

   public void setParticle(Particle<?> particle) {
      this.particle = particle;
   }

   @ApiStatus.Obsolete
   public Particle<?> getLargeExplosionParticles() {
      return this.getParticle();
   }

   @ApiStatus.Obsolete
   public void setLargeExplosionParticles(Particle<?> largeExplosionParticles) {
      this.setParticle(largeExplosionParticles);
   }

   @ApiStatus.Obsolete
   public WrapperPlayServerExplosion.BlockInteraction getBlockInteraction() {
      return this.blockInteraction;
   }

   @ApiStatus.Obsolete
   public void setBlockInteraction(WrapperPlayServerExplosion.BlockInteraction blockInteraction) {
      this.blockInteraction = blockInteraction;
   }

   public ResourceLocation getExplosionSoundKey() {
      return this.explosionSound.getSoundId();
   }

   public void setExplosionSoundKey(ResourceLocation explosionSoundKey) {
      this.explosionSound = new StaticSound(explosionSoundKey, this.explosionSound.getRange());
   }

   @Nullable
   public Float getExplosionSoundRange() {
      return this.explosionSound.getRange();
   }

   public void setExplosionSoundRange(@Nullable Float explosionSoundRange) {
      this.explosionSound = new StaticSound(this.explosionSound.getSoundId(), explosionSoundRange);
   }

   public Sound getExplosionSound() {
      return this.explosionSound;
   }

   public void setExplosionSound(Sound explosionSound) {
      this.explosionSound = explosionSound;
   }

   public WeightedList<WrapperPlayServerExplosion.ParticleInfo> getBlockParticles() {
      return this.blockParticles;
   }

   public void setBlockParticles(WeightedList<WrapperPlayServerExplosion.ParticleInfo> blockParticles) {
      this.blockParticles = blockParticles;
   }

   public static enum BlockInteraction {
      KEEP_BLOCKS,
      DESTROY_BLOCKS,
      DECAY_DESTROYED_BLOCKS,
      TRIGGER_BLOCKS;

      // $FF: synthetic method
      private static WrapperPlayServerExplosion.BlockInteraction[] $values() {
         return new WrapperPlayServerExplosion.BlockInteraction[]{KEEP_BLOCKS, DESTROY_BLOCKS, DECAY_DESTROYED_BLOCKS, TRIGGER_BLOCKS};
      }
   }

   public static final class ParticleInfo {
      private final Particle<?> particle;
      private final float scaling;
      private final float speed;

      public ParticleInfo(Particle<?> particle, float scaling, float speed) {
         this.particle = particle;
         this.scaling = scaling;
         this.speed = speed;
      }

      public static WrapperPlayServerExplosion.ParticleInfo read(PacketWrapper<?> wrapper) {
         Particle<?> particle = Particle.read(wrapper);
         float scaling = wrapper.readFloat();
         float speed = wrapper.readFloat();
         return new WrapperPlayServerExplosion.ParticleInfo(particle, scaling, speed);
      }

      public static void write(PacketWrapper<?> wrapper, WrapperPlayServerExplosion.ParticleInfo info) {
         Particle.write(wrapper, info.particle);
         wrapper.writeFloat(info.scaling);
         wrapper.writeFloat(info.speed);
      }

      public Particle<?> getParticle() {
         return this.particle;
      }

      public float getScaling() {
         return this.scaling;
      }

      public float getSpeed() {
         return this.speed;
      }
   }
}
