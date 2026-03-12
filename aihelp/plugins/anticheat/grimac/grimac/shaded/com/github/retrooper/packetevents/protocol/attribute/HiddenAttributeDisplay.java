package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HiddenAttributeDisplay implements AttributeDisplay {
   public static final HiddenAttributeDisplay INSTANCE = new HiddenAttributeDisplay();

   private HiddenAttributeDisplay() {
   }

   public static HiddenAttributeDisplay read(PacketWrapper<?> wrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> wrapper, HiddenAttributeDisplay display) {
   }

   public AttributeDisplayType<?> getType() {
      return AttributeDisplayTypes.HIDDEN;
   }
}
