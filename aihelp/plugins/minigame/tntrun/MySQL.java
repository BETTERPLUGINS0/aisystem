package tntrun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.utils.Utils;

public class MySQL {
   protected boolean connected = false;
   public Connection c = null;
   private String driver;
   private String connectionString;
   private TNTRun pl;
   private String username;
   private String password;
   private static Logger logger;

   public MySQL(TNTRun plugin) {
      this.pl = plugin;
      logger = this.pl.getLogger();
   }

   public MySQL(String hostname, int port, String database, String username, String password, String useSSL, String flags, boolean legacyDriver, TNTRun plugin) {
      this.driver = legacyDriver ? "com.mysql.jdbc.Driver" : "com.mysql.cj.jdbc.Driver";
      this.connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL + "&" + flags;
      this.username = username;
      this.password = password;
      this.pl = plugin;
      logger = this.pl.getLogger();
   }

   public Connection open() {
      try {
         Class.forName(this.driver);
         this.c = DriverManager.getConnection(this.connectionString, this.username, this.password);
         return this.c;
      } catch (SQLException var2) {
         logger.severe("Could not connect to Database! because: " + var2.getMessage());
      } catch (ClassNotFoundException var3) {
         logger.severe(this.driver + " not found!");
      } catch (Exception var4) {
         logger.severe(var4.getMessage());
      }

      return this.c;
   }

   public Connection getConn() {
      return this.c;
   }

   public void close() {
      try {
         if (this.c != null) {
            this.c.close();
         }
      } catch (SQLException var2) {
         logger.info(var2.getMessage());
      }

      this.c = null;
   }

   public boolean isConnected() {
      try {
         return this.c != null && !this.c.isClosed();
      } catch (SQLException var2) {
         return false;
      }
   }

   public MySQL.Result query(String query) {
      if (!this.isConnected()) {
         this.open();
      }

      return this.query(query, true);
   }

   public MySQL.Result query(final String query, boolean retry) {
      if (!this.isConnected()) {
         this.open();
      }

      try {
         PreparedStatement statement = null;

         try {
            if (!this.isConnected()) {
               this.open();
            }

            if (Utils.debug()) {
               logger.info("Mysql query = " + query);
            }

            statement = this.c.prepareStatement(query);
            if (statement.execute()) {
               return new MySQL.Result(this, statement, statement.getResultSet());
            }
         } catch (SQLException var6) {
            String msg = var6.getMessage();
            if (!msg.contains("looses")) {
               logger.severe("Database query error: " + msg);
            }

            if (retry && msg.contains("_BUSY")) {
               logger.severe("Retrying query...");
               (new BukkitRunnable() {
                  public void run() {
                     MySQL.this.query(query, false);
                  }
               }).runTaskLater(this.pl, 20L);
            }
         }

         if (statement != null) {
            statement.close();
         }
      } catch (SQLException var7) {
         logger.info(var7.getMessage());
      }

      return null;
   }

   protected MySQL.Statements getStatement(String query) {
      String trimmedQuery = query.trim();
      if (trimmedQuery.substring(0, 6).equalsIgnoreCase("SELECT")) {
         return MySQL.Statements.SELECT;
      } else if (trimmedQuery.substring(0, 6).equalsIgnoreCase("INSERT")) {
         return MySQL.Statements.INSERT;
      } else if (trimmedQuery.substring(0, 6).equalsIgnoreCase("UPDATE")) {
         return MySQL.Statements.UPDATE;
      } else if (trimmedQuery.substring(0, 6).equalsIgnoreCase("DELETE")) {
         return MySQL.Statements.DELETE;
      } else if (trimmedQuery.substring(0, 6).equalsIgnoreCase("CREATE")) {
         return MySQL.Statements.CREATE;
      } else if (trimmedQuery.substring(0, 5).equalsIgnoreCase("ALTER")) {
         return MySQL.Statements.ALTER;
      } else if (trimmedQuery.substring(0, 4).equalsIgnoreCase("DROP")) {
         return MySQL.Statements.DROP;
      } else if (trimmedQuery.substring(0, 8).equalsIgnoreCase("TRUNCATE")) {
         return MySQL.Statements.TRUNCATE;
      } else if (trimmedQuery.substring(0, 6).equalsIgnoreCase("RENAME")) {
         return MySQL.Statements.RENAME;
      } else if (trimmedQuery.substring(0, 2).equalsIgnoreCase("DO")) {
         return MySQL.Statements.DO;
      } else if (trimmedQuery.substring(0, 7).equalsIgnoreCase("REPLACE")) {
         return MySQL.Statements.REPLACE;
      } else if (trimmedQuery.substring(0, 4).equalsIgnoreCase("LOAD")) {
         return MySQL.Statements.LOAD;
      } else if (trimmedQuery.substring(0, 7).equalsIgnoreCase("HANDLER")) {
         return MySQL.Statements.HANDLER;
      } else {
         return trimmedQuery.substring(0, 4).equalsIgnoreCase("CALL") ? MySQL.Statements.CALL : MySQL.Statements.SELECT;
      }
   }

   public class Result {
      private ResultSet resultSet;
      private Statement statement;

      public Result(final MySQL param1, Statement param2, ResultSet param3) {
         this.statement = statement;
         this.resultSet = resultSet;
      }

      public ResultSet getResultSet() {
         return this.resultSet;
      }

      public void close() {
         try {
            this.statement.close();
            this.resultSet.close();
         } catch (SQLException var2) {
         }

      }
   }

   protected static enum Statements {
      SELECT,
      INSERT,
      UPDATE,
      DELETE,
      DO,
      REPLACE,
      LOAD,
      HANDLER,
      CALL,
      CREATE,
      ALTER,
      DROP,
      TRUNCATE,
      RENAME,
      START,
      COMMIT,
      ROLLBACK,
      SAVEPOINT,
      LOCK,
      UNLOCK,
      PREPARE,
      EXECUTE,
      DEALLOCATE,
      SET,
      SHOW,
      DESCRIBE,
      EXPLAIN,
      HELP,
      USE,
      ANALYZE,
      ATTACH,
      BEGIN,
      DETACH,
      END,
      INDEXED,
      ON,
      PRAGMA,
      REINDEX,
      RELEASE,
      VACUUM;

      // $FF: synthetic method
      private static MySQL.Statements[] $values() {
         return new MySQL.Statements[]{SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, CREATE, ALTER, DROP, TRUNCATE, RENAME, START, COMMIT, ROLLBACK, SAVEPOINT, LOCK, UNLOCK, PREPARE, EXECUTE, DEALLOCATE, SET, SHOW, DESCRIBE, EXPLAIN, HELP, USE, ANALYZE, ATTACH, BEGIN, DETACH, END, INDEXED, ON, PRAGMA, REINDEX, RELEASE, VACUUM};
      }
   }
}
