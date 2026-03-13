package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SoundEffect {
   public static final SoundEffect EMPTY = new SoundEffect("");
   private static final Property<String> SOUND_NAME;
   private static final Property<SoundCategory> CATEGORY;
   private static final Property<Float> PITCH;
   private static final Property<Float> VOLUME;
   private static final float MIN_VOLUME = 0.001F;
   public static final DataSerializer<SoundEffect> SERIALIZER;
   @Nullable
   private final Sound sound;
   @Nullable
   private final String soundName;
   @Nullable
   private final SoundCategory category;
   @Nullable
   private final Float pitch;
   @Nullable
   private final Float volume;

   public SoundEffect(Sound sound) {
      this(sound, (String)null, (SoundCategory)null, (Float)null, (Float)null);
   }

   public SoundEffect(String soundName) {
      this((Sound)null, soundName, (SoundCategory)null, (Float)null, (Float)null);
   }

   private SoundEffect(@Nullable Sound sound, @Nullable String soundName, @Nullable SoundCategory category, @Nullable Float pitch, @Nullable Float volume) {
      if (sound != null) {
         Validate.isTrue(soundName == null, "sound and soundName are both non-null");
      } else {
         Validate.notNull(soundName, (String)"sound and soundName are both null");
      }

      this.sound = sound;
      this.soundName = soundName;
      this.category = category;
      this.pitch = pitch;
      this.volume = volume;
   }

   public SoundEffect withCategory(@Nullable SoundCategory category) {
      return new SoundEffect(this.sound, this.soundName, category, this.pitch, this.volume);
   }

   public SoundEffect withPitch(@Nullable Float pitch) {
      return new SoundEffect(this.sound, this.soundName, this.category, pitch, this.volume);
   }

   public SoundEffect withVolume(@Nullable Float volume) {
      return new SoundEffect(this.sound, this.soundName, this.category, this.pitch, volume);
   }

   @Nullable
   public Sound getSound() {
      return this.sound;
   }

   @Nullable
   public String getSoundName() {
      return this.soundName;
   }

   @Nullable
   public SoundCategory getCategory() {
      return this.category;
   }

   public SoundCategory getEffectiveCategory() {
      return this.category != null ? this.category : (SoundCategory)Unsafe.assertNonNull((SoundCategory)CATEGORY.getDefaultValue());
   }

   @Nullable
   public Float getPitch() {
      return this.pitch;
   }

   public float getEffectivePitch() {
      return this.pitch != null ? this.pitch : (Float)Unsafe.assertNonNull((Float)PITCH.getDefaultValue());
   }

   @Nullable
   public Float getVolume() {
      return this.volume;
   }

   public float getEffectiveVolume() {
      return this.volume != null ? this.volume : (Float)Unsafe.assertNonNull((Float)VOLUME.getDefaultValue());
   }

   public boolean isDisabled() {
      return this.soundName != null && this.soundName.isEmpty() || this.volume != null && this.volume <= 0.001F;
   }

   public void play(Location location) {
      World world = LocationUtils.getWorld(location);
      if (!this.isDisabled()) {
         Sound sound = this.sound;
         if (sound != null) {
            world.playSound(location, sound, this.getEffectiveCategory(), this.getEffectiveVolume(), this.getEffectivePitch());
         } else {
            String soundName = (String)Unsafe.assertNonNull(this.soundName);

            assert !soundName.isEmpty();

            world.playSound(location, soundName, this.getEffectiveCategory(), this.getEffectiveVolume(), this.getEffectivePitch());
         }

      }
   }

   public void play(Player player) {
      Validate.notNull(player, (String)"player is null");
      this.play(player, player.getLocation());
   }

   public void play(Player player, Location location) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(location, (String)"location is null");
      if (!this.isDisabled()) {
         Sound sound = this.sound;
         if (sound != null) {
            player.playSound(location, sound, this.getEffectiveCategory(), this.getEffectiveVolume(), this.getEffectivePitch());
         } else {
            String soundName = (String)Unsafe.assertNonNull(this.soundName);

            assert !soundName.isEmpty();

            player.playSound(location, soundName, this.getEffectiveCategory(), this.getEffectiveVolume(), this.getEffectivePitch());
         }

      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SoundEffect [sound=");
      builder.append(this.sound);
      builder.append(", soundName=");
      builder.append(this.soundName);
      builder.append(", category=");
      builder.append(this.category);
      builder.append(", pitch=");
      builder.append(this.pitch);
      builder.append(", volume=");
      builder.append(this.volume);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + Objects.hashCode(this.sound);
      result = 31 * result + Objects.hashCode(this.soundName);
      result = 31 * result + Objects.hashCode(this.category);
      result = 31 * result + Objects.hashCode(this.pitch);
      result = 31 * result + Objects.hashCode(this.volume);
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof SoundEffect)) {
         return false;
      } else {
         SoundEffect other = (SoundEffect)obj;
         if (Objects.equals(this.sound, other.sound)) {
            return false;
         } else if (Objects.equals(this.soundName, other.soundName)) {
            return false;
         } else if (Objects.equals(this.category, other.category)) {
            return false;
         } else if (Objects.equals(this.pitch, other.pitch)) {
            return false;
         } else {
            return !Objects.equals(this.volume, other.volume);
         }
      }
   }

   public Object serialize() {
      return Unsafe.assertNonNull(SERIALIZER.serialize(this));
   }

   static {
      SOUND_NAME = (new BasicProperty()).dataKeyAccessor("sound", StringSerializers.SCALAR).build();
      CATEGORY = (new BasicProperty()).dataKeyAccessor("category", EnumSerializers.lenient(SoundCategory.class)).nullable().defaultValue(SoundCategory.MASTER).build();
      PITCH = (new BasicProperty()).dataKeyAccessor("pitch", NumberSerializers.FLOAT).nullable().defaultValue(1.0F).build();
      VOLUME = (new BasicProperty()).dataKeyAccessor("volume", NumberSerializers.FLOAT).nullable().defaultValue(1.0F).build();
      SERIALIZER = new DataSerializer<SoundEffect>() {
         @Nullable
         public Object serialize(SoundEffect value) {
            Validate.notNull(value, (String)"value is null");
            Sound sound = value.getSound();
            String soundName = value.getSoundName();
            SoundCategory category = value.getCategory();
            Float pitch = value.getPitch();
            Float volume = value.getVolume();
            String serializedSound = sound != null ? RegistryUtils.getKeyOrThrow(sound).toString() : soundName;
            if (category == null && pitch == null && volume == null) {
               return serializedSound;
            } else {
               DataContainer soundEffectData = DataContainer.create();
               soundEffectData.set((DataSaver)SoundEffect.SOUND_NAME, serializedSound);
               soundEffectData.set((DataSaver)SoundEffect.CATEGORY, category);
               soundEffectData.set((DataSaver)SoundEffect.PITCH, pitch);
               soundEffectData.set((DataSaver)SoundEffect.VOLUME, volume);
               return soundEffectData.serialize();
            }
         }

         public SoundEffect deserialize(Object data) throws InvalidDataException {
            Validate.notNull(data, "data is null");
            String soundName = null;
            SoundCategory category = null;
            Float pitch = null;
            Float volume = null;
            if (data instanceof String) {
               soundName = (String)data;
            } else {
               DataContainer soundEffectData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
               soundName = (String)soundEffectData.get((DataLoader)SoundEffect.SOUND_NAME);
               category = (SoundCategory)soundEffectData.get((DataLoader)SoundEffect.CATEGORY);
               pitch = (Float)soundEffectData.get((DataLoader)SoundEffect.PITCH);
               volume = (Float)soundEffectData.get((DataLoader)SoundEffect.VOLUME);
            }

            Registry<Sound> soundRegistry = Compat.getProvider().getRegistry(Sound.class);
            NamespacedKey soundKey = NamespacedKeyUtils.parse(soundName);
            Sound sound = soundKey != null ? (Sound)soundRegistry.get(soundKey) : null;
            if (sound != null) {
               soundName = null;
            }

            return new SoundEffect(sound, soundName, category, pitch, volume);
         }
      };
   }
}
