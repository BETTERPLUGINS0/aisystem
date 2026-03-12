package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public final class ComponentValue<T> {
   private final ComponentType<T> type;
   private final T value;

   public ComponentValue(ComponentType<T> type, T value) {
      this.type = type;
      this.value = value;
   }

   public static ComponentValue<?> read(PacketWrapper<?> wrapper) {
      ComponentType<?> type = (ComponentType)wrapper.readMappedEntity(ComponentTypes::getById);
      return read0(wrapper, type);
   }

   private static <T> ComponentValue<T> read0(PacketWrapper<?> wrapper, ComponentType<T> type) {
      return new ComponentValue(type, type.read(wrapper));
   }

   public static <T> void write(PacketWrapper<?> wrapper, ComponentValue<T> value) {
      wrapper.writeMappedEntity(value.type);
      value.type.write(wrapper, value.value);
   }

   public ComponentType<T> getType() {
      return this.type;
   }

   public T getValue() {
      return this.value;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ComponentValue)) {
         return false;
      } else {
         ComponentValue<?> that = (ComponentValue)obj;
         return !this.type.equals(that.type) ? false : this.value.equals(that.value);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.value});
   }

   public String toString() {
      return "ComponentValue{type=" + this.type + ", value=" + this.value + '}';
   }
}
