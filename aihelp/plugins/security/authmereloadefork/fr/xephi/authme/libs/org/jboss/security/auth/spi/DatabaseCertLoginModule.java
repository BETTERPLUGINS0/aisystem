package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.security.acl.Group;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class DatabaseCertLoginModule extends BaseCertLoginModule {
   private static final String DS_JNDI_NAME = "dsJndiName";
   private static final String ROLES_QUERY = "rolesQuery";
   private static final String SUSPEND_RESUME = "suspendResume";
   private static final String TRANSACTION_MANAGER_JNDI_NAME = "transactionManagerJndiName";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"dsJndiName", "rolesQuery", "suspendResume"};
   private String dsJndiName;
   protected String txManagerJndiName = "java:/TransactionManager";
   private String rolesQuery = "select Role, RoleGroup from Roles where PrincipalID=?";
   protected boolean suspendResume = true;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      this.dsJndiName = (String)options.get("dsJndiName");
      if (this.dsJndiName == null) {
         this.dsJndiName = "java:/DefaultDS";
      }

      Object tmp = options.get("rolesQuery");
      if (tmp != null) {
         this.rolesQuery = tmp.toString();
      }

      tmp = options.get("suspendResume");
      if (tmp != null) {
         this.suspendResume = Boolean.valueOf(tmp.toString());
      }

      tmp = options.get("transactionManagerJndiName");
      if (tmp != null) {
         this.txManagerJndiName = tmp.toString();
      }

      PicketBoxLogger.LOGGER.traceDBCertLoginModuleOptions(this.dsJndiName, "", this.rolesQuery, this.suspendResume);
   }

   protected Group[] getRoleSets() throws LoginException {
      String username = this.getUsername();
      Group[] roleSets = Util.getRoleSets(username, this.dsJndiName, this.txManagerJndiName, this.rolesQuery, this, this.suspendResume);
      return roleSets;
   }
}
