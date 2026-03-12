package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.plugins.TransactionManagerLocator;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

class DbUtil {
   static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName, String rolesQuery, AbstractServerLoginModule aslm, boolean suspendResume) throws LoginException {
      Connection conn = null;
      HashMap<String, Group> setsMap = new HashMap();
      PreparedStatement ps = null;
      ResultSet rs = null;
      TransactionManager tm = null;
      if (suspendResume) {
         TransactionManagerLocator tml = new TransactionManagerLocator();

         try {
            tm = tml.getTM(txManagerJndiName);
         } catch (NamingException var46) {
            throw new RuntimeException(var46);
         }

         if (tm == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
         }
      }

      Transaction tx = null;
      if (suspendResume) {
         try {
            tx = tm.suspend();
         } catch (SystemException var45) {
            throw new RuntimeException(var45);
         }
      }

      label280: {
         Group[] var15;
         try {
            LoginException le;
            try {
               InitialContext ctx = new InitialContext();
               DataSource ds = (DataSource)ctx.lookup(dsJndiName);
               conn = ds.getConnection();
               PicketBoxLogger.LOGGER.traceExecuteQuery(rolesQuery, username);
               ps = conn.prepareStatement(rolesQuery);

               try {
                  ps.setString(1, username);
               } catch (ArrayIndexOutOfBoundsException var44) {
               }

               rs = ps.executeQuery();
               if (rs.next()) {
                  while(true) {
                     String name = rs.getString(1);
                     String groupName = rs.getString(2);
                     if (groupName == null || groupName.length() == 0) {
                        groupName = "Roles";
                     }

                     Group group = (Group)setsMap.get(groupName);
                     if (group == null) {
                        group = new SimpleGroup(groupName);
                        setsMap.put(groupName, group);
                     }

                     try {
                        Principal p = aslm.createIdentity(name);
                        ((Group)group).addMember(p);
                     } catch (Exception var43) {
                        PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(name, var43);
                     }

                     if (!rs.next()) {
                        break label280;
                     }
                  }
               }

               if (aslm.getUnauthenticatedIdentity() == null) {
                  throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInRoles();
               }

               Group[] roleSets = new Group[]{new SimpleGroup("Roles")};
               var15 = roleSets;
            } catch (NamingException var47) {
               le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(dsJndiName));
               le.initCause(var47);
               throw le;
            } catch (SQLException var48) {
               le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
               le.initCause(var48);
               throw le;
            }
         } finally {
            if (rs != null) {
               try {
                  rs.close();
               } catch (SQLException var42) {
               }
            }

            if (ps != null) {
               try {
                  ps.close();
               } catch (SQLException var41) {
               }
            }

            if (conn != null) {
               try {
                  conn.close();
               } catch (Exception var40) {
               }
            }

            if (suspendResume) {
               try {
                  tm.resume(tx);
               } catch (Exception var39) {
                  throw new RuntimeException(var39);
               }
            }

         }

         return var15;
      }

      Group[] roleSets = new Group[setsMap.size()];
      setsMap.values().toArray(roleSets);
      return roleSets;
   }
}
