package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class SimpleUsersLoginModule extends PropertiesUsersLoginModule {
   protected static Set<String> invalidProperties = new HashSet();

   public SimpleUsersLoginModule() {
      invalidProperties.add("usersProperties");
      invalidProperties.add("defaultUsersProperties");
      invalidProperties.add("rolesProperties");
      invalidProperties.add("defaultRolesProperties");
      invalidProperties.add("roleGroupSeperator");
      invalidProperties.add("digestCallback");
      invalidProperties.add("storeDigestCallback");
      invalidProperties.add("legacyCreatePasswordHash");
      invalidProperties.add("inputValidator");
      invalidProperties.add("hashAlgorithm");
      invalidProperties.add("hashEncoding");
      invalidProperties.add("hashCharset");
      invalidProperties.add("hashStorePassword");
      invalidProperties.add("hashUserPassword");
      invalidProperties.add("ignorePasswordCase");
      invalidProperties.add("throwValidateError");
      invalidProperties.add("jboss.security.security_domain");
      invalidProperties.add("password-stacking");
      invalidProperties.add("principalClass");
      invalidProperties.add("unauthenticatedIdentity");
   }

   protected Properties createUsers(Map<String, ?> options) throws IOException {
      Properties properties = new Properties();
      Iterator i$ = options.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ?> entry = (Entry)i$.next();
         String key = (String)entry.getKey();
         Object value = entry.getValue();
         if (value != null && this.isValidEntry(key)) {
            properties.put(key, value);
         }
      }

      return properties;
   }

   protected boolean isValidEntry(String key) {
      return !invalidProperties.contains(key);
   }

   protected void checkOptions() {
   }
}
