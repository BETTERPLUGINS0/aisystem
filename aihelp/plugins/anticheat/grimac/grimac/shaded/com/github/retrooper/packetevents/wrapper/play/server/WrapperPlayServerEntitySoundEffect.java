package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.concurrent.ThreadLocalRandom;

public class WrapperPlayServerEntitySoundEffect extends PacketWrapper<WrapperPlayServerEntitySoundEffect> {
   private Sound sound;
   private SoundCategory soundCategory;
   private int entityId;
   private float volume;
   private float pitch;
   private long seed;

   public WrapperPlayServerEntitySoundEffect(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntitySoundEffect(int soundId, SoundCategory soundCategory, int entityId, float volume, float pitch) {
      this(Sounds.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), soundId), soundCategory, entityId, volume, pitch);
   }

   public WrapperPlayServerEntitySoundEffect(Sound sound, SoundCategory soundCategory, int entityId, float volume, float pitch) {
      this(sound, soundCategory, entityId, volume, pitch, ThreadLocalRandom.current().nextLong());
   }

   public WrapperPlayServerEntitySoundEffect(Sound sound, SoundCategory soundCategory, int entityId, float volume, float pitch, long seed) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_SOUND_EFFECT);
      this.sound = sound;
      this.soundCategory = soundCategory;
      this.entityId = entityId;
      this.volume = volume;
      this.pitch = pitch;
      this.seed = seed;
   }

   public void read() {
      this.sound = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3) ? Sound.read(this) : Sounds.getById(this.serverVersion.toClientVersion(), this.readVarInt());
      this.soundCategory = SoundCategory.fromId(this.readVarInt());
      this.entityId = this.readVarInt();
      this.volume = this.readFloat();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
         this.pitch = this.readFloat();
      } else {
         this.pitch = (float)this.readUnsignedByte() / 63.5F;
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.seed = this.readLong();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         Sound.write(this, this.sound);
      } else {
         this.writeVarInt(this.sound.getId(this.serverVersion.toClientVersion()));
      }

      this.writeVarInt(this.soundCategory.ordinal());
      this.writeVarInt(this.entityId);
      this.writeFloat(this.volume);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
         this.writeFloat(this.pitch);
      } else {
         this.writeByte((int)(this.pitch * 63.5F));
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeLong(this.seed);
      }

   }

   public void copy(WrapperPlayServerEntitySoundEffect wrapper) {
      this.sound = wrapper.sound;
      this.soundCategory = wrapper.soundCategory;
      this.entityId = wrapper.entityId;
      this.volume = wrapper.volume;
      this.pitch = wrapper.pitch;
      this.seed = wrapper.seed;
   }

   public Sound getSound() {
      return this.sound;
   }

   public void setSound(Sound sound) {
      this.sound = sound;
   }

   /** @deprecated */
   @Deprecated
   public int getSoundId() {
      return this.getSound().getId(this.serverVersion.toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public void setSoundId(int soundId) {
      this.setSound(Sounds.getById(this.serverVersion.toClientVersion(), soundId));
   }

   public SoundCategory getSoundCategory() {
      return this.soundCategory;
   }

   public void setSoundCategory(SoundCategory soundCategory) {
      this.soundCategory = soundCategory;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public float getVolume() {
      return this.volume;
   }

   public void setVolume(float volume) {
      this.volume = volume;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public long getSeed() {
      return this.seed;
   }

   public void setSeed(long seed) {
      this.seed = seed;
   }
}
