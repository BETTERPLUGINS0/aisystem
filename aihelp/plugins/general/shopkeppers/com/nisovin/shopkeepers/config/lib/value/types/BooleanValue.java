package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BooleanValue extends ValueType<Boolean> {
   public static final BooleanValue INSTANCE = new BooleanValue();

   public Boolean load(Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Boolean value = ConversionUtils.toBoolean(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid boolean value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Boolean value) {
      return value;
   }

   public Boolean parse(String input) throws ValueParseException {
      Boolean value = ConversionUtils.parseBoolean(input);
      if (value == null) {
         throw new ValueParseException("Invalid boolean value: " + input);
      } else {
         return value;
      }
   }
}
