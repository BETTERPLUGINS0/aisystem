package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.types.EnumPropertyType;

public class EnumProperty<E extends Enum<E>> extends TypeBasedProperty<E> {
   public EnumProperty(Class<E> clazz, String path, E defaultValue) {
      super(path, defaultValue, EnumPropertyType.of(clazz));
   }
}
