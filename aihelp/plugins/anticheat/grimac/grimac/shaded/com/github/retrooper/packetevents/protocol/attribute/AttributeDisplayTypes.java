package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AttributeDisplayTypes {
   private static final VersionedRegistry<AttributeDisplayType<?>> REGISTRY = new VersionedRegistry("attribute_display_type");
   public static final AttributeDisplayType<DefaultAttributeDisplay> DEFAULT = define("default", DefaultAttributeDisplay::read, DefaultAttributeDisplay::write);
   public static final AttributeDisplayType<HiddenAttributeDisplay> HIDDEN = define("hidden", HiddenAttributeDisplay::read, HiddenAttributeDisplay::write);
   public static final AttributeDisplayType<OverrideAttributeDisplay> OVERRIDE = define("override", OverrideAttributeDisplay::read, OverrideAttributeDisplay::write);

   private AttributeDisplayTypes() {
   }

   @ApiStatus.Internal
   public static <T extends AttributeDisplay> AttributeDisplayType<T> define(String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
      return (AttributeDisplayType)REGISTRY.define(name, (data) -> {
         return new StaticAttributeDisplayType(data, reader, writer);
      });
   }

   public static VersionedRegistry<AttributeDisplayType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
