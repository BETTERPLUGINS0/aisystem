package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import java.util.Collections;
import java.util.List;

public class SimpleResolution<T> implements Resolution<T> {
   private final T object;

   public SimpleResolution(T object) {
      this.object = object;
   }

   public List<ObjectIdentifier> getDependencies() {
      return Collections.emptyList();
   }

   public T instantiateWith(Object... values) {
      return this.object;
   }
}
