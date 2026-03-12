package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.init.ReloadableInitable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.manager.violationdatabase.mysql.MySQLViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.postgresql.PostgresqlViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.sqlite.SQLiteViolationDatabase;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.Generated;

public class ViolationDatabaseManager implements StartableInitable, ReloadableInitable {
   private final GrimPlugin plugin;
   private boolean enabled = false;
   private boolean loaded = false;
   @NotNull
   private ViolationDatabase database;

   public ViolationDatabaseManager(GrimPlugin plugin) {
      this.plugin = plugin;
      this.database = NoOpViolationDatabase.INSTANCE;
   }

   public void start() {
      this.load();
   }

   public void reload() {
      this.load();
   }

   public void load() {
      ConfigManager cfg = GrimAPI.INSTANCE.getConfigManager().getConfig();
      this.enabled = cfg.getBooleanElse("history.enabled", false);
      String rawType = this.enabled ? cfg.getStringElse("history.database.type", "SQLITE").toUpperCase() : "NOOP";
      byte var4 = -1;
      switch(rawType.hashCode()) {
      case -1841605620:
         if (rawType.equals("SQLITE")) {
            var4 = 0;
         }
         break;
      case -1620389036:
         if (rawType.equals("POSTGRESQL")) {
            var4 = 2;
         }
         break;
      case 73844866:
         if (rawType.equals("MYSQL")) {
            var4 = 1;
         }
      }

      int port;
      String host;
      String db;
      String user;
      String pwd;
      ViolationDatabase var11;
      String var10000;
      switch(var4) {
      case 0:
         if (!(this.database instanceof SQLiteViolationDatabase)) {
            this.database.disconnect();

            try {
               Class.forName("org.sqlite.JDBC");
               this.database = new SQLiteViolationDatabase(this.plugin);
               this.database.connect();
               this.loaded = true;
            } catch (ClassNotFoundException var14) {
               LogUtil.error("IMPORTANT: Could not load SQLite driver for /grim history database.\nDownload the minecraft-sqlite-jdbc mod/plugin for SQLite support, or change history.database.type\nAlternatively set history.enabled=false to remove this message if /grim history support is not desired");
               this.database = NoOpViolationDatabase.INSTANCE;
               this.loaded = false;
            } catch (SQLException var15) {
               LogUtil.error((Throwable)var15);
               this.database = NoOpViolationDatabase.INSTANCE;
               this.loaded = false;
            }
         }
         break;
      case 1:
         port = cfg.getIntElse("history.database.port", 3306);
         var10000 = cfg.getStringElse("history.database.host", "localhost");
         host = var10000 + ":" + port;
         db = cfg.getStringElse("history.database.database", "grimac");
         user = cfg.getStringElse("history.database.username", "root");
         pwd = cfg.getStringElse("history.database.password", "password");
         var11 = this.database;
         if (var11 instanceof MySQLViolationDatabase) {
            MySQLViolationDatabase mysql = (MySQLViolationDatabase)var11;
            if (mysql.sameConfig(host, db, user, pwd)) {
               break;
            }
         }

         this.database.disconnect();
         this.database = new MySQLViolationDatabase(this.plugin, host, db, user, pwd);

         try {
            this.database.connect();
            this.loaded = true;
         } catch (SQLException var13) {
            LogUtil.error((Throwable)var13);
            this.database = NoOpViolationDatabase.INSTANCE;
            this.loaded = false;
         }
         break;
      case 2:
         port = cfg.getIntElse("history.database.port", 3306);
         var10000 = cfg.getStringElse("history.database.host", "localhost");
         host = var10000 + ":" + port;
         db = cfg.getStringElse("history.database.database", "grimac");
         user = cfg.getStringElse("history.database.username", "root");
         pwd = cfg.getStringElse("history.database.password", "password");
         var11 = this.database;
         if (var11 instanceof PostgresqlViolationDatabase) {
            PostgresqlViolationDatabase postgresql = (PostgresqlViolationDatabase)var11;
            if (postgresql.sameConfig(host, db, user, pwd)) {
               break;
            }
         }

         this.database.disconnect();
         this.database = new PostgresqlViolationDatabase(host, db, user, pwd);

         try {
            this.database.connect();
            this.loaded = true;
         } catch (SQLException var12) {
            LogUtil.error((Throwable)var12);
            this.database = NoOpViolationDatabase.INSTANCE;
            this.loaded = false;
         }
         break;
      default:
         if (!(this.database instanceof NoOpViolationDatabase)) {
            this.database.disconnect();
            this.database = NoOpViolationDatabase.INSTANCE;
            this.loaded = false;
         }
      }

   }

   public void logAlert(GrimPlayer player, String verbose, String checkName, int vls) {
      String grimVersion = GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
      GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(this.plugin, () -> {
         this.database.logAlert(player, grimVersion, verbose, checkName, vls);
      });
   }

   public int getLogCount(UUID player) {
      return this.database.getLogCount(player);
   }

   public List<Violation> getViolations(UUID player, int page, int limit) {
      return this.database.getViolations(player, page, limit);
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public boolean isLoaded() {
      return this.loaded;
   }
}
