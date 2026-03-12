package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot.ParrotVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot.ParrotVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ParrotVariantComponent {
   private ParrotVariant variant;

   public ParrotVariantComponent(ParrotVariant variant) {
      this.variant = variant;
   }

   public static ParrotVariantComponent read(PacketWrapper<?> wrapper) {
      ParrotVariant type = (ParrotVariant)wrapper.readMappedEntity((IRegistry)ParrotVariants.getRegistry());
      return new ParrotVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, ParrotVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public ParrotVariant getVariant() {
      return this.variant;
   }

   public void setVariant(ParrotVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ParrotVariantComponent)) {
         return false;
      } else {
         ParrotVariantComponent that = (ParrotVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
