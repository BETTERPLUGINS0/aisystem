package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class AmbientSounds {
   public static final NbtCodec<AmbientSounds> CODEC = (new NbtMapCodec<AmbientSounds>() {
      public AmbientSounds decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         Sound sound = (Sound)compound.getOrNull("loop", Sound.CODEC, wrapper);
         BiomeEffects.MoodSettings mood = (BiomeEffects.MoodSettings)compound.getOrNull("mood", BiomeEffects.MoodSettings.CODEC, wrapper);
         List<BiomeEffects.AdditionsSettings> additions = (List)compound.getOr("additions", BiomeEffects.AdditionsSettings.LIST_CODEC, Collections.emptyList(), wrapper);
         return new AmbientSounds(sound, mood, additions);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, AmbientSounds value) throws NbtCodecException {
         if (value.loop != null) {
            compound.set("loop", value.loop, Sound.CODEC, wrapper);
         }

         if (value.mood != null) {
            compound.set("mood", value.mood, BiomeEffects.MoodSettings.CODEC, wrapper);
         }

         if (!value.additions.isEmpty()) {
            compound.set("additions", value.additions, BiomeEffects.AdditionsSettings.LIST_CODEC, wrapper);
         }

      }
   }).codec();
   public static final AmbientSounds EMPTY = new AmbientSounds((Sound)null, (BiomeEffects.MoodSettings)null, Collections.emptyList());
   @Nullable
   private final Sound loop;
   @Nullable
   private final BiomeEffects.MoodSettings mood;
   private final List<BiomeEffects.AdditionsSettings> additions;

   public AmbientSounds(@Nullable Sound loop, @Nullable BiomeEffects.MoodSettings mood, List<BiomeEffects.AdditionsSettings> additions) {
      this.loop = loop;
      this.mood = mood;
      this.additions = additions;
   }

   @Nullable
   public Sound getLoop() {
      return this.loop;
   }

   @Nullable
   public BiomeEffects.MoodSettings getMood() {
      return this.mood;
   }

   public List<BiomeEffects.AdditionsSettings> getAdditions() {
      return this.additions;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof AmbientSounds)) {
         return false;
      } else {
         AmbientSounds that = (AmbientSounds)obj;
         if (!Objects.equals(this.loop, that.loop)) {
            return false;
         } else {
            return !Objects.equals(this.mood, that.mood) ? false : this.additions.equals(that.additions);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.loop, this.mood, this.additions});
   }
}
