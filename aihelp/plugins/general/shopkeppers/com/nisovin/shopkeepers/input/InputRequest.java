package com.nisovin.shopkeepers.input;

@FunctionalInterface
public interface InputRequest<T> {
   void onInput(T var1);

   default void onAborted() {
   }
}
