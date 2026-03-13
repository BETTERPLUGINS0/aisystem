package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StringValue extends ValueType<String> {
   public static final StringValue INSTANCE = new StringValue();

   @Nullable
   public String load(@Nullable Object configValue) throws ValueLoadException {
      return configValue == null ? null : configValue.toString();
   }

   @Nullable
   public Object save(@Nullable String value) {
      return value;
   }

   public String parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      return input;
   }
}
