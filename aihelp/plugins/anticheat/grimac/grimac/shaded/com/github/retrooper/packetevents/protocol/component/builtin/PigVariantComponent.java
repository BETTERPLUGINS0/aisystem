package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig.PigVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig.PigVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class PigVariantComponent {
   private PigVariant variant;

   public PigVariantComponent(PigVariant variant) {
      this.variant = variant;
   }

   public static PigVariantComponent read(PacketWrapper<?> wrapper) {
      PigVariant type = (PigVariant)wrapper.readMappedEntity((IRegistry)PigVariants.getRegistry());
      return new PigVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, PigVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public PigVariant getVariant() {
      return this.variant;
   }

   public void setVariant(PigVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof PigVariantComponent)) {
         return false;
      } else {
         PigVariantComponent that = (PigVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
