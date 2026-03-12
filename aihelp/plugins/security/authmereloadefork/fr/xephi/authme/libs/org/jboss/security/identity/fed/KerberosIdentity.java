package fr.xephi.authme.libs.org.jboss.security.identity.fed;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface KerberosIdentity<T> extends Identity {
   T getKerberosToken();

   void setKerberosToken(T var1);
}
