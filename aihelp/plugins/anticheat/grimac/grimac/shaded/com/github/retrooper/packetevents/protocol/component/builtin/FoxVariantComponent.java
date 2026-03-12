package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox.FoxVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox.FoxVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class FoxVariantComponent {
   private FoxVariant variant;

   public FoxVariantComponent(FoxVariant variant) {
      this.variant = variant;
   }

   public static FoxVariantComponent read(PacketWrapper<?> wrapper) {
      FoxVariant type = (FoxVariant)wrapper.readMappedEntity((IRegistry)FoxVariants.getRegistry());
      return new FoxVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, FoxVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public FoxVariant getVariant() {
      return this.variant;
   }

   public void setVariant(FoxVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof FoxVariantComponent)) {
         return false;
      } else {
         FoxVariantComponent that = (FoxVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
