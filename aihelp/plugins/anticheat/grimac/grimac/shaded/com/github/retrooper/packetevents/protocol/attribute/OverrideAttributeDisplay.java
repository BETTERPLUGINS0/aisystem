package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class OverrideAttributeDisplay implements AttributeDisplay {
   private final Component component;

   public OverrideAttributeDisplay(Component component) {
      this.component = component;
   }

   public static OverrideAttributeDisplay read(PacketWrapper<?> wrapper) {
      return new OverrideAttributeDisplay(wrapper.readComponent());
   }

   public static void write(PacketWrapper<?> wrapper, OverrideAttributeDisplay display) {
      wrapper.writeComponent(display.component);
   }

   public AttributeDisplayType<?> getType() {
      return AttributeDisplayTypes.OVERRIDE;
   }

   public Component getComponent() {
      return this.component;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof OverrideAttributeDisplay)) {
         return false;
      } else {
         OverrideAttributeDisplay that = (OverrideAttributeDisplay)obj;
         return this.component.equals(that.component);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.component);
   }
}
