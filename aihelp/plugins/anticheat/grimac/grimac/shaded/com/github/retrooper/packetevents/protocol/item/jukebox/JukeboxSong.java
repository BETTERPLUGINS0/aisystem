package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class JukeboxSong extends AbstractMappedEntity implements IJukeboxSong {
   private Sound sound;
   private Component description;
   private float lengthInSeconds;
   private int comparatorOutput;

   public JukeboxSong(Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
      this((TypesBuilderData)null, sound, description, lengthInSeconds, comparatorOutput);
   }

   @ApiStatus.Internal
   public JukeboxSong(@Nullable TypesBuilderData data, Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
      super(data);
      this.sound = sound;
      this.description = description;
      this.lengthInSeconds = lengthInSeconds;
      this.comparatorOutput = comparatorOutput;
   }

   /** @deprecated */
   @Deprecated
   public static JukeboxSong read(PacketWrapper<?> wrapper) {
      return (JukeboxSong)IJukeboxSong.read(wrapper);
   }

   /** @deprecated */
   @Deprecated
   public static void write(PacketWrapper<?> wrapper, JukeboxSong song) {
      IJukeboxSong.write(wrapper, song);
   }

   public IJukeboxSong copy(@Nullable TypesBuilderData newData) {
      return new JukeboxSong(newData, this.sound, this.description, this.lengthInSeconds, this.comparatorOutput);
   }

   public Sound getSound() {
      return this.sound;
   }

   /** @deprecated */
   @Deprecated
   public void setSound(Sound sound) {
      this.sound = sound;
   }

   public Component getDescription() {
      return this.description;
   }

   /** @deprecated */
   @Deprecated
   public void setDescription(Component description) {
      this.description = description;
   }

   public float getLengthInSeconds() {
      return this.lengthInSeconds;
   }

   /** @deprecated */
   @Deprecated
   public void setLengthInSeconds(float lengthInSeconds) {
      this.lengthInSeconds = lengthInSeconds;
   }

   public int getComparatorOutput() {
      return this.comparatorOutput;
   }

   /** @deprecated */
   @Deprecated
   public void setComparatorOutput(int comparatorOutput) {
      this.comparatorOutput = comparatorOutput;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof JukeboxSong)) {
         return false;
      } else {
         JukeboxSong that = (JukeboxSong)obj;
         if (Float.compare(that.lengthInSeconds, this.lengthInSeconds) != 0) {
            return false;
         } else if (this.comparatorOutput != that.comparatorOutput) {
            return false;
         } else {
            return !this.sound.equals(that.sound) ? false : this.description.equals(that.description);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.sound, this.description, this.lengthInSeconds, this.comparatorOutput});
   }

   public String toString() {
      return "JukeboxSong{sound=" + this.sound + ", description=" + this.description + ", lengthInSeconds=" + this.lengthInSeconds + ", comparatorOutput=" + this.comparatorOutput + '}';
   }
}
