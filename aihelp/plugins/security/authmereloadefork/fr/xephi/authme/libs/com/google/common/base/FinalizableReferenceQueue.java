package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class FinalizableReferenceQueue implements Closeable {
   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
   private static final String FINALIZER_CLASS_NAME = "fr.xephi.authme.libs.com.google.common.base.internal.Finalizer";
   private static final Method startFinalizer;
   final ReferenceQueue<Object> queue = new ReferenceQueue();
   final PhantomReference<Object> frqRef;
   final boolean threadStarted;

   public FinalizableReferenceQueue() {
      this.frqRef = new PhantomReference(this, this.queue);
      boolean threadStarted = false;

      try {
         startFinalizer.invoke((Object)null, FinalizableReference.class, this.queue, this.frqRef);
         threadStarted = true;
      } catch (IllegalAccessException var3) {
         throw new AssertionError(var3);
      } catch (Throwable var4) {
         logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", var4);
      }

      this.threadStarted = threadStarted;
   }

   public void close() {
      this.frqRef.enqueue();
      this.cleanUp();
   }

   void cleanUp() {
      if (!this.threadStarted) {
         Reference reference;
         while((reference = this.queue.poll()) != null) {
            reference.clear();

            try {
               ((FinalizableReference)reference).finalizeReferent();
            } catch (Throwable var3) {
               logger.log(Level.SEVERE, "Error cleaning up after reference.", var3);
            }
         }

      }
   }

   private static Class<?> loadFinalizer(FinalizableReferenceQueue.FinalizerLoader... loaders) {
      FinalizableReferenceQueue.FinalizerLoader[] var1 = loaders;
      int var2 = loaders.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FinalizableReferenceQueue.FinalizerLoader loader = var1[var3];
         Class<?> finalizer = loader.loadFinalizer();
         if (finalizer != null) {
            return finalizer;
         }
      }

      throw new AssertionError();
   }

   static Method getStartFinalizer(Class<?> finalizer) {
      try {
         return finalizer.getMethod("startFinalizer", Class.class, ReferenceQueue.class, PhantomReference.class);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      Class<?> finalizer = loadFinalizer(new FinalizableReferenceQueue.SystemLoader(), new FinalizableReferenceQueue.DecoupledLoader(), new FinalizableReferenceQueue.DirectLoader());
      startFinalizer = getStartFinalizer(finalizer);
   }

   static class DirectLoader implements FinalizableReferenceQueue.FinalizerLoader {
      public Class<?> loadFinalizer() {
         try {
            return Class.forName("fr.xephi.authme.libs.com.google.common.base.internal.Finalizer");
         } catch (ClassNotFoundException var2) {
            throw new AssertionError(var2);
         }
      }
   }

   static class DecoupledLoader implements FinalizableReferenceQueue.FinalizerLoader {
      private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";

      @CheckForNull
      public Class<?> loadFinalizer() {
         try {
            ClassLoader finalizerLoader = this.newLoader(this.getBaseUrl());
            return finalizerLoader.loadClass("fr.xephi.authme.libs.com.google.common.base.internal.Finalizer");
         } catch (Exception var2) {
            FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.", var2);
            return null;
         }
      }

      URL getBaseUrl() throws IOException {
         String finalizerPath = String.valueOf("fr.xephi.authme.libs.com.google.common.base.internal.Finalizer".replace('.', '/')).concat(".class");
         URL finalizerUrl = this.getClass().getClassLoader().getResource(finalizerPath);
         if (finalizerUrl == null) {
            throw new FileNotFoundException(finalizerPath);
         } else {
            String urlString = finalizerUrl.toString();
            if (!urlString.endsWith(finalizerPath)) {
               IOException var10000 = new IOException;
               String var10003 = String.valueOf(urlString);
               String var10002;
               if (var10003.length() != 0) {
                  var10002 = "Unsupported path style: ".concat(var10003);
               } else {
                  String var10004 = new String;
                  var10002 = var10004;
                  var10004.<init>("Unsupported path style: ");
               }

               var10000.<init>(var10002);
               throw var10000;
            } else {
               urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
               return new URL(finalizerUrl, urlString);
            }
         }
      }

      URLClassLoader newLoader(URL base) {
         return new URLClassLoader(new URL[]{base}, (ClassLoader)null);
      }
   }

   static class SystemLoader implements FinalizableReferenceQueue.FinalizerLoader {
      @VisibleForTesting
      static boolean disabled;

      @CheckForNull
      public Class<?> loadFinalizer() {
         if (disabled) {
            return null;
         } else {
            ClassLoader systemLoader;
            try {
               systemLoader = ClassLoader.getSystemClassLoader();
            } catch (SecurityException var4) {
               FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
               return null;
            }

            if (systemLoader != null) {
               try {
                  return systemLoader.loadClass("fr.xephi.authme.libs.com.google.common.base.internal.Finalizer");
               } catch (ClassNotFoundException var3) {
                  return null;
               }
            } else {
               return null;
            }
         }
      }
   }

   interface FinalizerLoader {
      @CheckForNull
      Class<?> loadFinalizer();
   }
}
