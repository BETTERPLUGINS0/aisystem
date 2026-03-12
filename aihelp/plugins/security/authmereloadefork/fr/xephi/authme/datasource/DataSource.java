package fr.xephi.authme.datasource;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.security.crypts.HashedPassword;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DataSource extends Reloadable {
   default boolean isCached() {
      return false;
   }

   boolean isAuthAvailable(String var1);

   HashedPassword getPassword(String var1);

   PlayerAuth getAuth(String var1);

   boolean saveAuth(PlayerAuth var1);

   boolean updateSession(PlayerAuth var1);

   boolean updatePassword(PlayerAuth var1);

   boolean updatePassword(String var1, HashedPassword var2);

   Set<String> getRecordsToPurge(long var1);

   void purgeRecords(Collection<String> var1);

   boolean removeAuth(String var1);

   boolean updateQuitLoc(PlayerAuth var1);

   List<String> getAllAuthsByIp(String var1);

   int countAuthsByEmail(String var1);

   boolean updateEmail(PlayerAuth var1);

   void closeConnection();

   DataSourceType getType();

   boolean isLogged(String var1);

   void setLogged(String var1);

   void setUnlogged(String var1);

   boolean hasSession(String var1);

   void grantSession(String var1);

   void revokeSession(String var1);

   void purgeLogged();

   List<String> getLoggedPlayersWithEmptyMail();

   int getAccountsRegistered();

   boolean updateRealName(String var1, String var2);

   DataSourceValue<String> getEmail(String var1);

   List<PlayerAuth> getAllAuths();

   List<PlayerAuth> getRecentlyLoggedInPlayers();

   boolean setTotpKey(String var1, String var2);

   default boolean removeTotpKey(String user) {
      return this.setTotpKey(user, (String)null);
   }

   void reload();

   default void invalidateCache(String playerName) {
   }

   default void refreshCache(String playerName) {
   }
}
