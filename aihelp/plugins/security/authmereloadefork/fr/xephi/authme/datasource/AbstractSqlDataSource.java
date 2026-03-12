package fr.xephi.authme.datasource;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.columnshandler.AuthMeColumns;
import fr.xephi.authme.datasource.columnshandler.AuthMeColumnsHandler;
import fr.xephi.authme.datasource.columnshandler.DataSourceColumn;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValueImpl;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.UpdateValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.AlwaysTruePredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.Predicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.StandardPredicates;
import fr.xephi.authme.security.crypts.HashedPassword;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSqlDataSource implements DataSource {
   protected AuthMeColumnsHandler columnsHandler;

   public boolean isAuthAvailable(String user) {
      try {
         return this.columnsHandler.retrieve((String)user, (DataSourceColumn)AuthMeColumns.NAME).rowExists();
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return false;
      }
   }

   public HashedPassword getPassword(String user) {
      try {
         DataSourceValues values = this.columnsHandler.retrieve(user, AuthMeColumns.PASSWORD, AuthMeColumns.SALT);
         if (values.rowExists()) {
            return new HashedPassword((String)values.get(AuthMeColumns.PASSWORD), (String)values.get(AuthMeColumns.SALT));
         }
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
      }

      return null;
   }

   public boolean saveAuth(PlayerAuth auth) {
      return this.columnsHandler.insert(auth, AuthMeColumns.NAME, AuthMeColumns.NICK_NAME, AuthMeColumns.PASSWORD, AuthMeColumns.SALT, AuthMeColumns.EMAIL, AuthMeColumns.REGISTRATION_DATE, AuthMeColumns.REGISTRATION_IP, AuthMeColumns.UUID);
   }

   public boolean hasSession(String user) {
      try {
         DataSourceValue<Integer> result = this.columnsHandler.retrieve(user, AuthMeColumns.HAS_SESSION);
         return result.rowExists() && Integer.valueOf(1).equals(result.getValue());
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return false;
      }
   }

   public boolean updateSession(PlayerAuth auth) {
      return this.columnsHandler.update(auth, AuthMeColumns.LAST_IP, AuthMeColumns.LAST_LOGIN, AuthMeColumns.NICK_NAME);
   }

   public boolean updatePassword(PlayerAuth auth) {
      return this.updatePassword(auth.getNickname(), auth.getPassword());
   }

   public boolean updatePassword(String user, HashedPassword password) {
      return this.columnsHandler.update(user, UpdateValues.with(AuthMeColumns.PASSWORD, password.getHash()).and(AuthMeColumns.SALT, password.getSalt()).build());
   }

   public boolean updateQuitLoc(PlayerAuth auth) {
      return this.columnsHandler.update(auth, AuthMeColumns.LOCATION_X, AuthMeColumns.LOCATION_Y, AuthMeColumns.LOCATION_Z, AuthMeColumns.LOCATION_WORLD, AuthMeColumns.LOCATION_YAW, AuthMeColumns.LOCATION_PITCH);
   }

   public List<String> getAllAuthsByIp(String ip) {
      try {
         return this.columnsHandler.retrieve((Predicate)StandardPredicates.eq(AuthMeColumns.LAST_IP, ip), (DataSourceColumn)AuthMeColumns.NAME);
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return Collections.emptyList();
      }
   }

   public int countAuthsByEmail(String email) {
      return this.columnsHandler.count(StandardPredicates.eqIgnoreCase(AuthMeColumns.EMAIL, email));
   }

   public boolean updateEmail(PlayerAuth auth) {
      return this.columnsHandler.update(auth, AuthMeColumns.EMAIL);
   }

   public boolean isLogged(String user) {
      try {
         DataSourceValue<Integer> result = this.columnsHandler.retrieve(user, AuthMeColumns.IS_LOGGED);
         return result.rowExists() && Integer.valueOf(1).equals(result.getValue());
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return false;
      }
   }

   public void setLogged(String user) {
      this.columnsHandler.update((String)user, AuthMeColumns.IS_LOGGED, 1);
   }

   public void setUnlogged(String user) {
      this.columnsHandler.update((String)user, AuthMeColumns.IS_LOGGED, 0);
   }

   public void grantSession(String user) {
      this.columnsHandler.update((String)user, AuthMeColumns.HAS_SESSION, 1);
   }

   public void revokeSession(String user) {
      this.columnsHandler.update((String)user, AuthMeColumns.HAS_SESSION, 0);
   }

   public void purgeLogged() {
      this.columnsHandler.update((Predicate)StandardPredicates.eq(AuthMeColumns.IS_LOGGED, 1), AuthMeColumns.IS_LOGGED, 0);
   }

   public int getAccountsRegistered() {
      return this.columnsHandler.count(new AlwaysTruePredicate());
   }

   public boolean updateRealName(String user, String realName) {
      return this.columnsHandler.update((String)user, AuthMeColumns.NICK_NAME, realName);
   }

   public DataSourceValue<String> getEmail(String user) {
      try {
         return this.columnsHandler.retrieve((String)user, (DataSourceColumn)AuthMeColumns.EMAIL);
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return DataSourceValueImpl.unknownRow();
      }
   }

   abstract String getJdbcUrl(String var1, String var2, String var3);
}
