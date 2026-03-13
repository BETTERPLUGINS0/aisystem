package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.TypePattern;
import com.nisovin.shopkeepers.config.lib.value.TypePatterns;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.config.lib.value.ValueTypeProvider;
import com.nisovin.shopkeepers.config.lib.value.ValueTypeProviders;
import java.lang.reflect.Type;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ColoredStringListValue extends ListValue<String> {
   public static final ColoredStringListValue INSTANCE = new ColoredStringListValue();
   public static final TypePattern TYPE_PATTERN = TypePatterns.parameterized(List.class, String.class);
   public static final ValueTypeProvider PROVIDER;

   public ColoredStringListValue() {
      super(ColoredStringValue.INSTANCE);
   }

   static {
      PROVIDER = ValueTypeProviders.forTypePattern(TYPE_PATTERN, (type) -> {
         return INSTANCE;
      });
   }

   public static final class Provider implements ValueTypeProvider {
      @Nullable
      public ValueType<?> get(Type type) {
         return ColoredStringListValue.PROVIDER.get(type);
      }
   }
}
