package fr.xephi.authme.libs.org.jboss.security.identity.extensions;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface CredentialIdentity<T> extends Identity {
   T getCredential();

   void setCredential(T var1);
}
