package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.SubjectInfo;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.PolicyContextHandler;

public class SubjectPolicyContextHandler implements PolicyContextHandler {
   public static final HashSet<Object> EMPTY_SET = new HashSet();

   public Object getContext(String key, Object data) throws PolicyContextException {
      if (!key.equalsIgnoreCase("javax.security.auth.Subject.container")) {
         return null;
      } else {
         Subject subject = (Subject)AccessController.doPrivileged(SubjectPolicyContextHandler.GetSubjectAction.ACTION);
         return subject;
      }
   }

   public String[] getKeys() throws PolicyContextException {
      String[] keys = new String[]{"javax.security.auth.Subject.container"};
      return keys;
   }

   public boolean supports(String key) throws PolicyContextException {
      return key.equalsIgnoreCase("javax.security.auth.Subject.container");
   }

   private static class GetSubjectAction implements PrivilegedAction<Subject> {
      static PrivilegedAction<Subject> ACTION = new SubjectPolicyContextHandler.GetSubjectAction();

      public Subject run() {
         Subject theSubject = null;
         SecurityContext sc = SecurityContextAssociation.getSecurityContext();
         if (sc != null) {
            SubjectInfo si = sc.getSubjectInfo();
            if (si != null) {
               Subject activeSubject = si.getAuthenticatedSubject();
               RunAsIdentity callerRunAsIdentity = (RunAsIdentity)sc.getIncomingRunAs();
               Set principalsSet;
               if (activeSubject != null) {
                  if (callerRunAsIdentity == null) {
                     principalsSet = activeSubject.getPrincipals();
                  } else {
                     principalsSet = callerRunAsIdentity.getPrincipalsSet();
                  }

                  theSubject = new Subject(true, principalsSet, activeSubject.getPublicCredentials(), activeSubject.getPrivateCredentials());
               } else if (callerRunAsIdentity != null) {
                  principalsSet = callerRunAsIdentity.getPrincipalsSet();
                  theSubject = new Subject(true, principalsSet, SubjectPolicyContextHandler.EMPTY_SET, SubjectPolicyContextHandler.EMPTY_SET);
               }
            }
         }

         return theSubject;
      }
   }
}
