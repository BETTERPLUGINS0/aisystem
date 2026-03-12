package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse.HorseVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse.HorseVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class HorseVariantComponent {
   private HorseVariant variant;

   public HorseVariantComponent(HorseVariant variant) {
      this.variant = variant;
   }

   public static HorseVariantComponent read(PacketWrapper<?> wrapper) {
      HorseVariant variant = (HorseVariant)wrapper.readMappedEntity((IRegistry)HorseVariants.getRegistry());
      return new HorseVariantComponent(variant);
   }

   public static void write(PacketWrapper<?> wrapper, HorseVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public HorseVariant getVariant() {
      return this.variant;
   }

   public void setVariant(HorseVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof HorseVariantComponent)) {
         return false;
      } else {
         HorseVariantComponent that = (HorseVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
