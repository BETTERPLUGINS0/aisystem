package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NumberLeafValueHandler extends AbstractLeafValueHandler {
   private static final Map<Class<?>, Function<Number, Number>> NUMBER_CLASSES_TO_CONVERSION = createMapOfTypeToTransformFunction();

   public Object convert(Class<?> clazz, Object value) {
      if (value instanceof Number) {
         Function<Number, Number> numberFunction = (Function)NUMBER_CLASSES_TO_CONVERSION.get(clazz);
         return numberFunction == null ? null : numberFunction.apply((Number)value);
      } else {
         return null;
      }
   }

   public Object toExportValue(Object value) {
      Class<?> clazz = value == null ? null : value.getClass();
      return NUMBER_CLASSES_TO_CONVERSION.containsKey(clazz) ? value : null;
   }

   private static Map<Class<?>, Function<Number, Number>> createMapOfTypeToTransformFunction() {
      Map<Class<?>, Function<Number, Number>> map = new HashMap();
      map.put(Byte.TYPE, Number::byteValue);
      map.put(Byte.class, Number::byteValue);
      map.put(Short.TYPE, Number::shortValue);
      map.put(Short.class, Number::shortValue);
      map.put(Integer.TYPE, Number::intValue);
      map.put(Integer.class, Number::intValue);
      map.put(Long.TYPE, Number::longValue);
      map.put(Long.class, Number::longValue);
      map.put(Float.TYPE, Number::floatValue);
      map.put(Float.class, Number::floatValue);
      map.put(Double.TYPE, Number::doubleValue);
      map.put(Double.class, Number::doubleValue);
      return Collections.unmodifiableMap(map);
   }
}
