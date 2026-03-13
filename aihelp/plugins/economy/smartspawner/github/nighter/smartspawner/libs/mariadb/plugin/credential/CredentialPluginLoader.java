package github.nighter.smartspawner.libs.mariadb.plugin.credential;

import github.nighter.smartspawner.libs.mariadb.Driver;
import github.nighter.smartspawner.libs.mariadb.plugin.CredentialPlugin;
import java.util.Iterator;
import java.util.ServiceLoader;

public final class CredentialPluginLoader {
   public static CredentialPlugin get(String type) {
      if (type == null) {
         return null;
      } else {
         ServiceLoader<CredentialPlugin> loader = ServiceLoader.load(CredentialPlugin.class, Driver.class.getClassLoader());
         Iterator var2 = loader.iterator();

         CredentialPlugin implClass;
         do {
            if (!var2.hasNext()) {
               throw new IllegalArgumentException("No identity plugin registered with the type \"" + type + "\".");
            }

            implClass = (CredentialPlugin)var2.next();
         } while(!type.equals(implClass.type()));

         return implClass;
      }
   }
}
