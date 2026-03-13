package github.nighter.smartspawner.libs.mariadb.plugin.tls;

import github.nighter.smartspawner.libs.mariadb.Driver;
import github.nighter.smartspawner.libs.mariadb.plugin.TlsSocketPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.tls.main.DefaultTlsSocketPlugin;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ServiceLoader;

public final class TlsSocketPluginLoader {
   public static TlsSocketPlugin get(String type) throws SQLException {
      if (type == null) {
         return new DefaultTlsSocketPlugin();
      } else {
         ServiceLoader<TlsSocketPlugin> loader = ServiceLoader.load(TlsSocketPlugin.class, Driver.class.getClassLoader());
         Iterator var2 = loader.iterator();

         TlsSocketPlugin implClass;
         do {
            if (!var2.hasNext()) {
               throw new SQLException("Client has not found any TLS factory plugin with name '" + type + "'.", "08004", 1251);
            }

            implClass = (TlsSocketPlugin)var2.next();
         } while(!type.equals(implClass.type()));

         return implClass;
      }
   }
}
