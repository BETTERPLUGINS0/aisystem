package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class IntegerValue extends ValueType<Integer> {
   public static final IntegerValue INSTANCE = new IntegerValue();

   @Nullable
   public Integer load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Integer value = ConversionUtils.toInteger(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid integer value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Integer value) {
      return value;
   }

   public Integer parse(String input) throws ValueParseException {
      Integer value = ConversionUtils.parseInt(input);
      if (value == null) {
         throw new ValueParseException("Invalid integer value: " + input);
      } else {
         return value;
      }
   }
}
