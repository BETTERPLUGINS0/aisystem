package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class TypedComponentPredicate<T extends IComponentPredicate> {
   private final ComponentPredicateType<T> type;
   private final T predicate;

   public TypedComponentPredicate(ComponentPredicateType<T> type, T predicate) {
      this.type = type;
      this.predicate = predicate;
   }

   public static TypedComponentPredicate<?> read(PacketWrapper<?> wrapper) {
      ComponentPredicateType<?> type = (ComponentPredicateType)wrapper.readMappedEntity((IRegistry)ComponentPredicateTypes.getRegistry());
      IComponentPredicate predicate = type.read(wrapper);
      return new TypedComponentPredicate(type, predicate);
   }

   public static <T extends IComponentPredicate> void write(PacketWrapper<?> wrapper, TypedComponentPredicate<T> predicate) {
      wrapper.writeMappedEntity(predicate.type);
      predicate.type.write(wrapper, predicate.predicate);
   }

   public ComponentPredicateType<T> getType() {
      return this.type;
   }

   public T getPredicate() {
      return this.predicate;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof TypedComponentPredicate)) {
         return false;
      } else {
         TypedComponentPredicate<?> that = (TypedComponentPredicate)obj;
         return !this.type.equals(that.type) ? false : this.predicate.equals(that.predicate);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.predicate});
   }
}
