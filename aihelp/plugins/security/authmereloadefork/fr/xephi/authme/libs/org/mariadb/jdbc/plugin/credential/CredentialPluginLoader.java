package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.credential;

import fr.xephi.authme.libs.org.mariadb.jdbc.Driver;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ServiceLoader;

public final class CredentialPluginLoader {
   public static CredentialPlugin get(String type) throws SQLException {
      if (type == null) {
         return null;
      } else {
         ServiceLoader<CredentialPlugin> loader = ServiceLoader.load(CredentialPlugin.class, Driver.class.getClassLoader());
         Iterator var2 = loader.iterator();

         CredentialPlugin implClass;
         do {
            if (!var2.hasNext()) {
               throw new SQLException("No identity plugin registered with the type \"" + type + "\".", "08004", 1251);
            }

            implClass = (CredentialPlugin)var2.next();
         } while(!type.equals(implClass.type()));

         return implClass;
      }
   }
}
