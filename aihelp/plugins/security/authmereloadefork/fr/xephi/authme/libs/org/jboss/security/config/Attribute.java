package fr.xephi.authme.libs.org.jboss.security.config;

import java.util.HashMap;
import java.util.Map;

public enum Attribute {
   UNKNOWN((String)null),
   NAME("name"),
   EXTENDS("extends"),
   CODE("code"),
   FLAG("flag"),
   VALUE("value"),
   TYPE("type"),
   LOGIN_MODULE_STACK_REF("login-module-stack-ref");

   private final String name;
   private static final Map<String, Attribute> MAP;

   private Attribute(String name) {
      this.name = name;
   }

   public String getLocalName() {
      return this.name;
   }

   public static Attribute forName(String localName) {
      Attribute element = (Attribute)MAP.get(localName);
      return element == null ? UNKNOWN : element;
   }

   public String toString() {
      return this.getLocalName();
   }

   static {
      Map<String, Attribute> map = new HashMap();
      Attribute[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Attribute element = arr$[i$];
         String name = element.getLocalName();
         if (name != null) {
            map.put(name, element);
         }
      }

      MAP = map;
   }
}
