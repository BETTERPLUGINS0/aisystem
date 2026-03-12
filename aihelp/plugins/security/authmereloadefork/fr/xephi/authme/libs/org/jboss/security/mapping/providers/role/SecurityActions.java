package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Void setContextClassLoader(final ClassLoader cl) {
      return (Void)AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }

   static URL findResource(final URLClassLoader cl, final String name) {
      return (URL)AccessController.doPrivileged(new PrivilegedAction<URL>() {
         public URL run() {
            return cl.findResource(name);
         }
      });
   }

   static InputStream openStream(final URL url) throws PrivilegedActionException {
      return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
         public InputStream run() throws IOException {
            return url.openStream();
         }
      });
   }

   static Class<?> loadClass(final String name) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws PrivilegedActionException {
            try {
               return SecurityActions.getContextClassLoader().loadClass(name);
            } catch (Exception var2) {
               throw new PrivilegedActionException(var2);
            }
         }
      });
   }
}
