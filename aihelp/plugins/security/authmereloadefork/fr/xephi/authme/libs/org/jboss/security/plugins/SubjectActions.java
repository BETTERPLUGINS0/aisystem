package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextFactory;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;

class SubjectActions {
   static Subject getActiveSubject() {
      Subject subject = (Subject)AccessController.doPrivileged(SubjectActions.GetSubjectAction.ACTION);
      return subject;
   }

   static void copySubject(Subject fromSubject, Subject toSubject) {
      copySubject(fromSubject, toSubject, false);
   }

   static void copySubject(Subject fromSubject, Subject toSubject, boolean setReadOnly) {
      SubjectActions.CopySubjectAction action = new SubjectActions.CopySubjectAction(fromSubject, toSubject, setReadOnly);
      if (System.getSecurityManager() != null) {
         AccessController.doPrivileged(action);
      } else {
         action.run();
      }

   }

   static void copySubject(Subject fromSubject, Subject toSubject, boolean setReadOnly, boolean deepCopy) {
      SubjectActions.CopySubjectAction action = new SubjectActions.CopySubjectAction(fromSubject, toSubject, setReadOnly);
      action.setDeepCopy(deepCopy);
      if (System.getSecurityManager() != null) {
         AccessController.doPrivileged(action);
      } else {
         action.run();
      }

   }

   static LoginContext createLoginContext(String securityDomain, Subject subject, CallbackHandler handler) throws LoginException {
      SubjectActions.LoginContextAction action = new SubjectActions.LoginContextAction(securityDomain, subject, handler);

      try {
         LoginContext lc = (LoginContext)AccessController.doPrivileged(action);
         return lc;
      } catch (PrivilegedActionException var6) {
         Exception ex = var6.getException();
         if (ex instanceof LoginException) {
            throw (LoginException)ex;
         } else {
            throw new LoginException(ex.getLocalizedMessage());
         }
      }
   }

   static ClassLoader getContextClassLoader() {
      ClassLoader loader = (ClassLoader)AccessController.doPrivileged(SubjectActions.GetTCLAction.ACTION);
      return loader;
   }

   static void setContextClassLoader(ClassLoader cl) {
      AccessController.doPrivileged(new SubjectActions.SetTCLAction(cl));
   }

   static Object setContextInfo(String key, Object value) {
      SubjectActions.SetContextInfoAction action = new SubjectActions.SetContextInfoAction(key, value);
      Object prevInfo = AccessController.doPrivileged(action);
      return prevInfo;
   }

   static void pushSubjectContext(Principal principal, Object credential, Subject subject, String securityDomain) {
      if (System.getSecurityManager() == null) {
         SubjectActions.PrincipalInfoAction.NON_PRIVILEGED.push(principal, credential, subject, securityDomain);
      } else {
         SubjectActions.PrincipalInfoAction.PRIVILEGED.push(principal, credential, subject, securityDomain);
      }

   }

   static void popSubjectContext() {
      if (System.getSecurityManager() == null) {
         SubjectActions.PrincipalInfoAction.NON_PRIVILEGED.pop();
      } else {
         SubjectActions.PrincipalInfoAction.PRIVILEGED.pop();
      }

   }

   static String toString(Subject subject) {
      SubjectActions.ToStringSubjectAction action = new SubjectActions.ToStringSubjectAction(subject);
      String info = (String)AccessController.doPrivileged(action);
      return info;
   }

