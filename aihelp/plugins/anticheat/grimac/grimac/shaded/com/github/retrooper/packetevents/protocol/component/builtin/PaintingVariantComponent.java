package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting.PaintingVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class PaintingVariantComponent {
   private PaintingVariant variant;

   public PaintingVariantComponent(PaintingVariant variant) {
      this.variant = variant;
   }

   public static PaintingVariantComponent read(PacketWrapper<?> wrapper) {
      PaintingVariant variant = (PaintingVariant)wrapper.readMappedEntity((IRegistry)PaintingVariants.getRegistry());
      return new PaintingVariantComponent(variant);
   }

   public static void write(PacketWrapper<?> wrapper, PaintingVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public PaintingVariant getVariant() {
      return this.variant;
   }

   public void setVariant(PaintingVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof PaintingVariantComponent)) {
         return false;
      } else {
         PaintingVariantComponent that = (PaintingVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
