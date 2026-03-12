package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

public class DatabaseCallbackHandler extends AbstractCallbackHandler implements CallbackHandler {
   public static final String CONNECTION_URL = "connectionURL";
   public static final String DS_JNDI_NAME = "dsJndiName";
   public static final String DB_DRIVERNAME = "dbDriverName";
   public static final String DB_USERNAME = "dbUserName";
   public static final String DB_USERPASS = "dbUserPass";
   public static final String PRINCIPALS_QUERY = "principalsQuery";
   protected String connectionUrl;
   protected String dsJndiName;
   protected String dsUserName;
   protected String dsUserPass;
   protected String dbDriverName;
   protected String principalsQuery = "select Password from Principals where PrincipalID=?";

   public String getConnectionUrl() {
      return this.connectionUrl;
   }

   public void setConnectionUrl(String connectionUrl) {
      this.connectionUrl = connectionUrl;
   }

   public String getDsJndiName() {
      return this.dsJndiName;
   }

   public void setDsJndiName(String dsJndiName) {
      this.dsJndiName = dsJndiName;
   }

   public String getDsUserName() {
      return this.dsUserName;
   }

   public void setDsUserName(String dsUserName) {
      this.dsUserName = dsUserName;
   }

   public String getDsUserPass() {
      return this.dsUserPass;
   }

   public void setDsUserPass(String dsUserPass) {
      this.dsUserPass = dsUserPass;
   }

   public String getDbDriverName() {
      return this.dbDriverName;
   }

   public void setDbDriverName(String dbDriverName) {
      this.dbDriverName = dbDriverName;
   }

   public String getPrincipalsQuery() {
      return this.principalsQuery;
   }

   public void setPrincipalsQuery(String principalsQuery) {
      this.principalsQuery = principalsQuery;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String theUserName) {
      if (theUserName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("userName");
      } else {
         this.userName = theUserName;
      }
   }

   public void setConfiguration(Map<String, String> config) {
      String tmp = null;
      this.dbDriverName = (String)config.get("dbDriverName");
      this.connectionUrl = (String)config.get("connectionURL");
      if (this.connectionUrl == null || this.connectionUrl.length() == 0) {
         this.dsJndiName = (String)config.get("dsJndiName");
      }

      this.dsUserName = (String)config.get("dbUserName");
      this.dsUserPass = (String)config.get("dbUserPass");
      if (this.dsUserPass != null && SecurityVaultUtil.isVaultFormat(this.dsUserPass)) {
         try {
            this.dsUserPass = SecurityVaultUtil.getValueAsString(this.dsUserPass);
         } catch (SecurityVaultException var4) {
            throw new RuntimeException(var4);
         }
      }

      tmp = (String)config.get("principalsQuery");
      if (tmp != null) {
         this.principalsQuery = tmp;
      }

   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      if (this.userName == null) {
         this.userName = this.getUserName(callbacks);
      }

      for(int i = 0; i < callbacks.length; ++i) {
         Callback callback = callbacks[i];
         this.handleCallBack(callback);
      }

   }

   protected void handleCallBack(Callback c) throws UnsupportedCallbackException, IOException {
      if (c instanceof VerifyPasswordCallback) {
         VerifyPasswordCallback vpc = (VerifyPasswordCallback)c;

         try {
            this.handleVerification(vpc);
         } catch (LoginException var4) {
            throw new IOException(var4);
         }
      }

      if (c instanceof PasswordCallback) {
         PasswordCallback passwdCallback = (PasswordCallback)c;
         passwdCallback.setPassword(this.getPassword().toCharArray());
      }
   }

   protected void handleVerification(VerifyPasswordCallback vpc) throws LoginException {
      String userPass = vpc.getValue();
      String passwordFromDB = this.getPassword();
      if (userPass.equals(passwordFromDB)) {
         vpc.setVerified(true);
      } else {
         throw new LoginException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
      }
   }

   private String getPassword() {
      String password = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         conn = this.getConnection();
         ps = conn.prepareStatement(this.principalsQuery);
         ps.setString(1, this.userName);
         rs = ps.executeQuery();
         if (!rs.next()) {
            throw PicketBoxMessages.MESSAGES.unableToFindPrincipalInDB(this.userName);
         }

         password = rs.getString(1);
      } catch (Exception var9) {
         throw new RuntimeException(var9);
      } finally {
         this.safeClose(rs);
         this.safeClose((Statement)ps);
         this.safeClose(conn);
      }

      return password;
   }

   private Connection getConnection() throws SQLException, NamingException {
      Connection conn = null;
      if (this.dbDriverName != null) {
         try {
            Class.forName(this.dbDriverName);
         } catch (ClassNotFoundException var4) {
            throw new RuntimeException(var4);
         }
      }

      if (this.connectionUrl != null) {
         if (this.dsUserName != null) {
            conn = DriverManager.getConnection(this.connectionUrl, this.dsUserName, this.dsUserPass);
         } else {
            conn = DriverManager.getConnection(this.connectionUrl);
         }
      } else {
         InitialContext ic = new InitialContext();
         if (this.dsJndiName == null) {
            throw PicketBoxMessages.MESSAGES.unableToLookupDataSource();
         }

         DataSource ds = (DataSource)ic.lookup(this.dsJndiName);
         if (ds != null) {
            conn = ds.getConnection();
         }
      }

      return conn;
   }

   protected void safeClose(ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (SQLException var3) {
         }
      }

   }

   protected void safeClose(Connection conn) {
      if (conn != null) {
         try {
            conn.close();
         } catch (SQLException var3) {
         }
      }

   }

   protected void safeClose(Statement stat) {
      if (stat != null) {
         try {
            stat.close();
         } catch (SQLException var3) {
         }
      }

   }
}
