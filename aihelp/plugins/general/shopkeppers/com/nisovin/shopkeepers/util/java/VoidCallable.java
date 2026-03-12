package com.nisovin.shopkeepers.util.java;

import java.util.concurrent.Callable;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface VoidCallable extends Callable<Void> {
   @Nullable
   default Void call() throws Exception {
      this.voidCall();
      return null;
   }

   void voidCall() throws Exception;
}
