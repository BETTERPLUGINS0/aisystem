package fr.xephi.authme.libs.ch.jalu.injector.factory;

public interface Factory<P> {
   <C extends P> C newInstance(Class<C> var1);
}
