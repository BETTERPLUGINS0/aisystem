package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EnumValue<E extends Enum<E>> extends ValueType<E> {
   private static final StringValue STRING_VALUE = new StringValue();
   private final Class<E> enumType;

   public EnumValue(Class<E> enumType) {
      Validate.notNull(enumType, (String)"enumType is null");
      this.enumType = enumType;
   }

   @Nullable
   public E load(@Nullable Object configValue) throws ValueLoadException {
      String enumValueName = STRING_VALUE.load(configValue);
      if (enumValueName == null) {
         return null;
      } else {
         try {
            return this.parse(enumValueName);
         } catch (ValueParseException var4) {
            throw this.newInvalidEnumValueException(enumValueName, var4);
         }
      }
   }

   protected ValueLoadException newInvalidEnumValueException(String valueName, ValueParseException parseException) {
      return new ValueLoadException(parseException.getMessage(), parseException);
   }

   @Nullable
   public Object save(@Nullable E value) {
      return value == null ? null : value.name();
   }

   public String format(@Nullable E value) {
      return value == null ? "null" : value.name();
   }

   protected String normalize(String input) {
      assert input != null;

      return EnumUtils.normalizeEnumName(input);
   }

   @NonNull
   public E parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      String normalized = this.normalize(input);

      try {
         return Enum.valueOf(this.enumType, normalized);
      } catch (IllegalArgumentException var4) {
         String var10002 = this.enumType.getSimpleName();
         throw new ValueParseException("Unknown " + var10002 + ": " + input);
      }
   }
}
