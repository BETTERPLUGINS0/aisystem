package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import java.util.Collection;

public interface ACLPersistenceStrategy {
   ACL getACL(Resource var1);

   Collection<ACL> getACLs();

   ACL createACL(Resource var1);

   ACL createACL(Resource var1, Collection<ACLEntry> var2);

   boolean updateACL(ACL var1);

   boolean removeACL(ACL var1);

   boolean removeACL(Resource var1);
}
