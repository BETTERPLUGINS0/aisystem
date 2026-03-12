package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;

public abstract class SecurityContextUtil {
   protected SecurityContext securityContext = null;

   public void setSecurityContext(SecurityContext sc) {
      this.securityContext = sc;
   }

   public abstract String getUserName();

   public abstract Principal getUserPrincipal();

   public abstract Object getCredential();

   public abstract Subject getSubject();

   public abstract SecurityIdentity getSecurityIdentity();

   public abstract void setSecurityIdentity(SecurityIdentity var1);

   public abstract RoleGroup getRoles();

   public abstract void setRoles(RoleGroup var1);

   public void createSubjectInfo(Principal principal, Object credential, Subject subject) {
      SubjectInfo si = new SubjectInfo(principal, credential, subject);
      this.securityContext.setSubjectInfo(si);
   }

   public void createSubjectInfo(Identity identity, Subject theSubject) {
      this.securityContext.setSubjectInfo(new SubjectInfo(identity, theSubject));
   }

   public void addIdentity(Identity id) {
      this.securityContext.getSubjectInfo().addIdentity(id);
   }

   public void clearIdentities(Class<?> clazz) {
      Set<Identity> ids = this.securityContext.getSubjectInfo().getIdentities();
      if (ids != null) {
         Iterator iter = ids.iterator();

         while(iter.hasNext()) {
            Identity id = (Identity)iter.next();
            if (clazz.isAssignableFrom(id.getClass())) {
               this.securityContext.getSubjectInfo().removeIdentity(id);
            }
         }
      }

   }

   public Set<Identity> getIdentities(Class<?> clazz) {
      Set<Identity> resultSet = new HashSet();
      Set<Identity> ids = this.securityContext.getSubjectInfo().getIdentities();
      if (ids != null) {
         Iterator iter = ids.iterator();

         while(iter.hasNext()) {
            Identity id = (Identity)iter.next();
            if (clazz.isAssignableFrom(id.getClass())) {
               resultSet.add(id);
            }
         }
      }

      return resultSet;
   }

   public void setIdentities(Set<Identity> idSet) {
      this.securityContext.getSubjectInfo().setIdentities(idSet);
   }

   public abstract <T> void set(String var1, T var2);

   public abstract <T> T get(String var1);

   public abstract <T> T remove(String var1);
}
