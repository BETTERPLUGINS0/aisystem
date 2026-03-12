package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class SimpleRolesMappingProvider extends PropertiesRolesMappingProvider {
   protected Map<String, Object> options;

   public void init(Map<String, Object> options) {
      this.options = options;
      if (options != null) {
         try {
            this.roles = this.loadRoles();
         } catch (IOException var3) {
            throw new IllegalStateException(var3);
         }
      }

   }

   protected Properties loadRoles() throws IOException {
      this.roles = new Properties();
      Iterator i$ = this.options.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Object> entry = (Entry)i$.next();
         String key = (String)entry.getKey();
         if (this.isValidEntry(key)) {
            this.roles.put(key, entry.getValue());
         }
      }

      return this.roles;
   }

   protected boolean isValidEntry(String key) {
      return !key.equals("rolesProperties");
   }
}
