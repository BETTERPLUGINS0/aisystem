package ch.jalu.configme.properties;

import ch.jalu.configme.beanmapper.DefaultMapper;
import ch.jalu.configme.beanmapper.Mapper;
import ch.jalu.configme.exception.ConfigMeException;
import ch.jalu.configme.properties.types.BeanPropertyType;
import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;

public class BeanProperty<T> extends TypeBasedProperty<T> {
   public BeanProperty(@NotNull Class<T> beanType, @NotNull String path, @NotNull T defaultValue) {
      this(beanType, path, defaultValue, DefaultMapper.getInstance());
   }

   public BeanProperty(@NotNull Class<T> beanType, @NotNull String path, @NotNull T defaultValue, @NotNull Mapper mapper) {
      super(path, defaultValue, BeanPropertyType.of(beanType, mapper));
   }

   protected BeanProperty(@NotNull TypeInformation beanType, @NotNull String path, @NotNull T defaultValue, @NotNull Mapper mapper) {
      super(path, defaultValue, new BeanPropertyType(beanType, mapper));
      if (!beanType.getSafeToWriteClass().isInstance(defaultValue)) {
         throw new ConfigMeException("Default value for path '" + path + "' does not match bean type '" + beanType + "'");
      }
   }
}
