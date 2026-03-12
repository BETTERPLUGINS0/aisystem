package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticInstrument extends AbstractMappedEntity implements Instrument {
   private final Sound sound;
   private final float useSeconds;
   private final float range;
   private final Component description;

   /** @deprecated */
   @Deprecated
   public StaticInstrument(Sound sound, int useDuration, float range) {
      this(sound, (float)useDuration * 20.0F, range, Component.empty());
   }

   public StaticInstrument(Sound sound, float useSeconds, float range, Component description) {
      this((TypesBuilderData)null, sound, useSeconds, range, description);
   }

   @ApiStatus.Internal
   public StaticInstrument(@Nullable TypesBuilderData data, Sound sound, float useSeconds, float range, Component description) {
      super(data);
      this.sound = sound;
      this.useSeconds = useSeconds;
      this.range = range;
      this.description = description;
   }

   public Instrument copy(@Nullable TypesBuilderData newData) {
      return new StaticInstrument(newData, this.sound, this.useSeconds, this.range, this.description);
   }

   public Sound getSound() {
      return this.sound;
   }

   public float getUseSeconds() {
      return this.useSeconds;
   }

   public float getRange() {
      return this.range;
   }

   public Component getDescription() {
      return this.description;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticInstrument)) {
         return false;
      } else {
         StaticInstrument that = (StaticInstrument)obj;
         if (this.useSeconds != that.useSeconds) {
            return false;
         } else if (Float.compare(that.range, this.range) != 0) {
            return false;
         } else {
            return !this.sound.equals(that.sound) ? false : this.description.equals(that.description);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.sound, this.useSeconds, this.range, this.description});
   }
}
