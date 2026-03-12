package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TextValue extends ValueType<Text> {
   public static final TextValue INSTANCE = new TextValue();

   @Nullable
   public Text load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         String stringValue = configValue.toString();
         return Text.parse(stringValue);
      }
   }

   @Nullable
   public Object save(@Nullable Text value) {
      return value == null ? null : value.toFormat();
   }

   public String format(@Nullable Text value) {
      return value == null ? "null" : value.toFormat();
   }

   public Text parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      return Text.parse(input);
   }
}
