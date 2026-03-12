package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;

public interface PersistenceStrategy {
   Identity persistIdentity(Identity var1);

   Identity getIdentity(String var1);

   Identity updateIdentity(Identity var1);

   boolean removeIdentity(Identity var1);
}
