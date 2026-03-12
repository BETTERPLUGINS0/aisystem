package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.util.Collection;

public interface ACL {
   boolean addEntry(ACLEntry var1);

   boolean removeEntry(ACLEntry var1);

   Collection<? extends ACLEntry> getEntries();

   ACLEntry getEntry(Identity var1);

   ACLEntry getEntry(String var1);

   Resource getResource();

   boolean isGranted(ACLPermission var1, Identity var2);
}
