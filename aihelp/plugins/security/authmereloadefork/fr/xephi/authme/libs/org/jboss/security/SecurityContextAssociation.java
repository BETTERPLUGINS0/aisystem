package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;

public class SecurityContextAssociation {
   private static boolean SERVER = true;
   private static SecurityContext securityContext = null;
   private static RuntimePermission SetSecurityContextPermission = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.setSecurityContext");
   private static RuntimePermission GetSecurityContextPermission = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.getSecurityContext");
   private static RuntimePermission ClearSecurityContextPermission = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.clearSecurityContext");
   private static final RuntimePermission SetRunAsIdentity = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.setRunAsRole");
   private static final RuntimePermission GetContextInfo = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.accessContextInfo", "get");
   private static final RuntimePermission SetContextInfo = new RuntimePermission("fr.xephi.authme.libs.org.jboss.security.accessContextInfo", "set");
   public static final String SECURITYCONTEXT_THREADLOCAL = "fr.xephi.authme.libs.org.jboss.security.context.ThreadLocal";
   private static final String SECURITYASSOCIATION_THREADLOCAL = "fr.xephi.authme.libs.org.jboss.security.SecurityAssociation.ThreadLocal";
   private static ThreadLocal<SecurityContext> securityContextLocal;

   public static boolean isClient() {
      return !SERVER;
   }

   public static void setClient() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextAssociation.class.getName() + ".setClient"));
      }

      SERVER = false;
   }

   public static void setSecurityContext(SecurityContext sc) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetSecurityContextPermission);
      }

      if (!SERVER) {
         securityContext = sc;
      } else if (sc == null) {
         securityContextLocal.remove();
      } else {
         securityContextLocal.set(sc);
      }

   }

   public static SecurityContext getSecurityContext() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GetSecurityContextPermission);
      }

      return !SERVER ? securityContext : (SecurityContext)securityContextLocal.get();
   }

   public static void clearSecurityContext() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(ClearSecurityContextPermission);
      }

      if (!SERVER) {
         securityContext = null;
      } else {
         securityContextLocal.remove();
      }

   }

   public static void pushRunAsIdentity(RunAs runAs) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetRunAsIdentity);
      }

      SecurityContext sc = getSecurityContext();
      if (sc != null) {
         sc.setOutgoingRunAs(runAs);
      }

   }

   public static RunAs popRunAsIdentity() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetRunAsIdentity);
      }

      SecurityContext sc = getSecurityContext();
      RunAs ra = null;
      if (sc != null) {
         ra = sc.getOutgoingRunAs();
         sc.setOutgoingRunAs((RunAs)null);
      }

      return ra;
   }

   public static RunAs peekRunAsIdentity() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextAssociation.class.getName() + ".peekRunAsIdentity"));
      }

      RunAs ra = null;
      SecurityContext sc = getSecurityContext();
      if (sc != null) {
         ra = sc.getOutgoingRunAs();
      }

      return ra;
   }

   public static Object getContextInfo(String key) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GetContextInfo);
      }

      if (key == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      } else {
         SecurityContext sc = getSecurityContext();
         return sc != null ? sc.getData().get(key) : null;
      }
   }

   public static Object setContextInfo(String key, Object value) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetContextInfo);
      }

      SecurityContext sc = getSecurityContext();
      return sc != null ? sc.getData().put(key, value) : null;
   }

   private static String getSystemProperty(final String propertyName, final String defaultString) {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty(propertyName, defaultString);
         }
      });
   }

   public static Subject getSubject() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GetSecurityContextPermission);
      }

      SecurityContext sc = getSecurityContext();
      return sc != null ? sc.getUtil().getSubject() : null;
   }

   public static Principal getPrincipal() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GetSecurityContextPermission);
      }

      SecurityContext sc = getSecurityContext();
      return sc != null ? sc.getUtil().getUserPrincipal() : null;
   }

   public static void setPrincipal(Principal principal) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetSecurityContextPermission);
      }

      SecurityContext securityContext = getSecurityContext();
      if (securityContext == null) {
         try {
            securityContext = SecurityContextFactory.createSecurityContext("CLIENT_SIDE");
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }

         setSecurityContext(securityContext);
      }

      Object credential = securityContext.getUtil().getCredential();
      Subject subj = securityContext.getUtil().getSubject();
      securityContext.getUtil().createSubjectInfo(principal, credential, subj);
   }

   public static Object getCredential() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GetSecurityContextPermission);
      }

      SecurityContext sc = getSecurityContext();
      return sc != null ? sc.getUtil().getCredential() : null;
   }

   public static void setCredential(Object credential) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SetSecurityContextPermission);
      }

      SecurityContext securityContext = getSecurityContext();
      if (securityContext == null) {
         try {
            securityContext = SecurityContextFactory.createSecurityContext("CLIENT_SIDE");
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }

         setSecurityContext(securityContext);
      }

      Principal principal = securityContext.getUtil().getUserPrincipal();
      Subject subj = securityContext.getUtil().getSubject();
      securityContext.getUtil().createSubjectInfo(principal, credential, subj);
   }

   static {
      String saflag = getSystemProperty("fr.xephi.authme.libs.org.jboss.security.SecurityAssociation.ThreadLocal", "false");
      String scflag = getSystemProperty("fr.xephi.authme.libs.org.jboss.security.context.ThreadLocal", "false");
      boolean useThreadLocal = Boolean.valueOf(saflag) || Boolean.valueOf(scflag);
      if (useThreadLocal) {
         securityContextLocal = new ThreadLocal();
      } else {
         securityContextLocal = new InheritableThreadLocal();
      }

   }
}
