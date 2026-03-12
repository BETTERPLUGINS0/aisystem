package ch.jalu.configme.beanmapper.propertydescription;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface BeanDescriptionFactory {
   @NotNull
   Collection<BeanPropertyDescription> getAllProperties(@NotNull Class<?> var1);
}
