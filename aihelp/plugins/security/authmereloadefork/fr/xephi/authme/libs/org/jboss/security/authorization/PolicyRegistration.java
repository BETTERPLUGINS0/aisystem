package fr.xephi.authme.libs.org.jboss.security.authorization;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface PolicyRegistration {
   String XACML = "XACML";
   String JACC = "JACC";
   String CUSTOM = "CUSTOM";

   void registerPolicy(String var1, String var2, URL var3);

   void registerPolicy(String var1, String var2, InputStream var3);

   void registerPolicyConfigFile(String var1, String var2, InputStream var3);

   <P> void registerPolicyConfig(String var1, String var2, P var3);

   void deRegisterPolicy(String var1, String var2);

   <T> T getPolicy(String var1, String var2, Map<String, Object> var3);
}
