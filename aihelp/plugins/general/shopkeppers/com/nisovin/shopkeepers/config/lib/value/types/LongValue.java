package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LongValue extends ValueType<Long> {
   public static final LongValue INSTANCE = new LongValue();

   @Nullable
   public Long load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Long value = ConversionUtils.toLong(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid long value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Long value) {
      return value;
   }

   public Long parse(String input) throws ValueParseException {
      Long value = ConversionUtils.parseLong(input);
      if (value == null) {
         throw new ValueParseException("Invalid long value: " + input);
      } else {
         return value;
      }
   }
}
