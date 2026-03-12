package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FloatValue extends ValueType<Float> {
   public static final FloatValue INSTANCE = new FloatValue();

   @Nullable
   public Float load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Float value = ConversionUtils.toFloat(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid float value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Float value) {
      return value;
   }

   public Float parse(String input) throws ValueParseException {
      Float value = ConversionUtils.parseFloat(input);
      if (value == null) {
         throw new ValueParseException("Invalid float value: " + input);
      } else {
         return value;
      }
   }
}
