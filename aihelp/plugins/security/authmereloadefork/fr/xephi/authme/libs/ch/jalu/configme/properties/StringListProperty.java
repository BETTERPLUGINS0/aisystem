package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PrimitivePropertyType;
import java.util.List;

public class StringListProperty extends ListProperty<String> {
   public StringListProperty(String path, String... defaultValue) {
      super(path, PrimitivePropertyType.STRING, (Object[])defaultValue);
   }

   public StringListProperty(String path, List<String> defaultValue) {
      super(path, PrimitivePropertyType.STRING, defaultValue);
   }

   public Object toExportValue(List<String> value) {
      return value;
   }
}
