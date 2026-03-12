package fr.xephi.authme.data.auth;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
   private final Map<String, PlayerAuth> cache = new ConcurrentHashMap();

   PlayerCache() {
   }

   public void updatePlayer(PlayerAuth auth) {
      this.cache.put(auth.getNickname().toLowerCase(Locale.ROOT), auth);
   }

   public void removePlayer(String user) {
      this.cache.remove(user.toLowerCase(Locale.ROOT));
   }

   public boolean isAuthenticated(String user) {
      return this.cache.containsKey(user.toLowerCase(Locale.ROOT));
   }

   public PlayerAuth getAuth(String user) {
      return (PlayerAuth)this.cache.get(user.toLowerCase(Locale.ROOT));
   }

   public int getLogged() {
      return this.cache.size();
   }

   public Map<String, PlayerAuth> getCache() {
      return this.cache;
   }
}
