package fr.xephi.authme.libs.org.jboss.security.identity.fed;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface WSTrustIdentity<T> extends Identity {
   T getWSTrustToken();

   void setWSTrustToken(T var1);
}
