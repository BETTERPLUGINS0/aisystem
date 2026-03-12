package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ComponentPredicateTypes {
   private static final VersionedRegistry<ComponentPredicateType<?>> REGISTRY = new VersionedRegistry("data_component_predicate_type");
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> DAMAGE = define("damage", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> ENCHANTMENTS = define("enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> STORED_ENCHANTMENTS = define("stored_enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> POTION_CONTENTS = define("potion_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> CUSTOM_DATA = define("custom_data", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> CONTAINER = define("container", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> BUNDLE_CONTENTS = define("bundle_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> FIREWORK_EXPLOSION = define("firework_explosion", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> FIREWORKS = define("fireworks", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> WRITABLE_BOOK_CONTENT = define("writable_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> WRITTEN_BOOK_CONTENT = define("written_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> ATTRIBUTE_MODIFIERS = define("attribute_modifiers", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> TRIM = define("trim", NbtComponentPredicate::read, NbtComponentPredicate::write);
   @ApiStatus.Experimental
   public static final ComponentPredicateType<NbtComponentPredicate> JUKEBOX_PLAYABLE = define("jukebox_playable", NbtComponentPredicate::read, NbtComponentPredicate::write);

   private ComponentPredicateTypes() {
   }

   @ApiStatus.Internal
   public static <T extends IComponentPredicate> ComponentPredicateType<T> define(String key, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
      return (ComponentPredicateType)REGISTRY.define(key, (data) -> {
         return new StaticComponentPredicateType(data, reader, writer);
      });
   }

   public static VersionedRegistry<ComponentPredicateType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
