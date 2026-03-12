package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.RandomWeightedList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BackgroundMusic {
   public static final NbtCodec<BackgroundMusic> CODEC = (new NbtMapCodec<BackgroundMusic>() {
      public BackgroundMusic decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         BiomeEffects.MusicSettings defaultt = (BiomeEffects.MusicSettings)compound.getOrNull("default", BiomeEffects.MusicSettings.CODEC, wrapper);
         BiomeEffects.MusicSettings creative = (BiomeEffects.MusicSettings)compound.getOrNull("creative", BiomeEffects.MusicSettings.CODEC, wrapper);
         BiomeEffects.MusicSettings underwater = (BiomeEffects.MusicSettings)compound.getOrNull("underwater", BiomeEffects.MusicSettings.CODEC, wrapper);
         return new BackgroundMusic(defaultt, creative, underwater);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BackgroundMusic value) throws NbtCodecException {
         if (value.defaultMusic != null) {
            compound.set("default", value.defaultMusic, BiomeEffects.MusicSettings.CODEC, wrapper);
         }

         if (value.creativeMusic != null) {
            compound.set("creative", value.creativeMusic, BiomeEffects.MusicSettings.CODEC, wrapper);
         }

         if (value.underwaterMusic != null) {
            compound.set("underwater", value.underwaterMusic, BiomeEffects.MusicSettings.CODEC, wrapper);
         }

      }
   }).codec();
   public static final BackgroundMusic EMPTY = new BackgroundMusic((BiomeEffects.MusicSettings)null, (BiomeEffects.MusicSettings)null, (BiomeEffects.MusicSettings)null);
   @Nullable
   private final BiomeEffects.MusicSettings defaultMusic;
   @Nullable
   private final BiomeEffects.MusicSettings creativeMusic;
   @Nullable
   private final BiomeEffects.MusicSettings underwaterMusic;

   public BackgroundMusic(@Nullable BiomeEffects.MusicSettings defaultMusic, @Nullable BiomeEffects.MusicSettings creativeMusic, @Nullable BiomeEffects.MusicSettings underwaterMusic) {
      this.defaultMusic = defaultMusic;
      this.creativeMusic = creativeMusic;
      this.underwaterMusic = underwaterMusic;
   }

   @ApiStatus.Internal
   public RandomWeightedList<BiomeEffects.MusicSettings> asList() {
      List<RandomWeightedList.Entry<BiomeEffects.MusicSettings>> list = new ArrayList(3);
      if (this.defaultMusic != null) {
         list.add(new RandomWeightedList.Entry(this.defaultMusic, 0));
      }

      if (this.creativeMusic != null) {
         list.add(new RandomWeightedList.Entry(this.creativeMusic, 0));
      }

      if (this.underwaterMusic != null) {
         list.add(new RandomWeightedList.Entry(this.underwaterMusic, 0));
      }

      return new RandomWeightedList(list);
   }

   @Nullable
   public BiomeEffects.MusicSettings getDefaultMusic() {
      return this.defaultMusic;
   }

   @Nullable
   public BiomeEffects.MusicSettings getCreativeMusic() {
      return this.creativeMusic;
   }

   @Nullable
   public BiomeEffects.MusicSettings getUnderwaterMusic() {
      return this.underwaterMusic;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof BackgroundMusic)) {
         return false;
      } else {
         BackgroundMusic that = (BackgroundMusic)obj;
         if (!Objects.equals(this.defaultMusic, that.defaultMusic)) {
            return false;
         } else {
            return !Objects.equals(this.creativeMusic, that.creativeMusic) ? false : Objects.equals(this.underwaterMusic, that.underwaterMusic);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.defaultMusic, this.creativeMusic, this.underwaterMusic});
   }
}
