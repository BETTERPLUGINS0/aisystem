package fr.xephi.authme.libs.org.jboss.security.mapping.config;

import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingType;
import java.util.HashMap;
import java.util.Map;

public class MappingModuleEntry {
   private final String mappingModuleName;
   private final String mappingModuleType;
   private final Map<String, Object> options;

   public MappingModuleEntry(String name) {
      this(name, new HashMap());
   }

   public MappingModuleEntry(String name, Map<String, Object> options) {
      this(name, options, MappingType.ROLE.toString());
   }

   public MappingModuleEntry(String name, Map<String, Object> options, String type) {
      this.mappingModuleName = name;
      this.mappingModuleType = type;
      this.options = options;
   }

   public void add(ModuleOption option) {
      this.options.put(option.getName(), option.getValue());
   }

   public String getMappingModuleName() {
      return this.mappingModuleName;
   }

   public String getMappingModuleType() {
      return this.mappingModuleType;
   }

   public Map<String, Object> getOptions() {
      return this.options;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName()).append("{");
      builder.append(this.mappingModuleName).append("-").append(this.mappingModuleType);
      builder.append(":").append(this.options);
      builder.append("}");
      return builder.toString();
   }
}
