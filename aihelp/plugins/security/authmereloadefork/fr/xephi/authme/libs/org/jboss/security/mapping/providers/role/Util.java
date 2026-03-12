package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PrivilegedActionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class Util {
   static Properties loadProperties(String propertiesName) throws IOException {
      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = null;
      if (loader instanceof URLClassLoader) {
         URLClassLoader ucl = (URLClassLoader)loader;
         url = SecurityActions.findResource(ucl, propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }

      if (url == null) {
         url = loader.getResource(propertiesName);
      }

      File tmp;
      if (url == null) {
         try {
            url = new URL(propertiesName);
         } catch (MalformedURLException var13) {
            PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(var13);
            tmp = new File(propertiesName);
            if (tmp.exists()) {
               url = tmp.toURI().toURL();
            }
         }
      }

      if (url == null) {
         throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
      } else {
         Properties defaults = new Properties();
         bundle = new Properties(defaults);
         tmp = null;

         InputStream is;
         try {
            is = SecurityActions.openStream(url);
         } catch (PrivilegedActionException var12) {
            throw new IOException(var12.getLocalizedMessage());
         }

         if (is != null) {
            try {
               bundle.load(is);
            } finally {
               safeClose(is);
            }

            PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
            return bundle;
         } else {
            throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
         }
      }
   }

   static void addRolesToGroup(String username, RoleGroup roleGroup, Properties roles) {
      String[] roleNames = null;
      if (roles.containsKey(username)) {
         String value = roles.getProperty(username);
         PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, roleGroup.getRoleName());
         roleNames = parseRoles(value);
      }

      if (roleNames != null) {
         for(int i = 0; i < roleNames.length; ++i) {
            roleGroup.addRole(new SimpleRole(roleNames[i]));
         }
      }

   }

   static String[] parseRoles(String roles) {
      return roles.split(",");
   }

   static void addRolesToGroup(String username, RoleGroup roleGroup, String dsJndiName, String rolesQuery, boolean suspendResume, TransactionManager tm) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      if (suspendResume && tm == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
      } else {
         Transaction tx = null;
         if (suspendResume) {
            try {
               tx = tm.suspend();
            } catch (SystemException var33) {
               throw new RuntimeException(var33);
            }
         }

         try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(dsJndiName);
            conn = ds.getConnection();
            PicketBoxLogger.LOGGER.traceExecuteQuery(rolesQuery, username);
            ps = conn.prepareStatement(rolesQuery);

            try {
               ps.setString(1, username);
            } catch (ArrayIndexOutOfBoundsException var32) {
            }

            rs = ps.executeQuery();
            if (!rs.next()) {
               PicketBoxLogger.LOGGER.traceQueryWithEmptyResult();
            } else {
               do {
                  String name = rs.getString(1);
                  roleGroup.addRole(new SimpleRole(name));
               } while(rs.next());
            }
         } catch (NamingException var34) {
            throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(dsJndiName), var34);
         } catch (SQLException var35) {
            throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage(), var35);
         } finally {
            if (rs != null) {
               try {
                  rs.close();
               } catch (SQLException var31) {
               }
            }

            if (ps != null) {
               try {
                  ps.close();
               } catch (SQLException var30) {
               }
            }

            if (conn != null) {
               try {
                  conn.close();
               } catch (Exception var29) {
               }
            }

            if (suspendResume) {
               try {
                  tm.resume(tx);
               } catch (Exception var28) {
                  throw new RuntimeException(var28);
               }
            }

         }

      }
   }

   private static void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var2) {
      }

   }
}
