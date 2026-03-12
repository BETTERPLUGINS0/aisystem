package fr.xephi.authme.libs.org.jboss.security.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SysPropertyActions {
   public static String getProperty(String name, String defaultValue) {
      String prop;
      if (System.getSecurityManager() == null) {
         prop = SysPropertyActions.SysProps.NON_PRIVILEDGED.getProperty(name, defaultValue);
      } else {
         prop = SysPropertyActions.SysProps.PRIVILEDGED.getProperty(name, defaultValue);
      }

      return prop;
   }

   interface SysProps {
      SysPropertyActions.SysProps NON_PRIVILEDGED = new SysPropertyActions.SysProps() {
         public String getProperty(String name, String defaultValue) {
            return System.getProperty(name, defaultValue);
         }
      };
      SysPropertyActions.SysProps PRIVILEDGED = new SysPropertyActions.SysProps() {
         public String getProperty(final String name, final String defaultValue) {
            PrivilegedAction action = new PrivilegedAction() {
               public Object run() {
                  return System.getProperty(name, defaultValue);
               }
            };
            return (String)AccessController.doPrivileged(action);
         }
      };

      String getProperty(String var1, String var2);
   }
}
