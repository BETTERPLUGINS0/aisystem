package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticSound extends AbstractMappedEntity implements Sound {
   private final ResourceLocation soundId;
   @Nullable
   private final Float range;

   public StaticSound(ResourceLocation soundId, @Nullable Float range) {
      this((TypesBuilderData)null, soundId, range);
   }

   @ApiStatus.Internal
   public StaticSound(@Nullable TypesBuilderData data, ResourceLocation soundId, @Nullable Float range) {
      super(data);
      this.soundId = soundId;
      this.range = range;
   }

   public ResourceLocation getSoundId() {
      return this.soundId;
   }

   @Nullable
   public Float getRange() {
      return this.range;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         StaticSound that = (StaticSound)obj;
         return !this.soundId.equals(that.soundId) ? false : Objects.equals(this.range, that.range);
      } else {
         return false;
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.soundId, this.range});
   }
}
