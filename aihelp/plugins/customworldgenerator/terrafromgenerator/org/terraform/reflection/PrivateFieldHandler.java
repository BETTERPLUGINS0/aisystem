package org.terraform.reflection;

import java.lang.reflect.Field;

public abstract class PrivateFieldHandler {
   public abstract void injectField(Object var1, String var2, Object var3) throws Throwable;

   public void injectField(Object obj, Field target, Object value) throws IllegalArgumentException, IllegalAccessException {
      throw new UnsupportedOperationException();
   }
}
