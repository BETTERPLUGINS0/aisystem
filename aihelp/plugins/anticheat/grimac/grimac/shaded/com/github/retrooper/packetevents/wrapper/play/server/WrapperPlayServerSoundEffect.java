package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.StaticSound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class WrapperPlayServerSoundEffect extends PacketWrapper<WrapperPlayServerSoundEffect> {
   private Sound sound;
   private SoundCategory soundCategory;
   private Vector3i effectPosition;
   private float volume;
   private float pitch;
   private long seed;

   public WrapperPlayServerSoundEffect(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSoundEffect(int soundId, SoundCategory soundCategory, Vector3i effectPosition, float volume, float pitch) {
      this(soundId, soundCategory, effectPosition, volume, pitch, ThreadLocalRandom.current().nextLong());
   }

   public WrapperPlayServerSoundEffect(int soundId, SoundCategory soundCategory, Vector3i effectPosition, float volume, float pitch, long seed) {
      this(Sounds.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), soundId), soundCategory, effectPosition, volume, pitch, seed);
   }

   public WrapperPlayServerSoundEffect(Sound sound, SoundCategory soundCategory, Vector3i effectPosition, float volume, float pitch) {
      this(sound, soundCategory, effectPosition, volume, pitch, ThreadLocalRandom.current().nextLong());
   }

   public WrapperPlayServerSoundEffect(Sound sound, SoundCategory soundCategory, Vector3i effectPosition, float volume, float pitch, long seed) {
      super((PacketTypeCommon)PacketType.Play.Server.SOUND_EFFECT);
      this.sound = sound;
      this.soundCategory = soundCategory;
      this.effectPosition = effectPosition;
      this.volume = volume;
      this.pitch = pitch;
      this.seed = seed;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         this.sound = Sound.read(this);
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.sound = Sounds.getById(this.serverVersion.toClientVersion(), this.readVarInt());
      } else {
         ResourceLocation soundName = this.readIdentifier();
         Sound sound = Sounds.getByName(soundName.toString());
         this.sound = (Sound)(sound == null ? new StaticSound(soundName, (Float)null) : sound);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.soundCategory = SoundCategory.fromId(this.readVarInt());
      }

      this.effectPosition = new Vector3i(this.readInt(), this.readInt(), this.readInt());
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
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.writeVarInt(this.sound.getId(this.serverVersion.toClientVersion()));
      } else {
         this.writeString(this.sound.getSoundId().getKey());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.writeVarInt(this.soundCategory.ordinal());
      }

      this.writeInt(this.effectPosition.x);
      this.writeInt(this.effectPosition.y);
      this.writeInt(this.effectPosition.z);
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

   public void copy(WrapperPlayServerSoundEffect wrapper) {
      this.sound = wrapper.sound;
      this.soundCategory = wrapper.soundCategory;
      this.effectPosition = wrapper.effectPosition;
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

   @Nullable
   public SoundCategory getSoundCategory() {
      return this.soundCategory;
   }

   public void setSoundCategory(SoundCategory soundCategory) {
      this.soundCategory = soundCategory;
   }

   public Vector3i getEffectPosition() {
      return this.effectPosition;
   }

   public void setEffectPosition(Vector3i effectPosition) {
      this.effectPosition = effectPosition;
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
