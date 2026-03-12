package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class FrogVariantComponent {
   private FrogVariant variant;

   public FrogVariantComponent(FrogVariant variant) {
      this.variant = variant;
   }

   public static FrogVariantComponent read(PacketWrapper<?> wrapper) {
      FrogVariant variant = (FrogVariant)wrapper.readMappedEntity((IRegistry)FrogVariants.getRegistry());
      return new FrogVariantComponent(variant);
   }

   public static void write(PacketWrapper<?> wrapper, FrogVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public FrogVariant getVariant() {
      return this.variant;
   }

   public void setVariant(FrogVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof FrogVariantComponent)) {
         return false;
      } else {
         FrogVariantComponent that = (FrogVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
