package ac.grim.grimac.shaded.kyori.option;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.option.value.ValueType;
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
