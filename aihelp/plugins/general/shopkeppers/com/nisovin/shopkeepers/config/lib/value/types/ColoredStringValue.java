package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ColoredStringValue extends StringValue {
   public static final ColoredStringValue INSTANCE = new ColoredStringValue();

   @Nullable
   public String load(@Nullable Object configValue) throws ValueLoadException {
      String string = super.load(configValue);
      return string == null ? null : TextUtils.colorize(string);
   }

   @Nullable
   public Object save(@Nullable String value) {
      return value == null ? null : TextUtils.decolorize(value);
   }

   public String format(@Nullable String value) {
      return value == null ? "null" : TextUtils.decolorize(value);
   }

   public String parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      return TextUtils.colorize(input);
   }
}
