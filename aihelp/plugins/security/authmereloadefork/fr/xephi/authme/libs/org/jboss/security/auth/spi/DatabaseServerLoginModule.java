package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.plugins.TransactionManagerLocator;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class DatabaseServerLoginModule extends UsernamePasswordLoginModule {
   private static final String DS_JNDI_NAME = "dsJndiName";
   private static final String ROLES_QUERY = "rolesQuery";
   private static final String SUSPEND_RESUME = "suspendResume";
   private static final String PRINCIPALS_QUERY = "principalsQuery";
   private static final String TRANSACTION_MANAGER_JNDI_NAME = "transactionManagerJndiName";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"dsJndiName", "rolesQuery", "suspendResume", "principalsQuery", "transactionManagerJndiName"};
   protected String dsJndiName;
   protected String principalsQuery = "select Password from Principals where PrincipalID=?";
   protected String rolesQuery;
   protected boolean suspendResume = true;
   protected String txManagerJndiName = "java:/TransactionManager";
   protected TransactionManager tm = null;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      this.dsJndiName = (String)options.get("dsJndiName");
      if (this.dsJndiName == null) {
         this.dsJndiName = "java:/DefaultDS";
      }

      Object tmp = options.get("principalsQuery");
      if (tmp != null) {
         this.principalsQuery = tmp.toString();
      }

      tmp = options.get("rolesQuery");
      if (tmp != null) {
         this.rolesQuery = tmp.toString();
      }

      tmp = options.get("suspendResume");
      if (tmp != null) {
         this.suspendResume = Boolean.valueOf(tmp.toString());
      }

      String jname = (String)options.get("transactionManagerJndiName");
      if (jname != null) {
         this.txManagerJndiName = jname;
      }

      PicketBoxLogger.LOGGER.traceDBCertLoginModuleOptions(this.dsJndiName, this.principalsQuery, this.rolesQuery, this.suspendResume);

      try {
         if (this.suspendResume) {
            this.tm = this.getTransactionManager();
         }

      } catch (NamingException var8) {
         throw PicketBoxMessages.MESSAGES.failedToGetTransactionManager(var8);
      }
   }

   protected String getUsersPassword() throws LoginException {
      String username = this.getUsername();
      String password = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      Transaction tx = null;
      if (this.suspendResume) {
         try {
            if (this.tm == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
            }

            tx = this.tm.suspend();
         } catch (SystemException var27) {
            throw new RuntimeException(var27);
         }
      }

      try {
         LoginException le;
         try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(this.dsJndiName);
            conn = ds.getConnection();
            PicketBoxLogger.LOGGER.traceExecuteQuery(this.principalsQuery, username);
            ps = conn.prepareStatement(this.principalsQuery);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (!rs.next()) {
               throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInPrincipals();
            }

            password = rs.getString(1);
            password = this.convertRawPassword(password);
         } catch (NamingException var28) {
            le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(this.dsJndiName));
            le.initCause(var28);
            throw le;
         } catch (SQLException var29) {
            le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
            le.initCause(var29);
            throw le;
         }
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException var26) {
            }
         }

         if (ps != null) {
            try {
               ps.close();
            } catch (SQLException var25) {
            }
         }

         if (conn != null) {
            try {
               conn.close();
            } catch (SQLException var24) {
            }
         }

         if (this.suspendResume) {
            try {
               this.tm.resume(tx);
            } catch (Exception var23) {
               throw new RuntimeException(var23);
            }
         }

      }

      return password;
   }

   protected Group[] getRoleSets() throws LoginException {
      if (this.rolesQuery != null) {
         String username = this.getUsername();
         PicketBoxLogger.LOGGER.traceExecuteQuery(this.rolesQuery, username);
         Group[] roleSets = Util.getRoleSets(username, this.dsJndiName, this.txManagerJndiName, this.rolesQuery, this, this.suspendResume);
         return roleSets;
      } else {
         return new Group[0];
      }
   }

   protected String convertRawPassword(String rawPassword) {
      return rawPassword;
   }

   protected TransactionManager getTransactionManager() throws NamingException {
      TransactionManagerLocator tml = new TransactionManagerLocator();
      return tml.getTM(this.txManagerJndiName);
   }
}
