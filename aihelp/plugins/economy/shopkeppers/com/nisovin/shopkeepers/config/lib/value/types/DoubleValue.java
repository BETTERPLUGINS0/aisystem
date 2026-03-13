package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DoubleValue extends ValueType<Double> {
   public static final DoubleValue INSTANCE = new DoubleValue();

   @Nullable
   public Double load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Double value = ConversionUtils.toDouble(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid double value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Double value) {
      return value;
   }

   public Double parse(String input) throws ValueParseException {
      Double value = ConversionUtils.parseDouble(input);
      if (value == null) {
         throw new ValueParseException("Invalid double value: " + input);
      } else {
         return value;
      }
   }
}
