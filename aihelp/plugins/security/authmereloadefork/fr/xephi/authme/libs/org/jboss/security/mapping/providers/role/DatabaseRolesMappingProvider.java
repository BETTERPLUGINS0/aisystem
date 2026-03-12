package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.plugins.TransactionManagerLocator;
import java.security.Principal;
import java.util.Map;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

public class DatabaseRolesMappingProvider extends AbstractRolesMappingProvider {
   protected String dsJndiName;
   protected String rolesQuery;
   protected boolean suspendResume = true;
   protected String TX_MGR_JNDI_NAME = "java:/TransactionManager";
   protected TransactionManager tm = null;

   public void init(Map<String, Object> options) {
      if (options != null) {
         this.dsJndiName = (String)options.get("dsJndiName");
         if (this.dsJndiName == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("dsJndiName");
         }

         this.rolesQuery = (String)options.get("rolesQuery");
         if (this.rolesQuery == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("rolesQuery");
         }

         String option = (String)options.get("suspendResume");
         if (option != null) {
            this.suspendResume = Boolean.valueOf(option.toString());
         }

         option = (String)options.get("transactionManagerJndiName");
         if (option != null) {
            this.TX_MGR_JNDI_NAME = option;
         }

         try {
            if (this.suspendResume) {
               this.tm = this.getTransactionManager();
            }
         } catch (NamingException var4) {
            throw PicketBoxMessages.MESSAGES.failedToGetTransactionManager(var4);
         }
      }

   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      if (contextMap != null && !contextMap.isEmpty()) {
         Principal principal = this.getCallerPrincipal(contextMap);
         if (principal != null && this.rolesQuery != null) {
            String username = principal.getName();
            Util.addRolesToGroup(username, mappedObject, this.dsJndiName, this.rolesQuery, this.suspendResume, this.tm);
            this.result.setMappedObject(mappedObject);
         }

      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
      }
   }

   protected TransactionManager getTransactionManager() throws NamingException {
      TransactionManagerLocator tml = new TransactionManagerLocator();
      return tml.getTM(this.TX_MGR_JNDI_NAME);
   }
}
