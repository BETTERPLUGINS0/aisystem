package fr.xephi.authme.libs.com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

public class Finalizer implements Runnable {
   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
   private static final String FINALIZABLE_REFERENCE = "fr.xephi.authme.libs.com.google.common.base.FinalizableReference";
   private final WeakReference<Class<?>> finalizableReferenceClassReference;
   private final PhantomReference<Object> frqReference;
   private final ReferenceQueue<Object> queue;
   @CheckForNull
   private static final Constructor<Thread> bigThreadConstructor = getBigThreadConstructor();
   @CheckForNull
   private static final Field inheritableThreadLocals;

   public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
      if (!finalizableReferenceClass.getName().equals("fr.xephi.authme.libs.com.google.common.base.FinalizableReference")) {
         throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
      } else {
         Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
         String threadName = Finalizer.class.getName();
         Thread thread = null;
         if (bigThreadConstructor != null) {
            try {
               boolean inheritThreadLocals = false;
               long defaultStackSize = 0L;
               thread = (Thread)bigThreadConstructor.newInstance((ThreadGroup)null, finalizer, threadName, defaultStackSize, inheritThreadLocals);
            } catch (Throwable var10) {
               logger.log(Level.INFO, "Failed to create a thread without inherited thread-local values", var10);
            }
         }

         if (thread == null) {
            thread = new Thread((ThreadGroup)null, finalizer, threadName);
         }

         thread.setDaemon(true);

         try {
            if (inheritableThreadLocals != null) {
               inheritableThreadLocals.set(thread, (Object)null);
            }
         } catch (Throwable var9) {
            logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", var9);
         }

         thread.start();
      }
   }

   private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
      this.queue = queue;
      this.finalizableReferenceClassReference = new WeakReference(finalizableReferenceClass);
      this.frqReference = frqReference;
   }

   public void run() {
      while(true) {
         try {
            if (!this.cleanUp(this.queue.remove())) {
               return;
            }
         } catch (InterruptedException var2) {
         }
      }
   }

   private boolean cleanUp(Reference<?> reference) {
      Method finalizeReferentMethod = this.getFinalizeReferentMethod();
      if (finalizeReferentMethod == null) {
         return false;
      } else {
         do {
            reference.clear();
            if (reference == this.frqReference) {
               return false;
            }

            try {
               finalizeReferentMethod.invoke(reference);
            } catch (Throwable var4) {
               logger.log(Level.SEVERE, "Error cleaning up after reference.", var4);
            }
         } while((reference = this.queue.poll()) != null);

         return true;
      }
   }

   @CheckForNull
   private Method getFinalizeReferentMethod() {
      Class<?> finalizableReferenceClass = (Class)this.finalizableReferenceClassReference.get();
      if (finalizableReferenceClass == null) {
         return null;
      } else {
         try {
            return finalizableReferenceClass.getMethod("finalizeReferent");
         } catch (NoSuchMethodException var3) {
            throw new AssertionError(var3);
         }
      }
   }

   @CheckForNull
   private static Field getInheritableThreadLocalsField() {
      try {
         Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
         inheritableThreadLocals.setAccessible(true);
         return inheritableThreadLocals;
      } catch (Throwable var1) {
         logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
         return null;
      }
   }

   @CheckForNull
   private static Constructor<Thread> getBigThreadConstructor() {
      try {
         return Thread.class.getConstructor(ThreadGroup.class, Runnable.class, String.class, Long.TYPE, Boolean.TYPE);
      } catch (Throwable var1) {
         return null;
      }
   }

   static {
      inheritableThreadLocals = bigThreadConstructor == null ? getInheritableThreadLocalsField() : null;
   }
}
