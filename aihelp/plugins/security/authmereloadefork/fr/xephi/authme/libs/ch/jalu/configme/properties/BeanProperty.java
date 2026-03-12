package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.DefaultMapper;
import fr.xephi.authme.libs.ch.jalu.configme.beanmapper.Mapper;
import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.BeanPropertyType;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;

public class BeanProperty<T> extends TypeBasedProperty<T> {
   public BeanProperty(Class<T> beanType, String path, T defaultValue) {
      this(beanType, path, defaultValue, DefaultMapper.getInstance());
   }

   public BeanProperty(Class<T> beanType, String path, T defaultValue, Mapper mapper) {
      super(path, defaultValue, BeanPropertyType.of(beanType, mapper));
   }

   protected BeanProperty(TypeInformation beanType, String path, T defaultValue, Mapper mapper) {
      super(path, defaultValue, new BeanPropertyType(beanType, mapper));
      if (!beanType.getSafeToWriteClass().isInstance(defaultValue)) {
         throw new ConfigMeException("Default value for path '" + path + "' does not match bean type '" + beanType + "'");
      }
   }
}
