package com.bergerkiller.bukkit.tc.commands.selector;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.utils.BoundingRange;
import com.bergerkiller.bukkit.tc.utils.QuoteEscapedString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectorCondition {
   private final SelectorCondition.Key key;
   private final String value;

   /** @deprecated */
   @Deprecated
   protected SelectorCondition(String key, String value) {
      this(SelectorCondition.Key.parse(key), value);
   }

   protected SelectorCondition(SelectorCondition.Key key, String value) {
      this.key = key;
      this.value = value;
   }

   public String getKey() {
      return this.key.name();
   }

   public String getKeyPath() {
      return this.key.path();
   }

   public boolean hasKeyPath() {
      return !this.key.path().isEmpty();
   }

   public String getValue() {
      return this.value;
   }

   public boolean matchesAnyText(Collection<String> values) throws SelectorException {
      return values.contains(this.value);
   }

   public boolean matchesAnyText(Stream<String> values) throws SelectorException {
      return values.anyMatch(Predicate.isEqual(this.value));
   }

   public boolean matchesText(String value) throws SelectorException {
      return this.value.equals(value);
   }

   public BoundingRange getBoundingRange() throws SelectorException {
      throw new SelectorException(this.key + " value is not a number");
   }

   public double getDouble() throws SelectorException {
      BoundingRange range = this.getBoundingRange();
      if (range.isZeroLength()) {
         return range.getMin();
      } else {
         throw new SelectorException(this.key + " value is a range, expected a single number");
      }
   }

   public boolean getBoolean() throws SelectorException {
      throw new SelectorException(this.key + " value is not a boolean");
   }

   public boolean matchesNumber(double value) throws SelectorException {
      throw new SelectorException(this.key + " value is not a number");
   }

   public boolean matchesNumber(long value) throws SelectorException {
      throw new SelectorException(this.key + " value is not a number");
   }

   public boolean matchesBoolean(boolean value) throws SelectorException {
      throw new SelectorException(this.key + " value is not a boolean flag");
   }

   public boolean isNumber() {
      return false;
   }

   public boolean isBoolean() {
      return false;
   }

   /** @deprecated */
   public static SelectorCondition parse(String key, String value) {
      return parse(SelectorCondition.Key.parse(key), value);
   }

   public static SelectorCondition parse(SelectorCondition.Key key, String value) {
      if (value.startsWith("!")) {
         SelectorCondition base = parse(key, value.substring(1));
         return new SelectorCondition.SelectorConditionInverted(key, value, base);
      } else {
         int rangeStart = QuoteEscapedString.unquotedIndexOf(value, "..", 0);
         if (rangeStart != -1) {
            int rangeCurrent = QuoteEscapedString.unquotedIndexOf(value, "..", rangeStart + 2);
            if (rangeCurrent == -1) {
               String first = rangeStart > 0 ? value.substring(0, rangeStart).trim() : null;
               String second = rangeStart + 2 < value.length() ? value.substring(rangeStart + 2).trim() : null;
               SelectorCondition.SelectorConditionNumeric min = first != null ? SelectorCondition.SelectorConditionNumeric.tryParse(key, first) : SelectorCondition.SelectorConditionNumeric.RANGE_MIN;
               SelectorCondition.SelectorConditionNumeric max = second != null ? SelectorCondition.SelectorConditionNumeric.tryParse(key, second) : SelectorCondition.SelectorConditionNumeric.RANGE_MAX;
               return (SelectorCondition)(min != null && max != null ? new SelectorCondition.SelectorConditionNumericRange(key, value, min, max) : new SelectorCondition.SelectorConditionAnyOfText(key, value, new SelectorCondition[]{parsePart(key, first), parsePart(key, second)}));
            } else {
               List<SelectorCondition> selectorValues = new ArrayList(5);
               if (rangeStart > 0) {
                  selectorValues.add(parsePart(key, value.substring(0, rangeStart).trim()));
               }

               if (rangeCurrent > rangeStart + 2) {
                  selectorValues.add(parsePart(key, value.substring(rangeStart + 2, rangeCurrent).trim()));
               }

               int rangeNext;
               for(; (rangeNext = QuoteEscapedString.unquotedIndexOf(value, "..", rangeCurrent + 2)) != -1; rangeCurrent = rangeNext) {
                  if (rangeNext > rangeCurrent + 2) {
                     selectorValues.add(parsePart(key, value.substring(rangeCurrent, rangeNext).trim()));
                  }
               }

               if (rangeCurrent + 2 < value.length()) {
                  selectorValues.add(parsePart(key, value.substring(rangeCurrent + 2).trim()));
               }

               return new SelectorCondition.SelectorConditionAnyOfText(key, value, (SelectorCondition[])selectorValues.toArray(new SelectorCondition[0]));
            }
         } else {
            return parsePart(key, value.trim());
         }
      }
   }

   private static SelectorCondition parsePart(SelectorCondition.Key key, String value) {
      QuoteEscapedString unescapedValue = QuoteEscapedString.tryParseQuoted(value);
      value = unescapedValue.getUnescaped();
      if (!unescapedValue.isQuoteEscaped() && ParseUtil.isNumeric(value)) {
         SelectorCondition.SelectorConditionNumeric numeric = SelectorCondition.SelectorConditionNumeric.tryParse(key, value);
         if (numeric != null) {
            return numeric;
         }
      }

      String[] elements = value.split("\\*", -1);
      if (elements.length > 1) {
         boolean firstAny = value.startsWith("*");
         boolean lastAny = value.endsWith("*");
         return new SelectorCondition.SelectorConditionWildcardText(key, value, elements, firstAny, lastAny);
      } else {
         if (!unescapedValue.isQuoteEscaped()) {
            SelectorCondition.SelectorConditionBoolean truthy = SelectorCondition.SelectorConditionBoolean.tryParse(key, value);
            if (truthy != null) {
               return truthy;
            }
         }

         return new SelectorCondition(key, value);
      }
   }

   public static List<SelectorCondition> parseAll(String conditionsString) {
      int separator = QuoteEscapedString.unquotedIndexOf(conditionsString, ",", 0);
      int length = conditionsString.length();
      if (separator == -1) {
         int equals = QuoteEscapedString.unquotedIndexOf(conditionsString, "=", 0);
         if (equals != -1 && equals != 0 && equals != length - 1) {
            SelectorCondition.Key condKey = SelectorCondition.Key.parse(conditionsString.substring(0, equals));
            String condValue = conditionsString.substring(equals + 1);
            return Collections.singletonList(parse(condKey, condValue));
         } else {
            return null;
         }
      } else {
         List<SelectorCondition> conditions = new ArrayList(10);
         int argStart = 0;
         int argEnd = separator;
         boolean valid = true;

         while(true) {
            int equals = QuoteEscapedString.unquotedIndexOf(conditionsString, "=", argStart);
            if (equals == -1 || equals == argStart || equals >= argEnd - 1) {
               valid = false;
               break;
            }

            SelectorCondition.Key condKey = SelectorCondition.Key.parse(conditionsString.substring(argStart, equals));
            String condValue = conditionsString.substring(equals + 1, argEnd);
            conditions.add(parse(condKey, condValue));
            if (argEnd == length) {
               break;
            }

            argStart = argEnd + 1;
            argEnd = QuoteEscapedString.unquotedIndexOf(conditionsString, ",", argEnd + 1);
            if (argEnd == -1) {
               argEnd = length;
            }
         }

         return !valid ? null : conditions;
      }
   }

   public static final class Key {
      private final String name;
      private final String path;

      public static SelectorCondition.Key parse(String keyStr) {
         keyStr = keyStr.trim();
         QuoteEscapedString unescapedKey = QuoteEscapedString.tryParseQuoted(keyStr);
         keyStr = unescapedKey.getUnescaped();
         String keyPathStr = "";
         int keyPathStart = QuoteEscapedString.unquotedIndexOf(keyStr, ".", 0);
         if (keyPathStart != -1) {
            keyPathStr = keyStr.substring(keyPathStart + 1);
            keyStr = keyStr.substring(0, keyPathStart);
            if (!unescapedKey.isQuoteEscaped()) {
               keyStr = QuoteEscapedString.tryParseQuoted(keyStr.trim()).getUnescaped();
            }

            if (!unescapedKey.isQuoteEscaped()) {
               keyPathStr = QuoteEscapedString.tryParseQuoted(keyPathStr.trim()).getUnescaped();
            }
         }

         return of(keyStr, keyPathStr);
      }

      public static SelectorCondition.Key of(String name) {
         return new SelectorCondition.Key(name);
      }

      public static SelectorCondition.Key of(String name, String path) {
         return new SelectorCondition.Key(name, path);
      }

      private Key(String name) {
         this(name, "");
      }

      private Key(String name, String path) {
         this.name = name;
         this.path = path;
      }

      public String name() {
         return this.name;
      }

      public String path() {
         return this.path;
      }

      public int hashCode() {
         return 31 * this.name.hashCode() + this.path.hashCode();
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof SelectorCondition.Key)) {
            return false;
         } else {
            SelectorCondition.Key other = (SelectorCondition.Key)o;
            return this.name.equals(other.name) && this.path.equals(other.path);
         }
      }

      public String toString() {
         return this.path.isEmpty() ? "Key{name=" + this.name + "}" : "Key{name=" + this.name + ", path=" + this.path + "}";
      }
   }

   public static class SelectorConditionInverted extends SelectorCondition {
      private final SelectorCondition base;

      /** @deprecated */
      @Deprecated
      public SelectorConditionInverted(String key, String value, SelectorCondition base) {
         this(SelectorCondition.Key.parse(key), value, base);
      }

      public SelectorConditionInverted(SelectorCondition.Key key, String value, SelectorCondition base) {
         super(key, value);
         this.base = base;
      }

      public boolean matchesAnyText(Collection<String> values) throws SelectorException {
         return !this.base.matchesAnyText(values);
      }

      public boolean matchesAnyText(Stream<String> values) throws SelectorException {
         return !this.base.matchesAnyText(values);
      }

      public boolean matchesText(String value) throws SelectorException {
         return !this.base.matchesText(value);
      }

      public BoundingRange getBoundingRange() throws SelectorException {
         return this.base.getBoundingRange().invert();
      }

      public boolean matchesNumber(double value) throws SelectorException {
         return !this.base.matchesNumber(value);
      }

      public boolean matchesNumber(long value) throws SelectorException {
         return !this.base.matchesNumber(value);
      }

      public boolean matchesBoolean(boolean value) throws SelectorException {
         return !this.base.matchesBoolean(value);
      }

      public boolean isNumber() {
         return this.base.isNumber();
      }
   }

   private static class SelectorConditionNumeric extends SelectorCondition {
      public static final SelectorCondition.SelectorConditionNumeric RANGE_MIN = new SelectorCondition.SelectorConditionNumeric(SelectorCondition.Key.of("NONE"), "", Double.NEGATIVE_INFINITY, Long.MIN_VALUE);
      public static final SelectorCondition.SelectorConditionNumeric RANGE_MAX = new SelectorCondition.SelectorConditionNumeric(SelectorCondition.Key.of("NONE"), "", Double.POSITIVE_INFINITY, Long.MAX_VALUE);
      public final double valueDouble;
      public final long valueLong;

      /** @deprecated */
      @Deprecated
      public SelectorConditionNumeric(String key, String value, double valueDouble, long valueLong) {
         this(SelectorCondition.Key.parse(key), value, valueDouble, valueLong);
      }

      public SelectorConditionNumeric(SelectorCondition.Key key, String value, double valueDouble, long valueLong) {
         super(key, value);
         this.valueDouble = valueDouble;
         this.valueLong = valueLong;
      }

      public BoundingRange getBoundingRange() throws SelectorException {
         return BoundingRange.create(this.valueDouble, this.valueDouble);
      }

      public boolean matchesNumber(double value) throws SelectorException {
         return value == this.valueDouble;
      }

      public boolean matchesNumber(long value) throws SelectorException {
         return value == this.valueLong;
      }

      public boolean matchesBoolean(boolean value) throws SelectorException {
         return this.getBoolean() == value;
      }

      public boolean getBoolean() throws SelectorException {
         if (this.valueDouble == 0.0D) {
            return false;
         } else if (this.valueDouble == 1.0D) {
            return true;
         } else {
            throw new SelectorException(this.getKey() + " value is not a boolean (0, 1, true, etc.)");
         }
      }

      public boolean isNumber() {
         return true;
      }

      public boolean isBoolean() {
         return this.valueDouble == 0.0D || this.valueDouble == 1.0D;
      }

      /** @deprecated */
      @Deprecated
      public static SelectorCondition.SelectorConditionNumeric tryParse(String key, String value) {
         return tryParse(SelectorCondition.Key.parse(key), value);
      }

      public static SelectorCondition.SelectorConditionNumeric tryParse(SelectorCondition.Key key, String value) {
         double valueDouble = ParseUtil.parseDouble(value, Double.NaN);
         if (!Double.isNaN(valueDouble)) {
            long valueLong = ParseUtil.parseLong(value, 0L);
            return new SelectorCondition.SelectorConditionNumeric(key, value, valueDouble, valueLong);
         } else {
            return null;
         }
      }
   }

   private static class SelectorConditionNumericRange extends SelectorCondition {
      private final SelectorCondition.SelectorConditionNumeric min;
      private final SelectorCondition.SelectorConditionNumeric max;

      /** @deprecated */
      @Deprecated
      public SelectorConditionNumericRange(String key, String value, SelectorCondition.SelectorConditionNumeric min, SelectorCondition.SelectorConditionNumeric max) {
         this(SelectorCondition.Key.parse(key), value, min, max);
      }

      public SelectorConditionNumericRange(SelectorCondition.Key key, String value, SelectorCondition.SelectorConditionNumeric min, SelectorCondition.SelectorConditionNumeric max) {
         super(key, value);
         if (min.valueDouble > max.valueDouble) {
            this.min = max;
            this.max = min;
         } else {
            this.min = min;
            this.max = max;
         }

      }

      public boolean matchesAnyText(Collection<String> values) throws SelectorException {
         return this.min.matchesAnyText(values) || this.max.matchesAnyText(values);
      }

      public boolean matchesAnyText(Stream<String> values) throws SelectorException {
         Collection<String> tmp = (Collection)values.collect(Collectors.toList());
         return this.min.matchesAnyText(tmp) || this.max.matchesAnyText(tmp);
      }

      public boolean matchesText(String value) throws SelectorException {
         return this.min.matchesText(value) || this.max.matchesText(value);
      }

      public BoundingRange getBoundingRange() throws SelectorException {
         return BoundingRange.create(this.min.valueDouble, this.max.valueDouble);
      }

      public boolean matchesNumber(double value) throws SelectorException {
         return value >= this.min.valueDouble && value <= this.max.valueDouble;
      }

      public boolean matchesNumber(long value) throws SelectorException {
         return value >= this.min.valueLong && value <= this.max.valueLong;
      }

      public boolean isNumber() {
         return true;
      }
   }

   private static class SelectorConditionAnyOfText extends SelectorCondition {
      private final SelectorCondition[] selectorValues;

      /** @deprecated */
      @Deprecated
      public SelectorConditionAnyOfText(String key, String value, SelectorCondition... selectorValues) {
         this(SelectorCondition.Key.parse(key), value, selectorValues);
      }

      public SelectorConditionAnyOfText(SelectorCondition.Key key, String value, SelectorCondition... selectorValues) {
         super(key, value);
         this.selectorValues = selectorValues;
      }

      public boolean matchesAnyText(Collection<String> values) throws SelectorException {
         SelectorCondition[] var2 = this.selectorValues;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SelectorCondition selectorValue = var2[var4];
            if (selectorValue.matchesAnyText(values)) {
               return true;
            }
         }

         return false;
      }

      public boolean matchesAnyText(Stream<String> values) throws SelectorException {
         return values.anyMatch((s) -> {
            SelectorCondition[] var2 = this.selectorValues;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               SelectorCondition selectorValue = var2[var4];
               if (selectorValue.matchesText(s)) {
                  return true;
               }
            }

            return false;
         });
      }

      public boolean matchesText(String value) throws SelectorException {
         SelectorCondition[] var2 = this.selectorValues;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SelectorCondition selectorValue = var2[var4];
            if (selectorValue.matchesText(value)) {
               return true;
            }
         }

         return false;
      }

      public BoundingRange getBoundingRange() throws SelectorException {
         double min = Double.MAX_VALUE;
         double max = -1.7976931348623157E308D;
         SelectorCondition[] var5 = this.selectorValues;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            SelectorCondition selectorValue = var5[var7];
            double value = selectorValue.getBoundingRange().getMin();
            if (value < min) {
               min = value;
            }

            if (value > max) {
               max = value;
            }
         }

         return BoundingRange.create(min, max);
      }

      public boolean matchesNumber(double value) throws SelectorException {
         SelectorCondition[] var3 = this.selectorValues;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SelectorCondition selectorValue = var3[var5];
            if (selectorValue.matchesNumber(value)) {
               return true;
            }
         }

         return false;
      }

      public boolean matchesNumber(long value) throws SelectorException {
         SelectorCondition[] var3 = this.selectorValues;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SelectorCondition selectorValue = var3[var5];
            if (selectorValue.matchesNumber(value)) {
               return true;
            }
         }

         return false;
      }
   }

   public static class SelectorConditionWildcardText extends SelectorCondition {
      private final String[] elements;
      private final boolean firstAny;
      private final boolean lastAny;

      /** @deprecated */
      @Deprecated
      public SelectorConditionWildcardText(String key, String value, String[] elements, boolean firstAny, boolean lastAny) {
         this(SelectorCondition.Key.parse(key), value, elements, firstAny, lastAny);
      }

      public SelectorConditionWildcardText(SelectorCondition.Key key, String value, String[] elements, boolean firstAny, boolean lastAny) {
         super(key, value);
         this.elements = elements;
         this.firstAny = firstAny;
         this.lastAny = lastAny;
      }

      public boolean matchesAnyText(Collection<String> values) throws SelectorException {
         Iterator var2 = values.iterator();

         String value;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            value = (String)var2.next();
         } while(!this.matchesText(value));

         return true;
      }

      public boolean matchesAnyText(Stream<String> values) throws SelectorException {
         return values.anyMatch(this::matchesText);
      }

      public boolean matchesText(String value) throws SelectorException {
         return Util.matchText(value, this.elements, this.firstAny, this.lastAny);
      }
   }

   private static class SelectorConditionBoolean extends SelectorCondition {
      private static final Map<String, Boolean> booleanConstants = new HashMap();
      private final boolean booleanValue;

      private static void register(String key, Boolean value) {
         booleanConstants.put(key, value);
         booleanConstants.put(key.toLowerCase(Locale.ENGLISH), value);
         booleanConstants.put(key.substring(0, 1).toUpperCase(Locale.ENGLISH) + key.substring(1), value);
      }

      protected SelectorConditionBoolean(SelectorCondition.Key key, String value, boolean booleanValue) {
         super(key, value);
         this.booleanValue = booleanValue;
      }

      public boolean isBoolean() {
         return true;
      }

      public boolean matchesBoolean(boolean value) throws SelectorException {
         return value == this.booleanValue;
      }

      public boolean getBoolean() throws SelectorException {
         return this.booleanValue;
      }

      /** @deprecated */
      @Deprecated
      public static SelectorCondition.SelectorConditionBoolean tryParse(String key, String value) {
         return tryParse(SelectorCondition.Key.parse(key), value);
      }

      public static SelectorCondition.SelectorConditionBoolean tryParse(SelectorCondition.Key key, String value) {
         Boolean truthy = (Boolean)booleanConstants.get(value);
         return truthy == null ? null : new SelectorCondition.SelectorConditionBoolean(key, value, truthy);
      }

      static {
         register("yes", Boolean.TRUE);
         register("true", Boolean.TRUE);
         register("no", Boolean.FALSE);
         register("false", Boolean.FALSE);
      }
   }
}
