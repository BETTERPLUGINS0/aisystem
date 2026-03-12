package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import java.util.List;

public interface Resolution<T> {
   List<ObjectIdentifier> getDependencies();

   T instantiateWith(Object... var1);

   default boolean isInstantiation() {
      return false;
   }
}
