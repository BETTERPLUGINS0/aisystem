package com.nisovin.shopkeepers.config.lib.value;

import com.nisovin.shopkeepers.util.java.Validate;
import java.lang.reflect.Type;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ValueTypeProviders {
   public static ValueTypeProvider forTypePattern(final TypePattern typePattern, final Function<Type, ValueType<?>> valueTypeProvider) {
      Validate.notNull(typePattern, (String)"typePattern is null");
      Validate.notNull(valueTypeProvider, (String)"valueTypeProvider is null");
      return new ValueTypeProvider() {
         @Nullable
         public ValueType<?> get(Type type) {
            return typePattern.matches(type) ? (ValueType)valueTypeProvider.apply(type) : null;
         }
      };
   }

   private ValueTypeProviders() {
   }
}
