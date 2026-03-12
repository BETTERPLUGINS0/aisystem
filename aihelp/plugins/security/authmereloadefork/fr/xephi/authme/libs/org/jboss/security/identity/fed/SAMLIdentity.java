package fr.xephi.authme.libs.org.jboss.security.identity.fed;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface SAMLIdentity<T> extends Identity {
   T getSAMLObject();

   void setSAMLObject(T var1);
}
