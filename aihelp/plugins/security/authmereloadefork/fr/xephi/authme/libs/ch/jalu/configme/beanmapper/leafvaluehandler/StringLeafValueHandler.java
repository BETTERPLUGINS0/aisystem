package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

public class StringLeafValueHandler extends AbstractLeafValueHandler {
   public Object convert(Class<?> clazz, Object value) {
      return clazz != String.class || !(value instanceof String) && !(value instanceof Number) && !(value instanceof Boolean) ? null : value.toString();
   }

   public Object toExportValue(Object value) {
      return value instanceof String ? value : null;
   }
}
