package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Trilean;
import java.util.Locale;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TrileanValue extends ValueType<Trilean> {
   public static final TrileanValue INSTANCE = new TrileanValue();

   public Trilean load(Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         Trilean value = ConversionUtils.toTrilean(configValue);
         if (value == null) {
            throw new ValueLoadException("Invalid trilean value: " + String.valueOf(configValue));
         } else {
            return value;
         }
      }
   }

   @Nullable
   public Object save(@Nullable Trilean value) {
      if (value == null) {
         return null;
      } else {
         return value == Trilean.UNDEFINED ? value.name().toLowerCase(Locale.ROOT) : value.toBoolean();
      }
   }

   public Trilean parse(String input) throws ValueParseException {
      Trilean value = ConversionUtils.parseTrilean(input);
      if (value == null) {
         throw new ValueParseException("Invalid trilean value: " + input);
      } else {
         return value;
      }
   }
}
