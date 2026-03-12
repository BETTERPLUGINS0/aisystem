package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.value.ValueType;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface Option<V> {
   /** @deprecated */
   @Deprecated
   static Option<Boolean> booleanOption(final String id, final boolean defaultValue) {
      return OptionSchema.globalSchema().booleanOption(id, defaultValue);
   }

   /** @deprecated */
   @Deprecated
   static <E extends Enum<E>> Option<E> enumOption(final String id, final Class<E> enumClazz, final E defaultValue) {
      return OptionSchema.globalSchema().enumOption(id, enumClazz, defaultValue);
   }

   String id();

   /** @deprecated */
   @Deprecated
   default Class<V> type() {
      return this.valueType().type();
   }

   ValueType<V> valueType();

   @Nullable
   V defaultValue();
}
