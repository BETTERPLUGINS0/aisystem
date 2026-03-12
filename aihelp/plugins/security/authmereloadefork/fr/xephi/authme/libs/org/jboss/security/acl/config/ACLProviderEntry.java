package fr.xephi.authme.libs.org.jboss.security.acl.config;

import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import java.util.HashMap;
import java.util.Map;

public class ACLProviderEntry {
   private String aclProviderName;
   private ControlFlag controlFlag;
   private Map<String, Object> options = new HashMap();

   public ACLProviderEntry(String name) {
      this.aclProviderName = name;
   }

   public ACLProviderEntry(String name, Map<String, Object> options) {
      this.aclProviderName = name;
      this.options = options;
   }

   public void add(ModuleOption option) {
      this.options.put(option.getName(), option.getValue());
   }

   public String getAclProviderName() {
      return this.aclProviderName;
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
      builder.append(super.toString());
      builder.append("{").append(this.aclProviderName).append(":");
      builder.append(this.controlFlag).append(":").append(this.options).append("}");
      return builder.toString();
   }
}
