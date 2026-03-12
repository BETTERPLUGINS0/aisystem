package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface ACLEntry {
   String getIdentityOrRole();

   Identity getIdentity();

   ACLPermission getPermission();

   boolean checkPermission(ACLPermission var1);
}
