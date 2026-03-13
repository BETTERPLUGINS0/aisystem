package github.nighter.smartspawner.libs.mariadb.util.options;

import java.util.HashMap;
import java.util.Map;

public final class OptionAliases {
   public static final Map<String, String> OPTIONS_ALIASES = new HashMap();

   static {
      OPTIONS_ALIASES.put("clientcertificatekeystoreurl", "keyStore");
      OPTIONS_ALIASES.put("clientcertificatekeystorepassword", "keyStorePassword");
      OPTIONS_ALIASES.put("clientcertificatekeystoretype", "keyStoreType");
      OPTIONS_ALIASES.put("trustcertificatekeystoreurl", "trustStore");
      OPTIONS_ALIASES.put("trustcertificatekeystorepassword", "trustStorePassword");
      OPTIONS_ALIASES.put("trustcertificatekeystoretype", "trustStoreType");
      OPTIONS_ALIASES.put("nullcatalogmeanscurrent", "nullDatabaseMeansCurrent");
      OPTIONS_ALIASES.put("databaseterm", "useCatalogTerm");
   }
}
