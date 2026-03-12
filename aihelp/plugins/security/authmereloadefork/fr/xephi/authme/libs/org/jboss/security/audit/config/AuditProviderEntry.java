package fr.xephi.authme.libs.org.jboss.security.audit.config;

import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import java.util.HashMap;
import java.util.Map;

public class AuditProviderEntry {
   private String name;
   private Map<String, Object> options = new HashMap();

   public AuditProviderEntry(String name) {
      this.name = name;
   }

   public AuditProviderEntry(String name, Map<String, Object> options) {
      this.name = name;
      this.options = options;
   }

   public String getName() {
      return this.name;
   }

   public void add(ModuleOption option) {
      this.options.put(option.getName(), option.getValue());
   }

   public Map<String, Object> getOptions() {
      return this.options;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString()).append("{").append(this.name).append(":");
      builder.append(this.options).append("}");
      return builder.toString();
   }
}
