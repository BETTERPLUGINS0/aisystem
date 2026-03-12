package fr.xephi.authme.libs.org.jboss.security.config;

import java.util.HashMap;
import java.util.Map;

public enum Element {
   UNKNOWN((String)null),
   POLICY("policy"),
   APPLICATION_POLICY("application-policy"),
   SECURITY_DOMAIN("security-domain"),
   AUTHENTICATION("authentication"),
   AUTHENTICATION_JASPI("authentication-jaspi"),
   AUTHORIZATION("authorization"),
   ACL("acl"),
   ROLE_MAPPING("rolemapping"),
   MAPPING("mapping"),
   AUDIT("audit"),
   IDENTITY_TRUST("identity-trust"),
   ACL_MODULE("acl-module"),
   LOGIN_MODULE("login-module"),
   LOGIN_MODULE_STACK("login-module-stack"),
   AUTH_MODULE("auth-module"),
   PROVIDER_MODULE("provider-module"),
   POLICY_MODULE("policy-module"),
   TRUST_MODULE("trust-module"),
   MAPPING_MODULE("mapping-module"),
   MODULE_OPTION("module-option");

   private final String name;
   private static final Map<String, Element> MAP;

   private Element(String name) {
      this.name = name;
   }

   public String getLocalName() {
      return this.name;
   }

   public static Element forName(String localName) {
      Element element = (Element)MAP.get(localName);
      return element == null ? UNKNOWN : element;
   }

   static {
      Map<String, Element> map = new HashMap();
      Element[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Element element = arr$[i$];
         String name = element.getLocalName();
         if (name != null) {
            map.put(name, element);
         }
      }

      MAP = map;
   }
}
