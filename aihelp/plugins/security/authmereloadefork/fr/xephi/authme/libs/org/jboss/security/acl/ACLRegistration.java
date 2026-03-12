package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import java.util.Collection;

public interface ACLRegistration {
   void registerACL(Resource var1);

   void registerACL(Resource var1, Collection<ACLEntry> var2);

   void deRegisterACL(Resource var1);
}
