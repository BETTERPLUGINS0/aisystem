package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;

public interface Fallible {
   default void fail(String error) {
      try {
         throw new RuntimeException();
      } catch (Throwable var3) {
         Iris.reportError(var3);
         this.fail(error, var3);
      }
   }

   default void fail(Throwable e) {
      this.fail("Failed to generate", e);
   }

   void fail(String error, Throwable e);

   boolean hasFailed();
}
