package fr.xephi.authme.libs.ch.jalu.injector.factory;

import java.util.Collection;

public interface SingletonStore<P> {
   <C extends P> C getSingleton(Class<C> var1);

   Collection<P> retrieveAllOfType();

   <C extends P> Collection<C> retrieveAllOfType(Class<C> var1);
}
