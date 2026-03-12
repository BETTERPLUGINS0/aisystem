package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.Driver;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public final class AuthenticationPluginLoader {
   public static AuthenticationPlugin get(String type, Configuration conf) throws SQLException {
      ServiceLoader<AuthenticationPlugin> loader = ServiceLoader.load(AuthenticationPlugin.class, Driver.class.getClassLoader());
      String[] authList = conf.restrictedAuth() != null ? conf.restrictedAuth().split(",") : null;
      Iterator var4 = loader.iterator();

      AuthenticationPlugin implClass;
      do {
         if (!var4.hasNext()) {
            throw new SQLException("Client does not support authentication protocol requested by server. plugin type was = '" + type + "'", "08004", 1251);
         }

         implClass = (AuthenticationPlugin)var4.next();
      } while(!type.equals(implClass.type()));

      if (authList != null) {
         Stream var10000 = Arrays.stream(authList);
         Objects.requireNonNull(type);
         if (!var10000.anyMatch(type::contains)) {
            throw new SQLException(String.format("Client restrict authentication plugin to a limited set of authentication plugin and doesn't permit requested plugin ('%s'). Current list is `restrictedAuth=%s`", type, conf.restrictedAuth()), "08004", 1251);
         }
      }

      return implClass;
   }
}
