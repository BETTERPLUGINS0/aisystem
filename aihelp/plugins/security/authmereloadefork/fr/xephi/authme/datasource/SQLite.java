package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.columnshandler.AuthMeColumnsHandler;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SQLite extends AbstractSqlDataSource {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(SQLite.class);
   private final Settings settings;
   private final File dataFolder;
   private final String database;
   private final String tableName;
   private final Columns col;
   private Connection con;

   public SQLite(Settings settings, File dataFolder) throws SQLException {
      this.settings = settings;
      this.dataFolder = dataFolder;
      this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
      this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      this.col = new Columns(settings);

      try {
         this.connect();
         this.setup();
         this.migrateIfNeeded();
      } catch (Exception var4) {
         this.logger.logException("Error during SQLite initialization:", var4);
         throw var4;
      }
   }

   @VisibleForTesting
   SQLite(Settings settings, File dataFolder, Connection connection) {
      this.settings = settings;
      this.dataFolder = dataFolder;
      this.database = (String)settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
      this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      this.col = new Columns(settings);
      this.con = connection;
      this.columnsHandler = AuthMeColumnsHandler.createForSqlite(this.con, settings);
   }

   protected void connect() throws SQLException {
      try {
         Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException var2) {
         throw new IllegalStateException("Failed to load SQLite JDBC class", var2);
      }

      this.logger.debug("SQLite driver loaded");
      this.con = DriverManager.getConnection(this.getJdbcUrl(this.dataFolder.getAbsolutePath(), "", this.database));
      this.columnsHandler = AuthMeColumnsHandler.createForSqlite(this.con, this.settings);
   }

   @VisibleForTesting
   protected void setup() throws SQLException {
      Statement st = this.con.createStatement();

      try {
         st.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.col.ID + " INTEGER AUTO_INCREMENT, " + this.col.NAME + " VARCHAR(255) NOT NULL UNIQUE, CONSTRAINT table_const_prim PRIMARY KEY (" + this.col.ID + "));");
         DatabaseMetaData md = this.con.getMetaData();
         if (this.isColumnMissing(md, this.col.REAL_NAME)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REAL_NAME + " VARCHAR(255) NOT NULL DEFAULT 'Player';");
         }

         if (this.isColumnMissing(md, this.col.PASSWORD)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PASSWORD + " VARCHAR(255) NOT NULL DEFAULT '';");
         }

         if (!this.col.SALT.isEmpty() && this.isColumnMissing(md, this.col.SALT)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.SALT + " VARCHAR(255);");
         }

         if (this.isColumnMissing(md, this.col.LAST_IP)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_IP + " VARCHAR(40);");
         }

         if (this.isColumnMissing(md, this.col.LAST_LOGIN)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LAST_LOGIN + " TIMESTAMP;");
         }

         if (this.isColumnMissing(md, this.col.REGISTRATION_IP)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_IP + " VARCHAR(40);");
         }

         if (this.isColumnMissing(md, this.col.REGISTRATION_DATE)) {
            this.addRegistrationDateColumn(st);
         }

         if (this.isColumnMissing(md, this.col.LASTLOC_X)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_X + " DOUBLE NOT NULL DEFAULT '0.0';");
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_Y + " DOUBLE NOT NULL DEFAULT '0.0';");
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_Z + " DOUBLE NOT NULL DEFAULT '0.0';");
         }

         if (this.isColumnMissing(md, this.col.LASTLOC_WORLD)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_WORLD + " VARCHAR(255) NOT NULL DEFAULT 'world';");
         }

         if (this.isColumnMissing(md, this.col.LASTLOC_YAW)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_YAW + " FLOAT;");
         }

         if (this.isColumnMissing(md, this.col.LASTLOC_PITCH)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.LASTLOC_PITCH + " FLOAT;");
         }

         if (this.isColumnMissing(md, this.col.EMAIL)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.EMAIL + " VARCHAR(255);");
         }

         if (this.isColumnMissing(md, this.col.IS_LOGGED)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.IS_LOGGED + " INT NOT NULL DEFAULT '0';");
         }

         if (this.isColumnMissing(md, this.col.HAS_SESSION)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.HAS_SESSION + " INT NOT NULL DEFAULT '0';");
         }

         if (this.isColumnMissing(md, this.col.TOTP_KEY)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.TOTP_KEY + " VARCHAR(32);");
         }

         if (!this.col.PLAYER_UUID.isEmpty() && this.isColumnMissing(md, this.col.PLAYER_UUID)) {
            st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.PLAYER_UUID + " VARCHAR(36)");
         }
      } catch (Throwable var5) {
         if (st != null) {
            try {
               st.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (st != null) {
         st.close();
      }

      this.logger.info("SQLite Setup finished");
   }

   @VisibleForTesting
   void migrateIfNeeded() throws SQLException {
      DatabaseMetaData metaData = this.con.getMetaData();
      if (SqLiteMigrater.isMigrationRequired(metaData, this.tableName, this.col)) {
         (new SqLiteMigrater(this.settings, this.dataFolder)).performMigration(this);
         this.connect();
      }

   }

   private boolean isColumnMissing(DatabaseMetaData metaData, String columnName) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, this.tableName, columnName);

      boolean var4;
      try {
         var4 = !rs.next();
      } catch (Throwable var7) {
         if (rs != null) {
            try {
               rs.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (rs != null) {
         rs.close();
      }

      return var4;
   }

   public void reload() {
      close(this.con);

      try {
         this.connect();
         this.setup();
         this.migrateIfNeeded();
      } catch (SQLException var2) {
         this.logger.logException("Error while reloading SQLite:", var2);
      }

   }

   public PlayerAuth getAuth(String user) {
      String sql = "SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.col.NAME + ")=LOWER(?);";

      try {
         PreparedStatement pst = this.con.prepareStatement(sql);

         label78: {
            PlayerAuth var5;
            try {
               pst.setString(1, user);
               ResultSet rs = pst.executeQuery();

               label80: {
                  try {
                     if (rs.next()) {
                        var5 = this.buildAuthFromResultSet(rs);
                        break label80;
                     }
                  } catch (Throwable var9) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (rs != null) {
                     rs.close();
                  }
                  break label78;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var10) {
               if (pst != null) {
                  try {
                     pst.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (pst != null) {
               pst.close();
            }

            return var5;
         }

         if (pst != null) {
            pst.close();
         }
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
      }

      return null;
   }

   public Set<String> getRecordsToPurge(long until) {
      Set<String> list = new HashSet();
      String select = "SELECT " + this.col.NAME + " FROM " + this.tableName + " WHERE MAX( COALESCE(" + this.col.LAST_LOGIN + ", 0), COALESCE(" + this.col.REGISTRATION_DATE + ", 0)) < ?;";

      try {
         PreparedStatement selectPst = this.con.prepareStatement(select);

         try {
            selectPst.setLong(1, until);
            ResultSet rs = selectPst.executeQuery();

            try {
               while(rs.next()) {
                  list.add(rs.getString(this.col.NAME));
               }
            } catch (Throwable var11) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var12) {
            if (selectPst != null) {
               try {
                  selectPst.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (selectPst != null) {
            selectPst.close();
         }
      } catch (SQLException var13) {
         SqlDataSourceUtils.logSqlException(var13);
      }

      return list;
   }

   public void purgeRecords(Collection<String> toPurge) {
      String delete = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

      try {
         PreparedStatement deletePst = this.con.prepareStatement(delete);

         try {
            Iterator var4 = toPurge.iterator();

            while(var4.hasNext()) {
               String name = (String)var4.next();
               deletePst.setString(1, name.toLowerCase(Locale.ROOT));
               deletePst.executeUpdate();
            }
         } catch (Throwable var7) {
            if (deletePst != null) {
               try {
                  deletePst.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (deletePst != null) {
            deletePst.close();
         }
      } catch (SQLException var8) {
         SqlDataSourceUtils.logSqlException(var8);
      }

   }

   public boolean removeAuth(String user) {
      String sql = "DELETE FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";

      try {
         PreparedStatement pst = this.con.prepareStatement(sql);

         boolean var4;
         try {
            pst.setString(1, user.toLowerCase(Locale.ROOT));
            pst.executeUpdate();
            var4 = true;
         } catch (Throwable var7) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (pst != null) {
            pst.close();
         }

         return var4;
      } catch (SQLException var8) {
         SqlDataSourceUtils.logSqlException(var8);
         return false;
      }
   }

   public void closeConnection() {
      try {
         if (this.con != null && !this.con.isClosed()) {
            this.con.close();
         }
      } catch (SQLException var2) {
         SqlDataSourceUtils.logSqlException(var2);
      }

   }

   public DataSourceType getType() {
      return DataSourceType.SQLITE;
   }

   public List<PlayerAuth> getAllAuths() {
      List<PlayerAuth> auths = new ArrayList();
      String sql = "SELECT * FROM " + this.tableName + ";";

      try {
         PreparedStatement pst = this.con.prepareStatement(sql);

         try {
            ResultSet rs = pst.executeQuery();

            try {
               while(rs.next()) {
                  PlayerAuth auth = this.buildAuthFromResultSet(rs);
                  auths.add(auth);
               }
            } catch (Throwable var9) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var10) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (pst != null) {
            pst.close();
         }
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
      }

      return auths;
   }

   public List<String> getLoggedPlayersWithEmptyMail() {
      List<String> players = new ArrayList();
      String sql = "SELECT " + this.col.REAL_NAME + " FROM " + this.tableName + " WHERE " + this.col.IS_LOGGED + " = 1 AND (" + this.col.EMAIL + " = 'your@email.com' OR " + this.col.EMAIL + " IS NULL);";

      try {
         Statement st = this.con.createStatement();

         try {
            ResultSet rs = st.executeQuery(sql);

            try {
               while(rs.next()) {
                  players.add(rs.getString(1));
               }
            } catch (Throwable var9) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var10) {
            if (st != null) {
               try {
                  st.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (st != null) {
            st.close();
         }
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
      }

      return players;
   }

   public List<PlayerAuth> getRecentlyLoggedInPlayers() {
      List<PlayerAuth> players = new ArrayList();
      String sql = "SELECT * FROM " + this.tableName + " ORDER BY " + this.col.LAST_LOGIN + " DESC LIMIT 10;";

      try {
         Statement st = this.con.createStatement();

         try {
            ResultSet rs = st.executeQuery(sql);

            try {
               while(rs.next()) {
                  players.add(this.buildAuthFromResultSet(rs));
               }
            } catch (Throwable var9) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var10) {
            if (st != null) {
               try {
                  st.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (st != null) {
            st.close();
         }
      } catch (SQLException var11) {
         SqlDataSourceUtils.logSqlException(var11);
      }

      return players;
   }

   public boolean setTotpKey(String user, String totpKey) {
      String sql = "UPDATE " + this.tableName + " SET " + this.col.TOTP_KEY + " = ? WHERE " + this.col.NAME + " = ?";

      try {
         PreparedStatement pst = this.con.prepareStatement(sql);

         boolean var5;
         try {
            pst.setString(1, totpKey);
            pst.setString(2, user.toLowerCase(Locale.ROOT));
            pst.executeUpdate();
            var5 = true;
         } catch (Throwable var8) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (pst != null) {
            pst.close();
         }

         return var5;
      } catch (SQLException var9) {
         SqlDataSourceUtils.logSqlException(var9);
         return false;
      }
   }

   private PlayerAuth buildAuthFromResultSet(ResultSet row) throws SQLException {
      String salt = !this.col.SALT.isEmpty() ? row.getString(this.col.SALT) : null;
      return PlayerAuth.builder().name(row.getString(this.col.NAME)).email(row.getString(this.col.EMAIL)).realName(row.getString(this.col.REAL_NAME)).password(row.getString(this.col.PASSWORD), salt).totpKey(row.getString(this.col.TOTP_KEY)).lastLogin(SqlDataSourceUtils.getNullableLong(row, this.col.LAST_LOGIN)).lastIp(row.getString(this.col.LAST_IP)).registrationDate(row.getLong(this.col.REGISTRATION_DATE)).registrationIp(row.getString(this.col.REGISTRATION_IP)).locX(row.getDouble(this.col.LASTLOC_X)).locY(row.getDouble(this.col.LASTLOC_Y)).locZ(row.getDouble(this.col.LASTLOC_Z)).locWorld(row.getString(this.col.LASTLOC_WORLD)).locYaw(row.getFloat(this.col.LASTLOC_YAW)).locPitch(row.getFloat(this.col.LASTLOC_PITCH)).build();
   }

   private void addRegistrationDateColumn(Statement st) throws SQLException {
      st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.col.REGISTRATION_DATE + " TIMESTAMP NOT NULL DEFAULT '0';");
      long currentTimestamp = System.currentTimeMillis();
      int updatedRows = st.executeUpdate(String.format("UPDATE %s SET %s = %d;", this.tableName, this.col.REGISTRATION_DATE, currentTimestamp));
      this.logger.info("Created column '" + this.col.REGISTRATION_DATE + "' and set the current timestamp, " + currentTimestamp + ", to all " + updatedRows + " rows");
   }

   String getJdbcUrl(String dataPath, String ignored, String database) {
      return "jdbc:sqlite:" + dataPath + File.separator + database + ".db";
   }

   private static void close(Connection con) {
      if (con != null) {
         try {
            con.close();
         } catch (SQLException var2) {
            SqlDataSourceUtils.logSqlException(var2);
         }
      }

   }
}
