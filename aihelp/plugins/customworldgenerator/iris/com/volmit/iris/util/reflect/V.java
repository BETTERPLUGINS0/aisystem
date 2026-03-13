package com.volmit.iris.util.reflect;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import java.lang.annotation.Annotation;

public class V {
   private final Object o;
   private boolean local;
   private boolean suppress;

   public V(Class<?> c, Object... parameters) {
      this.suppress = false;
      this.o = Violator.construct(var1, var2);
      this.local = true;
   }

   public V(Object o) {
      this.suppress = false;
      this.o = var1;
      this.local = true;
   }

   public V(Object o, boolean local, boolean suppress) {
      this(var1);
      this.local = var2;
      this.suppress = var3;
   }

   public V(Object o, boolean local) {
      this(var1);
      this.local = var2;
   }

   public <T extends Annotation> T get(Class<? extends T> t) {
      try {
         return this.local ? Violator.getDeclaredAnnotation(this.o.getClass(), var1) : Violator.getAnnotation(this.o.getClass(), var1);
      } catch (Throwable var3) {
         Iris.reportError(var3);
         if (!this.suppress) {
            var3.printStackTrace();
         }

         return null;
      }
   }

   public <T extends Annotation> T get(Class<? extends T> t, String mn, Class<?>... pars) {
      try {
         return this.local ? Violator.getDeclaredAnnotation(Violator.getDeclaredMethod(this.o.getClass(), var2, var3), var1) : Violator.getAnnotation(Violator.getMethod(this.o.getClass(), var2, var3), var1);
      } catch (Throwable var5) {
         Iris.reportError(var5);
         if (!this.suppress) {
            var5.printStackTrace();
         }

         return null;
      }
   }

   public <T extends Annotation> T get(Class<? extends T> t, String mn) {
      try {
         return this.local ? Violator.getDeclaredAnnotation(Violator.getDeclaredField(this.o.getClass(), var2), var1) : Violator.getAnnotation(Violator.getField(this.o.getClass(), var2), var1);
      } catch (Throwable var4) {
         Iris.reportError(var4);
         if (!this.suppress) {
            var4.printStackTrace();
         }

         return null;
      }
   }

   public <T> T get(String field) {
      try {
         return (this.local ? Violator.getDeclaredField(this.o.getClass(), var1) : Violator.getField(this.o.getClass(), var1)).get(this.o);
      } catch (Throwable var3) {
         Iris.reportError(var3);
         if (!this.suppress) {
            var3.printStackTrace();
         }

         return null;
      }
   }

   public Object getSelf() {
      return this.o;
   }

   public Object invoke(String method, Object... parameters) {
      KList var3 = new KList();
      Object[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var4[var6];
         var3.add((Object)var7.getClass());
      }

      try {
         return (this.local ? Violator.getDeclaredMethod(this.o.getClass(), var1, (Class[])var3.toArray(new Class[0])) : Violator.getMethod(this.o.getClass(), var1, (Class[])var3.toArray(new Class[0]))).invoke(this.o, var2);
      } catch (Throwable var8) {
         Iris.reportError(var8);
         if (!this.suppress) {
            var8.printStackTrace();
         }

         return null;
      }
   }

   public void set(String field, Object value) {
      try {
         (this.local ? Violator.getDeclaredField(this.o.getClass(), var1) : Violator.getField(this.o.getClass(), var1)).set(this.o, var2);
      } catch (Throwable var4) {
         Iris.reportError(var4);
         if (!this.suppress) {
            var4.printStackTrace();
         }
      }

   }
}
