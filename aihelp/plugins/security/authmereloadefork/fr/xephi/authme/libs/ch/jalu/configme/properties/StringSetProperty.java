package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;
import java.util.Set;

public class StringSetProperty extends SetProperty<String> {
   public StringSetProperty(String path, String... defaultValue) {
      super(path, PrimitivePropertyType.STRING, (Object[])defaultValue);
   }

   public StringSetProperty(String path, Set<String> defaultValue) {
      super(path, PrimitivePropertyType.STRING, defaultValue);
   }

   public Object toExportValue(Set<String> value) {
      return value;
   }
}
