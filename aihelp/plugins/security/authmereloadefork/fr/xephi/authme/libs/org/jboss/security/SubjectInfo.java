package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.IdentityFactory;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.extensions.CredentialIdentityFactory;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;

public class SubjectInfo implements Serializable {
   private static final long serialVersionUID = 1L;
   private Subject authenticatedSubject;
   private RoleGroup roles;
   private Set<Identity> identities;

   SubjectInfo(Principal principal, Object credential, Subject subject) {
      this.addIdentity(IdentityFactory.getIdentity(principal, credential));
      this.authenticatedSubject = subject;
   }

   SubjectInfo(Identity identity, Subject theSubject) {
      this.addIdentity(identity);
      this.authenticatedSubject = theSubject;
   }

   public Subject getAuthenticatedSubject() {
      return this.authenticatedSubject;
   }

   public void setAuthenticatedSubject(Subject authenticatedSubject) {
      this.authenticatedSubject = authenticatedSubject;
   }

   public RoleGroup getRoles() {
      return this.roles;
   }

   public void setRoles(RoleGroup roles) {
      this.roles = roles;
   }

   public synchronized void addIdentity(Identity id) {
      if (this.identities == null) {
         this.identities = new HashSet();
      }

      if (id != null) {
         Identity identity = (Identity)this.getIdentity(id.getClass());
         if (identity == CredentialIdentityFactory.NULL_IDENTITY) {
            this.removeIdentity(identity);
         }
      }

      this.identities.add(id);
   }

   public synchronized <T> T getIdentity(Class<T> clazz) {
      if (clazz == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("clazz");
      } else {
         if (this.identities != null) {
            Iterator i$ = this.identities.iterator();

            while(i$.hasNext()) {
               Identity id = (Identity)i$.next();
               if (id == null) {
                  break;
               }

               Class<?> idClass = id.getClass();
               if (clazz.isAssignableFrom(idClass)) {
                  return id;
               }
            }
         }

         return null;
      }
   }

   public Set<Identity> getIdentities() {
      return Collections.unmodifiableSet(this.identities);
   }

   public synchronized void setIdentities(Set<Identity> ids) {
      if (this.identities == null) {
         this.identities = new HashSet();
      }

      this.identities.addAll(ids);
   }

   public synchronized void removeIdentity(Identity id) {
      this.identities.remove(id);
   }

   public synchronized String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Identities=" + this.identities);
      builder.append(" Subject=" + this.authenticatedSubject);
      builder.append(" Roles=" + this.roles);
      return builder.toString();
   }
}
