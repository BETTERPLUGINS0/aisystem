package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ListValue<E> extends ValueType<List<E>> {
   public static final String DEFAULT_LIST_DELIMITER = ",";
   private final ValueType<E> elementValueType;
   private final boolean nullElementsAllowed;

   public ListValue(ValueType<E> elementValueType) {
      this(elementValueType, false);
   }

   public ListValue(ValueType<E> elementValueType, boolean nullElementsAllowed) {
      Validate.notNull(elementValueType, (String)"elementValueType is null!");
      this.elementValueType = elementValueType;
      this.nullElementsAllowed = nullElementsAllowed;
   }

   public final ValueType<E> getElementValueType() {
      return this.elementValueType;
   }

   public final boolean isNullElementsAllowed() {
      return this.nullElementsAllowed;
   }

   public List<E> load(Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else if (!(configValue instanceof List)) {
         throw new ValueLoadException("Expecting a list of values, but got " + configValue.getClass().getName());
      } else {
         List<?> configValues = (List)configValue;
         List<E> values = new ArrayList(configValues.size());
         Iterator var4 = configValues.iterator();

         while(var4.hasNext()) {
            Object configElement = var4.next();
            E value = this.elementValueType.load(configElement);
            if (value == null && !this.nullElementsAllowed) {
               throw new ValueLoadException("List contains null element!");
            }

            values.add(Unsafe.cast(value));
         }

         return values;
      }
   }

   public Object save(List<E> value) {
      if (value == null) {
         return null;
      } else {
         List<Object> configValues = new ArrayList(value.size());
         Iterator var3 = value.iterator();

         while(var3.hasNext()) {
            E element = var3.next();
            configValues.add(this.elementValueType.save(element));
         }

         return configValues;
      }
   }

   public String format(@Nullable List<E> value) {
      return this.format(value, ",");
   }

   public String format(@Nullable List<E> value, String listDelimiter) {
      return value == null ? "null" : (String)value.stream().map((element) -> {
         return this.elementValueType.format(element);
      }).collect(Collectors.joining(listDelimiter, "[", "]"));
   }

   public List<E> parse(String input) throws ValueParseException {
      return this.parseValue(input, ",");
   }

   public List<E> parseValue(String input, String listDelimiter) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      String[] splits = input.split(listDelimiter);
      List<E> values = new ArrayList(splits.length);
      String[] var5 = splits;
      int var6 = splits.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String split = var5[var7];

         assert split != null;

         E element = this.elementValueType.parse(split.trim());
         values.add(element);
      }

      return values;
   }
}
