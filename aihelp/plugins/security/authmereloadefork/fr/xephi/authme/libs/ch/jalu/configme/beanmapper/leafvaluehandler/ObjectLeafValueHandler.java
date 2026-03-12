package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

public class ObjectLeafValueHandler extends AbstractLeafValueHandler {
   public Object convert(Class<?> clazz, Object value) {
      return clazz == Object.class ? value : null;
   }

   public Object toExportValue(Object value) {
      return null;
   }
}