   static SecurityContext getSecurityContext() {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
         public SecurityContext run() {
            return SecurityContextAssociation.getSecurityContext();
         }
      });
   }

   static void setSecurityContext(final SecurityContext sc) {
      AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
         public SecurityContext run() {
            SecurityContextAssociation.setSecurityContext(sc);
            return null;
         }
      });
   }

   static String getRefreshSecurityContextRoles() {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty("jbosssx.context.roles.refresh", "false");
         }
      });
   }

   static String getSystemProperty(final String key, final String defaultValue) {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty(key, defaultValue);
         }
      });
   }

   static Principal getPrincipal() {
      return (Principal)AccessController.doPrivileged(new PrivilegedAction<Principal>() {
         public Principal run() {
            Principal principal = null;
            SecurityContext sc = SubjectActions.getSecurityContext();
            if (sc != null) {
               principal = sc.getUtil().getUserPrincipal();
            }

            return principal;
         }
      });
   }

   static Object getCredential() {
      return AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            Object credential = null;
            SecurityContext sc = SubjectActions.getSecurityContext();
            if (sc != null) {
               credential = sc.getUtil().getCredential();
            }

            return credential;
         }
      });
   }

   interface PrincipalInfoAction {
      SubjectActions.PrincipalInfoAction PRIVILEGED = new SubjectActions.PrincipalInfoAction() {
         public void push(final Principal principal, final Object credential, final Subject subject, final String securityDomain) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
               public Object run() {
                  SecurityContext sc = SecurityContextAssociation.getSecurityContext();
                  if (sc == null) {
                     try {
                        sc = SecurityContextFactory.createSecurityContext(principal, credential, subject, securityDomain);
                     } catch (Exception var3) {
                        throw new RuntimeException(var3);
                     }
                  }

                  SecurityContextAssociation.setSecurityContext(sc);
                  return null;
               }
            });
         }

         public void pop() {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
               public Object run() {
                  SecurityContextAssociation.clearSecurityContext();
                  return null;
               }
            });
         }
      };
      SubjectActions.PrincipalInfoAction NON_PRIVILEGED = new SubjectActions.PrincipalInfoAction() {
         public void push(Principal principal, Object credential, Subject subject, String securityDomain) {
            SecurityContext sc = SecurityContextAssociation.getSecurityContext();
            if (sc == null) {
               try {
                  sc = SecurityContextFactory.createSecurityContext(principal, credential, subject, securityDomain);
               } catch (Exception var7) {
                  throw new RuntimeException(var7);
               }
            } else {
               sc.getUtil().createSubjectInfo(principal, credential, subject);
            }

            SecurityContextAssociation.setSecurityContext(sc);
         }

         public void pop() {
            SecurityContextAssociation.clearSecurityContext();
         }
      };

      void push(Principal var1, Object var2, Subject var3, String var4);

      void pop();
   }

   private static class SetContextInfoAction implements PrivilegedAction<Object> {
      String key;
      Object value;

      SetContextInfoAction(String key, Object value) {
         this.key = key;
         this.value = value;
      }

      public Object run() {
         SecurityContext sc = SecurityContextAssociation.getSecurityContext();
         if (sc != null) {
            sc.getData().put(this.key, this.value);
         }

         return SecurityContextAssociation.setContextInfo(this.key, this.value);
      }
   }

   private static class SetTCLAction implements PrivilegedAction<Void> {
      ClassLoader cl;

      SetTCLAction(ClassLoader cl) {
         this.cl = cl;
      }

      public Void run() {
         Thread.currentThread().setContextClassLoader(this.cl);
         return null;
      }
   }

   private static class GetTCLAction implements PrivilegedAction<ClassLoader> {
      static PrivilegedAction<ClassLoader> ACTION = new SubjectActions.GetTCLAction();

      public ClassLoader run() {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         return loader;
      }
   }

   private static class LoginContextAction implements PrivilegedExceptionAction<LoginContext> {
      String securityDomain;
      Subject subject;
      CallbackHandler handler;

      LoginContextAction(String securityDomain, Subject subject, CallbackHandler handler) {
         this.securityDomain = securityDomain;
         this.subject = subject;
         this.handler = handler;
      }

      public LoginContext run() throws Exception {
         LoginContext lc = new LoginContext(this.securityDomain, this.subject, this.handler);
         return lc;
      }
   }

   private static class CopySubjectAction implements PrivilegedAction<Object> {
      Subject fromSubject;
      Subject toSubject;
      boolean setReadOnly;
      boolean deepCopy;

      CopySubjectAction(Subject fromSubject, Subject toSubject, boolean setReadOnly) {
         this.fromSubject = fromSubject;
         this.toSubject = toSubject;
         this.setReadOnly = setReadOnly;
      }

      public void setDeepCopy(boolean flag) {
         this.deepCopy = flag;
      }

      public Object run() {
         Set<Principal> principals = this.fromSubject.getPrincipals();
         Set<Principal> principals2 = this.toSubject.getPrincipals();
         Iterator iter = principals.iterator();

         while(iter.hasNext()) {
            principals2.add((Principal)this.getCloneIfNeeded(iter.next()));
         }

         Set<Object> privateCreds = this.fromSubject.getPrivateCredentials();
         Set<Object> privateCreds2 = this.toSubject.getPrivateCredentials();
         Iterator iterCred = privateCreds.iterator();

         while(iterCred.hasNext()) {
            privateCreds2.add(this.getCloneIfNeeded(iter.next()));
         }

         Set<Object> publicCreds = this.fromSubject.getPublicCredentials();
         Set<Object> publicCreds2 = this.toSubject.getPublicCredentials();
         iterCred = publicCreds.iterator();

         while(iterCred.hasNext()) {
            publicCreds2.add(this.getCloneIfNeeded(iter.next()));
         }

         if (this.setReadOnly) {
            this.toSubject.setReadOnly();
         }

         return null;
      }

      private Object getCloneIfNeeded(Object obj) {
         Object clonedObject = null;
         if (this.deepCopy && obj instanceof Cloneable) {
            Class clazz = obj.getClass();

            try {
               Method cloneMethod = clazz.getMethod("clone", (Class[])null);
               clonedObject = cloneMethod.invoke(obj, (Object[])null);
            } catch (Exception var5) {
            }
         }

         if (clonedObject == null) {
            clonedObject = obj;
         }

         return clonedObject;
      }
   }

   private static class GetSubjectAction implements PrivilegedAction<Subject> {
      static PrivilegedAction<Subject> ACTION = new SubjectActions.GetSubjectAction();

      public Subject run() {
         Subject subject = null;

         try {
            subject = (Subject)PolicyContext.getContext("javax.security.auth.Subject.container");
         } catch (PolicyContextException var4) {
            SecurityContext sc = SubjectActions.getSecurityContext();
            subject = sc.getUtil().getSubject();
         }

         return subject;
      }
   }

   private static class ToStringSubjectAction implements PrivilegedAction<String> {
      Subject subject;

      ToStringSubjectAction(Subject subject) {
         this.subject = subject;
      }

      public String run() {
         StringBuffer tmp = new StringBuffer();
         tmp.append("Subject(");
         tmp.append(System.identityHashCode(this.subject));
         tmp.append(").principals=");
         Iterator principals = this.subject.getPrincipals().iterator();

         while(principals.hasNext()) {
            Object p = principals.next();
            Class<?> c = p.getClass();
            tmp.append(c.getName());
            tmp.append('@');
            tmp.append(System.identityHashCode(c));
            tmp.append('(');
            tmp.append(p);
            tmp.append(')');
         }

         return tmp.toString();
      }
   }
}
