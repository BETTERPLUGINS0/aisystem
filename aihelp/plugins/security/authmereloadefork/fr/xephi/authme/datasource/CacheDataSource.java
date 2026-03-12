package fr.xephi.authme.datasource;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValueImpl;
import fr.xephi.authme.libs.com.google.common.cache.CacheBuilder;
import fr.xephi.authme.libs.com.google.common.cache.CacheLoader;
import fr.xephi.authme.libs.com.google.common.cache.LoadingCache;
import fr.xephi.authme.libs.com.google.common.util.concurrent.ListenableFuture;
import fr.xephi.authme.libs.com.google.common.util.concurrent.ListeningExecutorService;
import fr.xephi.authme.libs.com.google.common.util.concurrent.MoreExecutors;
import fr.xephi.authme.libs.com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.Utils;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CacheDataSource implements DataSource {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(CacheDataSource.class);
   private final DataSource source;
   private final PlayerCache playerCache;
   private final LoadingCache<String, Optional<PlayerAuth>> cachedAuths;
   private ListeningExecutorService executorService;

   public CacheDataSource(final DataSource source, PlayerCache playerCache) {
      this.source = source;
      this.playerCache = playerCache;
      if ((Boolean)AuthMe.settings.getProperty(DatabaseSettings.USE_VIRTUAL_THREADS)) {
         try {
            Method method = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            method.setAccessible(true);
            ExecutorService ex = (ExecutorService)method.invoke((Object)null);
            this.executorService = MoreExecutors.listeningDecorator(ex);
            this.logger.info("Using virtual threads for cache loader");
         } catch (Exception var5) {
            this.executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("AuthMe-CacheLoader").build()));
            this.logger.info("Cannot enable virtual threads, fallback to CachedThreadPool");
         }
      } else {
         this.executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("AuthMe-CacheLoader").build()));
      }

      this.cachedAuths = CacheBuilder.newBuilder().refreshAfterWrite(5L, TimeUnit.MINUTES).expireAfterAccess(15L, TimeUnit.MINUTES).build(new CacheLoader<String, Optional<PlayerAuth>>() {
         public Optional<PlayerAuth> load(String key) {
            return Optional.ofNullable(source.getAuth(key));
         }

         public ListenableFuture<Optional<PlayerAuth>> reload(String key, Optional<PlayerAuth> oldValue) {
            return CacheDataSource.this.executorService.submit(() -> {
               return this.load(key);
            });
         }
      });
   }

   public LoadingCache<String, Optional<PlayerAuth>> getCachedAuths() {
      return this.cachedAuths;
   }

   public void reload() {
      this.source.reload();
   }

   public boolean isCached() {
      return true;
   }

   public boolean isAuthAvailable(String user) {
      return this.getAuth(user) != null;
   }

   public HashedPassword getPassword(String user) {
      user = user.toLowerCase(Locale.ROOT);
      Optional<PlayerAuth> pAuthOpt = (Optional)this.cachedAuths.getIfPresent(user);
      return pAuthOpt != null && pAuthOpt.isPresent() ? ((PlayerAuth)pAuthOpt.get()).getPassword() : this.source.getPassword(user);
   }

   public PlayerAuth getAuth(String user) {
      user = user.toLowerCase(Locale.ROOT);
      return (PlayerAuth)((Optional)this.cachedAuths.getUnchecked(user)).orElse((Object)null);
   }

   public boolean saveAuth(PlayerAuth auth) {
      boolean result = this.source.saveAuth(auth);
      if (result) {
         this.cachedAuths.refresh(auth.getNickname());
      }

      return result;
   }

   public boolean updatePassword(PlayerAuth auth) {
      boolean result = this.source.updatePassword(auth);
      if (result) {
         this.cachedAuths.refresh(auth.getNickname());
      }

      return result;
   }

   public boolean updatePassword(String user, HashedPassword password) {
      user = user.toLowerCase(Locale.ROOT);
      boolean result = this.source.updatePassword(user, password);
      if (result) {
         this.cachedAuths.refresh(user);
      }

      return result;
   }

   public boolean updateSession(PlayerAuth auth) {
      boolean result = this.source.updateSession(auth);
      if (result) {
         this.cachedAuths.refresh(auth.getNickname());
      }

      return result;
   }

   public boolean updateQuitLoc(PlayerAuth auth) {
      boolean result = this.source.updateQuitLoc(auth);
      if (result) {
         this.cachedAuths.refresh(auth.getNickname());
      }

      return result;
   }

   public Set<String> getRecordsToPurge(long until) {
      return this.source.getRecordsToPurge(until);
   }

   public boolean removeAuth(String name) {
      name = name.toLowerCase(Locale.ROOT);
      boolean result = this.source.removeAuth(name);
      if (result) {
         this.cachedAuths.invalidate(name);
      }

      return result;
   }

   public void closeConnection() {
      this.executorService.shutdown();

      try {
         this.executorService.awaitTermination(5L, TimeUnit.SECONDS);
      } catch (InterruptedException var2) {
         this.logger.logException("Could not close executor service:", var2);
      }

      this.cachedAuths.invalidateAll();
      this.source.closeConnection();
   }

   public boolean updateEmail(PlayerAuth auth) {
      boolean result = this.source.updateEmail(auth);
      if (result) {
         this.cachedAuths.refresh(auth.getNickname());
      }

      return result;
   }

   public List<String> getAllAuthsByIp(String ip) {
      return this.source.getAllAuthsByIp(ip);
   }

   public int countAuthsByEmail(String email) {
      return this.source.countAuthsByEmail(email);
   }

   public void purgeRecords(Collection<String> banned) {
      this.source.purgeRecords(banned);
      this.cachedAuths.invalidateAll(banned);
   }

   public DataSourceType getType() {
      return this.source.getType();
   }

   public boolean isLogged(String user) {
      return this.source.isLogged(user);
   }

   public void setLogged(String user) {
      this.source.setLogged(user.toLowerCase(Locale.ROOT));
   }

   public void setUnlogged(String user) {
      this.source.setUnlogged(user.toLowerCase(Locale.ROOT));
   }

   public boolean hasSession(String user) {
      return this.source.hasSession(user);
   }

   public void grantSession(String user) {
      this.source.grantSession(user);
   }

   public void revokeSession(String user) {
      this.source.revokeSession(user);
   }

   public void purgeLogged() {
      this.source.purgeLogged();
      this.cachedAuths.invalidateAll();
   }

   public int getAccountsRegistered() {
      return this.source.getAccountsRegistered();
   }

   public boolean updateRealName(String user, String realName) {
      boolean result = this.source.updateRealName(user, realName);
      if (result) {
         this.cachedAuths.refresh(user);
      }

      return result;
   }

   public DataSourceValue<String> getEmail(String user) {
      return (DataSourceValue)((Optional)this.cachedAuths.getUnchecked(user)).map((auth) -> {
         return DataSourceValueImpl.of(auth.getEmail());
      }).orElse(DataSourceValueImpl.unknownRow());
   }

   public List<PlayerAuth> getAllAuths() {
      return this.source.getAllAuths();
   }

   public List<String> getLoggedPlayersWithEmptyMail() {
      return (List)this.playerCache.getCache().values().stream().filter((auth) -> {
         return Utils.isEmailEmpty(auth.getEmail());
      }).map(PlayerAuth::getRealName).collect(Collectors.toList());
   }

   public List<PlayerAuth> getRecentlyLoggedInPlayers() {
      return this.source.getRecentlyLoggedInPlayers();
   }

   public boolean setTotpKey(String user, String totpKey) {
      boolean result = this.source.setTotpKey(user, totpKey);
      if (result) {
         this.cachedAuths.refresh(user);
      }

      return result;
   }

   public void invalidateCache(String playerName) {
      this.cachedAuths.invalidate(playerName);
   }

   public void refreshCache(String playerName) {
      if (this.cachedAuths.getIfPresent(playerName) != null) {
         this.cachedAuths.refresh(playerName);
      }

   }
}
