package fr.xephi.authme.libs.org.jboss.security.mapping.providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.Policy;
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

   static URL findResource(final URLClassLoader cl, final String name) {
      return (URL)AccessController.doPrivileged(new PrivilegedAction<URL>() {
         public URL run() {
            return cl.findResource(name);
         }
      });
   }

   static Policy getPolicy() {
      return (Policy)AccessController.doPrivileged(new PrivilegedAction<Policy>() {
         public Policy run() {
            return Policy.getPolicy();
         }
      });
   }

   static URL getResource(final ClassLoader cl, final String name) {
      return (URL)AccessController.doPrivileged(new PrivilegedAction<URL>() {
         public URL run() {
            return cl.getResource(name);
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
}
