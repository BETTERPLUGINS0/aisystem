package fr.xephi.authme.libs.org.jboss.security.auth.container.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.login.LoginModuleStackHolder;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.ModuleOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthModuleEntry {
   private ControlFlag controlFlag;
   private Map<String, Object> options;
   private String name;
   private LoginModuleStackHolder loginModuleStackHolder;
   private String loginModuleStackHolderName;

   public AuthModuleEntry(String authModuleName, Map<String, Object> options, String loginModuleStackHolderName) {
      this.controlFlag = ControlFlag.REQUIRED;
      this.options = new HashMap();
      this.name = null;
      this.loginModuleStackHolder = null;
      this.loginModuleStackHolderName = null;
      this.name = authModuleName;
      if (options != null) {
         this.options = options;
      }

      this.loginModuleStackHolderName = loginModuleStackHolderName;
   }

   public String getAuthModuleName() {
      return this.name;
   }

   public void addOption(ModuleOption option) {
      if (option == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("option");
      } else {
         this.options.put(option.getName(), option.getValue());
      }
   }

   public Map<String, Object> getOptions() {
      return Collections.unmodifiableMap(this.options);
   }

   public void setOptions(Map<String, Object> options) {
      if (options == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("options");
      } else {
         this.options = options;
      }
   }

   public LoginModuleStackHolder getLoginModuleStackHolder() {
      return this.loginModuleStackHolder;
   }

   public void setLoginModuleStackHolder(LoginModuleStackHolder loginModuleStackHolder) {
      if (loginModuleStackHolder == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("loginModuleStackHolder");
      } else {
         this.loginModuleStackHolder = loginModuleStackHolder;
         this.loginModuleStackHolderName = this.loginModuleStackHolder.getName();
      }
   }

   public String getLoginModuleStackHolderName() {
      return this.loginModuleStackHolderName;
   }

   public void setLoginModuleStackHolderName(String loginModuleStackHolderName) {
      if (loginModuleStackHolderName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("loginModuleStackHolderName");
      } else {
         this.loginModuleStackHolderName = loginModuleStackHolderName;
      }
   }

   public ControlFlag getControlFlag() {
      return this.controlFlag;
   }

   public void setControlFlag(ControlFlag flag) {
      this.controlFlag = flag;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName()).append("{");
      builder.append(this.name).append(":").append(this.options);
      builder.append("}");
      return builder.toString();
   }
}
