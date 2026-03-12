package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

public class BooleanLeafValueHandler extends AbstractLeafValueHandler {
   public Object convert(Class<?> clazz, Object value) {
      return (clazz == Boolean.TYPE || clazz == Boolean.class) && value instanceof Boolean ? value : null;
   }

   public Object toExportValue(Object value) {
      return value instanceof Boolean ? value : null;
   }
}
