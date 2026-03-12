package fr.xephi.authme.libs.org.jboss.security.identity.fed;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface OpenIdIdentity<T> extends Identity {
   T getOpenIdObject();

   void setOpenIdObject(T var1);
}
