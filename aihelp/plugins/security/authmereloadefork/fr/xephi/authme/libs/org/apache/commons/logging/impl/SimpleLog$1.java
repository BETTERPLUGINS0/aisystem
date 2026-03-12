package fr.xephi.authme.libs.org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

final class SimpleLog$1 implements PrivilegedAction {
   // $FF: synthetic field
   private final String val$name;

   SimpleLog$1(String var1) {
      this.val$name = var1;
   }

   public Object run() {
      ClassLoader threadCL = SimpleLog.access$000();
      return threadCL != null ? threadCL.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
   }
}
