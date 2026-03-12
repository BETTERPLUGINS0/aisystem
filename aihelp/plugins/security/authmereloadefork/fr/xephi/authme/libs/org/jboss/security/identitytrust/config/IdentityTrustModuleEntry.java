package fr.xephi.authme.libs.org.jboss.security.identitytrust.config;

import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import java.util.HashMap;
import java.util.Map;

public class IdentityTrustModuleEntry {
   private String name;
   private ControlFlag controlFlag;
   private Map<String, Object> options = new HashMap();

   public IdentityTrustModuleEntry(String name) {
      this.name = name;
   }

   public IdentityTrustModuleEntry(String name, Map<String, Object> options) {
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

   public ControlFlag getControlFlag() {
      return this.controlFlag;
   }

   public void setControlFlag(ControlFlag controlFlag) {
      this.controlFlag = controlFlag;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName()).append("{");
      builder.append(this.name).append(":").append(this.options);
      builder.append("}");
      return builder.toString();
   }
}
