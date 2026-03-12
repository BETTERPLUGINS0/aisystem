package fr.xephi.authme.libs.org.mariadb.jdbc.util.options;

import java.util.HashMap;
import java.util.Map;

public final class OptionAliases {
   public static final Map<String, String> OPTIONS_ALIASES = new HashMap();

   static {
      OPTIONS_ALIASES.put("clientcertificatekeystoreurl", "keyStore");
      OPTIONS_ALIASES.put("clientcertificatekeystorepassword", "keyStorePassword");
      OPTIONS_ALIASES.put("clientcertificatekeystoretype", "keyStoreType");
   }
}
