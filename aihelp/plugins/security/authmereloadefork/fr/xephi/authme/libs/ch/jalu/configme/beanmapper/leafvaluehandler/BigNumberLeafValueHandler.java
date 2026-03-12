package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nullable;

public class BigNumberLeafValueHandler extends AbstractLeafValueHandler {
   private static final BigDecimal BIG_DECIMAL_SCIENTIFIC_THRESHOLD = new BigDecimal("1E100");

   protected Object convert(Class<?> clazz, Object value) {
      if (clazz != BigInteger.class && clazz != BigDecimal.class) {
         return null;
      } else if (value instanceof String) {
         return this.fromString(clazz, (String)value);
      } else {
         return value instanceof Number ? this.fromNumber(clazz, (Number)value) : null;
      }
   }

   public Object toExportValue(Object value) {
      Class<?> valueType = value == null ? null : value.getClass();
      if (valueType == BigInteger.class) {
         return value.toString();
      } else if (valueType == BigDecimal.class) {
         BigDecimal bigDecimal = (BigDecimal)value;
         return bigDecimal.abs().compareTo(BIG_DECIMAL_SCIENTIFIC_THRESHOLD) >= 0 ? bigDecimal.toString() : bigDecimal.toPlainString();
      } else {
         return null;
      }
   }

   @Nullable
   protected Object fromString(Class<?> targetClass, String value) {
      try {
         return targetClass == BigInteger.class ? new BigInteger(value) : new BigDecimal(value);
      } catch (NumberFormatException var4) {
         return null;
      }
   }

   protected Object fromNumber(Class<?> targetClass, Number value) {
      if (targetClass.isInstance(value)) {
         return value;
      } else if (targetClass == BigInteger.class) {
         return !(value instanceof Double) && !(value instanceof Float) ? BigInteger.valueOf(value.longValue()) : BigDecimal.valueOf(value.doubleValue()).toBigInteger();
      } else {
         return !(value instanceof Integer) && !(value instanceof Long) ? BigDecimal.valueOf(value.doubleValue()).stripTrailingZeros() : BigDecimal.valueOf(value.longValue());
      }
   }
}
