package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AttributeDisplay {
   static AttributeDisplay read(PacketWrapper<?> wrapper) {
      AttributeDisplayType<?> type = (AttributeDisplayType)wrapper.readMappedEntity((IRegistry)AttributeDisplayTypes.getRegistry());
      return type.read(wrapper);
   }

   static void write(PacketWrapper<?> wrapper, AttributeDisplay display) {
      wrapper.writeMappedEntity(display.getType());
      display.getType().write(wrapper, display);
   }

   AttributeDisplayType<?> getType();
}
