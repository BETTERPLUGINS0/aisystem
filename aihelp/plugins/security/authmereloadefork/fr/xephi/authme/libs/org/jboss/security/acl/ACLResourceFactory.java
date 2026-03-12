package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;

public interface ACLResourceFactory {
   Resource instantiateResource(String var1, Object var2);
}
