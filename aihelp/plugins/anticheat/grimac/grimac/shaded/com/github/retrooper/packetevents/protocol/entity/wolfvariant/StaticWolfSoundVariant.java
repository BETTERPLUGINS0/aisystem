package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticWolfSoundVariant extends AbstractMappedEntity implements WolfSoundVariant {
   private final Sound ambientSound;
   private final Sound deathSound;
   private final Sound growlSound;
   private final Sound hurtSound;
   private final Sound pantSound;
   private final Sound whineSound;

   public StaticWolfSoundVariant(Sound ambientSound, Sound deathSound, Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound) {
      this((TypesBuilderData)null, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
   }

   @ApiStatus.Internal
   public StaticWolfSoundVariant(@Nullable TypesBuilderData data, Sound ambientSound, Sound deathSound, Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound) {
      super(data);
      this.ambientSound = ambientSound;
      this.deathSound = deathSound;
      this.growlSound = growlSound;
      this.hurtSound = hurtSound;
      this.pantSound = pantSound;
      this.whineSound = whineSound;
   }

   public WolfSoundVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticWolfSoundVariant(newData, this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound);
   }

   public Sound getAmbientSound() {
      return this.ambientSound;
   }

   public Sound getDeathSound() {
      return this.deathSound;
   }

   public Sound getGrowlSound() {
      return this.growlSound;
   }

   public Sound getHurtSound() {
      return this.hurtSound;
   }

   public Sound getPantSound() {
      return this.pantSound;
   }

   public Sound getWhineSound() {
      return this.whineSound;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof StaticWolfSoundVariant)) {
         return false;
      } else {
         StaticWolfSoundVariant that = (StaticWolfSoundVariant)obj;
         if (!this.ambientSound.equals(that.ambientSound)) {
            return false;
         } else if (!this.deathSound.equals(that.deathSound)) {
            return false;
         } else if (!this.growlSound.equals(that.growlSound)) {
            return false;
         } else if (!this.hurtSound.equals(that.hurtSound)) {
            return false;
         } else {
            return !this.pantSound.equals(that.pantSound) ? false : this.whineSound.equals(that.whineSound);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound});
   }
}
