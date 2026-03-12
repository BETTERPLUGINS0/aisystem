package fr.xephi.authme.libs.org.jboss.security.authorization.config;

import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationModuleEntry {
   private String policyModuleName;
   private ControlFlag controlFlag;
   private Map<String, Object> options = new HashMap();

   public AuthorizationModuleEntry(String name) {
      this.policyModuleName = name;
   }

   public AuthorizationModuleEntry(String name, Map<String, Object> options) {
      this.policyModuleName = name;
      this.options = options;
   }

   public void add(ModuleOption option) {
      this.options.put(option.getName(), option.getValue());
   }

   public String getPolicyModuleName() {
      return this.policyModuleName;
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
      builder.append(this.policyModuleName).append(":").append(this.options);
      builder.append(this.controlFlag).append("}");
      return builder.toString();
   }
}
