package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Set;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;

class SubjectActions {
   static void addCredentials(Subject subject, PasswordCredential cred) {
      SubjectActions.AddCredentialsAction action = new SubjectActions.AddCredentialsAction(subject, cred);
      AccessController.doPrivileged(action);
   }

   static void addPrincipals(Subject subject, Principal p) {
      SubjectActions.AddPrincipalsAction action = new SubjectActions.AddPrincipalsAction(subject, p);
      AccessController.doPrivileged(action);
   }

   static void removeCredentials(Subject subject) {
      SubjectActions.RemoveCredentialsAction action = new SubjectActions.RemoveCredentialsAction(subject);
      AccessController.doPrivileged(action);
   }

   static void addRoles(Subject subject, Set<Principal> runAsRoles) {
      if (System.getSecurityManager() != null) {
         SubjectActions.AddRolesActions.PRIVILEGED.addRoles(subject, runAsRoles);
      } else {
         SubjectActions.AddRolesActions.NON_PRIVILEGED.addRoles(subject, runAsRoles);
      }

   }

   private static Group addSubjectRoles(Subject theSubject, Set<Principal> roles) {
      Set<Group> subjectGroups = theSubject.getPrincipals(Group.class);
      Iterator<Group> iter = subjectGroups.iterator();
      Object roleGrp = null;

      while(iter.hasNext()) {
         Group grp = (Group)iter.next();
         String name = grp.getName();
         if (name.equals("Roles")) {
            roleGrp = grp;
         }
      }

      if (roleGrp == null) {
         roleGrp = new SimpleGroup("Roles");
         theSubject.getPrincipals().add(roleGrp);
      }

      Iterator iter2 = roles.iterator();

      while(iter2.hasNext()) {
         Principal role = (Principal)iter2.next();
         ((Group)roleGrp).addMember(role);
      }

      return (Group)roleGrp;
   }

   static class RemoveCredentialsAction implements PrivilegedAction<Void> {
      Subject subject;

      RemoveCredentialsAction(Subject subject) {
         this.subject = subject;
      }

      public Void run() {
         Iterator i = this.subject.getPrivateCredentials(PasswordCredential.class).iterator();

         while(i.hasNext()) {
            i.remove();
         }

         return null;
      }
   }

   static class AddPrincipalsAction implements PrivilegedAction<Void> {
      Subject subject;
      Principal p;

      AddPrincipalsAction(Subject subject, Principal p) {
         this.subject = subject;
         this.p = p;
      }

      public Void run() {
         this.subject.getPrincipals().add(this.p);
         return null;
      }
   }

   static class AddCredentialsAction implements PrivilegedAction<Void> {
      Subject subject;
      PasswordCredential cred;

      AddCredentialsAction(Subject subject, PasswordCredential cred) {
         this.subject = subject;
         this.cred = cred;
      }

      public Void run() {
         this.subject.getPrivateCredentials().add(this.cred);
         return null;
      }
   }

   interface AddRolesActions {
      SubjectActions.AddRolesActions PRIVILEGED = new SubjectActions.AddRolesActions() {
         public void addRoles(final Subject subject, final Set<Principal> roles) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
               public Void run() {
                  SubjectActions.addSubjectRoles(subject, roles);
                  return null;
               }
            });
         }
      };
      SubjectActions.AddRolesActions NON_PRIVILEGED = new SubjectActions.AddRolesActions() {
         public void addRoles(Subject subject, Set<Principal> roles) {
            SubjectActions.addSubjectRoles(subject, roles);
         }
      };

      void addRoles(Subject var1, Set<Principal> var2);
   }
}
