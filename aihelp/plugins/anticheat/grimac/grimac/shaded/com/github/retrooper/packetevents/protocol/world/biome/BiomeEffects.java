package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.AmbientSounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.BackgroundMusic;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.RandomWeightedList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BiomeEffects {
   private static final Color FALLBACK_FOG_COLOR = new Color(12638463);
   private static final Color FALLBACK_WATER_FOG_COLOR = new Color(329011);
   private static final Color FALLBACK_SKY_COLOR = new Color(7842047);
   private static final float FALLBACK_MUSIC_VOLUME = 1.0F;
   public static final NbtCodec<BiomeEffects> CODEC = codecWithAttributes((EnvironmentAttributeMap)null);
   @ApiStatus.Obsolete
   private final Color fogColor;
   private final Color waterColor;
   @ApiStatus.Obsolete
   private final Color waterFogColor;
   @ApiStatus.Obsolete
   private final Color skyColor;
   @Nullable
   private final Color foliageColor;
   @Nullable
   private final Color dryFoliageColor;
   @Nullable
   private final Color grassColor;
   private final BiomeEffects.GrassColorModifier grassColorModifier;
   @ApiStatus.Obsolete
   @Nullable
   private final BiomeEffects.ParticleSettings particle;
   @ApiStatus.Obsolete
   @Nullable
   private final Sound ambientSound;
   @ApiStatus.Obsolete
   @Nullable
   private final BiomeEffects.MoodSettings moodSound;
   @ApiStatus.Obsolete
   @Nullable
   private final BiomeEffects.AdditionsSettings additionsSound;
   @ApiStatus.Obsolete
   private final RandomWeightedList<BiomeEffects.MusicSettings> music;
   @ApiStatus.Obsolete
   private final float musicVolume;

   public static NbtCodec<BiomeEffects> codecWithAttributes(@Nullable EnvironmentAttributeMap attributes) {
      return (new NbtMapCodec<BiomeEffects>() {
         public BiomeEffects decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Color waterColor = (Color)compound.getOrThrow("water_color", NbtCodecs.RGB_COLOR, wrapper);
            Color foliageColor = (Color)compound.getOrNull("foliage_color", NbtCodecs.RGB_COLOR, wrapper);
            Color grassColor = (Color)compound.getOrNull("grass_color", NbtCodecs.RGB_COLOR, wrapper);
            BiomeEffects.GrassColorModifier grassColorModifier = (BiomeEffects.GrassColorModifier)compound.getOr("grass_color_modifier", BiomeEffects.GrassColorModifier.CODEC, BiomeEffects.GrassColorModifier.NONE, wrapper);
            Color dryFoliageColor = null;
            Color fogColor = null;
            Color waterFogColor = null;
            Color skyColor = null;
            BiomeEffects.ParticleSettings particle = null;
            Sound ambientSound = null;
            BiomeEffects.MoodSettings moodSound = null;
            BiomeEffects.AdditionsSettings additionsSound = null;
            float musicVolume;
            RandomWeightedList music;
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
               dryFoliageColor = (Color)compound.getOrNull("dry_foliage_color", NbtCodecs.RGB_COLOR, wrapper);
               if (attributes != null) {
                  fogColor = (Color)attributes.getOrDefault(EnvironmentAttributes.VISUAL_FOG_COLOR);
                  waterFogColor = (Color)attributes.getOrDefault(EnvironmentAttributes.VISUAL_WATER_FOG_COLOR);
                  skyColor = (Color)attributes.getOrDefault(EnvironmentAttributes.VISUAL_SKY_COLOR);
                  List<BiomeEffects.ParticleSettings> particles = (List)attributes.getOrDefault(EnvironmentAttributes.VISUAL_AMBIENT_PARTICLES);
                  particle = particles.isEmpty() ? null : (BiomeEffects.ParticleSettings)particles.get(0);
                  AmbientSounds ambientSounds = (AmbientSounds)attributes.getOrDefault(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS);
                  ambientSound = ambientSounds.getLoop();
                  moodSound = ambientSounds.getMood();
                  additionsSound = ambientSounds.getAdditions().isEmpty() ? null : (BiomeEffects.AdditionsSettings)ambientSounds.getAdditions().get(0);
                  musicVolume = (Float)attributes.getOrDefault(EnvironmentAttributes.AUDIO_MUSIC_VOLUME);
                  music = ((BackgroundMusic)attributes.getOrDefault(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC)).asList();
               } else {
                  musicVolume = 1.0F;
                  music = new RandomWeightedList();
               }
            } else {
               fogColor = (Color)compound.getOrThrow("fog_color", NbtCodecs.RGB_COLOR, wrapper);
               waterFogColor = (Color)compound.getOrThrow("water_fog_color", NbtCodecs.RGB_COLOR, wrapper);
               skyColor = (Color)compound.getOrThrow("sky_color", NbtCodecs.RGB_COLOR, wrapper);
               particle = (BiomeEffects.ParticleSettings)compound.getOrNull("particle", BiomeEffects.ParticleSettings.CODEC, wrapper);
               ambientSound = (Sound)compound.getOrNull("ambient_sound", Sound.CODEC, wrapper);
               moodSound = (BiomeEffects.MoodSettings)compound.getOrNull("mood_sound", BiomeEffects.MoodSettings.CODEC, wrapper);
               additionsSound = (BiomeEffects.AdditionsSettings)compound.getOrNull("additions_sound", BiomeEffects.AdditionsSettings.CODEC, wrapper);
               if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                  musicVolume = (Float)compound.getOr("music_volume", NbtCodecs.FLOAT, 1.0F, wrapper);
                  music = (RandomWeightedList)compound.getOrSupply("music", BiomeEffects.MusicSettings.LIST_CODEC, RandomWeightedList::new, wrapper);
               } else {
                  BiomeEffects.MusicSettings entry = (BiomeEffects.MusicSettings)compound.getOrNull("music", BiomeEffects.MusicSettings.CODEC, wrapper);
                  music = entry != null ? new RandomWeightedList(entry, 1) : new RandomWeightedList();
                  musicVolume = 1.0F;
               }
            }

            if (fogColor == null) {
               fogColor = BiomeEffects.FALLBACK_FOG_COLOR;
            }

            if (waterFogColor == null) {
               waterFogColor = BiomeEffects.FALLBACK_WATER_FOG_COLOR;
            }

            if (skyColor == null) {
               skyColor = BiomeEffects.FALLBACK_SKY_COLOR;
            }

            return new BiomeEffects(fogColor, waterColor, waterFogColor, skyColor, foliageColor, dryFoliageColor, grassColor, grassColorModifier, particle, ambientSound, moodSound, additionsSound, music, musicVolume);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects value) throws NbtCodecException {
            compound.set("water_color", value.waterColor, NbtCodecs.RGB_COLOR, wrapper);
            if (value.foliageColor != null) {
               compound.set("foliage_color", value.foliageColor, NbtCodecs.RGB_COLOR, wrapper);
            }

            if (value.grassColor != null) {
               compound.set("grass_color", value.grassColor, NbtCodecs.RGB_COLOR, wrapper);
            }

            if (value.grassColorModifier != BiomeEffects.GrassColorModifier.NONE) {
               compound.set("grass_color_modifier", value.grassColorModifier, BiomeEffects.GrassColorModifier.CODEC, wrapper);
            }

            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
               if (value.dryFoliageColor != null) {
                  compound.set("dry_foliage_color", value.dryFoliageColor, NbtCodecs.RGB_COLOR, wrapper);
               }
            } else {
               compound.set("fog_color", value.fogColor, NbtCodecs.RGB_COLOR, wrapper);
               compound.set("water_fog_color", value.waterFogColor, NbtCodecs.RGB_COLOR, wrapper);
               compound.set("sky_color", value.skyColor, NbtCodecs.RGB_COLOR, wrapper);
               if (value.particle != null) {
                  compound.set("particle", value.particle, BiomeEffects.ParticleSettings.CODEC, wrapper);
               }

               if (value.ambientSound != null) {
                  compound.set("ambient_sound", value.ambientSound, Sound.CODEC, wrapper);
               }

               if (value.moodSound != null) {
                  compound.set("mood_sound", value.moodSound, BiomeEffects.MoodSettings.CODEC, wrapper);
               }

               if (value.additionsSound != null) {
                  compound.set("additions_sound", value.additionsSound, BiomeEffects.AdditionsSettings.CODEC, wrapper);
               }

               if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                  compound.set("music_volume", value.musicVolume, NbtCodecs.FLOAT, wrapper);
                  compound.set("music", value.music, BiomeEffects.MusicSettings.LIST_CODEC, wrapper);
               } else if (!value.music.isEmpty()) {
                  RandomWeightedList.Entry<BiomeEffects.MusicSettings> entry = (RandomWeightedList.Entry)value.music.getEntries().get(0);
                  compound.set("music", (BiomeEffects.MusicSettings)entry.getData(), BiomeEffects.MusicSettings.CODEC, wrapper);
               }
            }

         }
      }).codec();
   }

   @ApiStatus.Obsolete
   public BiomeEffects(int fogColor, int waterColor, int waterFogColor, int skyColor, OptionalInt foliageColor, OptionalInt grassColor, BiomeEffects.GrassColorModifier grassColorModifier, Optional<BiomeEffects.ParticleSettings> particle, Optional<Sound> ambientSound, Optional<BiomeEffects.MoodSettings> moodSound, Optional<BiomeEffects.AdditionsSettings> additionsSound, Optional<BiomeEffects.MusicSettings> music) {
      this(fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor, grassColorModifier, particle, ambientSound, moodSound, additionsSound, (RandomWeightedList)music.map((musicSettings) -> {
         return new RandomWeightedList(musicSettings, 1);
      }).orElseGet(RandomWeightedList::new), 1.0F);
   }

   @ApiStatus.Obsolete
   public BiomeEffects(int fogColor, int waterColor, int waterFogColor, int skyColor, OptionalInt foliageColor, OptionalInt grassColor, BiomeEffects.GrassColorModifier grassColorModifier, Optional<BiomeEffects.ParticleSettings> particle, Optional<Sound> ambientSound, Optional<BiomeEffects.MoodSettings> moodSound, Optional<BiomeEffects.AdditionsSettings> additionsSound, RandomWeightedList<BiomeEffects.MusicSettings> music, float musicVolume) {
      this(new Color(fogColor), new Color(waterColor), new Color(waterFogColor), new Color(skyColor), foliageColor.isPresent() ? new Color(foliageColor.getAsInt()) : null, (Color)null, grassColor.isPresent() ? new Color(grassColor.getAsInt()) : null, grassColorModifier, (BiomeEffects.ParticleSettings)particle.orElse((Object)null), (Sound)ambientSound.orElse((Object)null), (BiomeEffects.MoodSettings)moodSound.orElse((Object)null), (BiomeEffects.AdditionsSettings)additionsSound.orElse((Object)null), music, musicVolume);
   }

   public BiomeEffects(Color waterColor, @Nullable Color foliageColor, @Nullable Color dryFoliageColor, @Nullable Color grassColor, BiomeEffects.GrassColorModifier grassColorModifier) {
      this(Color.BLACK, waterColor, Color.BLACK, Color.BLACK, foliageColor, dryFoliageColor, grassColor, grassColorModifier, (BiomeEffects.ParticleSettings)null, (Sound)null, (BiomeEffects.MoodSettings)null, (BiomeEffects.AdditionsSettings)null, new RandomWeightedList(), 1.0F);
   }

   public BiomeEffects(Color fogColor, Color waterColor, Color waterFogColor, Color skyColor, @Nullable Color foliageColor, @Nullable Color dryFoliageColor, @Nullable Color grassColor, BiomeEffects.GrassColorModifier grassColorModifier, @Nullable BiomeEffects.ParticleSettings particle, @Nullable Sound ambientSound, @Nullable BiomeEffects.MoodSettings moodSound, @Nullable BiomeEffects.AdditionsSettings additionsSound, RandomWeightedList<BiomeEffects.MusicSettings> music, float musicVolume) {
      this.fogColor = fogColor;
      this.waterColor = waterColor;
      this.waterFogColor = waterFogColor;
      this.skyColor = skyColor;
      this.foliageColor = foliageColor;
      this.dryFoliageColor = dryFoliageColor;
      this.grassColor = grassColor;
      this.grassColorModifier = grassColorModifier;
      this.particle = particle;
      this.ambientSound = ambientSound;
      this.moodSound = moodSound;
      this.additionsSound = additionsSound;
      this.music = music;
      this.musicVolume = musicVolume;
   }

   @ApiStatus.Obsolete
   public int getFogColor() {
      return this.fogColor.asRGB();
   }

   public int getWaterColor() {
      return this.waterColor.asRGB();
   }

   @ApiStatus.Obsolete
   public int getWaterFogColor() {
      return this.waterFogColor.asRGB();
   }

   @ApiStatus.Obsolete
   public int getSkyColor() {
      return this.skyColor.asRGB();
   }

   public OptionalInt getFoliageColor() {
      return this.foliageColor != null ? OptionalInt.of(this.foliageColor.asRGB()) : OptionalInt.empty();
   }

   @Nullable
   public Color getDryFoliageColor() {
      return this.dryFoliageColor;
   }

   public OptionalInt getGrassColor() {
      return this.grassColor != null ? OptionalInt.of(this.grassColor.asRGB()) : OptionalInt.empty();
   }

   public BiomeEffects.GrassColorModifier getGrassColorModifier() {
      return this.grassColorModifier;
   }

   @ApiStatus.Obsolete
   public Optional<BiomeEffects.ParticleSettings> getParticle() {
      return Optional.ofNullable(this.particle);
   }

   @ApiStatus.Obsolete
   public Optional<Sound> getAmbientSound() {
      return Optional.ofNullable(this.ambientSound);
   }

   @ApiStatus.Obsolete
   public Optional<BiomeEffects.MoodSettings> getMoodSound() {
      return Optional.ofNullable(this.moodSound);
   }

   @ApiStatus.Obsolete
   public RandomWeightedList<BiomeEffects.MusicSettings> getMusics() {
      return this.music;
   }

   @ApiStatus.Obsolete
   public Optional<BiomeEffects.MusicSettings> getMusic() {
      List<RandomWeightedList.Entry<BiomeEffects.MusicSettings>> entries = this.music.getEntries();
      return entries.isEmpty() ? Optional.empty() : Optional.of((BiomeEffects.MusicSettings)((RandomWeightedList.Entry)entries.get(0)).getData());
   }

   @ApiStatus.Obsolete
   public Optional<BiomeEffects.AdditionsSettings> getAdditionsSound() {
      return Optional.ofNullable(this.additionsSound);
   }

   public static final class AdditionsSettings {
      public static final NbtCodec<BiomeEffects.AdditionsSettings> CODEC = (new NbtMapCodec<BiomeEffects.AdditionsSettings>() {
         public BiomeEffects.AdditionsSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Sound sound = (Sound)compound.getOrThrow("sound", Sound.CODEC, wrapper);
            double tickChance = (Double)compound.getOrThrow("tick_chance", NbtCodecs.DOUBLE, wrapper);
            return new BiomeEffects.AdditionsSettings(sound, tickChance);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects.AdditionsSettings value) throws NbtCodecException {
            compound.set("sound", value.sound, Sound.CODEC, wrapper);
            compound.set("tick_chance", value.tickChance, NbtCodecs.DOUBLE, wrapper);
         }
      }).codec();
      public static final NbtCodec<List<BiomeEffects.AdditionsSettings>> LIST_CODEC;
      private final Sound sound;
      private final double tickChance;

      public AdditionsSettings(Sound sound, double tickChance) {
         this.sound = sound;
         this.tickChance = tickChance;
      }

      /** @deprecated */
      @Deprecated
      public static BiomeEffects.AdditionsSettings decode(NBT nbt, ClientVersion version) {
         NBTCompound compound = (NBTCompound)nbt;
         Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
         double tickChance = compound.getNumberTagOrThrow("tick_chance").getAsDouble();
         return new BiomeEffects.AdditionsSettings(sound, tickChance);
      }

      /** @deprecated */
      @Deprecated
      public static NBT encode(BiomeEffects.AdditionsSettings settings, ClientVersion version) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("sound", Sound.encode(settings.sound, version));
         compound.setTag("tick_chance", new NBTDouble(settings.tickChance));
         return compound;
      }

      public Sound getSound() {
         return this.sound;
      }

      public double getTickChance() {
         return this.tickChance;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof BiomeEffects.AdditionsSettings)) {
            return false;
         } else {
            BiomeEffects.AdditionsSettings that = (BiomeEffects.AdditionsSettings)obj;
            return Double.compare(that.tickChance, this.tickChance) != 0 ? false : this.sound.equals(that.sound);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.sound, this.tickChance});
      }

      static {
         LIST_CODEC = CODEC.applyList();
      }
   }

   public static final class MoodSettings {
      public static final NbtCodec<BiomeEffects.MoodSettings> CODEC = (new NbtMapCodec<BiomeEffects.MoodSettings>() {
         public BiomeEffects.MoodSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Sound sound = (Sound)compound.getOrThrow("sound", Sound.CODEC, wrapper);
            int tickDelay = (Integer)compound.getOrThrow("tick_delay", NbtCodecs.INT, wrapper);
            int blockSearchExtent = (Integer)compound.getOrThrow("block_search_extent", NbtCodecs.INT, wrapper);
            double soundOffset = (Double)compound.getOrThrow("offset", NbtCodecs.DOUBLE, wrapper);
            return new BiomeEffects.MoodSettings(sound, tickDelay, blockSearchExtent, soundOffset);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects.MoodSettings value) throws NbtCodecException {
            compound.set("sound", value.sound, Sound.CODEC, wrapper);
            compound.set("tick_delay", value.tickDelay, NbtCodecs.INT, wrapper);
            compound.set("block_search_extent", value.blockSearchExtent, NbtCodecs.INT, wrapper);
            compound.set("offset", value.soundOffset, NbtCodecs.DOUBLE, wrapper);
         }
      }).codec();
      private final Sound sound;
      private final int tickDelay;
      private final int blockSearchExtent;
      private final double soundOffset;

      public MoodSettings(Sound sound, int tickDelay, int blockSearchExtent, double soundOffset) {
         this.sound = sound;
         this.tickDelay = tickDelay;
         this.blockSearchExtent = blockSearchExtent;
         this.soundOffset = soundOffset;
      }

      /** @deprecated */
      @Deprecated
      public static BiomeEffects.MoodSettings decode(NBT nbt, ClientVersion version) {
         NBTCompound compound = (NBTCompound)nbt;
         Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
         int tickDelay = compound.getNumberTagOrThrow("tick_delay").getAsInt();
         int blockSearchExtent = compound.getNumberTagOrThrow("block_search_extent").getAsInt();
         double soundOffset = compound.getNumberTagOrThrow("offset").getAsDouble();
         return new BiomeEffects.MoodSettings(sound, tickDelay, blockSearchExtent, soundOffset);
      }

      /** @deprecated */
      @Deprecated
      public static NBT encode(BiomeEffects.MoodSettings settings, ClientVersion version) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("sound", Sound.encode(settings.sound, version));
         compound.setTag("tick_delay", new NBTInt(settings.tickDelay));
         compound.setTag("block_search_extent", new NBTInt(settings.blockSearchExtent));
         compound.setTag("offset", new NBTDouble(settings.soundOffset));
         return compound;
      }

      public Sound getSound() {
         return this.sound;
      }

      public int getTickDelay() {
         return this.tickDelay;
      }

      public int getBlockSearchExtent() {
         return this.blockSearchExtent;
      }

      public double getSoundOffset() {
         return this.soundOffset;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof BiomeEffects.MoodSettings)) {
            return false;
         } else {
            BiomeEffects.MoodSettings that = (BiomeEffects.MoodSettings)obj;
            if (this.tickDelay != that.tickDelay) {
               return false;
            } else if (this.blockSearchExtent != that.blockSearchExtent) {
               return false;
            } else {
               return Double.compare(that.soundOffset, this.soundOffset) != 0 ? false : this.sound.equals(that.sound);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.sound, this.tickDelay, this.blockSearchExtent, this.soundOffset});
      }
   }

   public static final class ParticleSettings {
      public static final NbtCodec<BiomeEffects.ParticleSettings> CODEC = (new NbtMapCodec<BiomeEffects.ParticleSettings>() {
         public BiomeEffects.ParticleSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            String key = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11) ? "particle" : "options";
            Particle<?> particle = (Particle)Particle.CODEC.decode(compound.getTagOrThrow(key), wrapper);
            float probability = compound.getNumberTagOrThrow("probability").getAsFloat();
            return new BiomeEffects.ParticleSettings(particle, probability);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects.ParticleSettings value) throws NbtCodecException {
            String key = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11) ? "particle" : "options";
            compound.set(key, value.particle, Particle.CODEC, wrapper);
            compound.setTag("probability", new NBTFloat(value.probability));
         }
      }).codec();
      private final Particle<?> particle;
      private final float probability;

      public ParticleSettings(Particle<?> particle, float probability) {
         this.particle = particle;
         this.probability = probability;
      }

      /** @deprecated */
      @Deprecated
      public static BiomeEffects.ParticleSettings decode(NBT nbt, ClientVersion version) {
         NBTCompound compound = (NBTCompound)nbt;
         Particle<?> particle = Particle.decode(compound.getTagOrNull("options"), version);
         float probability = compound.getNumberTagOrThrow("probability").getAsFloat();
         return new BiomeEffects.ParticleSettings(particle, probability);
      }

      /** @deprecated */
      @Deprecated
      public static NBT encode(BiomeEffects.ParticleSettings settings, ClientVersion version) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("options", Particle.encode(settings.particle, version));
         compound.setTag("probability", new NBTFloat(settings.probability));
         return compound;
      }

      public Particle<?> getParticle() {
         return this.particle;
      }

      public float getProbability() {
         return this.probability;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof BiomeEffects.ParticleSettings)) {
            return false;
         } else {
            BiomeEffects.ParticleSettings that = (BiomeEffects.ParticleSettings)obj;
            return Float.compare(that.probability, this.probability) != 0 ? false : this.particle.equals(that.particle);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.particle, this.probability});
      }
   }

   public static enum GrassColorModifier implements CodecNameable {
      NONE("none"),
      DARK_FOREST("dark_forest"),
      SWAMP("swamp");

      public static final NbtCodec<BiomeEffects.GrassColorModifier> CODEC = NbtCodecs.forEnum(values());
      public static final Index<String, BiomeEffects.GrassColorModifier> ID_INDEX = Index.create(BiomeEffects.GrassColorModifier.class, BiomeEffects.GrassColorModifier::getId);
      private final String id;

      private GrassColorModifier(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      public String getCodecName() {
         return this.id;
      }

      // $FF: synthetic method
      private static BiomeEffects.GrassColorModifier[] $values() {
         return new BiomeEffects.GrassColorModifier[]{NONE, DARK_FOREST, SWAMP};
      }
   }

   public static final class MusicSettings {
      public static final NbtCodec<BiomeEffects.MusicSettings> CODEC = (new NbtMapCodec<BiomeEffects.MusicSettings>() {
         public BiomeEffects.MusicSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Sound sound = (Sound)compound.getOrThrow("sound", Sound.CODEC, wrapper);
            int minDelay = compound.getNumberTagOrThrow("min_delay").getAsInt();
            int maxDelay = compound.getNumberTagOrThrow("max_delay").getAsInt();
            boolean replaceMusic = compound.getBoolean("replace_current_music");
            return new BiomeEffects.MusicSettings(sound, minDelay, maxDelay, replaceMusic);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects.MusicSettings value) throws NbtCodecException {
            compound.set("sound", value.sound, Sound.CODEC, wrapper);
            compound.setTag("min_delay", new NBTInt(value.minDelay));
            compound.setTag("max_delay", new NBTInt(value.maxDelay));
            compound.setTag("replace_current_music", new NBTByte(value.replaceMusic));
         }
      }).codec();
      public static final NbtCodec<RandomWeightedList<BiomeEffects.MusicSettings>> LIST_CODEC;
      private final Sound sound;
      private final int minDelay;
      private final int maxDelay;
      private final boolean replaceMusic;

      public MusicSettings(Sound sound, int minDelay, int maxDelay, boolean replaceMusic) {
         this.sound = sound;
         this.minDelay = minDelay;
         this.maxDelay = maxDelay;
         this.replaceMusic = replaceMusic;
      }

      /** @deprecated */
      @Deprecated
      public static BiomeEffects.MusicSettings decode(NBT nbt, ClientVersion version) {
         NBTCompound compound = (NBTCompound)nbt;
         Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
         int minDelay = compound.getNumberTagOrThrow("min_delay").getAsInt();
         int maxDelay = compound.getNumberTagOrThrow("max_delay").getAsInt();
         boolean replaceMusic = compound.getBoolean("replace_current_music");
         return new BiomeEffects.MusicSettings(sound, minDelay, maxDelay, replaceMusic);
      }

      /** @deprecated */
      @Deprecated
      public static NBT encode(BiomeEffects.MusicSettings settings, ClientVersion version) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("sound", Sound.encode(settings.sound, version));
         compound.setTag("min_delay", new NBTInt(settings.minDelay));
         compound.setTag("max_delay", new NBTInt(settings.maxDelay));
         compound.setTag("replace_current_music", new NBTByte(settings.replaceMusic));
         return compound;
      }

      public Sound getSound() {
         return this.sound;
      }

      public int getMinDelay() {
         return this.minDelay;
      }

      public int getMaxDelay() {
         return this.maxDelay;
      }

      public boolean isReplaceMusic() {
         return this.replaceMusic;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof BiomeEffects.MusicSettings)) {
            return false;
         } else {
            BiomeEffects.MusicSettings that = (BiomeEffects.MusicSettings)obj;
            if (this.minDelay != that.minDelay) {
               return false;
            } else if (this.maxDelay != that.maxDelay) {
               return false;
            } else {
               return this.replaceMusic != that.replaceMusic ? false : this.sound.equals(that.sound);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.sound, this.minDelay, this.maxDelay, this.replaceMusic});
      }

      static {
         LIST_CODEC = RandomWeightedList.codec(CODEC);
      }
   }
}
