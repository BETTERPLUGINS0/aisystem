package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class WolfSoundVariantComponent {
   private WolfSoundVariant soundVariant;

   public WolfSoundVariantComponent(WolfSoundVariant soundVariant) {
      this.soundVariant = soundVariant;
   }

   public static WolfSoundVariantComponent read(PacketWrapper<?> wrapper) {
      WolfSoundVariant type = (WolfSoundVariant)wrapper.readMappedEntity((IRegistry)WolfSoundVariants.getRegistry());
      return new WolfSoundVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, WolfSoundVariantComponent component) {
      wrapper.writeMappedEntity(component.soundVariant);
   }

   public WolfSoundVariant getSoundVariant() {
      return this.soundVariant;
   }

   public void setSoundVariant(WolfSoundVariant soundVariant) {
      this.soundVariant = soundVariant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof WolfSoundVariantComponent)) {
         return false;
      } else {
         WolfSoundVariantComponent that = (WolfSoundVariantComponent)obj;
         return this.soundVariant.equals(that.soundVariant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.soundVariant);
   }
}
